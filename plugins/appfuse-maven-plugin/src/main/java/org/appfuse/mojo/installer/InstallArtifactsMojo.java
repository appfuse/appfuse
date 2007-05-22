package org.appfuse.mojo.installer;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Copy;
import org.apache.tools.ant.taskdefs.Echo;
import org.apache.tools.ant.taskdefs.LoadFile;
import org.apache.tools.ant.taskdefs.Replace;
import org.apache.tools.ant.taskdefs.optional.ReplaceRegExp;
import org.apache.tools.ant.types.FileSet;
import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;

import java.io.File;
import java.util.ArrayList;

/**
 * This mojo is used to "install" generated artifacts (Java files, XML files) into an AppFuse project.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @goal install
 * @phase generate-sources
 * @execute phase="compile"
 */
public class InstallArtifactsMojo extends AbstractMojo {
    private static final String FILE_SEP = System.getProperty("file.separator");
    Project antProject;
    String pojoName;
    String pojoNameLower;

    /**
     * This is a prompter that can be user within the maven framework.
     *
     * @component
     */
    Prompter prompter;

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
     * The path where the generated artifacts will be placed. This is intentionally not set to the
     * default location for maven generated sources. This is to keep these files out of the
     * eclipse/idea generated sources directory as the intention is that these files will be copied
     * to a source directory to be edited and modified and not re generated each time the plugin is
     * run. If you want to regenerate the files each time you build the project just set this value
     * to ${basedir}/target/generated-sources or set the flag on eclipse/idea plugin to include this
     * file in your project file as a source directory.
     *
     * @parameter expression="${appfuse.destinationDirectory}" default-value="${basedir}"
     */
    private String destinationDirectory;

    /**
     * The directory containing the source code.
     *
     * @parameter expression="${appfuse.sourceDirectory}" default-value="${basedir}/target/appfuse/generated-sources"
     */
    private String sourceDirectory;

    /**
     * @parameter expression="${appfuse.genericCore}" default-value="true"
     */
    private boolean genericCore;

