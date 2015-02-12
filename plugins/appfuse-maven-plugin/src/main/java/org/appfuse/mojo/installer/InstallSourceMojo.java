package org.appfuse.mojo.installer;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectBuilder;
import org.apache.maven.settings.Settings;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Get;
import org.apache.tools.ant.taskdefs.Move;
import org.apache.tools.ant.types.FileSet;
import org.appfuse.tool.RenamePackages;
import org.appfuse.tool.SubversionUtils;
import org.tmatesoft.svn.core.SVNErrorMessage;
import org.tmatesoft.svn.core.SVNException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import static java.lang.String.format;


/**
 * This mojo is used to "install" source artifacts from Subversion into an AppFuse project.
 * If you get an OutOfMemoryError when running this plugin, you should be able to fix it
 * by setting your MAVEN_OPTS environment variable to "-Xms128M -Xmx256M".
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @goal full-source
 */
public class InstallSourceMojo extends AbstractMojo {
    private static final String APPFUSE_GROUP_ID = "org.appfuse";
    private static final String FILE_SEP = System.getProperty("file.separator");
    private static final String LINE_SEP = System.getProperty("line.separator");

    Project antProject = AntUtils.createProject();
    Properties appfuseProperties;

    // ThreadLocale to hold properties as they're built when traversing through a modular project
    private static final ThreadLocal<Map> propertiesContextHolder = new ThreadLocal<Map>();

    /**
     * The path where the files from SVN will be placed. This is intentionally set to "src" since that's the default
     * src directory used for exporting AppFuse artifacts.
     *
     * @parameter expression="${appfuse.destinationDirectory}" default-value="${basedir}/src"
     */
    private String destinationDirectory;

    /**
     * The branch containing the source code
     *
     * @parameter expression="${appfuse.branch}"
     */
    private String branch;

    /**
     * The directory containing the source code.
     *
     * @parameter expression="${appfuse.trunk}" default-value="https://github.com/appfuse/appfuse/"
     */
    private String trunk;

    /**
     * The tag containing the source code - defaults to '/trunk', but you may want to set it to '/tags/TAGNAME'
     *
     * @parameter expression="${appfuse.tag}" default-value="trunk/"
     */
    private String tag;

    /**
     * <i>Maven Internal</i>: Project to interact with.
     *
     * @parameter expression="${project}"
     * @required
     * @readonly
     * @noinspection UnusedDeclaration
     */
    private MavenProject project;

    /**
     * @parameter expression="${settings}"
     * @required
     * @readonly
     */
    private Settings settings;

    /**
     * @component
     */
    private MavenProjectBuilder mavenProjectBuilder;

    /**
     * @parameter default-value= "${localRepository}"
     */
    private ArtifactRepository local;

