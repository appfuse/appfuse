package org.appfuse.mojo.appfuse.mojo.web;

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
 * This mojo class will create Spring Web classes for List style web pages using a set of
 * pre-existing hbm.xml files. These hbm.xml Hibernate mapping files can be generated as well using
 * the dbtoxml goal. Once the backing beans are created they can be copied into the working project
 * using the copywebfiles goal.
 *
 * @author                        <a href="mailto:dlwhitehurst@gmail.com">David L. Whitehurst</a>
 * @version                       $Id: $
 * @description                   Generate Spring Web Form module for the AppFuse Pojo Form.*
 * @goal                          genspringform
 * @requiresDependencyResolution  compile
 */
public class GenerateSpringFormMojo extends MojoBase
{
    /**
     * This is the package name for the web objects.
     *
     * @parameter  expression="${appfuse.web.package.name}" default-value="${project.groupId}.web"
     */
    private String webPackageName;

    /**
     * This is the output file pattern for web form objects. The package name will be added to the
     * beginning of the pattern with . replaced with slashes.
     *
     * @parameter  expression="${appfuse.web.form.output.file.pattern}"
     *             default-value="{class-name}Form.java"
     */
    private String webFormFilePattern;

    /**
     * This is the template name used to generate the spring form objects.
     *
     * @parameter  expression="${appfuse.spring.form.template.name}"
     *             default-value="/appfuseweb/SpringForm.ftl"
     */
    private String springFormTemplateName;

    /**
     * Creates a new GenerateSpringFormMojo object.
     */
    public GenerateSpringFormMojo()
    {
        super();
        this.setMojoName("GenerateSpringFormMojo");
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
            task.setOutputFilePattern(this.getWebFormFilePattern());
            task.setInputFilePattern(this.promptForInputPattern());
            task.setPackageName(this.webPackageName);
            task.setModelPackageName(this.getModelPackageName());
            task.setTemplateProperties(props);
            task.setTemplateName(this.getSpringFormTemplateName());
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
     * Getter for property spring form template name.
     *
     * @return  The value of spring form template name.
     */
    public String getSpringFormTemplateName()
    {
        return this.springFormTemplateName;
    }

    /**
     * Setter for the spring form template name.
     *
     * @param  inSpringFormTemplateName  The value of spring form template name.
     */
    public void setSpringFormTemplateName(final String inSpringFormTemplateName)
    {
        this.springFormTemplateName = inSpringFormTemplateName;
    }

    /**
     * Getter for property web form file pattern.
     *
     * @return  The value of web form file pattern.
     */
    public String getWebFormFilePattern()
    {
        return this.webFormFilePattern;
    }

    /**
     * Setter for the web form file pattern.
     *
     * @param  inWebFormFilePattern  The value of web form file pattern.
     */
    public void setWebFormFilePattern(final String inWebFormFilePattern)
    {
        this.webFormFilePattern = inWebFormFilePattern;
    }

    /**
     * Getter for property web package name.
     *
     * @return  The value of web package name.
     */
    public String getWebPackageName()
    {
        return this.webPackageName;
    }

    /**
     * Setter for the web package name.
     *
     * @param  inWebPackageName  The value of web package name.
     */
    public void setWebPackageName(final String inWebPackageName)
    {
        this.webPackageName = inWebPackageName;
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
        buffer.append("GenerateSpringFormMojo[");
        buffer.append("springFormTemplateName = ").append(springFormTemplateName);
        buffer.append("\n webFormFilePattern = ").append(webFormFilePattern);
        buffer.append("\n webPackageName = ").append(webPackageName);
        buffer.append("]");

        return buffer.toString();
    }
}
