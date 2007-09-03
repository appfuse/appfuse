package org.appfuse.mojo.exporter;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.appfuse.mojo.HibernateExporterMojo;
import org.appfuse.tool.AppFuseExporter;
import org.appfuse.tool.Installer;
import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;
import org.hibernate.tool.hbm2x.Exporter;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Generates Java classes from set of annotated POJOs. Use -DdisableInstallation to prevent installation.
 * If using this goal in a "core" module or project, only DAOs and Managers will be created. For "web"
 * modules, the same principle applies.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @goal gen
 * @phase generate-sources
 * @execute phase="compile"
 */
public class AppFuseGeneratorMojo extends HibernateExporterMojo {
    boolean generateCoreOnly;
    boolean generateWebOnly;
    String pojoName;
    String pojoNameLower;

    /**
     * This is a prompter that can be user within the maven framework.
     *
     * @component
     */
    Prompter prompter;

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
     * Allows disabling installation - for tests and end users that don't want to do a full installation
     *
     * @parameter expression="${appfuse.disableInstallation}" default-value="false"
     */
    private boolean disableInstallation;

    /**
     * @parameter expression="${appfuse.genericCore}" default-value="true"
     * @noinspection UnusedDeclaration
     */
    private boolean genericCore;

    /**
     * Default constructor.
     */
    public AppFuseGeneratorMojo() {
        addDefaultComponent("target/appfuse/generated-sources", "configuration", false);
        addDefaultComponent("target/appfuse/generated-sources", "annotationconfiguration", true);
    }

// --------------------- Interface ExporterMojo ---------------------

    /**
     * Returns <b>gen</b>.
     *
     * @return String goal's name
     */
    public String getName() {
        return "gen";
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        // if project is of type "pom", throw an error
        if (getProject().getPackaging().equalsIgnoreCase("pom")) {
            String errorMsg = "Doh! This plugin cannot be run from a pom project, please run it from a jar or war project (i.e. core or web).";
            //getLog().error(errorMsg);
            throw new MojoFailureException(errorMsg);
        }

        /*String objectType = System.getProperty("type");
        if (objectType == null) {
            try {
                List<String> options = new ArrayList<String>(2);
                options.add("pojo");
                options.add("table");

                objectType = prompter.prompt("Would you like to generate code from a table or a POJO?", options, "pojo");
            } catch (PrompterException pe) {
                pe.printStackTrace();
            }
        }

        if (objectType != null && objectType.equalsIgnoreCase("table")) {
            // see http://issues.appfuse.org/browse/APF-869
        }*/

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

        String daoFramework = getProject().getProperties().getProperty("dao.framework");

        // If dao.framework is jpa, change to jpaconfiguration and persistence.xml should be found in classpath.
        // No other configuration is needed.
        if (daoFramework.indexOf("jpa") > -1) {
            getComponentProperties().put("implementation", "jpaconfiguration");
        }

        // for war projects that have a parent pom, don't reset classpath
        // this is to allow using hibernate.cfg.xml from core module
        if (getProject().getPackaging().equals("war") && getProject().hasParent()) {
            // assume first module in parent project has hibernate.cfg.xml
            String moduleName = (String) getProject().getParent().getModules().get(0);
            String pathToParent = getProject().getOriginalModel().getParent().getRelativePath();
            pathToParent = pathToParent.substring(0, pathToParent.lastIndexOf('/') + 1);
            log("Assuming '" + moduleName + "' has hibernate.cfg.xml in its src/main/resources directory");
            getComponentProperties().put("configurationfile",
                    getProject().getBasedir() + "/" + pathToParent + moduleName + "/src/main/resources/hibernate.cfg.xml");
        }

        if (getComponentProperty("configurationfile") == null) {
            getComponentProperties().put("configurationfile", "src/main/resources/hibernate.cfg.xml");
        }

        // if entity is not in hibernate.cfg.xml, add it
        String hibernateConfig = getComponentProperty("configurationfile");
        try {
            String hibernateCfgXml = FileUtils.readFileToString(new File(hibernateConfig));
            addEntityToHibernateCfgXml(hibernateCfgXml);
        } catch (IOException io) {
            throw new MojoFailureException(io.getMessage());
        }

        // If dao.framework is ibatis, programmatically create a hibernate.cfg.xml and put it in the classpath
        if (daoFramework.equals("ibatis")) {
            try {
                // if no hibernate.cfg.xml exists, create one from template in plugin
                String hibernateCfgXml;
                File existingConfig = new File(hibernateConfig);
                if (!existingConfig.exists()) {
                    InputStream in = this.getClass().getResourceAsStream("/appfuse/dao/ibatis/hibernate.cfg.ftl");
                    StringBuffer configFile = new StringBuffer();
                    try {
                        InputStreamReader isr = new InputStreamReader(in);
                        BufferedReader reader = new BufferedReader(isr);
                        String line;
                        while ((line = reader.readLine()) != null) {
                            configFile.append(line).append("\n");
                        }
                        reader.close();
                    } catch (IOException io) {
                        throw new MojoFailureException(io.getMessage());
                    }

                    hibernateCfgXml = configFile.toString();
                } else {
                    hibernateCfgXml = FileUtils.readFileToString(existingConfig);
                }

                addEntityToHibernateCfgXml(hibernateCfgXml);
            } catch (IOException io) {
                io.printStackTrace();
                getLog().error("Failed to copy hibernate.cfg.xml into classpath: " + io.getMessage());
            }
        }

        super.execute();
    }

