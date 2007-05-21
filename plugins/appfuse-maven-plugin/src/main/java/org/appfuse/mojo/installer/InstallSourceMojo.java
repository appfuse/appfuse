package org.appfuse.mojo.installer;

import org.appfuse.tool.SubversionUtils;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNErrorMessage;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.tools.ant.taskdefs.optional.ReplaceRegExp;
import org.apache.tools.ant.Project;

import java.io.File;

/**
 * This mojo is used to "install" source artifacts from Subversion into an AppFuse project.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @goal full-source
 */
public class InstallSourceMojo extends AbstractMojo {

    /**
     * The path where the files from SVN will be placed. This is intentionally set to "src" since that's the default
     * src directory used for exporting AppFuse artifacts.
     *
     * @parameter expression="${appfuse.destinationDirectory}" default-value="${basedir}/src"
     */
    private String destinationDirectory;
    
    /**
     * The directory containing the source code.
     *
     * @parameter expression="${appfuse.trunk}" default-value="https://appfuse.dev.java.net/svn/appfuse/"
     */
    private String trunk;

    /**
     * The tag containing the source code - defaults to '/trunk', but you may want to set it to '/tags/TAGNAME'
     *
     * @parameter expression="${appfuse.tag}" default-value="trunk/"
     */
    private String tag;

    /**
     * <i>Maven Internal</i>: Project to interact with.
     *
     * @parameter expression="${project}"
     * @required
     * @readonly
     * @noinspection UnusedDeclaration
     */
    private MavenProject project;

    public void execute() throws MojoExecutionException, MojoFailureException {
        // todo: get this working from the top-level directory - only works with basic project for now
        
        if (!project.getPackaging().equalsIgnoreCase("war")) {
            String errorMsg = "This plugin can currently only be run from an AppFuse web project (packaging == 'war').";
            //getLog().error(errorMsg);
            throw new MojoFailureException(errorMsg);
        }

        // install dao and manager source if modular/core or war w/o parent (basic)
        if (project.getPackaging().equals("jar") || (project.getPackaging().equals("war") && project.getParent() == null)) {
            log("Installing source from data modules...");
            // export data-common
            export("data/common/src");

            // export persistence framework
            export("data/" + getDaoFramework() + "/src");

            // replace appfuse-${dao.framework} dependency with one from dao.framework's pom.xml


            // export service module
            log("Installing source from service module...");
            export("service/src");

            // add dependencies from appfuse-service to pom.xml
        }

        if (project.getPackaging().equalsIgnoreCase("war")) {
            // export web-common
            log("Installing source from web-common module...");
            export("web/common/src");

            // export web framework
            String webFramework = project.getProperties().getProperty("web.framework");
            log("Installing source from " + webFramework + " module...");
            export("web/" + webFramework + "/src");
        }

        log("Source successfully exported, modifying pom.xml...");
        log("Removing maven-warpath-plugin ...");
        Project antProject = AntUtils.createProject();
        ReplaceRegExp regExpTask = (ReplaceRegExp) antProject.createTask("replaceregexp");
        regExpTask.setFile(new File("pom.xml"));
        regExpTask.setMatch("<artifactId>maven-warpath-plugin</artifactId>(?s:.)</plugin>");
        regExpTask.setReplace("");
        regExpTask.setFlags("m");
        regExpTask.execute();
    }

    private void export(String url) throws MojoExecutionException {

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
                System.err.println(err.getErrorCode().getCode() + " : " + err.getMessage());
                err = err.getChildErrorMessage();
            }

            throw new  MojoExecutionException(e.getMessage());
        }
    }

    private String getDaoFramework() {
        String fw = project.getProperties().getProperty("dao.framework");
        if (fw.equalsIgnoreCase("jpa-hibernate")) {
            return "jpa";
        } else {
            return fw;
        }
    }

    private void log(String msg) {
        getLog().info("[AppFuse] " + msg);
    }
}
