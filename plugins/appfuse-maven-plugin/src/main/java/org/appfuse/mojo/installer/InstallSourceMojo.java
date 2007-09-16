package org.appfuse.mojo.installer;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.io.FileUtils;
import org.apache.maven.embedder.MavenEmbedder;
import org.apache.maven.embedder.MavenEmbedderConsoleLogger;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Get;
import org.apache.tools.ant.taskdefs.LoadFile;
import org.apache.tools.ant.taskdefs.optional.ReplaceRegExp;
import org.appfuse.tool.SubversionUtils;
import org.appfuse.tool.RenamePackages;
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
     * @parameter expression="${appfuse.trunk}" default-value="https://appfuse.dev.java.net/svn/appfuse/"
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

    public void execute() throws MojoExecutionException, MojoFailureException {
        // If appfuse.version is specified as a property, and not a SNAPSHOT, use it for the tag
        String appfuseVersion = project.getProperties().getProperty("appfuse.version");

        if ((appfuseVersion != null) && !appfuseVersion.endsWith("SNAPSHOT") && tag.equals("trunk/")) {
            tag = "tags/APPFUSE_" + appfuseVersion.toUpperCase().replaceAll("-", "_") + "/";
        }

        String daoFramework = project.getProperties().getProperty("dao.framework");

        if (daoFramework == null) {
            log("No dao.framework property specified, defaulting to 'hibernate'");
        }

        String webFramework = project.getProperties().getProperty("web.framework");

        boolean modular = (project.getPackaging().equals("pom") && !project.hasParent());

        if (project.getPackaging().equals("jar") || (project.getPackaging().equals("war") && !project.hasParent())) {
            log("Installing source from data modules...");
            // export data-common
            export("data/common/src", (modular) ? "core/src" : destinationDirectory);

            // export persistence framework
            export("data/" + daoFramework + "/src", (modular) ? "core/src" : destinationDirectory);

            // if jpa or hibernate, remove duplicate file in test directory
            if ("hibernate".equalsIgnoreCase(daoFramework)) {
                String filePath;
                if (project.getPackaging().equalsIgnoreCase("jar") && project.hasParent()) {
                    filePath = "src/main/resources/hibernate.cfg.xml";
                } else {
                    filePath = "src/test/resources/hibernate.cfg.xml";
                }

                File duplicateFile = new File(getFilePath(filePath));

                if (duplicateFile.exists()) {
                    //log("Deleting duplicate hibernate.cfg.xml from src/test/resources...");

                    try {
                        FileUtils.forceDeleteOnExit(duplicateFile);
                    } catch (IOException io) {
                        getLog().error("Failed to delete '" + filePath + "', please delete manually.");
                    }
                }
            } else if ("jpa".equalsIgnoreCase(daoFramework)) {
                String filePath;
                if (project.getPackaging().equalsIgnoreCase("jar") && !project.hasParent()) {
                    filePath = "src/main/resources/META-INF";
                } else {
                    filePath = "src/test/resources/META-INF";
                }

                File duplicateFile = new File(getFilePath(filePath));

                if (duplicateFile.exists()) {
                    try {
                        // For some reason, this just deletes persistence.xml, not the META-INF directory.
                        // I tried FileUtils.deleteDirectory(duplicateFile), but no dice.
                        FileUtils.forceDeleteOnExit(duplicateFile);
                    } catch (IOException io) {
                        getLog().error("Failed to delete '" + filePath + "/persistence.xml', please delete manually.");
                    }
                }
            } else if ("ibatis".equalsIgnoreCase(daoFramework)) {
                String filePath;
                if (project.getPackaging().equalsIgnoreCase("jar") && project.hasParent()) {
                    filePath = "src/main/resources/sql-map-config.xml";
                } else {
                    filePath = "src/test/resources/sql-map-config.xml";
                }

                File duplicateFile = new File(getFilePath(filePath));

                if (duplicateFile.exists()) {
                    try {
                        FileUtils.forceDeleteOnExit(duplicateFile);
                    } catch (IOException io) {
                        getLog().error("Failed to delete '" + filePath + "', please delete manually.");
                    }
                }
            }

            // export service module
            log("Installing source from service module...");
            export("service/src", (modular) ? "core/src" : destinationDirectory);
        }

        if (project.getPackaging().equalsIgnoreCase("war")) {
            if (webFramework == null) {
                getLog().error("The web.framework property is not specified - please modify your pom.xml to add " +
                    " this property. For example: <web.framework>struts</web.framework>.");
                throw new MojoExecutionException("No web.framework property specified, please modify pom.xml to add it.");
            }

            // export web-common
            log("Installing source from web-common module...");
            export("web/common/src", (modular) ? "web/src" : destinationDirectory);

            // export web framework
            log("Installing source from " + webFramework + " module...");
            export("web/" + webFramework + "/src", (modular) ? "web/src" : destinationDirectory);

            if (project.hasParent()) {
                try {
                    // copy jdbc.properties to core/src/test/resources
                    FileUtils.copyFileToDirectory(new File("web/src/main/resources/jdbc.properties"), new File("core/src/test/resources"));
                } catch (IOException io) {
                    getLog().error("Failed to copy jdbc.properties to core module");
                }
            }
        }

        log("Source successfully exported, modifying pom.xml...");

        if (project.getPackaging().equals("pom")) {
            removeWarpathPlugin(new File("web/pom.xml"));
        } else if (project.getPackaging().equals("war")) {
            removeWarpathPlugin(new File("pom.xml"));
        }

        List dependencies = project.getOriginalModel().getDependencies();
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
            newDependencies = addModuleDependencies(newDependencies, "root", "");

            // Add dependencies from appfuse-data-common
            newDependencies = addModuleDependencies(newDependencies, "data-common", "data/common");

            // Add dependencies from appfuse-${dao.framework}
            newDependencies = addModuleDependencies(newDependencies, daoFramework, "data/" + daoFramework);

            // Add dependencies from appfuse-service
            newDependencies = addModuleDependencies(newDependencies, "service", "service");

            if (project.getPackaging().equals("war")) {
                // Add dependencies from appfuse-common-web
                newDependencies = addModuleDependencies(newDependencies, "web-common", "web/common");

                // Add dependencies from appfuse-${web.framework}
                newDependencies = addModuleDependencies(newDependencies, webFramework, "web/" + webFramework, true);
            }

            createFullSourcePom(newDependencies);
        } else {
            if (project.getPackaging().equals("pom")) {
                // add dependencies from root appfuse pom
                newDependencies = addModuleDependencies(newDependencies, "root", "");

                createFullSourcePom(newDependencies);
            }

            if (project.getPackaging().equals("jar")) {
                newDependencies.clear();

                // Add dependencies from appfuse-data-common
                newDependencies = addModuleDependencies(newDependencies, "data-common", "data/common");

                // Add dependencies from appfuse-${dao.framework}
                newDependencies = addModuleDependencies(newDependencies, daoFramework, "data/" + daoFramework);

                // Add dependencies from appfuse-service
                newDependencies = addModuleDependencies(newDependencies, "service", "service");

                createFullSourcePom(newDependencies);
            }

            if (project.getPackaging().equals("war")) {
                newDependencies.clear();

                // Add dependencies from appfuse-common-web
                newDependencies = addModuleDependencies(newDependencies, "web-common", "web/common");

                // Add dependencies from appfuse-${web.framework}
                newDependencies = addModuleDependencies(newDependencies, webFramework, "web/" + webFramework, true);

                createFullSourcePom(newDependencies);
            }
        }
    }

    private void createFullSourcePom(List<Dependency> newDependencies) throws MojoFailureException {
        // Change spring-mock and jmock dependencies to use <optional>true</option> instead of <scope>test</scope>.
        // This is necessary because Base*TestCase classes are in src/main/java. If we move these classes to their
        // own test module, this will no longer be necessary. For the first version of this mojo, it seems easier
        // to follow the convention used in AppFuse rather than creating a test module and changing all modules to
        // depend on it.

        // create properties based on dependencies while we're at it
        Set<String> projectProperties = new TreeSet<String>();

        for (Dependency dep : newDependencies) {
            if (dep.getArtifactId().equals("spring-mock") || dep.getArtifactId().equals("jmock") ||
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
            core.setGroupId("${pom.parent.groupId}");
            core.setArtifactId("${pom.parent.artifactId}-core");
            core.setVersion("${pom.parent.version}");
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
                project.getOriginalModel().getProperties().setProperty("jsp.version", "2.1");
            }
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

            String originalPom = FileUtils.readFileToString(new File(pathToPom));
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

            FileUtils.writeStringToFile(new File(pathToPom), adjustedPom); // was pomWithProperties
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
                    renamePackagesTool.setBaseDir("core");
                } else {
                    renamePackagesTool.setBaseDir("web");
                }
            }

            renamePackagesTool.execute();
        }

        // todo: gather and add repositories from appfuse projects
        // should work for now since most artifacts are in static.appfuse.org/repository

        // when performing full-source on a modular project, add the properties to the root pom.xml at the end
        if (project.getPackaging().equals("war") && project.hasParent()) {
            // store sorted properties in a thread local for later retrieval
            Map properties = propertiesContextHolder.get();
             // alphabetize the properties by key
            Set<String> propertiesToAdd = new TreeSet<String>(properties.keySet());

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
                String originalPom = FileUtils.readFileToString(new File("pom.xml"));

                String pomWithProperties = addPropertiesToPom(originalPom, calculatedProperties);

                FileUtils.writeStringToFile(new File("pom.xml"), pomWithProperties);
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

        // fix for Windows
        if (adjustedPom.contains("</properties>\r\n</project>")) {
            adjustedPom = adjustedPom.replace("</properties>\r\n</project>", "</properties>\n</project>");
        }

        // add new properties
        adjustedPom = adjustedPom.replace("</properties>\n</project>", LINE_SEP +
                "        <!-- Properties calculated by AppFuse when running full-source plugin -->\n" +
                        sortedProperties + "    </properties>\n</project>");
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

    private void removeWarpathPlugin(File pom) {
        log("Removing maven-warpath-plugin...");

        Project antProject = AntUtils.createProject();
        ReplaceRegExp regExpTask = (ReplaceRegExp) antProject.createTask("replaceregexp");
        regExpTask.setFile(pom);
        regExpTask.setMatch("\\s*<plugin>\\s*<groupId>org.appfuse</groupId>(?s:.)*?<artifactId>maven-warpath-plugin</artifactId>(?s:.)*?</plugin>");
        regExpTask.setReplace("");
        regExpTask.setFlags("g");
        regExpTask.execute();

        // remove any warpath dependencies as well
        ReplaceRegExp regExpTask2 = (ReplaceRegExp) antProject.createTask("replaceregexp");
        regExpTask2.setFile(pom);
        regExpTask2.setMatch("\\s*<dependency>\\s*<groupId>\\$\\{pom\\.groupId\\}</groupId>(?s:.)*?<type>warpath</type>(?s:.)*?</dependency>");
        regExpTask2.setReplace("");
        regExpTask2.setFlags("g");
        regExpTask2.execute();

        ReplaceRegExp regExpTask3 = (ReplaceRegExp) antProject.createTask("replaceregexp");
        regExpTask3.setFile(pom);
        regExpTask3.setMatch("\\s*<dependency>\\s*<groupId>org.appfuse</groupId>(?s:.)*?<type>warpath</type>(?s:.)*?</dependency>");
        regExpTask3.setReplace("");
        regExpTask3.setFlags("g");
        regExpTask3.execute();
    }

    // Convenience method that doesn't remove warpath plugin
    private List<Dependency> addModuleDependencies(List<Dependency> dependencies, String moduleName, String moduleLocation) {
        return addModuleDependencies(dependencies, moduleName, moduleLocation, false);
    }

    private List<Dependency> addModuleDependencies(List<Dependency> dependencies, String moduleName, String moduleLocation, boolean removeWarpath) {
        log("Adding dependencies from " + moduleName + " module...");

        // Read dependencies from module's pom.xml
        URL pomLocation = null;
        File newDir = new File("target", "appfuse-" + moduleName);

        if (!newDir.exists()) {
            newDir.mkdirs();
        }

        File pom = new File("target/appfuse-" + moduleName + "/pom.xml");

        try {
            pomLocation = new URL(trunk + tag + moduleLocation + "/pom.xml");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Get get = (Get) AntUtils.createProject().createTask("get");
        get.setSrc(pomLocation);
        get.setDest(pom);
        get.setUsername("guest");
        get.setPassword("");
        get.execute();

        if (removeWarpath) {
            removeWarpathPlugin(pom);
        }

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
        MavenEmbedder maven = new MavenEmbedder();
        maven.setOffline(true);
        maven.setClassLoader(Thread.currentThread().getContextClassLoader());
        maven.setLogger(new MavenEmbedderConsoleLogger());

        MavenProject p = null;

        try {
            maven.start();
            p = maven.readProjectWithDependencies(pom);
            maven.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return p;
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
}
