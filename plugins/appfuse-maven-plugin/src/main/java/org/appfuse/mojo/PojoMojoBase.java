package org.appfuse.mojo;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.MojoExecutionException;

import org.appfuse.mojo.appfuse.utility.MojoUtilities;
import org.appfuse.mojo.tasks.GeneratePojoTask;

import org.codehaus.plexus.components.interactivity.PrompterException;

import java.util.Properties;


/**
 * This class serves as a base class for all mojos that use the pojo generation scheme.
 *
 * @author   <a href="mailto:scott@theryansplace.com">Scott Ryan</a>
 * @version  $Id: $
 */
public class PojoMojoBase extends MojoBase
{
    /**
     * This method will generate a set of DAO test objects to match the input file pattern.
     *
     * @throws  MojoExecutionException  Thrown if an error is encountered in executing the mojo.
     */
    public void execute() throws MojoExecutionException
    {
        this.getLog().info("Running Mojo " + this.getMojoName());

        // Prompt for the input file pattern
        this.getLog().info("Parameters are " + this.toString());

        try
        {
            // Create a new task
            GeneratePojoTask task = new GeneratePojoTask();

            // Generate the properties list to be set to the generation task.
            Properties props = new Properties();

            // Allow the user to add properties
            addProperties(props);

            // Set the task parameters.
            addTaskItems(task, props);
            task.execute();
        }
        catch (DependencyResolutionRequiredException ex)
        {
            throw new MojoExecutionException(ex.getMessage());
        }
        catch (PrompterException ex)
        {
            throw new MojoExecutionException(ex.getMessage());
        }
    }

    /**
     * This method is called to add properties to the task for use inside the freemarker template.
     *
     * @param  inProperties  The properties objects to use in the template.
     */
    public void addProperties(final Properties inProperties)
    {
        inProperties.put("modelpackagename", this.getModelPackageName());
    }

    /**
     * This method is called to add items to the task such as output directories etc. Classes may
     * override this class to add or override such task items as the templace , filepattern or
     * pacakge name. Best practice is for inheriting classes to call super.addTaskItems and then add
     * their own items typically the template name, filepattern, and package name.
     *
     * @param   inTask        The task to add items to.
     * @param   inProperties  The properties object to pass to the freemarker templates.
     *
     * @throws  PrompterException                      Thrown if a prompt for information thrown an
     *                                                 exception.
     * @throws  DependencyResolutionRequiredException  Thrown if the mojo does not declare
     *                                                 dependency resolution in its initial stanza
     *                                                 as this is required for resolution of
     *                                                 dependencies.
     */
    public void addTaskItems(final GeneratePojoTask inTask, final Properties inProperties)
        throws PrompterException, DependencyResolutionRequiredException
    {
        inTask.setEjb3(this.isEjb3());
        inTask.setJdk5(this.isJdk5());
        inTask.setOutputDirectory(this.getOutputDirectory());
        inTask.setInputFilePattern(this.promptForInputPattern());
        inTask.setModelPackageName(this.getModelPackageName());
        inTask.setTemplateProperties(inProperties);
        inTask.setHibernateConfigurationFile(this.getHibernateConfigurationFile());

        // Get a ouput file classpath for this project
        String outputClasspath;
        outputClasspath = MojoUtilities.getOutputClasspath(this.getMavenProject());
        inTask.setOuputClassDirectory(outputClasspath);
    }
}