    public void execute() throws MojoExecutionException, MojoFailureException {
        // http://issues.appfuse.org/browse/APF-1025
        System.setProperty("file.encoding", "UTF-8");

        // If appfuse.version is specified as a property, and not a SNAPSHOT, use it for the tag
        String appfuseVersion = project.getProperties().getProperty("appfuse.version");

        if ((appfuseVersion != null) && !appfuseVersion.endsWith("SNAPSHOT") && tag.equals("trunk/")) {
            tag = "tags/APPFUSE_" + appfuseVersion.toUpperCase().replaceAll("-", "_") + "/";
            // APF-1168: Allow milestone and release candidates
            if (tag.contains("_M")) {
                tag = tag.replace("_M", "-M");
            } else if (tag.contains("_R")) {
                tag = tag.replace("_R", "-R");
            }
        }

        if (branch != null && !"".equals(branch) && !"master".equals(branch) && !"HEAD".equals(branch)) {
            log("Using branch: " + branch);
            tag = "branches/" + branch + "/";
        }

        String daoFramework = project.getProperties().getProperty("dao.framework");

        if (daoFramework == null) {
            log("No dao.framework property specified, defaulting to 'hibernate'");
        }

        String webFramework = project.getProperties().getProperty("web.framework");

        boolean modular = project.getPackaging().equals("pom");
        // if core project or modular web
        if (project.getPackaging().equals("jar") ||
            (project.getPackaging().equals("war") && project.getParentArtifact().getGroupId().contains("appfuse"))) {
            // export data-common
            log("Importing source from data-common module...");
            String coreSource = project.getBuild().getSourceDirectory();
            export("data/common/src", (modular) ? coreSource : destinationDirectory);

            // Keep web project original testing hibernate.properties instead of overwriting it: rename
            File orig = new File((modular ? coreSource : destinationDirectory) + "/test/resources/hibernate.properties");
            File dest = new File((modular ? coreSource : destinationDirectory) + "/test/resources/hibernate.properties.orig");
            if (orig.exists() && webFramework != null && !webFramework.isEmpty()) {
                renameFile(orig, dest);
            }

            // export persistence framework
            log("Importing source from " + daoFramework + " module...");
            export("data/" + daoFramework + "/src", (modular) ? coreSource : destinationDirectory);

            // export service module
            log("Importing source from service module...");
            export("service/src", (modular) ? coreSource : destinationDirectory);

            // move Base*TestCase to test directory
            moveFiles((modular) ? coreSource + "/main" : destinationDirectory + "/main",
                (modular) ? coreSource + "/test" : destinationDirectory + "/test", "**/Base*TestCase.java");

            // delete dao.framework related files from test directory
            deleteFile("test/resources/hibernate.cfg.xml");
            deleteFile("test/resources/META-INF");
            deleteFile("test/resources/sql-map-config.xml");

            // If JPA, delete hibernate.cfg.xml b/c it will cause issues when
            // using jpaconfiguration with the hibernate3-maven-plugin
            if ("jpa".equalsIgnoreCase(daoFramework)) {
                deleteFile("main/resources/hibernate.cfg.xml");
            }

            // Keep web project original testing hibernate.properties instead of overwriting it: delete copied and rename back
            if (dest.exists() && webFramework != null && !webFramework.isEmpty()) {
                deleteFile(orig.getPath());
                renameFile(dest, orig);
            }

            log("Source successfully imported!");
        }

        if (modular) {
            try {
                String pom = FileUtils.readFileToString(new File("pom.xml"), "UTF-8");

                // Move modules to build section.
                pom = pom.replaceAll("  <modules>\n", "");
                pom = pom.replaceAll("    <module>.*?</module>\n", "");
                pom = pom.replaceAll("  </modules>\n", "");

                pom = pom.replace("<build>", "<modules>\n" +
                    "        <module>core</module>\n" +
                    "        <module>web</module>\n" +
                    "    </modules>\n\n    <build>");

                FileUtils.writeStringToFile(new File("pom.xml"), pom, "UTF-8");
            } catch (IOException ex) {
                // no big deal if this fails, everything will still work
            }

            try {
                String pom = FileUtils.readFileToString(new File("core/pom.xml"), "UTF-8");

                // remove appfuse-data-common as a dependency
                pom = pom.replaceAll("<dependencies>\n" +
                    "        <dependency>\n" +
                    "            <groupId>org.appfuse</groupId>\n" +
                    "            <artifactId>appfuse-data-common</artifactId>\n" +
                    "            <version>(.*?)appfuse.version}</version>\n" +
                    "        </dependency>", "<dependencies>");

                // modify the appfuse dependencies to be type pom
                pom = pom.replaceAll("<version>(.*?)appfuse.version}</version>",
                    "<version>$1appfuse.version}</version>\n            <type>pom</type>");

                // modify hibernate4-plugin to exclude dependency scan
                pom = pom.replaceAll("<artifactId>hibernate4-maven-plugin</artifactId>\n" +
                    "                <configuration>", "<artifactId>hibernate4-maven-plugin</artifactId>\n" +
                    "                <configuration>\n" +
                    "                    <scanDependencies>none</scanDependencies>");

                pom = adjustLineEndingsForOS(pom);
                FileUtils.writeStringToFile(new File("core/pom.xml"), pom, "UTF-8");
            } catch (IOException io) {
                getLog().error("Failed to change core module's dependencies to use <type>pom</type>.\n" +
                    "Please make this change manually.");
            }

            // exclude all appfuse dependencies in web module
            try {
                String pom = FileUtils.readFileToString(new File("web/pom.xml"), "UTF-8");
                pom = pom.replaceAll("appfuse-hibernate", "*");

                // modify hibernate4-plugin to exclude dependency scan
                pom = pom.replaceAll("<artifactId>hibernate4-maven-plugin</artifactId>",
                    "<artifactId>hibernate4-maven-plugin</artifactId>\n" +
                        "                <configuration>\n                    " +
                        "                    <scanDependencies>none</scanDependencies>\n" +
                        "                </configuration>");
                pom = adjustLineEndingsForOS(pom);
                FileUtils.writeStringToFile(new File("web/pom.xml"), pom, "UTF-8");
            } catch (IOException io) {
                getLog().error("Failed to change web module to exclude AppFuse dependencies.\n" +
                    "Please make this change manually: %s/appfuse-hibernate/*");
            }
        } else {
            try {
                String pom = FileUtils.readFileToString(new File("pom.xml"), "UTF-8");
                // modify hibernate4-plugin to exclude dependency scan
                if (project.getPackaging().equals("jar")) {
                    pom = pom.replaceAll("<artifactId>hibernate4-maven-plugin</artifactId>\n" +
                        "                <configuration>", "<artifactId>hibernate4-maven-plugin</artifactId>\n" +
                        "                <configuration>\n" +
                        "                    <scanDependencies>none</scanDependencies>");
                } else {
                    pom = pom.replaceAll("<artifactId>hibernate4-maven-plugin</artifactId>",
                        "<artifactId>hibernate4-maven-plugin</artifactId>\n" +
                        "                <configuration>\n" +
                        "                    <scanDependencies>none</scanDependencies>\n" +
                        "                </configuration>");
                }

                // remove appfuse-data-common as a dependency
                pom = pom.replaceAll("<dependencies>\n" +
                    "        <dependency>\n" +
                    "            <groupId>org.appfuse</groupId>\n" +
                    "            <artifactId>appfuse-data-common</artifactId>\n" +
                    "            <version>(.*?)appfuse.version}</version>\n" +
                    "        </dependency>", "<dependencies>");

                pom = adjustLineEndingsForOS(pom);
                FileUtils.writeStringToFile(new File("pom.xml"), pom, "UTF-8");
            } catch (IOException io) {
                getLog().error("Failed to add <scanDependencies>none</scanDependencies> to hibernate4-maven-plugin. " +
                    "Please make this change manually.");
            }
        }

        // it's OK if a project created with appfuse-ws doesn't have a web framework defined
        // currently, a project with appfuse-ws can be identified by enunciate
        boolean isWebServicesProject = false;
        for (Object pluginArtifact : project.getPluginArtifacts()) {
            if (((Artifact) pluginArtifact).getArtifactId().contains("enunciate")) {
                isWebServicesProject = true;
                try {
                    String enunciate = FileUtils.readFileToString(new File("enunciate.xml"), "UTF-8");
                    enunciate = enunciate.replaceAll("org.appfuse", project.getGroupId());
                    enunciate = adjustLineEndingsForOS(enunciate);
                    FileUtils.writeStringToFile(new File("enunciate.xml"), enunciate, "UTF-8");
                } catch (IOException e) {
                    getLog().error("Failed to rename 'org.appfuse' to '" + project.getGroupId() + 
                            " in enunciate.xml'. Please make this change manually.");
                }
                break;
            }
        }

        if (project.getPackaging().equalsIgnoreCase("war")) {
            if (webFramework == null && !isWebServicesProject) {
                getLog().error("The web.framework property is not specified - please modify your pom.xml to add " +
                    " this property. For example: <web.framework>struts</web.framework>.");
                throw new MojoExecutionException("No web.framework property specified, please modify pom.xml to add it.");
            }

            if (project.getArtifactId().contains("core")) {
                // delete hibernate, ibatis and jpa files from web project
                deleteFile("main/resources/hibernate.cfg.xml");
                deleteFile("main/resources/META-INF");
                deleteFile("main/resources/sql-map-config.xml");

                // there's a jdbc.properties in test/resources that shouldn't be there
                deleteFile("test/resources/jdbc.properties");

            } else if (!isAppFuse()) {
                // there's a jdbc.properties in test/resources that shouldn't be there
                deleteFile("test/resources/jdbc.properties");
                deleteFile("test/resources/log4j2.xml");
            }
        }

        // As of AppFuse 3.5, pom files no longer need to be adjusted when running full-source
        //calculateDependencies(appfuseVersion, daoFramework, webFramework, isWebServicesProject);
        renamePackages();
    }

