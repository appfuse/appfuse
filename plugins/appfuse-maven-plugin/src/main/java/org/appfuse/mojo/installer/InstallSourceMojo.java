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
import org.tmatesoft.svn.core.SVNErrorMessage;
import org.tmatesoft.svn.core.SVNException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import static java.util.Arrays.asList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * This mojo is used to "install" source artifacts from Subversion into an AppFuse project.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @goal full-source
 */
public class InstallSourceMojo extends AbstractMojo {
    private static final String APPFUSE_GROUP_ID = "org.appfuse";
    private static final List<String> JSF_PROPERTIES = asList("ajax4jsf", "myfaces.tomahawk", "corejsf.validator", "myfaces", "facelets", "el");
    private static final List<String> SPRING_PROPERTIES = asList("springmodules.validation");
    private static final List<String> STRUTS_PROPERTIES = asList("struts");
    private static final List<String> TAPESTRY_PROPERTIES = asList("tapestry.flash", "tapestry", "tapestry.spring");

    private String daoFramework;
    private String webFramework;
    Project antProject = AntUtils.createProject();
    
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
        // todo: get this working from the top-level directory - only works with basic project for now
        
        if (!project.getPackaging().equalsIgnoreCase("war")) {
            String errorMsg = "This plugin can currently only be run from an AppFuse web project (packaging == 'war').";
            //getLog().error(errorMsg);
            throw new MojoFailureException(errorMsg);
        }

        // If appfuse.version is specified as a property, and not a SNAPSHOT, use it for the tag
        String appfuseVersion = project.getProperties().getProperty("appfuse.version");
        if (appfuseVersion != null && !appfuseVersion.endsWith("SNAPSHOT") && tag.equals("trunk/")) {
            // todo: convert version to match tag
        }

        daoFramework = project.getProperties().getProperty("dao.framework");
        webFramework = project.getProperties().getProperty("web.framework");

        // install dao and manager source if modular/core or war writer/o parent (basic)
        if (project.getPackaging().equals("jar") || (project.getPackaging().equals("war") && project.getParent() == null)) {
            log("Installing source from data modules...");
            // export data-common
            export("data/common/src");

            // export persistence framework
            export("data/" + getDaoFramework() + "/src");

            // export service module
            log("Installing source from service module...");
            export("service/src");

            // add dependencies from appfuse-service to pom.xml
        }

        if (project.getPackaging().equalsIgnoreCase("war")) {
            // export web-common
            log("Installing source from web-common module...");
            export("web/common/src");

            // export web framework
            log("Installing source from " + webFramework + " module...");
            export("web/" + webFramework + "/src");
        }

        log("Source successfully exported, modifying pom.xml...");
        removeWarpathPlugin(new File("pom.xml"));

        List dependencies = project.getOriginalModel().getDependencies();
        List<Dependency> newDependencies = new ArrayList<Dependency>();

        // remove all appfuse dependencies
        for (Object dependency : dependencies) {
            Dependency dep = (Dependency) dependency;
            if (!dep.getGroupId().equals(APPFUSE_GROUP_ID)) {
                newDependencies.add(dep);
            }
        }

        // add dependencies from root appfuse pom
        newDependencies = addModuleDependencies(newDependencies, "root", "");

        // Add dependencies from appfuse-${dao.framework}
        newDependencies = addModuleDependencies(newDependencies, daoFramework, "data/" + daoFramework);
        
        // Add dependencies from appfuse-service
        newDependencies = addModuleDependencies(newDependencies, "service", "service");
        
        // Add dependencies from appfuse-common-web
        newDependencies = addModuleDependencies(newDependencies, "web-common", "web/common");

        // Add dependencies from appfuse-${web.framework}
        newDependencies = addModuleDependencies(newDependencies, webFramework, "web/" + webFramework, true);

        // Change spring-mock and jmock dependencies to use <optional>true</option> instead of <scope>test</scope>.
        // This is necessary because Base*TestCase classes are in src/main/java. If we move these classes to their
        // own test module, this will no longer be necessary. For the first version of this mojo, it seems easier
        // to follow the convention used in AppFuse rather than creating a test module and changing all modules to
        // depend on it.
        for (Dependency dep : newDependencies) {
            if (dep.getArtifactId().equals("spring-mock") || dep.getArtifactId().equals("jmock") || dep.getArtifactId().equals("junit")) {
                dep.setOptional(true);
                dep.setScope(null);
            }
        }

        Collections.sort(newDependencies, new BeanComparator("groupId"));

        project.getOriginalModel().setDependencies(newDependencies);

        // todo: figure out how to get these written in alphabetical order
        project.getOriginalModel().setProperties(getAppFuseProperties());

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
        dependencyXml = "    <!-- Dependencies calculated by AppFuse when running full-source plugin -->" + dependencyXml;

