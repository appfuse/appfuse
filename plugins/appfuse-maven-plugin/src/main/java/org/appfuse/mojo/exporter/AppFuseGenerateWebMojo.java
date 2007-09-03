package org.appfuse.mojo.exporter;

import org.hibernate.tool.hbm2x.Exporter;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Generates Java classes and tests for DAOs and Managers from set of annotated POJOs.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @goal gen-web
 * @phase generate-sources
 * @execute phase="compile"
 */
public class AppFuseGenerateWebMojo extends AppFuseGeneratorMojo {

    public AppFuseGenerateWebMojo() {
        addDefaultComponent("target/appfuse/generated-sources", "configuration", false);
        addDefaultComponent("target/appfuse/generated-sources", "annotationconfiguration", true);
    }

    /**
     * Returns <b>gen-web</b>.
     *
     * @return String goal's name
     */
    public String getName() {
        return "gen-web";
    }

    @Override
    protected Exporter configureExporter(Exporter exp) throws MojoExecutionException {
        super.setGenerateWebOnly(true);
        return super.configureExporter(exp);
    }
}