    private void calculateDependencies(String appfuseVersion, String daoFramework, String webFramework, boolean isWebServicesProject) throws MojoFailureException {
        List dependencies = project.getOriginalModel().getDependencies();
        List<Dependency> newDependencies = new ArrayList<>();

        // remove all appfuse dependencies
        for (Object dependency : dependencies) {
            Dependency dep = (Dependency) dependency;

            if (!dep.getGroupId().equals(APPFUSE_GROUP_ID)) {
                newDependencies.add(dep);
            }
        }

        if (!project.getPackaging().equals("pom")) {

            // add dependencies from root appfuse pom
            newDependencies = addModuleDependencies(newDependencies, "root", "", "");

            // Add dependencies from appfuse-data
            newDependencies = addModuleDependencies(newDependencies, "data", "data", "appfuse-root");

            // Add dependencies from appfuse-data-common
            newDependencies = addModuleDependencies(newDependencies, "data-common", "data/common", "appfuse-root/appfuse-data");

            // Add dependencies from appfuse-${dao.framework}
            newDependencies = addModuleDependencies(newDependencies, daoFramework, "data/" + daoFramework, "appfuse-root/appfuse-data");

            // Add dependencies from appfuse-service
            newDependencies = addModuleDependencies(newDependencies, "service", "service", "appfuse-root");

            if (!isWebServicesProject && project.getPackaging().equals("war")) {
                newDependencies = addWebDependencies(appfuseVersion, newDependencies, webFramework);
            }

            createFullSourcePom(newDependencies);
        } else {
            if (project.getPackaging().equals("pom")) {
                // add dependencies from root appfuse pom
                newDependencies = addModuleDependencies(newDependencies, "root", "", "");

                createFullSourcePom(newDependencies);
            }

            if (project.getPackaging().equals("jar")) {
                newDependencies.clear();

                // add dependencies from root appfuse pom
                newDependencies = addModuleDependencies(newDependencies, "root", "", "");

                // Add dependencies from appfuse-data
                newDependencies = addModuleDependencies(newDependencies, "data", "data", "appfuse-root");

                // Add dependencies from appfuse-data-common
                newDependencies = addModuleDependencies(newDependencies, "data-common", "data/common", "appfuse-root/appfuse-data");

                // Add dependencies from appfuse-${dao.framework}
                newDependencies = addModuleDependencies(newDependencies, daoFramework, "data/" + daoFramework, "appfuse-root/appfuse-data");

                // Add dependencies from appfuse-service
                newDependencies = addModuleDependencies(newDependencies, "service", "service", "appfuse-root");

                createFullSourcePom(newDependencies);
            }

            if (project.getPackaging().equals("war")) {
                newDependencies.clear();

                // add dependencies from root appfuse pom
                newDependencies = addModuleDependencies(newDependencies, "root", "", "");

                newDependencies = addWebDependencies(appfuseVersion, newDependencies, webFramework);

                createFullSourcePom(newDependencies);
            }
        }
    }

