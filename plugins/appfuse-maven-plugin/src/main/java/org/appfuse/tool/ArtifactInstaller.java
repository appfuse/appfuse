package org.appfuse.tool;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.apache.maven.project.MavenProject;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Copy;
import org.apache.tools.ant.taskdefs.Echo;
import org.apache.tools.ant.taskdefs.LoadFile;
import org.apache.tools.ant.taskdefs.Replace;
import org.apache.tools.ant.taskdefs.optional.ReplaceRegExp;
import org.apache.tools.ant.types.FileSet;
import org.appfuse.mojo.installer.AntUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * This class is responsible for installing generated CRUD artifacts into an AppFuse application.
 * @author mraible
 */
public class ArtifactInstaller {
    private Log log;
    static final String FILE_SEP = System.getProperty("file.separator");
    Project antProject;
    String pojoName;
    String pojoNameLower;
    String destinationDirectory;
    String sourceDirectory;
    MavenProject project;
    boolean genericCore;

    public ArtifactInstaller(MavenProject project, String pojoName, String sourceDirectory, String destinationDirectory, boolean genericCore) {
        this.project = project;
        this.pojoName = pojoName;
        this.pojoNameLower = pojoLowerCase(pojoName);
        this.sourceDirectory = sourceDirectory;
        this.destinationDirectory = destinationDirectory;
        this.genericCore = genericCore;
    }

    public void execute() {
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
            // only installs if iBATIS is configured as dao.framework
            installiBATISFiles();
        }

