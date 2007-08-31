package org.appfuse.mojo.refactor;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.appfuse.mojo.HibernateExporterMojo;
import org.appfuse.tool.FileUtils;
import org.hibernate.tool.hbm2x.Exporter;

/**
 * Generates Java classes from set of annotated POJOs
 *
 * @author <a href="mailto:david@capehenrytech.com">David L. Whitehurst</a>
 * @goal refactor-packages
 */
public class AppFusePackageRefactorMojo extends HibernateExporterMojo {

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        FileUtils tool = new FileUtils(getProject().getGroupId());
        tool.execute();
        
    }

    /**
     * extending Hibernate exporter only to access the Maven project
     *
     * @return null
     */
    protected Exporter createExporter() {
        return null;  //unused at this time
    }

    /**
     * extending Hibernate exporter only to access the Maven project
     *
     * @return null
     */
    public String getName() {
        return null;  //unused at this time
    }
}
