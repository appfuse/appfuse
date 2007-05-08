package org.appfuse.mojo.exporter;

import org.apache.maven.plugin.MojoExecutionException;
import org.appfuse.mojo.HibernateExporterMojo;
import org.appfuse.tool.AppFuseExporter;
import org.hibernate.tool.hbm2x.Exporter;

import java.io.File;

/**
 * Generates Java classes from set of annotated POJOs
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @goal gen-ui
 * @phase generate-sources
 * @execute phase="compile"
 */
public class AppFuseGeneratorMojo extends HibernateExporterMojo {
    /**
     * Default constructor.
     */
    public AppFuseGeneratorMojo() {
        addDefaultComponent("target/appfuse/generated-sources", "configuration", false);
        addDefaultComponent("target/appfuse/generated-sources", "annotationconfiguration", true);
    }

// --------------------- Interface ExporterMojo ---------------------

    /**
     * Returns <b>gen-ui</b>.
     *
     * @return String goal's name
     */
    public String getName() {
        return "gen-ui";
    }

    /**
     * @see org.appfuse.mojo.HibernateExporterMojo#configureExporter(org.hibernate.tool.hbm2x.Exporter)
     */
    protected Exporter configureExporter(Exporter exp) throws MojoExecutionException {
        // add output directory to compile roots
        getProject().addCompileSourceRoot(new File(getComponent().getOutputDirectory()).getPath());

        // now set the extra properties for the AppFuseExporter
        AppFuseExporter exporter = (AppFuseExporter) super.configureExporter(exp);
        exporter.getProperties().setProperty("ejb3", getComponentProperty("ejb3", "true"));
        exporter.getProperties().setProperty("jdk5", getComponentProperty("jdk5", "true"));

        // AppFuse-specific values
        exporter.getProperties().setProperty("basepackage", getProject().getGroupId());
        exporter.getProperties().setProperty("daoframework", getProject().getProperties().getProperty("dao.framework"));
        exporter.getProperties().setProperty("webframework", getProject().getProperties().getProperty("web.framework"));
        exporter.getProperties().setProperty("packaging", getProject().getPackaging());
        exporter.getProperties().setProperty("genericcore", getComponentProperty("genericcore", "true"));

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
}