    public void execute() throws MojoExecutionException, MojoFailureException {
        // if project is of type "pom", throw an error
        if (project.getPackaging().equalsIgnoreCase("pom")) {
            String errorMsg = "Doh! This plugin cannot be run from a pom project, please run it from a jar or war project (i.e. core or web).";
            //getLog().error(errorMsg);
            throw new MojoFailureException(errorMsg);
        }

        pojoName = System.getProperty("entity");

        if (pojoName == null) {
            try {
                pojoName = prompter.prompt("What is the name of your pojo (i.e. Person)?");
            } catch (PrompterException pe) {
                pe.printStackTrace();
            }
        }

        if (pojoName == null) {
            throw new MojoExecutionException("You must specify an entity name to continue.");
        }

        pojoNameLower = pojoLowerCase(pojoName);

        //log("Installing generated .java files...");
        copyGeneratedObjects(this.sourceDirectory, this.destinationDirectory, "**/*.java");

        log("Installing sample data for DbUnit...");
        installSampleData();

        // install dao and manager if jar (modular/core) or war w/o parent (basic)
        if (project.getPackaging().equals("jar") || (project.getPackaging().equals("war") && project.getParent() == null)) {
            log("Installing Spring bean definitions...");
            if (genericCore) {
               installGenericBeanDefinitions();
            } else {
               installDaoAndManagerBeanDefinitions();
            }
        }

        if (project.getPackaging().equalsIgnoreCase("war")) {
            String webFramework = project.getProperties().getProperty("web.framework");

            if ("jsf".equalsIgnoreCase(webFramework)) {
                log("Installing JSF views and configuring...");
                installJSFNavigationAndBeans();
                installJSFViews();
            } else if ("struts".equalsIgnoreCase(webFramework)) {
                log("Installing Struts views and configuring...");
                installStrutsBeanDefinition();
                installStrutsActionDefinitions();
                copyGeneratedObjects(sourceDirectory + "/src/main/resources",
                        destinationDirectory + "/src/main/resources", "**/model/*.xml");
                copyGeneratedObjects(sourceDirectory + "/src/main/resources",
                        destinationDirectory + "/src/main/resources", "**/webapp/action/*.xml");
                installStrutsViews();
            } else if ("spring".equalsIgnoreCase(webFramework)) {
                log("Installing Spring views and configuring...");
                installSpringControllerBeanDefinitions();
                installSpringValidation();
                installSpringViews();
            } else if ("tapestry".equalsIgnoreCase(webFramework)) {
                log("Installing Tapestry views and configuring...");
                installTapestryViews();

                // The version of Tapestry we're using requires i18n messages to be in WEB-INF/tapestry.properties
                // todo: update to Tapestry 4.1.2 and remove the code below
                createLoadFileTask("src/main/resources/" + pojoName + "-ApplicationResources.properties", "i18n.tapestry").execute();
                File i18nFile = new File(destinationDirectory +  "/src/main/webapp/WEB-INF/tapestry.properties"); // todo: handle modular projects

                if (i18nFile.exists()) {
                    parsePropertiesFile(i18nFile, pojoName);

                    Echo echoTask = (Echo) antProject.createTask("echo");
                    echoTask.setFile(i18nFile);
                    echoTask.setAppend(true);
                    echoTask.setMessage(antProject.getProperty("i18n.tapestry"));
                    echoTask.execute();
                } else {
                    log("Installing i18n messages...");
                    installInternationalizationKeys();
                    Copy copy = (Copy) antProject.createTask("copy");
                    copy.setFile(new File(destinationDirectory + "/src/main/resources/ApplicationResources.properties"));
                    copy.setTofile(new File(destinationDirectory + "/src/main/webapp/WEB-INF/tapestry.properties"));
                    copy.execute();
                }
            }

            // todo: remove if statement when Tapestry i18n issue above is solved
            if (!"tapestry".equalsIgnoreCase(webFramework)) {
                log("Installing i18n messages...");
                installInternationalizationKeys();
            }

            log("Installing menu...");
            installMenu();

            log("Installing UI tests...");
            installUITests();
        }
    }

    /**
     * This method will copy files from the source directory to the destination directory based on
     * the pattenr.
     *
     * @param inSourceDirectory      The source directory to copy from.
     * @param inDestinationDirectory The destination directory to copy to.
     * @param inPattern              The file pattern to match to locate files to copy.
     */
    protected void copyGeneratedObjects(final String inSourceDirectory, final String inDestinationDirectory,
                                        final String inPattern) {
        antProject = AntUtils.createProject();
        Copy copyTask = (Copy) antProject.createTask("copy");

        FileSet fileSet = AntUtils.createFileset(inSourceDirectory, inPattern, new ArrayList());
        log("Installing generated files (pattern: " + inPattern + ")...");
        copyTask.setTodir(new File(inDestinationDirectory));
        copyTask.addFileset(fileSet);
        copyTask.execute();
    }

    private String pojoLowerCase(String name) {
        return name.substring(0,1).toLowerCase()+ name.substring(1);
    }

    private String getPathToApplicationContext() {
        if (project.getPackaging().equalsIgnoreCase("war")) {
            return "/src/main/webapp/WEB-INF/applicationContext.xml";
        } else { // if (project.getPackaging().equalsIgnoreCase("jar")) {
            return "/src/main/resources/applicationContext.xml";
        } 
    }

    /**
     * Add sample-data.xml to project's sample-data.xml
     */
    private void installSampleData() {
        createLoadFileTask("src/test/resources/" + pojoName + "-sample-data.xml", "sample.data").execute();
        File existingFile = new File(destinationDirectory + "/src/test/resources/sample-data.xml"); // todo: handle modular projects

        parseXMLFile(existingFile, null, "</dataset>", "sample.data");
    }

