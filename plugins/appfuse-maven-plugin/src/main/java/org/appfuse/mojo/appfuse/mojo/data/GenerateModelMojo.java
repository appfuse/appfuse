package org.appfuse.mojo.appfuse.mojo.data;

import org.apache.maven.plugin.MojoExecutionException;

import org.appfuse.mojo.MojoBase;
import org.appfuse.mojo.appfuse.utility.FileUtilities;
import org.appfuse.mojo.tasks.GenerateModelTask;

import org.codehaus.plexus.components.interactivity.PrompterException;


/**
 * This mojo class will create model objects from a set of database tables controled by the reverse
 * engineering file.
 *
 * @author                        <a href="mailto:dlwhitehurst@gmail.com">David L. Whitehurst</a>
 * @version                       $Id: $
 * @description                   Generate one or more model/value objects from the database.
 * @goal                          genmodel
 * @requiresDependencyResolution  compile
 */
public class GenerateModelMojo extends MojoBase
{
    /**
     * The full name of the location of the hibernate format database properties file.
     *
     * @parameter  default-value="${basedir}/target/classes/jdbc.properties"
     */
    private String databasePropertiesFile;

    /**
     * The name of the package to be used for all model object generation.
     *
     * @parameter  expression="${appfuse.model.package.name}"
     *             default-value="${project.groupId}.model"
     */
    private String modelPackageName;

    /**
     * The location and name of the file that defines the attrbutes and other data that will be used
     * to control the reverse engineering process. This file controls such things as the schema and
     * table names that will be reverse engineered. If the file is not defined then all objects will
     * be reverse engineered.
     *
     * @parameter  expression="${appfuse.reveng.file}" default-value = ""
     */
    private String reverseEngineeringConfigurationFile;

    /**
     * Creates a new GenerateModelMojo object.
     */
    public GenerateModelMojo()
    {
        super();
        this.setMojoName("GenerateModelMojo");
    }

    /**
     * This method will run the database conversion to create a set of annotated model objects.
     *
     * @throws  MojoExecutionException  Thrown if we fail to obtain an appfuse resource.
     */
    public void execute() throws MojoExecutionException
    {
        this.getLog().info("Running Mojo " + this.getMojoName());

        boolean copyFiles = false;
        boolean generateConfigFile = false;

        // prompt for copy yes no and whether to generate the hibernate.cfg.xml file
        try
        {
            copyFiles = promptForCopy();
            generateConfigFile = promptForGenConfig();
        }
        catch (PrompterException ex)
        {
            throw new MojoExecutionException(ex.getMessage());
        }

        this.getLog().info("Parameters are " + this.toString());

        // Create a new task
        GenerateModelTask task = new GenerateModelTask();

        // Set all the properties required.
        task.setGenerateConfigurationFile(generateConfigFile);
        task.setEjb3(this.isEjb3());
        task.setJdk5(this.isJdk5());
        task.setReverseEngineeringFile(this.getReverseEngineeringConfigurationFile());
        task.setOutputDirectory(this.getOutputDirectory());
        task.setDatabasePropertiesFile(this.getDatabasePropertiesFile());
        task.setPackageName(this.getModelPackageName());
        task.execute();

        if (copyFiles)
        {
            this.getLog().info("Copying generated files to source tree");

            // Create the pattern from the package name
            String pattern = FileUtilities.convertPackageNameToFileLocation(this.modelPackageName) +
                "**/*.java";
            this.getLog().info("Copy pattern is " + pattern + " from " + this.getOutputDirectory() +
                " to " + this.getSourceDirectory());
            this.copyGeneratedObjects(this.getOutputDirectory(), this.getSourceDirectory(),
                pattern);
        }
        else
        {
            this.getLog().info(
                "You must copy the generated files to source tree before geneating any additional pojos");
        }
    }

    /**
     * This method will prompt the user to see if they want to copy the model objects to the source
     * tree or not.
     *
     * @return  True if we should copy the generated objects to the source tree.
     *
     * @throws  PrompterException  Thrown if the user input cannot be generated.
     */
    private boolean promptForCopy() throws PrompterException
    {
        boolean returnValue = false;
        String copyToSourceTree = prompter.prompt(
                "Do you want to copy the generated objects to the source tree (Y/N)");

        if (copyToSourceTree.equalsIgnoreCase("Y"))
        {
            returnValue = true;
        }

        return returnValue;
    }

    /**
     * This method will prompt the user to see if they want to generate the hibernate.cfg.xml file.
     *
     * @return  True if we should generate the hibernate.cfg.xml file.
     *
     * @throws  PrompterException  Thrown if the user input cannot be generated.
     */
    private boolean promptForGenConfig() throws PrompterException
    {
        boolean returnValue = false;
        String copyToSourceTree = prompter.prompt(
                "Do you want to generate a hibernate.cfg.xml file (Y/N)");

        if (copyToSourceTree.equalsIgnoreCase("Y"))
        {
            returnValue = true;
        }

        return returnValue;
    }

    /**
     * Getter for property database properties file.
     *
     * @return  The value of database properties file.
     */
    public String getDatabasePropertiesFile()
    {
        return this.databasePropertiesFile;
    }

    /**
     * Setter for the database properties file.
     *
     * @param  inDatabasePropertiesFile  The value of database properties file.
     */
    public void setDatabasePropertiesFile(String inDatabasePropertiesFile)
    {
        this.databasePropertiesFile = inDatabasePropertiesFile;
    }

    /**
     * Getter for property reverse engineering configuration file.
     *
     * @return  The value of reverse engineering configuration file.
     */
    public String getReverseEngineeringConfigurationFile()
    {
        return this.reverseEngineeringConfigurationFile;
    }

    /**
     * Setter for the reverse engineering configuration file.
     *
     * @param  inReverseEngineeringConfigurationFile  The value of reverse engineering configuration
     *                                                file.
     */
    public void setReverseEngineeringConfigurationFile(
        final String inReverseEngineeringConfigurationFile)
    {
        this.reverseEngineeringConfigurationFile = inReverseEngineeringConfigurationFile;
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
     * This method creates a String representation of this object.
     *
     * @return  the String representation of this object
     */
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(super.toString());
        buffer.append("GenModelMojo[");
        buffer.append("databasePropertiesFile = ").append(databasePropertiesFile);
        buffer.append("\n modelPackageName = ").append(modelPackageName);
        buffer.append("\n reverseEngineeringConfigurationFile = ").append(
            reverseEngineeringConfigurationFile);
        buffer.append("]");

        return buffer.toString();
    }
}