    private List<Dependency> addWebDependencies(String appfuseVersion, List<Dependency> newDependencies, String webFramework) {
        // Add dependencies from appfuse-common-web
        newDependencies = addModuleDependencies(newDependencies, "web", "web", "appfuse-root");

        Double appfuseVersionAsDouble = new Double(appfuseVersion.substring(0, appfuseVersion.lastIndexOf(".")));
        if (StringUtils.countMatches(".", appfuseVersion) == 1) {
            appfuseVersionAsDouble = new Double(appfuseVersion);
        }

        getLog().debug("Detected AppFuse version: " + appfuseVersionAsDouble);

        if (isAppFuse() && appfuseVersionAsDouble < 2.1) {
            // Add dependencies from appfuse-common-web
            newDependencies = addModuleDependencies(newDependencies, "web-common", "web/common", "appfuse-root/appfuse-web");
        }

        if (isAppFuse() && project.getPackaging().equals("war") && project.hasParent()) {
            newDependencies = addModuleDependencies(newDependencies, "web-common", "web/common", "appfuse-root/appfuse-web");
            newDependencies = addModuleDependencies(newDependencies, webFramework, "web/" + webFramework, "appfuse-root/appfuse-web");
        }

        return newDependencies;
    }

    // TODO: Improve logic or remove need for calculation
    private boolean isAppFuse() {
        return (project.getParent().getArtifactId().contains("appfuse-web"));
    }

