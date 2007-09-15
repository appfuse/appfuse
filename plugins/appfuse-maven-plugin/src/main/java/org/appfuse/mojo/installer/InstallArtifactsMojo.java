package org.appfuse.mojo.installer;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;
import org.appfuse.tool.ArtifactInstaller;

/**
 * This mojo is used to "install" generated artifacts (Java files, XML files) into an AppFuse project.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @goal install
 * @phase generate-sources
 * @execute phase="compile"
 */
public class InstallArtifactsMojo extends AbstractMojo {

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

        ArtifactInstaller installer = new ArtifactInstaller(project, pojoName, sourceDirectory, destinationDirectory, genericCore);
        installer.execute();
    }
}
