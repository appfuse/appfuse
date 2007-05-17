package org.appfuse.mojo.exporter;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.appfuse.mojo.HibernateExporterMojo;
import org.appfuse.tool.AppFuseExporter;
import org.hibernate.tool.hbm2x.Exporter;
import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Generates Java classes from set of annotated POJOs
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @goal gen
 * @phase generate-sources
 * @execute phase="compile"
 */
public class AppFuseGeneratorMojo extends HibernateExporterMojo {
    /**
     * This is a prompter that can be user within the maven framework.
     *
     * @component
     */
    protected Prompter prompter;

    private boolean generateCoreOnly;
    private boolean generateWebOnly;

    /**
     * @parameter expression="${appfuse.genericCore}" default-value="true"
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
        
        String pojoName = System.getProperty("entity");

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
