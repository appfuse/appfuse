package org.appfuse.mojo.installer;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.io.FileUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectBuilder;
import org.apache.maven.settings.Settings;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Get;
import org.apache.tools.ant.taskdefs.LoadFile;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;


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
     *
     * @parameter expression="${settings}"
     * @required
     * @readonly
     *
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

        String daoFramework = project.getProperties().getProperty("dao.framework");

        if (daoFramework == null) {
            log("No dao.framework property specified, defaulting to 'hibernate'");
        }

        String webFramework = project.getProperties().getProperty("web.framework");

        boolean modular = (project.getPackaging().equals("pom") && !project.hasParent());
        if (project.getPackaging().equals("jar") || (project.getPackaging().equals("war") && !project.hasParent())) {
            // export data-common
            log("Installing source from data-common module...");
            export("data/common/src", (modular) ? "core/src" : destinationDirectory);

            // export persistence framework
            log("Installing source from " + daoFramework + " module...");
            export("data/" + daoFramework + "/src", (modular) ? "core/src" : destinationDirectory);

            // export service module
            log("Installing source from service module...");
            export("service/src", (modular) ? "core/src" : destinationDirectory);

            // move Base*TestCase to test directory
            moveFiles((modular) ? "core/src/main" : destinationDirectory + "/main",
                    (modular) ? "core/src/test" : destinationDirectory + "/test", "**/Base*TestCase.java");

            // delete dao.framework related files from test directory
            deleteFile("test/resources/hibernate.cfg.xml");
            deleteFile("test/resources/META-INF");
            deleteFile("test/resources/sql-map-config.xml");

            // If JPA, delete hibernate.cfg.xml b/c it will cause issues when
            // using jpaconfiguration with the hibernate3-maven-plugin
            if ("jpa".equalsIgnoreCase(daoFramework)) {
                deleteFile("main/resources/hibernate.cfg.xml");
            }
        }

        // it's OK if a project created with appfuse-ws doesn't have a web framework defined
        // currently, a project with appfuse-ws can be identified by enunciate
        boolean isWebServicesProject = false;
        for (Object pluginArtifact : project.getPluginArtifacts()) {
            if (((Artifact) pluginArtifact).getArtifactId().contains("enunciate")) {
                isWebServicesProject = true;
                break;
            }
        }

        if (project.getPackaging().equalsIgnoreCase("war")) {

            if (webFramework == null && !isWebServicesProject) {
                getLog().error("The web.framework property is not specified - please modify your pom.xml to add " +
                        " this property. For example: <web.framework>struts</web.framework>.");
                throw new MojoExecutionException("No web.framework property specified, please modify pom.xml to add it.");
            }

            if (project.hasParent()) {
                // copy jdbc.properties to core/src/test/resources
                //FileUtils.copyFileToDirectory(new File("src/main/resources/jdbc.properties"), new File("../core/src/test/resources"));

                // delete hibernate, ibatis and jpa files from web project
                deleteFile("main/resources/hibernate.cfg.xml");
                deleteFile("main/resources/META-INF");
                deleteFile("main/resources/sql-map-config.xml");

                // there's a jdbc.properties in test/resources that shouldn't be there
                deleteFile("test/resources/jdbc.properties");
            } else if (!isAppFuse()) {
                // there's a jdbc.properties in test/resources that shouldn't be there
                deleteFile("test/resources/jdbc.properties");
            }
        }

        log("Source successfully exported, modifying pom.xml...");

        List dependencies = project.getDependencies();
        List<Dependency> newDependencies = new ArrayList<Dependency>();

        // remove all appfuse dependencies
        for (Object dependency : dependencies) {
            Dependency dep = (Dependency) dependency;

            if (!dep.getGroupId().equals(APPFUSE_GROUP_ID)) {
                newDependencies.add(dep);
            }
        }

        if (!project.getPackaging().equals("pom") && !project.hasParent()) {

            // add dependencies from root appfuse pom
            newDependencies = addModuleDependencies(newDependencies, "root", "", "");

            // Add dependencies from appfuse-data
            newDependencies = addModuleDependencies(newDependencies, "data", "data", "");

            // Add dependencies from appfuse-data-common
            newDependencies = addModuleDependencies(newDependencies, "data-common", "data/common", "appfuse-data");

            // Add dependencies from appfuse-${dao.framework}
            newDependencies = addModuleDependencies(newDependencies, daoFramework, "data/" + daoFramework, "appfuse-data");

            // Add dependencies from appfuse-service
            newDependencies = addModuleDependencies(newDependencies, "service", "service", "");

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

                // Add dependencies from appfuse-data
                newDependencies = addModuleDependencies(newDependencies, "data", "data", "");

                // Add dependencies from appfuse-data-common
                newDependencies = addModuleDependencies(newDependencies, "data-common", "data/common", "appfuse-data");

                // Add dependencies from appfuse-${dao.framework}
                newDependencies = addModuleDependencies(newDependencies, daoFramework, "data/" + daoFramework, "appfuse-data");

                // Add dependencies from appfuse-service
                newDependencies = addModuleDependencies(newDependencies, "service", "service", "service");

                createFullSourcePom(newDependencies);
            }

            if (project.getPackaging().equals("war")) {
                newDependencies.clear();

                newDependencies = addWebDependencies(appfuseVersion, newDependencies, webFramework);

                createFullSourcePom(newDependencies);
            }
        }
    }

    private List<Dependency> addWebDependencies(String appfuseVersion, List<Dependency> newDependencies, String webFramework) {
        // Add dependencies from appfuse-common-web
        newDependencies = addModuleDependencies(newDependencies, "web", "web", "web");

        Double appfuseVersionAsDouble = new Double(appfuseVersion.substring(0, appfuseVersion.lastIndexOf(".")));

        getLog().debug("Detected AppFuse version: " + appfuseVersionAsDouble);

        if (isAppFuse() && appfuseVersionAsDouble < 2.1) {

            // Add dependencies from appfuse-common-web
            newDependencies = addModuleDependencies(newDependencies, "web-common", "web/common", "common");

            //newDependencies = addModuleDependencies(newDependencies, webFramework, "web/" + webFramework);
        }

        // modular archetypes still seem to need these - todo: figure out why
        if (isAppFuse() && project.getPackaging().equals("war") && project.hasParent()) {
            newDependencies = addModuleDependencies(newDependencies, "web-common", "web/common", "common");

            newDependencies = addModuleDependencies(newDependencies, webFramework, "web/" + webFramework, webFramework);
        }
        return newDependencies;
    }

    private boolean isAppFuse() {
        return (project.getProperties().getProperty("copyright.year") != null);
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
        // Change spring-test and jmock dependencies to use <optional>true</option> instead of <scope>test</scope>.
        // This is necessary because Base*TestCase classes are in src/main/java. If we move these classes to their
        // own test module, this will no longer be necessary. For the first version of this mojo, it seems easier
        // to follow the convention used in AppFuse rather than creating a test module and changing all modules to
        // depend on it.

        // create properties based on dependencies while we're at it
        Set<String> projectProperties = new TreeSet<String>();

        for (Dependency dep : newDependencies) {
            if (dep.getArtifactId().equals("spring-test") || dep.getArtifactId().contains("jmock") ||
                    dep.getArtifactId().equals("junit") || dep.getArtifactId().equals("shale-test")) {
                dep.setOptional(true);
                dep.setScope(null);
            }
            String version = dep.getVersion();
            // trim off ${}
            if (version.startsWith("${")) {
                version = version.substring(2);
            }

            if (version.endsWith("}")) {
                version = version.substring(0, version.length() - 1);
            }
            projectProperties.add(version);
        }

        // add core as a dependency for modular wars
        if (project.getPackaging().equals("war") && project.hasParent()) {
            Dependency core = new Dependency();
            core.setGroupId("${project.parent.groupId}");
            core.setArtifactId("core");
            core.setVersion("${project.parent.version}");
            newDependencies.add(core);

            // workaround for JSF requiring JSP 2.1 - this is a true hack
            if (project.getProperties().getProperty("web.framework").equals("jsf")) {
                Dependency jsp21 = new Dependency();
                jsp21.setGroupId("javax.servlet.jsp");
                jsp21.setArtifactId("jsp-api");
                jsp21.setVersion("${jsp.version}");
                jsp21.setScope("provided");
                newDependencies.add(jsp21);

                // replace jsp.version property as well
                project.getProperties().setProperty("jsp.version", "2.1");
            }
        }

        Collections.sort(newDependencies, new BeanComparator("groupId"));

        project.setDependencies(newDependencies);

        Properties currentProperties = project.getProperties();

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

                // hack for Tapestry depending on commons-pool (a.k.a. commons-dbcp 1.2.2)
                if ("tapestry".equals(project.getProperties().getProperty("web.framework")) && key.equals("commons.dbcp.version")) {
                    value = "1.2.2";
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

        log("Updated dependencies in pom.xml...");

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
            if (project.hasParent()) {
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
            if (!project.getPackaging().equals("pom") && !project.hasParent()) {
                adjustedPom = addPropertiesToPom(adjustedPom, sortedProperties);
            }

            adjustedPom = adjustLineEndingsForOS(adjustedPom);

            FileUtils.writeStringToFile(new File(pathToPom), adjustedPom, "UTF-8"); // was pomWithProperties
        } catch (IOException ex) {
            getLog().error("Unable to write to pom.xml: " + ex.getMessage(), ex);
            throw new MojoFailureException(ex.getMessage());
        }

        boolean renamePackages = true;
        if (System.getProperty("renamePackages") != null) {
            renamePackages = Boolean.valueOf(System.getProperty("renamePackages"));
        }

        if (renamePackages && !project.getPackaging().equals("pom")) {
            log("Renaming packages to '" + project.getGroupId() + "'...");
            RenamePackages renamePackagesTool = new RenamePackages(project.getGroupId());
            if (project.hasParent()) {
                if (project.getPackaging().equals("jar")) {
                    renamePackagesTool.setBaseDir("core/src");
                } else {
                    renamePackagesTool.setBaseDir("web/src");
                }
            }

            renamePackagesTool.execute();
        }

        // when performing full-source on a modular project, add the properties to the root pom.xml at the end
        if (project.getPackaging().equals("war") && project.hasParent()) {
            // store sorted properties in a thread local for later retrieval
            Map properties = propertiesContextHolder.get();
            // alphabetize the properties by key
            Set<String> propertiesToAdd = new TreeSet<String>(properties.keySet());

            StringBuffer calculatedProperties = new StringBuffer();

            for (String key : propertiesToAdd) {
                // don't add property if it already exists in project
                Set<Object> keysInProject = project.getParent().getProperties().keySet();
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
                // Because I hate fucking regex.
                originalPom = originalPom.replaceAll("    <module>.*?</module>", "");
                originalPom = originalPom.replaceAll("  </modules>", "");

                originalPom = originalPom.replace("<repositories>", "<modules>\n" +
                        "        <module>core</module>\n" +
                        "        <module>web</module>\n" +
                        "    </modules>\n\n    <repositories>");

                String pomWithProperties = addPropertiesToPom(originalPom, calculatedProperties);

                FileUtils.writeStringToFile(new File("pom.xml"), pomWithProperties, "UTF-8");
            } catch (IOException ex) {
                getLog().error("Unable to read root pom.xml: " + ex.getMessage(), ex);
                throw new MojoFailureException(ex.getMessage());
            }

        }

        // cleanup so user isn't aware that files were created
        File pom = new File("pom-fullsource.xml");

        if (pom.exists()) {
            pom.delete();
        }
    }

    private static String addPropertiesToPom(String existingPomXmlAsString, StringBuffer sortedProperties) {
        String adjustedPom = existingPomXmlAsString;

        // add new properties
        adjustedPom = adjustedPom.replace("<jdbc.password/>", "<jdbc.password/>" + LINE_SEP + LINE_SEP +
                "        <!-- Properties calculated by AppFuse when running full-source plugin -->\n" +
                sortedProperties);

        // also look for empty jdbc.password tag since the archetype plugin sometimes expands empty elements
        adjustedPom = adjustedPom.replace("<jdbc.password></jdbc.password>", "<jdbc.password/>" + LINE_SEP + LINE_SEP +
                "        <!-- Properties calculated by AppFuse when running full-source plugin -->\n" +
                sortedProperties);

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
            log("create appfuse-root/pom.xml");
            File pom = new File(project.getFile().getParent(),"target/appfuse-root/pom.xml");
            appfuseProperties = createProjectFromPom(pom).getProperties();
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

        File newDir = new File(project.getFile().getParent(), "target/"+ parentModule +"/appfuse-" + moduleName);

        if (!newDir.exists()) {
            newDir.mkdirs();
        }

        File pom = new File(project.getFile().getParent(),"target/" + parentModule +"/appfuse-" + moduleName + "/pom.xml");

        try {
            // replace github.com with raw.github.com and trunk with master
            trunk = trunk.replace("https://github.com", "https://raw.github.com");
            tag = tag.replace("trunk", "master");
            pomLocation = new URL(trunk + tag + moduleLocation + "/pom.xml");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Get get = (Get) AntUtils.createProject().createTask("get");
        get.setSrc(pomLocation);
        get.setDest(pom);
        get.execute();

        MavenProject p = createProjectFromPom(pom);

        List moduleDependencies = p.getDependencies();//  p.getOriginalModel().getDependencies();

        // set the properties for appfuse if root module
        if (moduleName.equalsIgnoreCase("root")) {
            appfuseProperties = p.getProperties();
        }

        // create a list of artifactIds to check for duplicates (there's no equals() on Dependency)
        Set<String> artifactIds = new LinkedHashSet<String>();

        for (Dependency dep : dependencies) {
            artifactIds.add(dep.getArtifactId());
        }

        // add all non-appfuse dependencies
        for (Object moduleDependency : moduleDependencies) {
            Dependency dep = (Dependency) moduleDependency;

            if (dep.getGroupId().equals("javax.servlet") && dep.getArtifactId().equals("jsp-api")
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
        /*MavenEmbedder maven = new MavenEmbedder();
        maven.setOffline(settings.isOffline());
        maven.setClassLoader(Thread.currentThread().getContextClassLoader());
        maven.setLogger(new MavenEmbedderConsoleLogger());

        MavenProject p = null;

        try {
            maven.setAlignWithUserInstallation( true );
            maven.setLocalRepositoryDirectory( new File(settings.getLocalRepository() ));

            maven.start();
            p = maven.readProjectWithDependencies(pom);
            maven.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return p;*/
        // olamy: this catch Exception and returns is IMHO crappy !
        // an exception must be throw and handle by caller !!
        try {
            return mavenProjectBuilder.buildWithDependencies( pom, local, null );
        } catch (Exception e) {
            getLog().warn( "skip error reading maven project: " + e.getMessage(), e );
        }
        return null;
    }

    /**
     * This method will create an ANT based LoadFile task based on an infile and a property name.
     * The property will be loaded with the infile for use later by the Replace task.
     *
     * @param inFile   The file to process
     * @param propName the name to assign it to
     * @return The ANT LoadFile task that loads a property with a file
     */
    protected LoadFile createLoadFileTask(String inFile, String propName) {
        LoadFile loadFileTask = (LoadFile) antProject.createTask("loadfile");
        loadFileTask.init();
        loadFileTask.setProperty(propName);
        loadFileTask.setSrcFile(new File(inFile));

        return loadFileTask;
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
}