    private void deleteFile(String filePath) {
        if (!filePath.startsWith("/")) {
            filePath = "/" + filePath;
        }
        File duplicateFile = new File(getFilePath(destinationDirectory + filePath));
        try {
            getLog().debug("Looking for duplicate file at '" + duplicateFile.getCanonicalPath());
            if (duplicateFile.exists()) {
                getLog().debug("Deleting duplicate file at '" + duplicateFile.getCanonicalPath());
                if (duplicateFile.isDirectory()) {
                    FileUtils.deleteDirectory(duplicateFile);
                } else {
                    FileUtils.forceDeleteOnExit(duplicateFile);
                }
            }
        } catch (IOException io) {
            getLog().error("Failed to delete '" + filePath + "', please delete manually.");
        }
    }

    private void createFullSourcePom(List<Dependency> newDependencies) throws MojoFailureException {
        // create properties based on dependencies
        Set<String> projectProperties = new TreeSet<>();

        for (Dependency dep : newDependencies) {
            projectProperties.add(getDependencyVersionOrThrowExceptionIfNotAvailable(dep));
        }

        // add core as a dependency for modular wars
        if (project.getPackaging().equals("war") && !project.getParentArtifact().getGroupId().contains("appfuse")) {
            Dependency core = new Dependency();
            core.setGroupId("${project.parent.groupId}");
            // This assumes you're following conventions of ${project.artifactId}-core
            core.setArtifactId("${project.parent.artifactId}-core");
            core.setVersion("${project.parent.version}");
            newDependencies.add(core);
        }

        Collections.sort(newDependencies, new BeanComparator("groupId"));

        project.getOriginalModel().setDependencies(newDependencies);

        Properties currentProperties = project.getOriginalModel().getProperties();

        Set<String> currentKeys = new LinkedHashSet<String>();
        for (Object key : currentProperties.keySet()) {
            currentKeys.add((String) key);
        }

        StringBuffer sortedProperties = new StringBuffer();

        Properties appfuseProperties = getAppFuseProperties();

        // holder for properties - stored in ThreadLocale
        Map<String, String> propertiesForPom = new LinkedHashMap<String, String>();

        for (String key : projectProperties) {
            // don't add property if it already exists in project
            if (!currentKeys.contains(key)) {
                String value = appfuseProperties.getProperty(key);

                // this happens when the version number is hard-coded
                if (value == null) {
                    continue;
                }

                if (value.contains("&amp;")) {
                    value = "<![CDATA[" + value + "]]>";
                }

                sortedProperties.append("        <").append(key).append(">")
                    .append(value).append("</").append(key).append(">" + "\n");
                propertiesForPom.put(key, value);
            }
        }

        if (project.getPackaging().equals("pom") || project.hasParent()) {
            // store sorted properties in a thread local for later retrieval
            Map<String, String> properties = new LinkedHashMap<String, String>();
            if (propertiesContextHolder.get() != null) {
                properties = (LinkedHashMap) propertiesContextHolder.get();
            }

            for (String key : propertiesForPom.keySet()) {
                if (!properties.containsKey(key)) {
                    properties.put(key, propertiesForPom.get(key));
                }
            }

            propertiesContextHolder.set(properties);
        }

        StringWriter writer = new StringWriter();

        try {
            project.writeOriginalModel(writer);

            File pom = new File("pom-fullsource.xml");

            if (pom.exists()) {
                pom.delete();
            }

            FileWriter fw = new FileWriter(pom);
            fw.write(writer.toString());
            fw.flush();
            fw.close();
        } catch (IOException ex) {
            getLog().error("Unable to create pom-fullsource.xml: " + ex.getMessage(), ex);
            throw new MojoFailureException(ex.getMessage());
        }

        //log("Updated dependencies in pom.xml...");

        // I tried to use regex here, but couldn't get it to work - going with the old fashioned way instead
        String pomXml = writer.toString();
        int startTag = pomXml.indexOf("\n  <dependencies>");

        String dependencyXml = pomXml.substring(startTag, pomXml.indexOf("</dependencies>", startTag));
        // change 2 spaces to 4
        dependencyXml = dependencyXml.replaceAll("  ", "    ");
        dependencyXml = "\n    <!-- Dependencies calculated by AppFuse when running full-source plugin -->" + dependencyXml;

        try {
            String packaging = project.getPackaging();
            String pathToPom = "pom.xml";
            if (project.hasParent() && !project.getParentArtifact().getGroupId().contains("appfuse")) {
                if (packaging.equals("jar")) {
                    pathToPom = "core/" + pathToPom;
                } else if (packaging.equals("war")) {
                    pathToPom = "web/" + pathToPom;
                }
            }

            String originalPom = FileUtils.readFileToString(new File(pathToPom), "UTF-8");
            // replace tabs with spaces (in case user has changed their pom.xml
            originalPom = originalPom.replace("\t", "    ");
            startTag = originalPom.indexOf("\n    <dependencies>");

            StringBuffer sb = new StringBuffer();
            sb.append(originalPom.substring(0, startTag));
            sb.append(dependencyXml);
            sb.append(originalPom.substring(originalPom.indexOf("</dependencies>", startTag)));

            String adjustedPom = sb.toString();

            // Calculate properties and add them to pom if not a modular project - otherwise properties are added
            // near the end of this method from a threadlocal
            if (!project.getPackaging().equals("pom") && project.getParentArtifact().getGroupId().contains("appfuse")) {
                adjustedPom = addPropertiesToPom(adjustedPom, sortedProperties);
            }

            adjustedPom = adjustLineEndingsForOS(adjustedPom);

            FileUtils.writeStringToFile(new File(pathToPom), adjustedPom, "UTF-8");
        } catch (IOException ex) {
            getLog().error("Unable to write to pom.xml: " + ex.getMessage(), ex);
            throw new MojoFailureException(ex.getMessage());
        }

        // cleanup so user isn't aware that files were created
        File pom = new File("pom-fullsource.xml");

        if (pom.exists()) {
            pom.delete();
        }

        // when performing full-source on a modular project, add the properties to the root pom.xml at the end
        if (project.getPackaging().equals("war") &&
            (project.hasParent() && !project.getParentArtifact().getGroupId().contains("appfuse"))) {
            // store sorted properties in a thread local for later retrieval
            Map properties = propertiesContextHolder.get();
            // alphabetize the properties by key
            Set<String> propertiesToAdd = new TreeSet<>(properties.keySet());

            StringBuffer calculatedProperties = new StringBuffer();

            for (String key : propertiesToAdd) {
                // don't add property if it already exists in project
                Set<Object> keysInProject = project.getParent().getOriginalModel().getProperties().keySet();
                if (!keysInProject.contains(key)) {
                    String value = getAppFuseProperties().getProperty(key);

                    if (value.contains("&amp;")) {
                        value = "<![CDATA[" + value + "]]>";
                    }

                    calculatedProperties.append("        <");
                    calculatedProperties.append(key);
                    calculatedProperties.append(">");
                    calculatedProperties.append(value);
                    calculatedProperties.append("</");
                    calculatedProperties.append(key);
                    calculatedProperties.append(">");
                    calculatedProperties.append("\n");
                }
            }

            try {
                String originalPom = FileUtils.readFileToString(new File("pom.xml"), "UTF-8");

                // Move modules to build section.
                originalPom = originalPom.replaceAll("  <modules>", "");
                originalPom = originalPom.replaceAll("    <module>.*?</module>", "");
                originalPom = originalPom.replaceAll("  </modules>", "");

                originalPom = originalPom.replace("<repositories>", "<modules>\n" +
                    "        <module>core</module>\n" +
                    "        <module>web</module>\n" +
                    "    </modules>\n\n    <repositories>");

                String pomWithProperties = addPropertiesToPom(originalPom, calculatedProperties);

                FileUtils.writeStringToFile(new File("pom.xml"), pomWithProperties, "UTF-8");
            } catch (IOException ex) {
                getLog().error("Unable to modify root pom.xml: " + ex.getMessage(), ex);
                throw new MojoFailureException(ex.getMessage());
            }
        }
    }

