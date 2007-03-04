package org.appfuse.mojo.appfuse.mojo.service;

/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.MojoExecutionException;

import org.appfuse.mojo.MojoBase;
import org.appfuse.mojo.appfuse.utility.MojoUtilities;
import org.appfuse.mojo.tasks.GeneratePojoTask;

import org.codehaus.plexus.components.interactivity.PrompterException;

import java.util.Properties;


/**
 * This mojo class will create manager interfaces from a set of hbm.xml files.
 *
 * @author                        <a href="mailto:scott@theryansplace.com">Scott Ryan</a>
 * @version                       $Id: $
 * @description                   Generate one or more manager interfaces from the input hbm.xml
 *                                files.*
 * @goal                          genmanager
 * @requiresDependencyResolution  compile
 */
public class GenerateManagerMojo extends MojoBase
{
    /**
     * This is the package name for the dao objects.
     *
     * @parameter  expression="${appfuse.manager.package.name}"
     *             default-value="${project.groupId}.manager"
     */
    private String managerPackageName;

    /**
     * This is the output file pattern for manager objects. The package name will be added to the
     * beginning of the pattern with . replaced with slashes.
     *
     * @parameter  expression="${appfuse.manager.output.file.pattern}"
     *             default-value="{class-name}Manager.java"
     */
    private String managerFilePattern;

    /**
     * This is the template name used to generate the manager objects.
     *
     * @parameter  expression="${appfuse.manager.template.name}"
     *             default-value="/appfusemanager/managerinterface.ftl"
     */
    private String managerTemplateName;

    /**
     * Creates a new GenerateManagerMojo object.
     */
    public GenerateManagerMojo()
    {
        super();
        this.setMojoName("GenerateManagerMojo");
    }

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
            props.put("modelpackagename", this.getModelPackageName());

            // Set all the properties required.
            task.setEjb3(this.isEjb3());
            task.setJdk5(this.isJdk5());
            task.setOutputDirectory(this.getOutputDirectory());
            task.setOutputFilePattern(this.getManagerFilePattern());
            task.setInputFilePattern(this.promptForInputPattern());
            task.setPackageName(this.getManagerPackageName());
            task.setModelPackageName(this.getModelPackageName());
            task.setTemplateProperties(props);
            task.setTemplateName(this.getManagerTemplateName());
            task.setHibernateConfigurationFile(this.getHibernateConfigurationFile());

            // Get a ouput file classpath for this project
            String outputClasspath;
            outputClasspath = MojoUtilities.getOutputClasspath(this.getMavenProject());
            task.setOuputClassDirectory(outputClasspath);
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
     * Getter for property manager file pattern.
     *
     * @return  The value of manager file pattern.
     */
    public String getManagerFilePattern()
    {
        return this.managerFilePattern;
    }

    /**
     * Setter for the manager file pattern.
     *
     * @param  inManagerFilePattern  The value of manager file pattern.
     */
    public void setManagerFilePattern(final String inManagerFilePattern)
    {
        this.managerFilePattern = inManagerFilePattern;
    }

    /**
     * Getter for property manager package name.
     *
     * @return  The value of manager package name.
     */
    public String getManagerPackageName()
    {
        return this.managerPackageName;
    }

    /**
     * Setter for the manager package name.
     *
     * @param  inManagerPackageName  The value of manager package name.
     */
    public void setManagerPackageName(final String inManagerPackageName)
    {
        this.managerPackageName = inManagerPackageName;
    }

    /**
     * Getter for property manager template name.
     *
     * @return  The value of manager template name.
     */
    public String getManagerTemplateName()
    {
        return this.managerTemplateName;
    }

    /**
     * Setter for the manager template name.
     *
     * @param  inManagerTemplateName  The value of manager template name.
     */
    public void setManagerTemplateName(final String inManagerTemplateName)
    {
        this.managerTemplateName = inManagerTemplateName;
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
        buffer.append("GenerateManagerMojo[");
        buffer.append("managerFilePattern = ").append(managerFilePattern);
        buffer.append("\n managerPackageName = ").append(managerPackageName);
        buffer.append("\n managerTemplateName = ").append(managerTemplateName);
        buffer.append("]");

        return buffer.toString();
    }
}
