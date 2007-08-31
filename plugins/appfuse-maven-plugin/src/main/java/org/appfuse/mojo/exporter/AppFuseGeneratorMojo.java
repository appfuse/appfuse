package org.appfuse.mojo.exporter;

import org.apache.maven.plugin.MojoExecutionException;
import org.appfuse.mojo.HibernateExporterMojo;
import org.appfuse.tool.AppFuseExporter;
import org.appfuse.tool.Installer;
import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;
import org.hibernate.tool.hbm2x.Exporter;

import java.io.File;

/**
 * Generates Java classes from set of annotated POJOs
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

    /**
     * @see org.appfuse.mojo.HibernateExporterMojo#configureExporter(org.hibernate.tool.hbm2x.Exporter)
     */
    protected Exporter configureExporter(Exporter exp) throws MojoExecutionException {
        /*String objectType = System.getProperty("type");
        if (objectType == null) {
            try {
                List<String> options = new ArrayList<String>(2);
                options.add("pojo");
                options.add("table");
                
                objectType = prompter.prompt("Would you like to generate code from a table or POJO?", options, "pojo");
            } catch (PrompterException pe) {
                pe.printStackTrace();
            }
        }

        if (objectType != null && objectType.equalsIgnoreCase("table")) {
            // todo: generate from database with ModelGeneratorMojo
            *//*ModelGeneratorMojo mojo = new ModelGeneratorMojo();
            mojo.setProject(getProject());
            Map<String, String> m = new HashMap<String, String>();
            m.put("propertiesfile", "src/main/resources/jdbc.properties");
            mojo.setComponentProperties(m);
            mojo.addDefaultComponent("target/appfuse/generated-sources", "jdbcconfiguration", false);
            try {
                mojo.execute();
            } catch (MojoFailureException mfe) {
                mfe.printStackTrace();
            }*//*
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
}
