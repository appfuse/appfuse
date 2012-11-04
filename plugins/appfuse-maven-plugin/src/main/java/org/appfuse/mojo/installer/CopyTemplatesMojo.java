package org.appfuse.mojo.installer;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.appfuse.tool.SubversionUtils;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNErrorMessage;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * This mojo is used to copy FreeMarker templates from AMP into an AppFuse project.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @goal copy-templates
 */
public class CopyTemplatesMojo extends AbstractMojo {

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
     * The directory containing the source code.
     *
     * @parameter expression="${appfuse.trunk}" default-value="https://github.com/appfuse/appfuse/"
     */
    private String trunk;

    /**
     * The tag containing the source code - defaults to '/trunk', but you may want to set it to '/tags/TAGNAME'
     *
     * @parameter expression="${appfuse.tag}" default-value="trunk/"
     */
    private String tag;

    /**
     * @parameter expression="${appfuse.templateDirectory}" default-value="src/test/resources/appfuse"
     * @noinspection UnusedDeclaration
     */
    private String templateDirectory;

    public void execute() throws MojoExecutionException, MojoFailureException {
        // TODO: Copy files from JAR instead of SVN
        // If appfuse.version is specified as a property, and not a SNAPSHOT, use it for the tag
        String appfuseVersion = project.getProperties().getProperty("appfuse.version");

        if ((appfuseVersion != null) && !appfuseVersion.endsWith("SNAPSHOT") && tag.equals("trunk/")) {
            tag = "tags/APPFUSE_" + appfuseVersion.toUpperCase().replaceAll("-", "_") + "/";
        }

        String daoFramework = project.getProperties().getProperty("dao.framework");

        if (daoFramework == null) {
            log("No dao.framework property specified, defaulting to 'hibernate'");
        }

        String webFramework = project.getProperties().getProperty("web.framework");

        boolean modular = (project.getPackaging().equals("pom") && !project.hasParent());

        log("Installing templates in " + templateDirectory + "...");

        if (!new File(templateDirectory).exists()) {
            FileUtils.mkdir(templateDirectory);
        }

        if (project.getPackaging().equals("jar") || (project.getPackaging().equals("war") && !project.hasParent())) {

            // export model templates
            log("Installing model templates...");
            if (!new File(templateDirectory + "/model").exists()) {
                FileUtils.mkdir(templateDirectory + "/model");
            }
            export("plugins/appfuse-maven-plugin/src/main/resources/appfuse/model/",
              ((modular) ? "core/" + templateDirectory : templateDirectory) + "/model");

            // export dao templates
            log("Installing " + daoFramework + " templates...");
            if (!new File(templateDirectory + "/dao").exists()) {
                FileUtils.mkdir(templateDirectory + "/dao");
            }

            export("plugins/appfuse-maven-plugin/src/main/resources/appfuse/dao",
              ((modular) ? "core/" + templateDirectory : templateDirectory) + "/dao");

            // delete templates that aren't for current dao.framework
            try {
                File daoDir = new File(templateDirectory + "/dao");
                String[] dirs = daoDir.list();

                for (String dir : dirs) {
                    if (new File(templateDirectory + "/dao/" + dir).isDirectory()) {
                        if (!dir.equals(daoFramework)) {
                            FileUtils.deleteDirectory(templateDirectory + "/dao/" + dir);
                        }
                    }
                }
            } catch (IOException io) {
                throw new MojoFailureException(io.getMessage());
            }

            // export manager templates
            log("Installing service templates...");
            if (!new File(templateDirectory + "/service").exists()) {
                FileUtils.mkdir(templateDirectory + "/service");
            }
            export("plugins/appfuse-maven-plugin/src/main/resources/appfuse/service/",
              ((modular) ? "core/" + templateDirectory : templateDirectory) + "/service");
        }

        if (project.getPackaging().equalsIgnoreCase("war")) {
            if (webFramework == null) {
                getLog().error("The web.framework property is not specified - please modify your pom.xml to add " +
                    " this property. For example: <web.framework>struts</web.framework>.");
                throw new MojoExecutionException("No web.framework property specified, please modify pom.xml to add it.");
            }

            // export web templates
            log("Installing " + webFramework + " templates...");
            if (!new File(templateDirectory + "/web").exists()) {
                FileUtils.mkdir(templateDirectory + "/web");
            }

            export("plugins/appfuse-maven-plugin/src/main/resources/appfuse/web",
              ((modular) ? "web/" + templateDirectory : templateDirectory) + "/web");

            // delete templates that aren't for current web.framework
            try {
                File webDir = new File(templateDirectory + "/web");
                String[] dirs = webDir.list();

                for (String dir : dirs) {
                    if (new File(templateDirectory + "/web/" + dir).isDirectory()) {
                        if (!dir.equals(webFramework)) {
                            FileUtils.deleteDirectory(templateDirectory + "/web/" + dir);
                        }
                    }
                }
            } catch (IOException io) {
                throw new MojoFailureException(io.getMessage());
            }

        }
    }

    // Allow setting project from tests (AbstractAppFuseMojoTestCase)
    void setProject(MavenProject project) {
        this.project = project;
    }

    private void export(String url, String destinationDirectory) throws MojoExecutionException {
        SubversionUtils svn = new SubversionUtils(trunk + tag + url, destinationDirectory);

        try {
            svn.export();
        } catch (SVNException e) {
            SVNErrorMessage err = e.getErrorMessage();

            /*
             * Display all tree of error messages.
             * Utility method SVNErrorMessage.getFullMessage() may be used instead of the loop.
             */
            while (err != null) {
                getLog()
                    .error(err.getErrorCode().getCode() + " : " +
                    err.getMessage());
                err = err.getChildErrorMessage();
            }

            throw new MojoExecutionException(e.getMessage());
        }
    }

    private void log(String msg) {
        getLog().info("[AppFuse] " + msg);
    }
}