    private void installDaoAndManagerBeanDefinitions() {
        createLoadFileTask("src/main/resources/" + pojoName + "Dao-bean.xml", "dao.context.file").execute();
        File generatedFile = new File(destinationDirectory + getPathToApplicationContext());

        parseXMLFile(generatedFile, pojoName + "Dao", "<!-- Add new DAOs here -->", "dao.context.file");

        createLoadFileTask("src/main/resources/" + pojoName + "Manager-bean.xml", "mgr.context.file").execute();
        generatedFile = new File(destinationDirectory + getPathToApplicationContext()); 

        parseXMLFile(generatedFile, pojoName + "Manager", "<!-- Add new Managers here -->", "mgr.context.file");
    }

    private void installGenericBeanDefinitions() {
        createLoadFileTask("src/main/resources/" + pojoName + "-generic-beans.xml", "context.file").execute();
        File generatedFile = new File(destinationDirectory + getPathToApplicationContext());

        parseXMLFile(generatedFile, pojoName + "Manager", "<!-- Add new Managers here -->", "context.file");
    }

    private void installJSFNavigationAndBeans() {
        createLoadFileTask("src/main/webapp/WEB-INF/" + pojoName + "-navigation.xml", "navigation.rules").execute();
        File generatedFile = new File(destinationDirectory + "/src/main/webapp/WEB-INF/faces-config.xml"); // todo: handle modular projects
        parseXMLFile(generatedFile, pojoName + "-nav", "<!-- Add additional rules here -->", "navigation.rules");

        createLoadFileTask("src/main/webapp/WEB-INF/" + pojoName + "-managed-beans.xml", "managed.beans").execute();
        generatedFile = new File(destinationDirectory + "/src/main/webapp/WEB-INF/faces-config.xml"); // todo: handle modular projects
        parseXMLFile(generatedFile, pojoName + "-beans", "<!-- Add additional beans here -->", "managed.beans");
    }


    private void installSpringControllerBeanDefinitions() {
        createLoadFileTask("src/main/webapp/WEB-INF/" + pojoName + "-beans.xml", "dispatcher.servlet").execute();
        File generatedFile = new File(destinationDirectory + "/src/main/webapp/WEB-INF/dispatcher-servlet.xml"); // todo: handle modular projects

        parseXMLFile(generatedFile, pojoName, "<!-- Add additional controller beans here -->", "dispatcher.servlet");
    }

    private void installSpringValidation() {
        createLoadFileTask("src/main/webapp/WEB-INF/" + pojoName + "-validation.xml", "struts.validation").execute();
        File generatedFile = new File(destinationDirectory + "/src/main/webapp/WEB-INF/validation.xml"); // todo: handle modular projects

        parseXMLFile(generatedFile, pojoName, "    </formset>", "struts.validation");
    }

    private void installStrutsBeanDefinition() {
        createLoadFileTask("src/main/webapp/WEB-INF/" + pojoName + "-struts-bean.xml", "struts.context.file").execute();
        File generatedFile = new File(destinationDirectory + getPathToApplicationContext()); // todo: handle modular projects

        parseXMLFile(generatedFile, pojoName + "Action", "<!-- Add new Actions here -->", "struts.context.file");
    }

    private void installStrutsActionDefinitions() {
        createLoadFileTask("src/main/resources/" + pojoName + "-struts.xml", "struts.file").execute();
        File existingFile = new File(destinationDirectory + "/src/main/resources/struts.xml");

        parseXMLFile(existingFile, pojoName + "Action", "<!-- Add additional actions here -->", "struts.file");
    }

    // =================== Views ===================

