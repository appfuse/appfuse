package org.appfuse.mojo;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Copy;
import org.apache.tools.ant.types.FileSet;

import org.appfuse.mojo.appfuse.utility.AntUtilities;

import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;

import java.io.File;

import java.util.ArrayList;


/**
 * This class is a base class for all appfuse plugin components to use for access to shared
 * resources.
 *
 * @author       <a href="mailto:scott@theryansplace.com">Scott Ryan</a>
 * @version      $Id: $
 * @description  Base mojo for use by appfuse mojos.
 */
public abstract class MojoBase extends AbstractMojo
{
    /**
     * This is a prompter that can be user within the maven framework.
     *
     * @component
     */
    protected Prompter prompter;

    /**
     * This is a pointer to the maven project information which is used to access dependencies etc.
     *
     * @parameter  default-value="${project}"
     */
    private MavenProject mavenProject;

    /**
     * The directory containing the soure code.
     *
     * @parameter  expression="${appfuse.source.directory}" default-value="${basedir}/src/main/java"
     */
    private String sourceDirectory;

    /**
     * This is the name of the mojo that is used when outputing logging information for any mojos
     * that extend this based class.
     */
    private String mojoName;

    /**
     * The path where the generated artifacts will be placed. This is intentionally not set to the
     * default location for maven generated sources. This is to keep these files out of the
     * eclipse/idea generated sources directory as the intention is that these files will be copied
     * to a source directory to be edited and modified and not re generated each time the plugin is
     * run. If you want to regenerate the files each time you build the project just set this value
     * to ${basedir}/target/generated-sources or set the flag on eclipse/idea plugin to include this
     * file in your project file as a source directory.
     *
     * @parameter  expression="${appfuse.output.directory}"
     *             default-value="${basedir}/target/generated-sources/"
     */
    private String outputDirectory;

    /**
     * This is the package name for the project objects.
     *
     * @parameter  expression="${appfuse.base.package.name}" default-value="${project.groupId}"
     */
    private String basePackageName;

    /**
     * This is the package name for the model objects.
     *
     * @parameter  expression="${appfuse.model.package.name}"
     *             default-value="${project.groupId}.model"
     */
    private String modelPackageName;

    /**
     * This is the flag that indicates if you want to use jdk 5 extentions.
     *
     * @parameter  expression="${appfuse.jdk5}" default-value="true"
     */
    private boolean jdk5;

    /**
     * This is the flag that indicates if you want to use ejb3.
     *
     * @parameter  expression="${appfuse.ejb3}" default-value="true"
     */
    private boolean ejb3;

    /**
     * This is the location of the hibernate configuration file.
     *
     * @parameter  expression="${appfuse.hibernate.configuration.file}"
     *             default-value="${basedir}/target/generated-sources/hibernate.cfg.xml"
     */
    private String hibernateConfigurationFile;

    /**
     * This method will run an appfuse mojo.
     *
     * @throws  MojoExecutionException  Thrown if we fail to obtain an appfuse resource.
     */
    public abstract void execute() throws MojoExecutionException;

    /**
     * Getter for property output directory.
     *
     * @return  The value of output directory.
     */
    public String getOutputDirectory()
    {
        return this.outputDirectory;
    }

    /**
     * Setter for the output directory.
     *
     * @param  inOutputDirectory  The value of output directory.
     */
    public void setOutputDirectory(final String inOutputDirectory)
    {
        this.outputDirectory = inOutputDirectory;
    }

    /**
     * Getter for property mojoName.
     *
     * @return  The value of the mojo name.
     */
    public String getMojoName()
    {
        return this.mojoName;
    }

    /**
     * Setter for the mojo name.
     *
     * @param  inMojoName  The value of the mojo name.
     */
    public void setMojoName(final String inMojoName)
    {
        this.mojoName = inMojoName;
    }

    /**
     * Getter for ejb 3 flag.
     *
     * @return  The value of ejb3 flag.
     */
    public boolean isEjb3()
    {
        return ejb3;
    }

    /**
     * Setter for the ejb3 parameter.
     *
     * @param  inEjb3  The value of ejb3 flag.
     */
    public void setEjb3(boolean inEjb3)
    {
        this.ejb3 = inEjb3;
    }

    /**
     * Getter for jdk5 flag.
     *
     * @return  The value of the jdk 5 flag.
     */
    public boolean isJdk5()
    {
        return jdk5;
    }

