package org.appfuse.mojo.installer;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.tools.ant.taskdefs.Replace;
import org.apache.tools.ant.Project;
import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;
import org.appfuse.tool.ArtifactUninstaller;

import java.io.File;

/**
 * This mojo is used to "remove" installed artifacts installed by AMP.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @goal remove
 */
public class UninstallArtifactsMojo extends AbstractMojo {

    /**
     * Prompter for user interaction.
     *
     * @component
     * @readonly
     * @noinspection UnusedDeclaration
     */
    private Prompter prompter;

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
     * The path where the artifacts are installed.
     *
     * @parameter expression="${appfuse.installedDirectory}" default-value="${basedir}"
     * @noinspection UnusedDeclaration
     */
    private String installedDirectory;

    /**
     * @parameter expression="${appfuse.genericCore}" default-value="true"
     * @noinspection UnusedDeclaration
     */
    private boolean genericCore;

    public void execute() throws MojoExecutionException, MojoFailureException {
        // if project is of type "pom", throw an error
        if (project.getPackaging().equalsIgnoreCase("pom")) {
            String errorMsg = "[ERROR] This plugin cannot be run from a pom project, please run it from a jar or war project (i.e. core or web).";
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

        if (!"true".equals(System.getProperty("skip.areyousure"))) {
            try {
                String text = "WARNING: ALL artifacts will be removed, including your model object!";
                       text += "\nAre you sure you want to remove '" + pojoName + ".java' and all related artifacts? [Y/N]";
                String proceed = prompter.prompt(text);
                if (!"Y".equalsIgnoreCase(proceed)) {
                    log("Cancelling removal at your request.");
                    return;
                } else {
                    log("Proceeding... let's hope you're using source control!");
                }
            } catch (PrompterException pe) {
                pe.printStackTrace();
            }
        }

        if (pojoName == null) {
            throw new MojoExecutionException("You must specify an entity name to continue.");
        }

        ArtifactUninstaller uninstaller = new ArtifactUninstaller(project, pojoName, installedDirectory, genericCore);
        uninstaller.execute();

        String hibernateCfgLocation = installedDirectory + "/src/main/resources/hibernate.cfg.xml";
        // remove entity from hibernate.cfg.xml
        // this is to allow using hibernate.cfg.xml from core module
        if (project.getPackaging().equals("war") && (project.hasParent()
            && !project.getParentArtifact().getGroupId().contains("appfuse"))) {
            // assume first module in parent project has hibernate.cfg.xml
            String moduleName = (String) project.getParent().getModules().get(0);
            String pathToParent = project.getOriginalModel().getParent().getRelativePath();
            pathToParent = pathToParent.substring(0, pathToParent.lastIndexOf('/') + 1);
            log("Assuming '" + moduleName + "' has hibernate.cfg.xml in its src/main/resources directory");
            hibernateCfgLocation = project.getBasedir() + "/"
                    + pathToParent + moduleName + "/src/main/resources/hibernate.cfg.xml";
        }

        String daoFramework = (String) project.getProperties().get("dao.framework");

        if (daoFramework == null) {
            getLog().error("[ERROR] No <dao.framework> property found in pom.xml. Please add this property.");
        }

        if ("hibernate".equals(daoFramework)) {
            log("Removing mapping for '" + pojoName + "' from hibernate.cfg.xml");
            String className = project.getGroupId() + ".model." + pojoName;
            Project antProject = AntUtils.createProject();
            Replace replace = (Replace) antProject.createTask("replace");
            replace.setFile(new File(hibernateCfgLocation));
            replace.setToken("    <mapping class=\"" + className + "\"/>");
            replace.execute();
        }

        log("Removal succeeded! Please run 'mvn clean' to remove any compiled classes.");
    }

    private void log(String msg) {
        getLog().info("[AppFuse] " + msg);
    }
}