    private void installJSFViews() {
        Copy copy = (Copy) antProject.createTask("copy");
        copy.setFile(new File(sourceDirectory + "/src/main/webapp/" + pojoName + "Form.xhtml"));
        copy.setTofile(new File(destinationDirectory + "/src/main/webapp/" + pojoNameLower + "Form.xhtml"));
        copy.execute();

        copy.setFile(new File(sourceDirectory + "/src/main/webapp/" + pojoName + "s.xhtml"));
        copy.setTofile(new File(destinationDirectory + "/src/main/webapp/" + pojoNameLower + "s.xhtml"));
        copy.execute();
    }

    private void installSpringViews() {
        Copy copy = (Copy) antProject.createTask("copy");
        copy.setFile(new File(sourceDirectory + "/src/main/webapp/WEB-INF/pages/" + pojoName + "form.jsp"));
        copy.setTofile(new File(destinationDirectory + "/src/main/webapp/WEB-INF/pages/" + pojoNameLower + "form.jsp"));
        copy.execute();

        copy.setFile(new File(sourceDirectory + "/src/main/webapp/WEB-INF/pages/" + pojoName + "s.jsp"));
        copy.setTofile(new File(destinationDirectory + "/src/main/webapp/WEB-INF/pages/" + pojoNameLower + "s.jsp"));
        copy.execute();
    }

    private void installStrutsViews() {
        Copy copy = (Copy) antProject.createTask("copy");
        copy.setFile(new File(sourceDirectory + "/src/main/webapp/WEB-INF/pages/" + pojoName + "Form.jsp"));
        copy.setTofile(new File(destinationDirectory + "/src/main/webapp/WEB-INF/pages/" + pojoNameLower + "Form.jsp"));
        copy.execute();

        copy.setFile(new File(sourceDirectory + "/src/main/webapp/WEB-INF/pages/" + pojoName + "List.jsp"));
        copy.setTofile(new File(destinationDirectory + "/src/main/webapp/WEB-INF/pages/" + pojoNameLower + "List.jsp"));
        copy.execute();
    }

    private void installTapestryViews() {
        Copy copy = (Copy) antProject.createTask("copy");
        copy.setFile(new File(sourceDirectory + "/src/main/webapp/WEB-INF/tapestry/" + pojoName + "Form.html"));
        copy.setTodir(new File(destinationDirectory + "/src/main/webapp/WEB-INF/tapestry"));
        copy.execute();

        copy.setFile(new File(sourceDirectory + "/src/main/webapp/WEB-INF/tapestry/" + pojoName + "Form.page"));
        copy.execute();

        copy.setFile(new File(sourceDirectory + "/src/main/webapp/WEB-INF/tapestry/" + pojoName + "List.html"));
        copy.execute();

        copy.setFile(new File(sourceDirectory + "/src/main/webapp/WEB-INF/tapestry/" + pojoName + "List.page"));
        copy.execute();
    }

    // =================== End of Views ===================

    private void installMenu() {
        createLoadFileTask("src/main/webapp/common/" + pojoName + "-menu.jsp", "menu.jsp").execute();
        File existingFile = new File(destinationDirectory + "/src/main/webapp/common/menu.jsp"); // todo: handle modular projects

        parseXMLFile(existingFile, pojoName, "</ul>", "menu.jsp");

        createLoadFileTask("src/main/webapp/WEB-INF/" + pojoName + "-menu-config.xml", "menu.config").execute();
        existingFile = new File(destinationDirectory + "/src/main/webapp/WEB-INF/menu-config.xml"); // todo: handle modular projects

        parseXMLFile(existingFile, pojoName, "    </Menus>", "menu.config");
    }

    private void installInternationalizationKeys() {
        createLoadFileTask("src/main/resources/" + pojoName + "-ApplicationResources.properties", "i18n.file").execute();
        File existingFile = new File(destinationDirectory +  "/src/main/resources/ApplicationResources.properties"); // todo: handle modular projects

        parsePropertiesFile(existingFile, pojoName);

        Echo echoTask = (Echo) antProject.createTask("echo");
        echoTask.setFile(existingFile);
        echoTask.setAppend(true);
        echoTask.setMessage(antProject.getProperty("i18n.file"));
        echoTask.execute();
    }