        try {
            String originalPom = FileUtils.readFileToString(new File("pom.xml"));
            startTag = originalPom.indexOf("\n    <dependencies>");

            StringBuffer sb = new StringBuffer();
            sb.append(originalPom.substring(0, startTag));
            sb.append(dependencyXml);
            sb.append(originalPom.substring(originalPom.indexOf("</dependencies>", startTag)));

            startTag = pomXml.indexOf("\n  <properties>");

            String propertiesXml = pomXml.substring(startTag + 16, pomXml.lastIndexOf("\n  </properties>"));
            // change 2 spaces to 4
            propertiesXml = propertiesXml.replaceAll("  ", "    ");
            // add the Spring version to the new properties since spring.version is replaced below
            propertiesXml = "        <spring.version>" + project.getProperties().getProperty("spring.version") + "</spring.version>\n" + propertiesXml;
            propertiesXml = "        <!-- Sorry about the ordering of the properties below, we hope to fix this in a future release -->\n" + propertiesXml;

            // add new properties
            String pomWithProperties = sb.toString().replaceFirst("        <spring.version>(.*)</spring.version>", propertiesXml);

            FileUtils.writeStringToFile(new File("pom.xml"), pomWithProperties);
        } catch (IOException ex) {
            getLog().error("Unable to write to pom.xml: " + ex.getMessage(), ex);
            throw new MojoFailureException(ex.getMessage());
        }

        log("Yeehaw - it worked! Unfortunately, this plugin doesn't rename packages yet, but it will in 2.0 Final");
        log("If you manually rename your packages, make sure and set <amp.fullSource> to true.");
        // todo: rename packages

        // todo: gather and add repositories from appfuse projects
        // should work for now since most artifacts are in static.appfuse.org/repository

        // cleanup so user isn't aware that files were created
        File pom = new File("pom-fullsource.xml");
        if (pom.exists()) {
            pom.delete();
        }
    }

    private void export(String url) throws MojoExecutionException {
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
                getLog().error(err.getErrorCode().getCode() + " : " + err.getMessage());
                err = err.getChildErrorMessage();
            }
            throw new MojoExecutionException(e.getMessage());
        }
    }

    private String getDaoFramework() {
        String fw = project.getProperties().getProperty("dao.framework");
        if (fw.equalsIgnoreCase("jpa-hibernate")) {
            return "jpa";
        } else {
            return fw;
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
        log("Adding dependencies from '" + moduleName + "' module...");
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
           this.removeWarpathPlugin(pom);
        }
        
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

        List moduleDependencies = p.getOriginalModel().getDependencies();

        // create a list of artifactIds to check for duplicates (there's no equals() on Dependency)
        Set<String> artifactIds = new LinkedHashSet<String>();
        for (Dependency dep : dependencies) {
            artifactIds.add(dep.getArtifactId());
        }

        // add all non-appfuse dependencies
        for (Object moduleDependency : moduleDependencies) {
            Dependency dep = (Dependency) moduleDependency;
            if (!artifactIds.contains(dep.getArtifactId()) && !dep.getArtifactId().contains("appfuse")) {
                dependencies.add(dep);
            }
        }

        return dependencies;
    }

    private Properties getAppFuseProperties() {
        log("Getting properties from 'appfuse' module...");
        // Read dependencies from module's pom.xml
        URL pomLocation = null;
        File newDir = new File("target", "appfuse");
        if (!newDir.exists()) {
            newDir.mkdirs();
        }

        File pom = new File("target/appfuse/pom.xml");

        try {
            pomLocation = new URL(trunk + tag + "/pom.xml");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Get get = (Get) AntUtils.createProject().createTask("get");
        get.setSrc(pomLocation);
        get.setDest(pom);
        get.setUsername("guest");
        get.setPassword("");
        get.execute();

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

        Properties props = p.getOriginalModel().getProperties();
        Properties projectProperties = project.getOriginalModel().getProperties();
        Properties newProperties = new Properties();

        // don't include dao and web framework dependencies
        List<String> webDependencies = new ArrayList<String>();
        webDependencies.addAll(JSF_PROPERTIES);
        webDependencies.addAll(SPRING_PROPERTIES);
        webDependencies.addAll(STRUTS_PROPERTIES);
        webDependencies.addAll(TAPESTRY_PROPERTIES);

        for (Object o : props.keySet()) {
            String key = (String) o;
            if (!projectProperties.containsKey(key)) {
                newProperties.put(key, props.getProperty(key));
            }
        }

        // NOTE: There's *got* to be a cleaner way to do this, but I can't think of it right now
        // ensure properties are only for the current frameworks being used

        // PROPOSED FIX: Read versions from webapp pom.xml and link keys to properties
        for (String key : webDependencies) {
            newProperties.remove(key + ".version");
        }

        newProperties.remove("hibernate.version");
        newProperties.remove("ibatis.version");
        newProperties.remove("jpa.version");

        if (webFramework.equalsIgnoreCase("jsf")) {
            for (String key : JSF_PROPERTIES) {
                key += ".version";
                newProperties.put(key, props.getProperty(key));
            }
        } else if (webFramework.equalsIgnoreCase("spring")) {
            for (String key : SPRING_PROPERTIES) {
                key += ".version";
                newProperties.put(key, props.getProperty(key));
            }
        } else if (webFramework.equalsIgnoreCase("struts")) {
            for (String key : STRUTS_PROPERTIES) {
                key += ".version";
                newProperties.put(key, props.getProperty(key));
            }
        } else if (webFramework.equalsIgnoreCase("tapestry")) {
            for (String key : TAPESTRY_PROPERTIES) {
                key += ".version";
                newProperties.put(key, props.getProperty(key));
            }
        }

        newProperties.put(getDaoFramework() + ".version", props.getProperty(getDaoFramework() + ".version"));

        return newProperties;
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