    /**
     * Setter for the jdk5 flag.
     *
     * @param  inJdk5  The value of jdk5 flag.
     */
    public void setJdk5(boolean inJdk5)
    {
        this.jdk5 = inJdk5;
    }

    /**
     * Getter for property maven project.
     *
     * @return  The value of maven project.
     */
    public MavenProject getMavenProject()
    {
        return this.mavenProject;
    }

    /**
     * Setter for the maven project.
     *
     * @param  inMavenProject  The value of maven project.
     */
    public void setMavenProject(final MavenProject inMavenProject)
    {
        this.mavenProject = inMavenProject;
    }

    /**
     * Getter for property model package name.
     *
     * @return  The value of model package name.
     */
    public String getModelPackageName()
    {
        return this.modelPackageName;
    }

    /**
     * Setter for the model package name.
     *
     * @param  inModelPackageName  The value of model package name.
     */
    public void setModelPackageName(final String inModelPackageName)
    {
        this.modelPackageName = inModelPackageName;
    }

    /**
     * Getter for property hibernate configuration file.
     *
     * @return  The value of hibernate configuration file.
     */
    public String getHibernateConfigurationFile()
    {
        return this.hibernateConfigurationFile;
    }

    /**
     * Setter for the hibernate configuration file.
     *
     * @param  inHibernateConfigurationFile  The value of hibernate configuration file.
     */
    public void setHibernateConfigurationFile(final String inHibernateConfigurationFile)
    {
        this.hibernateConfigurationFile = inHibernateConfigurationFile;
    }

    /**
     * This method will prompt the user for an ant pattern to determine which objects to generate
     * from..
     *
     * @return  The ant pattern to use to find model objects.
     *
     * @throws  PrompterException  Thrown if the user input cannot be generated.
     */
    public String promptForInputPattern() throws PrompterException
    {
        String returnValue = prompter.prompt(
                "Please enter an Ant Pattern to determine which objects to generate. (Default is **/*.java)");

        return returnValue;
    }

    /**
     * Getter for property base package name.
     *
     * @return  The value of base package name.
     */
    public String getBasePackageName()
    {
        return this.basePackageName;
    }

    /**
     * Setter for the base package name.
     *
     * @param  inBasePackageName  The value of base package name.
     */
    public void setBasePackageName(final String inBasePackageName)
    {
        this.basePackageName = inBasePackageName;
    }

    /**
     * This method will copy files from the source directory to the destination directory based on
     * the pattenr.
     *
     * @param  inSourceDirectory       The source directory to copy from.
     * @param  inDestinationDirectory  The destination directory to copy to.
     * @param  inPattern               The file pattern to match to locate files to copy.
     */
    protected void copyGeneratedObjects(final String inSourceDirectory,
        final String inDestinationDirectory, final String inPattern)
    {
        Project project = AntUtilities.createProject();
        Copy copyTask = (Copy) project.createTask("copy");
        copyTask.init();

        FileSet fileSet = AntUtilities.createFileset(inSourceDirectory, inPattern, new ArrayList());
        getLog().info("Copying files based on pattern " + inPattern + " from directory " +
            inSourceDirectory);
        copyTask.setTodir(new File(inDestinationDirectory));
        copyTask.addFileset(fileSet);
        copyTask.execute();
    }

    /**
     * Getter for property source directory.
     *
     * @return  The value of source directory.
     */
    public String getSourceDirectory()
    {
        return this.sourceDirectory;
    }

    /**
     * Setter for the source directory.
     *
     * @param  inSourceDirectory  The value of source directory.
     */
    public void setSourceDirectory(final String inSourceDirectory)
    {
        this.sourceDirectory = inSourceDirectory;
    }

    /**
     * This method creates a String representation of this object.
     *
     * @return  the String representation of this object
     */
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("MojoBase[");
        buffer.append("\n ejb3 = ").append(ejb3);
        buffer.append("\n jdk5 = ").append(jdk5);
        buffer.append("\n mojoName = ").append(mojoName);
        buffer.append("\n modelPackageName = ").append(modelPackageName);
        buffer.append("\n basePackageName = ").append(basePackageName);
        buffer.append("\n outputDirectory = ").append(outputDirectory);
        buffer.append("\n sourceDirectory = ").append(sourceDirectory);
        buffer.append("\n hibernateConfigurationFile = ").append(hibernateConfigurationFile);
        buffer.append("]");

        return buffer.toString();
    }
}