    private void installUITests() {
        createLoadFileTask("src/test/resources/" + pojoName + "-web-tests.xml", "web.tests").execute();
        File existingFile = new File(destinationDirectory + "/src/test/resources/web-tests.xml"); // todo: handle modular projects

        parseXMLFile(existingFile, pojoName, "</project>", "web.tests");

        // Add main target to run-all-tests target
        Replace replace = (Replace) antProject.createTask("replace");
        replace.setFile(existingFile);
        // todo: figure out how to fix the 2 lines below so they don't include pojoNameTest
        // multiple times on subsequent installs
        replace.setToken(",FileUpload");
        replace.setValue(",FileUpload," + pojoName + "Tests");
        replace.execute();
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
        inFile = sourceDirectory + FILE_SEP + inFile;
        LoadFile loadFileTask = (LoadFile) antProject.createTask("loadfile");
        loadFileTask.init();
        loadFileTask.setProperty(propName);
        loadFileTask.setSrcFile(new File(inFile));

        return loadFileTask;
    }

    private void parseXMLFile(File existingFile, String beanName, String tokenToReplace, String fileVariable) {
        String nameInComment = beanName;
        if (beanName == null) {
            nameInComment = pojoName;
        }
        Replace replace1 = (Replace) antProject.createTask("replace");
        replace1.setFile(existingFile);
        replace1.setToken("<!--" + nameInComment + "-START-->");
        replace1.setValue("REGULAR-START");
        replace1.execute();

        Replace replace2 = (Replace) antProject.createTask("replace");
        replace2.setFile(existingFile);
        replace2.setToken("<!--" + nameInComment + "-END-->");
        replace2.setValue("REGULAR-END");
        replace2.execute();

        ReplaceRegExp regExpTask = (ReplaceRegExp) antProject.createTask("replaceregexp");
        regExpTask.setFile(existingFile);
        regExpTask.setMatch("REGULAR-START(?s:.)*REGULAR-END");
        regExpTask.setReplace("");
        regExpTask.setFlags("g");
        regExpTask.execute();

        Replace replaceData = (Replace) antProject.createTask("replace");
        replaceData.setFile(existingFile);
        replaceData.setToken(tokenToReplace);
        replaceData.setValue(antProject.getProperty(fileVariable));
        replaceData.execute();
    }

    /**
     * This file is the same as the method above, except for different comment placeholder formats.
     * Yeah, I know, it's ugly.
     * @param existingFile file to merge with in project
     * @param beanName name of placeholder string that goes in comment
     */
    private void parsePropertiesFile(File existingFile, String beanName) {
        String nameInComment = beanName;
        if (beanName == null) {
            nameInComment = pojoName;
        }

        Replace replace1 = (Replace) antProject.createTask("replace");
        replace1.setFile(existingFile);
        replace1.setToken("# -- " + nameInComment + "-START");
        replace1.setValue("REGULAR-START");
        replace1.execute();

        Replace replace2 = (Replace) antProject.createTask("replace");
        replace2.setFile(existingFile);
        replace2.setToken("# -- " + nameInComment + "-END");
        replace2.setValue("REGULAR-END");
        replace2.execute();

        ReplaceRegExp regExpTask = (ReplaceRegExp) antProject.createTask("replaceregexp");
        regExpTask.setFile(existingFile);
        regExpTask.setMatch("REGULAR-START(?s:.)*REGULAR-END");
        regExpTask.setReplace("");
        regExpTask.setFlags("g");
        regExpTask.execute();
    }

    private void log(String msg) {
        getLog().info("[AppFuse] " + msg);
    }

    public void setProject(MavenProject project) {
        this.project = project;
    }

    public void setGenericCore(boolean genericCore) {
        this.genericCore = genericCore;
    }
}