    private void renamePackages() {
        boolean renamePackages = true;
        if (System.getProperty("renamePackages") != null) {
            renamePackages = Boolean.valueOf(System.getProperty("renamePackages"));
        }

        if (renamePackages && !project.getPackaging().equals("pom")) {
            log("Renaming packages to '" + project.getGroupId() + "'...");
            RenamePackages renamePackagesTool = new RenamePackages(project.getGroupId());
            if (project.hasParent() && !project.getParentArtifact().getGroupId().contains("appfuse")) {
                renamePackagesTool.setBaseDir(project.getBasedir() + "/src");
            }

            renamePackagesTool.execute();
        }
    }

    private String getDependencyVersionOrThrowExceptionIfNotAvailable(Dependency dep) {
        String version = dep.getVersion();

        if (version == null) {
            version = getDependencyVersionFromDependencyManagementOrThrowExceptionIfNotAvailable(dep);
        }

        // trim off ${}
        if (version.startsWith("${")) {
            version = version.substring(2);
        }

        if (version.endsWith("}")) {
            version = version.substring(0, version.length() - 1);
        }
        return version;
    }

    @SuppressWarnings("unchecked")
    private String getDependencyVersionFromDependencyManagementOrThrowExceptionIfNotAvailable(Dependency dep) {
        DependencyManagement dependencyManagement = project.getDependencyManagement();
        if (dependencyManagement != null) {
            List<Dependency> managedDeps = dependencyManagement.getDependencies();
            for (Dependency managedDep : managedDeps) {
                if (managedDep.getArtifactId().equals(dep.getArtifactId()) &&
                    managedDep.getGroupId().equals(dep.getGroupId())) {
                    return managedDep.getVersion();
                }
            }
            throw new IllegalArgumentException(format(
                "Unable to determine version for dependency: %s:%s", dep.getGroupId(), dep.getArtifactId()));
        } else {
            throw new IllegalArgumentException(format(
                "Unable to determine version for dependency: %s:%s. DependencyManagement is null",
                dep.getGroupId(), dep.getArtifactId()));
        }
    }

