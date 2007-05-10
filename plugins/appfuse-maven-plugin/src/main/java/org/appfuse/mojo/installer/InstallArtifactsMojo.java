package org.appfuse.mojo.installer;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
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

    /**
     * This is a prompter that can be user within the maven framework.
     *
     * @component
     */
    Prompter prompter;
    Project project;
    String pojoName;
    String pojoNameLower;

    /**
     * The path where the generated artifacts will be placed. This is intentionally not set to the
     * default location for maven generated sources. This is to keep these files out of the
     * eclipse/idea generated sources directory as the intention is that these files will be copied
     * to a source directory to be edited and modified and not re generated each time the plugin is
     * run. If you want to regenerate the files each time you build the project just set this value
     * to ${basedir}/target/generated-sources or set the flag on eclipse/idea plugin to include this
     * file in your project file as a source directory.
     *
     * @parameter expression="${destinationDirectory}" default-value="${basedir}"
     */
    private String destinationDirectory;

    /**
     * The directory containing the source code.
     *
     * @parameter expression="${sourceDirectory}" default-value="${basedir}/target/appfuse/generated-sources"
     */
    private String sourceDirectory;

    public void execute() throws MojoExecutionException, MojoFailureException {
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
        copyGeneratedObjects(this.sourceDirectory, this.destinationDirectory, "**/*.java");
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
        project = AntUtils.createProject();
        Copy copyTask = (Copy) project.createTask("copy");
        copyTask.init();

        FileSet fileSet = AntUtils.createFileset(inSourceDirectory, inPattern, new ArrayList());
        getLog().info("Installing generated .java files...");
        copyTask.setTodir(new File(inDestinationDirectory));
        copyTask.addFileset(fileSet);
        copyTask.execute();

        getLog().info("Installing sample data for DbUnit...");
        installSampleData();

        getLog().info("Installing Spring bean definitions...");
        // todo: check if genericcore is off
        // todo: find/replace bean names instead of Person-START
        if ("false".equals(System.getProperty("generic-core"))) {
           installDaoAndManagerBeanDefinitions();
        } else {
           installGenericBeanDefinitions();
        }

        // todo: changed based on web framework
        getLog().info("Installing Struts and configuring...");
        installStrutsBeanDefinition();
        installStrutsActionDefinitions();
        installStrutsViews();

        getLog().info("Installing menu...");
        installMenu();

        getLog().info("Installing internationalization messages...");
        installInternationalizationKeys();

        getLog().info("Installing UI tests...");
        installUITests();
    }

    private String pojoLowerCase(String name) {
        return name.substring(0,1).toLowerCase()+ name.substring(1);
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
        File generatedFile = new File(destinationDirectory + "/src/main/webapp/WEB-INF/applicationContext.xml"); // todo: handle modular projects

        parseXMLFile(generatedFile, pojoName + "Dao", "<!-- Add new DAOs here -->", "dao.context.file");

        createLoadFileTask("src/main/resources/" + pojoName + "Manager-bean.xml", "mgr.context.file").execute();
        generatedFile = new File(destinationDirectory + "/src/main/webapp/WEB-INF/applicationContext.xml"); // todo: handle modular projects

        parseXMLFile(generatedFile, pojoName + "Manager", "<!-- Add new Managers here -->", "mgr.context.file");
    }

    private void installGenericBeanDefinitions() {
        createLoadFileTask("src/main/resources/" + pojoName + "-generic-beans.xml", "context.file").execute();
        File generatedFile = new File(destinationDirectory + "/src/main/webapp/WEB-INF/applicationContext.xml"); // todo: handle modular projects

        parseXMLFile(generatedFile, pojoName + "Manager", "<!-- Add new Managers here -->", "context.file");
    }

    private void installStrutsBeanDefinition() {
        createLoadFileTask("src/main/webapp/WEB-INF/" + pojoName + "-struts-bean.xml", "struts.context.file").execute();
        File generatedFile = new File(destinationDirectory + "/src/main/webapp/WEB-INF/applicationContext.xml"); // todo: handle modular projects

        parseXMLFile(generatedFile, pojoName + "Action", "<!-- Add new Actions here -->", "struts.context.file");
    }

    private void installStrutsActionDefinitions() {
        createLoadFileTask("src/main/resources/" + pojoName + "-struts.xml", "struts.file").execute();
        File existingFile = new File(destinationDirectory + "/src/main/resources/struts.xml");

        parseXMLFile(existingFile, pojoName + "Action", "<!-- Add additional actions here -->", "struts.file");
    }

    private void installStrutsViews() {
        Copy c1 = (Copy) project.createTask("copy");
        c1.setFile(new File(sourceDirectory + "/src/main/webapp/WEB-INF/pages/" + pojoName + "Form.jsp"));
        c1.setTofile(new File(destinationDirectory + "/src/main/webapp/WEB-INF/pages/" + pojoNameLower + "Form.jsp"));
        c1.execute();

        Copy c2 = (Copy) project.createTask("copy");
        c2.setFile(new File(sourceDirectory + "/src/main/webapp/WEB-INF/pages/" + pojoName + "List.jsp"));
        c2.setTofile(new File(destinationDirectory + "/src/main/webapp/WEB-INF/pages/" + pojoNameLower + "List.jsp"));
        c2.execute();
    }

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

        Echo echoTask = (Echo) project.createTask("echo");
        echoTask.setFile(existingFile);
        echoTask.setAppend(true);
        echoTask.setMessage(project.getProperty("i18n.file"));
        echoTask.execute();
    }

    private void installUITests() {
        createLoadFileTask("src/test/resources/" + pojoName + "-web-tests.xml", "web.tests").execute();
        File existingFile = new File(destinationDirectory + "/src/test/resources/web-tests.xml"); // todo: handle modular projects

        parseXMLFile(existingFile, pojoName, "</project>", "web.tests");

        // Add main target to run-all-tests target
        Replace replace = (Replace) project.createTask("replace");
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
        LoadFile loadFileTask = (LoadFile) project.createTask("loadfile");
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
        Replace replace1 = (Replace) project.createTask("replace");
        replace1.setFile(existingFile);
        replace1.setToken("<!--" + nameInComment + "-START-->");
        replace1.setValue("REGULAR-START");
        replace1.execute();

        Replace replace2 = (Replace) project.createTask("replace");
        replace2.setFile(existingFile);
        replace2.setToken("<!--" + nameInComment + "-END-->");
        replace2.setValue("REGULAR-END");
        replace2.execute();

        ReplaceRegExp regExpTask = (ReplaceRegExp) project.createTask("replaceregexp");
        regExpTask.setFile(existingFile);
        regExpTask.setMatch("REGULAR-START(?s:.)*REGULAR-END");
        regExpTask.setReplace("");
        regExpTask.setFlags("g");
        regExpTask.execute();

        Replace replaceData = (Replace) project.createTask("replace");
        replaceData.setFile(existingFile);
        replaceData.setToken(tokenToReplace);
        replaceData.setValue(project.getProperty(fileVariable));
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

        Replace replace1 = (Replace) project.createTask("replace");
        replace1.setFile(existingFile);
        replace1.setToken("# -- " + nameInComment + "-START");
        replace1.setValue("REGULAR-START");
        replace1.execute();

        Replace replace2 = (Replace) project.createTask("replace");
        replace2.setFile(existingFile);
        replace2.setToken("# -- " + nameInComment + "-END");
        replace2.setValue("REGULAR-END");
        replace2.execute();

        ReplaceRegExp regExpTask = (ReplaceRegExp) project.createTask("replaceregexp");
        regExpTask.setFile(existingFile);
        regExpTask.setMatch("REGULAR-START(?s:.)*REGULAR-END");
        regExpTask.setReplace("");
        regExpTask.setFlags("g");
        regExpTask.execute();
    }
}