        if (project.getPackaging().equalsIgnoreCase("war")) {
            String webFramework = project.getProperties().getProperty("web.framework");

            if ("jsf".equalsIgnoreCase(webFramework)) {
                log("Installing JSF views and configuring...");
                installJSFNavigationAndBeans();
                installJSFViews();
            } else if ("struts".equalsIgnoreCase(webFramework)) {
                log("Installing Struts views and configuring...");
                // A bean definition for an Action is not used anymore (APF-798)
                // installStrutsBeanDefinition();
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
            }

            log("Installing i18n messages...");
            installInternationalizationKeys();

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
        File existingFile = new File(destinationDirectory + "/src/test/resources/sample-data.xml");

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

    private void installiBATISFiles() {
        if (project.getProperties().getProperty("dao.framework").equals("ibatis")) {
            log("Installing iBATIS SQL Maps...");
            createLoadFileTask("src/main/resources/" + pojoName + "-sql-map-config.xml", "sql.map.config").execute();
            File sqlMapConfig = new File(destinationDirectory + "/src/main/resources/sql-map-config.xml");
            parseXMLFile(sqlMapConfig, null, "</sqlMapConfig>", "sql.map.config");

            File sqlMapsDir = new File(destinationDirectory + "/src/main/resources/sqlmaps");
            if (sqlMapsDir.exists()) {
                sqlMapsDir.mkdir();
            }

            Copy copy = (Copy) antProject.createTask("copy");
            copy.setFile(new File(sourceDirectory + "/src/main/resources/sqlmaps/" + pojoName + "SQL.xml"));
            copy.setTodir(new File(destinationDirectory + "/src/main/resources/sqlmaps"));
            copy.execute();
        }
    }

    private void installGenericBeanDefinitions() {
        createLoadFileTask("src/main/resources/" + pojoName + "-generic-beans.xml", "context.file").execute();
        File generatedFile = new File(destinationDirectory + getPathToApplicationContext());

        parseXMLFile(generatedFile, pojoName + "Manager", "<!-- Add new Managers here -->", "context.file");
    }

    private void installJSFNavigationAndBeans() {
        createLoadFileTask("src/main/webapp/WEB-INF/" + pojoName + "-navigation.xml", "navigation.rules").execute();
        File generatedFile = new File(destinationDirectory + "/src/main/webapp/WEB-INF/faces-config.xml");
        parseXMLFile(generatedFile, pojoName + "-nav", "<!-- Add additional rules here -->", "navigation.rules");

        createLoadFileTask("src/main/webapp/WEB-INF/" + pojoName + "-managed-beans.xml", "managed.beans").execute();
        generatedFile = new File(destinationDirectory + "/src/main/webapp/WEB-INF/faces-config.xml");
        parseXMLFile(generatedFile, pojoName + "-beans", "<!-- Add additional beans here -->", "managed.beans");
    }

    private void installSpringControllerBeanDefinitions() {
        createLoadFileTask("src/main/webapp/WEB-INF/" + pojoName + "-beans.xml", "dispatcher.servlet").execute();
        File generatedFile = new File(destinationDirectory + "/src/main/webapp/WEB-INF/dispatcher-servlet.xml");

        parseXMLFile(generatedFile, pojoName, "<!-- Add additional controller beans here -->", "dispatcher.servlet");
    }

    private void installSpringValidation() {
        createLoadFileTask("src/main/webapp/WEB-INF/" + pojoName + "-validation.xml", "struts.validation").execute();
        File generatedFile = new File(destinationDirectory + "/src/main/webapp/WEB-INF/validation.xml");

        parseXMLFile(generatedFile, pojoName, "    </formset>", "struts.validation");
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
        copy.setFile(new File(sourceDirectory + "/src/main/webapp/" + pojoName + "Form.tml"));
        copy.setTodir(new File(destinationDirectory + "/src/main/webapp"));
        copy.execute();

        copy.setFile(new File(sourceDirectory + "/src/main/webapp/" + pojoName + "List.tml"));
        copy.execute();
    }

    // =================== End of Views ===================

    private void installMenu() {
        createLoadFileTask("src/main/webapp/common/" + pojoName + "-menu.jsp", "menu.jsp").execute();
        File existingFile = new File(destinationDirectory + "/src/main/webapp/common/menu.jsp");

        parseXMLFile(existingFile, pojoName, "</ul>", "menu.jsp");

        createLoadFileTask("src/main/webapp/WEB-INF/" + pojoName + "-menu-config.xml", "menu.config").execute();
        existingFile = new File(destinationDirectory + "/src/main/webapp/WEB-INF/menu-config.xml");

        parseXMLFile(existingFile, pojoName, "    </Menus>", "menu.config");
    }

    private void installInternationalizationKeys() {
        createLoadFileTask("src/main/resources/" + pojoName + "-ApplicationResources.properties", "i18n.file").execute();
        File existingFile = new File(destinationDirectory +  "/src/main/resources/ApplicationResources.properties");

        parsePropertiesFile(existingFile, pojoName);

        Echo echoTask = (Echo) antProject.createTask("echo");
        echoTask.setFile(existingFile);
        echoTask.setAppend(true);
        echoTask.setMessage(antProject.getProperty("i18n.file"));
        echoTask.execute();
    }

    private void installUITests() {
        createLoadFileTask("src/test/resources/" + pojoName + "-web-tests.xml", "web.tests").execute();
        File existingFile = new File(destinationDirectory + "/src/test/resources/web-tests.xml");

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
        String stringWithProperLineEndings = adjustLineEndingsForOS(antProject.getProperty(fileVariable));
        replaceData.setValue(stringWithProperLineEndings);
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
    
    private static String adjustLineEndingsForOS(String property) {
        String os = System.getProperty("os.name");

        if (os.startsWith("Linux") || os.startsWith("Mac")) {
            // remove the \r returns
            property = property.replaceAll("\r", "");
        } else if (os.startsWith("Windows")) {
            // use windows line endings
            property = property.replaceAll(">\n", ">\r\n");
        }

        return property;
    }

    private void log(String msg) {
        getLog().info("[AppFuse] " + msg);
    }

    public Log getLog() {
        if (log == null) {
            log = new SystemStreamLog();
        }

        return log;
    }
    
    public void setProject(MavenProject project) {
        this.project = project;
    }

    public void setGenericCore(boolean genericCore) {
        this.genericCore = genericCore;
    }
}