    private static String addPropertiesToPom(String existingPomXmlAsString, StringBuffer sortedProperties) {
        String adjustedPom = existingPomXmlAsString;

        if (!"".equals(sortedProperties)) {
            // add new properties
            adjustedPom = adjustedPom.replace("<jdbc.password/>", "<jdbc.password/>" + LINE_SEP + LINE_SEP +
                "        <!-- Properties calculated by AppFuse when running full-source plugin -->\n" +
                sortedProperties);

            // also look for empty jdbc.password tag since the archetype plugin sometimes expands empty elements
            adjustedPom = adjustedPom.replace("<jdbc.password></jdbc.password>", "<jdbc.password/>" + LINE_SEP + LINE_SEP +
                "        <!-- Properties calculated by AppFuse when running full-source plugin -->\n" +
                sortedProperties);
        }

        adjustedPom = adjustedPom.replaceAll("<amp.fullSource>false</amp.fullSource>", "<amp.fullSource>true</amp.fullSource>");

        return adjustLineEndingsForOS(adjustedPom);
    }

    private static String adjustLineEndingsForOS(String adjustedPom) {
        String os = System.getProperty("os.name");

        if (os.startsWith("Linux") || os.startsWith("Mac")) {
            // remove the \r returns
            adjustedPom = adjustedPom.replaceAll("\r", "");
        } else if (os.startsWith("Windows")) {
            // use windows line endings
            adjustedPom = adjustedPom.replaceAll(">\n", ">\r\n");
        }

        return adjustedPom;
    }

    private Properties getAppFuseProperties() {
        // should only happen when executing full-source on modular modules (b/c they don't export root).
        if (appfuseProperties == null) {
            File pom = new File("target/appfuse-root/pom.xml");
            appfuseProperties = createProjectFromPom(pom).getOriginalModel().getProperties();
        }
        return appfuseProperties;
    }

    private String getFilePath(String s) {
        s = s.replace("/", FILE_SEP);
        //log("returning: " + s);
        return s;
    }