    private void addEntityToHibernateCfgXml(String hibernateCfgXml) throws MojoFailureException {
        String className = getProject().getGroupId() + ".model." + pojoName;
        if (!hibernateCfgXml.contains(pojoName)) {
            hibernateCfgXml = hibernateCfgXml.replace("</session-factory>",
                    "    <mapping class=\"" + className + "\"/>"
                    + "\n    </session-factory>");
            log("Adding '" + pojoName + "' to hibernate.cfg.xml...");
        }

        hibernateCfgXml = hibernateCfgXml.replaceAll("\\$\\{appfusepackage}",
                (isFullSource()) ? getProject().getGroupId() : "org.appfuse");

        try {
            FileUtils.writeStringToFile(new File(
                    getComponentProperty("configurationfile", "src/main/resources/hibernate.cfg.xml")), hibernateCfgXml);
        } catch (IOException io) {
            throw new MojoFailureException(io.getMessage());
        }
    }

    /**
     * @see org.appfuse.mojo.HibernateExporterMojo#configureExporter(org.hibernate.tool.hbm2x.Exporter)
     */
    protected Exporter configureExporter(Exporter exp) throws MojoExecutionException {
        // Read in AppFuseExporter#configureExporter to decide if a class should be generated or not
        System.setProperty("appfuse.entity", pojoName);

        // add output directory to compile roots
        getProject().addCompileSourceRoot(new File(getComponent().getOutputDirectory()).getPath());

        // now set the extra properties for the AppFuseExporter
        AppFuseExporter exporter = (AppFuseExporter) super.configureExporter(exp);
        exporter.getProperties().setProperty("ejb3", getComponentProperty("ejb3", "true"));
        exporter.getProperties().setProperty("jdk5", getComponentProperty("jdk5", "true"));

        if (generateCoreOnly) {
            exporter.getProperties().setProperty("generate-core", "true");
        } else if (generateWebOnly) {
            exporter.getProperties().setProperty("generate-web", "true");
        }

        // AppFuse-specific values
        exporter.getProperties().setProperty("basepackage", getProject().getGroupId());
        exporter.getProperties().setProperty("daoframework", getProject().getProperties().getProperty("dao.framework"));
        exporter.getProperties().setProperty("webframework", getProject().getProperties().getProperty("web.framework"));
        exporter.getProperties().setProperty("packaging", getProject().getPackaging());
        exporter.getProperties().setProperty("genericcore", String.valueOf(genericCore));

        if (isFullSource())
            exporter.getProperties().setProperty("appfusepackage", getProject().getGroupId());
        else {
            exporter.getProperties().setProperty("appfusepackage", "org.appfuse");
        }

        return exporter;
    }

    /**
     * Executes the plugin in an isolated classloader.
     *
     * @throws MojoExecutionException When there is an erro executing the plugin
     */
    @Override
    protected void doExecute() throws MojoExecutionException {
        super.doExecute();

        if (System.getProperty("disableInstallation") != null) {
            disableInstallation = Boolean.valueOf(System.getProperty("disableInstallation"));
        }
        
        // allow installation to be supressed when testing
        if (!disableInstallation) {
            Installer installer = new Installer(getProject(), pojoName, sourceDirectory, destinationDirectory, genericCore);
            installer.execute();
        }
    }

    /**
     * Instantiates a org.appfuse.tool.AppFuseExporter object.
     *
     * @return POJOExporter
     */
    protected Exporter createExporter() {
        return new AppFuseExporter();
    }

    protected void setGenerateCoreOnly(boolean generateCoreOnly) {
        this.generateCoreOnly = generateCoreOnly;
    }

    protected void setGenerateWebOnly(boolean generateWebOnly) {
        this.generateWebOnly = generateWebOnly; 
    }

    private void log(String msg) {
        getLog().info("[AppFuse] " + msg);
    }
}
