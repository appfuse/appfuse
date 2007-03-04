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
 * This mojo class will create manager implementations implementations of the manager interfaces
 * from a set of hbm.xml files.
 *
 * @author                        <a href="mailto:scott@theryansplace.com">Scott Ryan</a>
 * @version                       $Id: $
 * @description                   Generate one or more manager implementations of the manager
 *                                interfaces from the input hbm.xml files.
 * @goal                          genmanagerimpl
 * @requiresDependencyResolution  compile
 */
public class GenerateManagerImplMojo extends MojoBase
{
    /**
     * This is the package name for the managerimpl objects.
     *
     * @parameter  expression="${appfuse.manager.impl.package.name}"
     *             default-value="${project.groupId}.manager.impl"
     */
    private String managerImplPackageName;

    /**
     * This is the output file pattern for Manager objects. The package name will be added to the
     * beginning of the pattern with . replaced with slashes.
     *
     * @parameter  expression="${appfuse.manager.impl.output.file.pattern}"
     *             default-value="{class-name}ManagerImpl.java"
     */
    private String managerImplFilePattern;

    /**
     * This is the template name used to generate the manager impl test objects.
     *
     * @parameter  expression="${appfuse.dao.template.name}"
     *             default-value="/appfusemanager/managerimpl.ftl"
     */
    private String managerImplTemplateName;

    /**
     * Creates a new GenerateManagerImplMojo object.
     */
    public GenerateManagerImplMojo()
    {
        super();
        this.setMojoName("GenerateManagerImplMojo");
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
            task.setOutputFilePattern(this.getManagerImplFilePattern());
            task.setInputFilePattern(this.promptForInputPattern());
            task.setPackageName(this.getManagerImplPackageName());
            task.setModelPackageName(this.getModelPackageName());
            task.setTemplateProperties(props);
            task.setTemplateName(this.getManagerImplTemplateName());
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
     * Getter for property manager impl package name.
     *
     * @return  The value of manager impl package name.
     */
    public String getManagerImplPackageName()
    {
        return this.managerImplPackageName;
    }

    /**
     * Setter for the manager impl package name.
     *
     * @param  inManagerImplPackageName  The value of manager impl package name.
     */
    public void setManagerImplPackageName(final String inManagerImplPackageName)
    {
        this.managerImplPackageName = inManagerImplPackageName;
    }

    /**
     * Getter for property manager impl file pattern.
     *
     * @return  The value of manager impl file pattern.
     */
    public String getManagerImplFilePattern()
    {
        return this.managerImplFilePattern;
    }

    /**
     * Setter for the manager impl file pattern.
     *
     * @param  inManagerImplFilePattern  The value of manager impl file pattern.
     */
    public void setManagerImplFilePattern(final String inManagerImplFilePattern)
    {
        this.managerImplFilePattern = inManagerImplFilePattern;
    }

    /**
     * Getter for property manager impl template name.
     *
     * @return  The value of manager impl template name.
     */
    public String getManagerImplTemplateName()
    {
        return this.managerImplTemplateName;
    }

    /**
     * Setter for the manager impl template name.
     *
     * @param  inManagerImplTemplateName  The value of manager impl template name.
     */
    public void setManagerImplTemplateName(final String inManagerImplTemplateName)
    {
        this.managerImplTemplateName = inManagerImplTemplateName;
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
        buffer.append("GenerateManagerImplMojo[");
        buffer.append("managerImplFilePattern = ").append(managerImplFilePattern);
        buffer.append("\n managerImplPackageName = ").append(managerImplPackageName);
        buffer.append("\n managerImplTemplateName = ").append(managerImplTemplateName);
        buffer.append("]");

        return buffer.toString();
    }
}