    private void export(String url, String destinationDirectory) throws MojoExecutionException {
        SubversionUtils svn = new SubversionUtils(trunk + tag + url, destinationDirectory);

        try {
            svn.export();
        } catch (SVNException e) {
            SVNErrorMessage err = e.getErrorMessage();

            /*
             * Display all tree of error messages.
             * Utility method SVNErrorMessage.getFullMessage() may be used instead of the loop.
             */
            while (err != null) {
                getLog()
                    .error(err.getErrorCode().getCode() + " : " +
                        err.getMessage());
                err = err.getChildErrorMessage();
            }

            throw new MojoExecutionException(e.getMessage());
        }
    }

    private void log(String msg) {
        getLog().info("[AppFuse] " + msg);
    }

    private List<Dependency> addModuleDependencies(List<Dependency> dependencies, String moduleName, String moduleLocation, String parentModule) {
        log("Adding dependencies from " + moduleName + " module...");

        // Read dependencies from module's pom.xml
        URL pomLocation = null;
        File newDir = new File(project.getFile().getParent(), "target/" + parentModule + "/appfuse-" + moduleName);

        if (!newDir.exists()) {
            newDir.mkdirs();
        }

        File pom = new File(project.getFile().getParent(), "target/" + parentModule + "/appfuse-" + moduleName + "/pom.xml");

        try {
            // replace github.com with raw.github.com and trunk with master
            trunk = trunk.replace("https://github.com", "https://raw.githubusercontent.com");
            tag = tag.replace("trunk", "master");
            // replace tag/branch with nothing
            if (tag.contains("tags/")) {
                tag = tag.replace("tags/", "");
            }
            if (tag.contains("branches/")) {
                tag = tag.replace("branches/", "");
            }

            // add separator if moduleLocation is populated
            if (!"".equals(moduleLocation)) {
                moduleLocation += "/";
            }
            pomLocation = new URL(trunk + tag + moduleLocation + "pom.xml");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Get get = (Get) AntUtils.createProject().createTask("get");
        get.setSrc(pomLocation);
        get.setDest(pom);
        get.execute();

        MavenProject p = createProjectFromPom(pom);

        List moduleDependencies = p.getOriginalModel().getDependencies();

        // set the properties for appfuse if root module
        if (moduleName.equalsIgnoreCase("root")) {
            appfuseProperties = p.getOriginalModel().getProperties();
        }

        // create a list of artifactIds to check for duplicates (there's no equals() on Dependency)
        Set<String> artifactIds = new LinkedHashSet<String>();

        for (Dependency dep : dependencies) {
            artifactIds.add(dep.getArtifactId());
        }

        // add all non-appfuse dependencies
        for (Object moduleDependency : moduleDependencies) {
            Dependency dep = (Dependency) moduleDependency;

            if (dep.getGroupId().equals("javax.servlet.jsp") && dep.getArtifactId().equals("jsp-api")
                && "jsf".equals(project.getProperties().getProperty("web.framework"))) {
                // skip adding dependency for old group id of jsp-api
                continue;
            }

            if (!artifactIds.contains(dep.getArtifactId()) &&
                !dep.getArtifactId().contains("appfuse")) {
                dependencies.add(dep);
            }
        }

        return dependencies;
    }

    private MavenProject createProjectFromPom(File pom) {
        try {
            return mavenProjectBuilder.buildWithDependencies(pom, local, null);
        } catch (Exception e) {
            getLog().warn("skip error reading maven project: " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * This method will movie files from the source directory to the destination directory based on
     * the pattern.
     *
     * @param inSourceDirectory      The source directory to copy from.
     * @param inDestinationDirectory The destination directory to copy to.
     * @param inPattern              The file pattern to match to locate files to copy.
     */
    protected void moveFiles(final String inSourceDirectory, final String inDestinationDirectory,
                             final String inPattern) {
        Move moveTask = (Move) antProject.createTask("move");

        FileSet fileSet = AntUtils.createFileset(inSourceDirectory, inPattern, new ArrayList());
        moveTask.setTodir(new File(inDestinationDirectory));
        moveTask.addFileset(fileSet);
        moveTask.execute();
    }

    /**
     * This method will movie files from the source directory to the destination directory based on
     * the pattern.
     *
     * @param from The source file to rename.
     * @param to   The file to rename to.
     */
    protected void renameFile(final File from, final File to) {
        Move moveTask = (Move) antProject.createTask("move");

        moveTask.setFile(from);
        moveTask.setTofile(to);
        moveTask.execute();
    }
}
