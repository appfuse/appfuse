package org.appfuse.mojo.exporter;

import org.hibernate.tool.hbm2x.Exporter;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Generates Java classes and tests for DAOs and Managers from set of annotated POJOs.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @goal gen-core
 * @phase generate-sources
 * @execute phase="compile"
 */
public class AppFuseGenerateCoreMojo extends AppFuseGeneratorMojo {

    public AppFuseGenerateCoreMojo() {
        addDefaultComponent("target/appfuse/generated-sources", "configuration", false);
        addDefaultComponent("target/appfuse/generated-sources", "annotationconfiguration", true);
    }

    /**
     * Returns <b>gen-core</b>.
     *
     * @return String goal's name
     */
    public String getName() {
        return "gen-core";
    }

    @Override
    protected Exporter configureExporter(Exporter exp) throws MojoExecutionException {
        super.setGenerateCoreOnly(true);
        return super.configureExporter(exp);
    }
}
