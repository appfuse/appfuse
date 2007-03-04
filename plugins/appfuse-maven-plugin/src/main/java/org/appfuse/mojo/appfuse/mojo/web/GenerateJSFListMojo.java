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
 * This mojo class will create Java Server Faces (JSF) backing beans for List style web pages using
 * a set of pre-existing hbm.xml files. These hbm.xml Hibernate mapping files can be generated as
 * well using the dbtoxml goal. Once the backing beans are created they can be copied into the
 * working project using the copywebfiles goal.
 *
 * @author                        <a href="mailto:dlwhitehurst@gmail.com">David L. Whitehurst</a>
 * @version                       $Id: $
 * @description                   Generate one or more JSF Backing Bean List modules from the input
 *                                hbm.xml files.*
 * @goal                          genjsflist
 * @requiresDependencyResolution  compile
 */
public class GenerateJSFListMojo extends MojoBase
{
    /**
     * This is the package name for the web objects.
     *
     * @parameter  expression="${appfuse.web.package.name}" default-value="${project.groupId}.web"
     */
    private String webPackageName;

    /**
     * This is the output file pattern for web list objects. The package name will be added to the
     * beginning of the pattern with . replaced with slashes.
     *
     * @parameter  expression="${appfuse.web.list.output.file.pattern}"
     *             default-value="{class-name}List.java"
     */
    private String webListFilePattern;

    /**
     * This is the template name used to generate the jsf list objects.
     *
     * @parameter  expression="${appfuse.jsf.list.template.name}"
     *             default-value="/appfuseweb/JsfList.ftl"
     */
    private String jsfListTemplateName;

    /**
     * Creates a new GenerateJSFListMojo object.
     */
    public GenerateJSFListMojo()
    {
        super();
        this.setMojoName("GenerateJSFListMojo");
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
            task.setOutputFilePattern(this.getWebListFilePattern());
            task.setInputFilePattern(this.promptForInputPattern());
            task.setPackageName(this.webPackageName);
            task.setModelPackageName(this.getModelPackageName());
            task.setTemplateProperties(props);
            task.setTemplateName(this.getJsfListTemplateName());
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
     * Getter for property jsf list template name.
     *
     * @return  The value of jsf list template name.
     */
    public String getJsfListTemplateName()
    {
        return this.jsfListTemplateName;
    }

    /**
     * Setter for the jsf list template name.
     *
     * @param  inJsfListTemplateName  The value of jsf list template name.
     */
    public void setJsfListTemplateName(final String inJsfListTemplateName)
    {
        this.jsfListTemplateName = inJsfListTemplateName;
    }

    /**
     * Getter for property web list file pattern.
     *
     * @return  The value of web list file pattern.
     */
    public String getWebListFilePattern()
    {
        return this.webListFilePattern;
    }

    /**
     * Setter for the web list file pattern.
     *
     * @param  inWebListFilePattern  The value of web list file pattern.
     */
    public void setWebListFilePattern(final String inWebListFilePattern)
    {
        this.webListFilePattern = inWebListFilePattern;
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
        buffer.append("GenerateJSFListMojo[");
        buffer.append("jsfListTemplateName = ").append(jsfListTemplateName);
        buffer.append("\n webListFilePattern = ").append(webListFilePattern);
        buffer.append("\n webPackageName = ").append(webPackageName);
        buffer.append("]");

        return buffer.toString();
    }
}
