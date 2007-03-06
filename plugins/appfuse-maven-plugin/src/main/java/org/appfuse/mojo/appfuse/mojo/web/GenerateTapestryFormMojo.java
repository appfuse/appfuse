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

import org.appfuse.mojo.tasks.GeneratePojoTask;

import org.codehaus.plexus.components.interactivity.PrompterException;

import java.util.Properties;


/**
 * This mojo class will create Spring Web classes for Form style web pages using a set of
 * pre-existing hbm.xml files. These hbm.xml Hibernate mapping files can be generated as well using
 * the dbtoxml goal. Once the backing beans are created they can be copied into the working project
 * using the copywebfiles goal.
 *
 * @author                        <a href="mailto:dlwhitehurst@gmail.com">David L. Whitehurst</a>
 * @version                       $Id: $
 * @description                   Generate one or more Tapestry Web Form modules from the input
 *                                annotated model pojos.*
 * @goal                          gentapestryform
 * @requiresDependencyResolution  compile
 */
public class GenerateTapestryFormMojo extends WebMojoBase
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
     * This is the template name used to generate the tapestry form objects.
     *
     * @parameter  expression="${appfuse.tapestry.form.template.name}"
     *             default-value="/appfuseweb/TapestryForm.ftl"
     */
    private String tapestryFormTemplateName;

    /**
     * Creates a new GenerateTapestryFormMojo object.
     */
    public GenerateTapestryFormMojo()
    {
        super();
        this.setMojoName("GenerateTapestryFormMojo");
    }

    /**
     * This method is called to add properties to the task for use inside the freemarker template.
     *
     * @param  inProperties  The properties objects to use in the template.
     */
    public void addProperties(final Properties inProperties)
    {
        super.addProperties(inProperties);
        // inProperties.setProperty("hibernatedaopackagename", this.getHibernateDaoPackageName());
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
        super.addTaskItems(inTask, inProperties);
        inTask.setOutputFilePattern(this.getWebFormFilePattern());
        inTask.setPackageName(this.getWebPackageName());
        inTask.setTemplateName(this.getTapestryFormTemplateName());
    }

    /**
     * Getter for property tapestry form template name.
     *
     * @return  The value of tapestry form template name.
     */
    public String getTapestryFormTemplateName()
    {
        return this.tapestryFormTemplateName;
    }

    /**
     * Setter for the tapestry form template name.
     *
     * @param  inTapestryFormTemplateName  The value of tapestry form template name.
     */
    public void setTapestryFormTemplateName(final String inTapestryFormTemplateName)
    {
        this.tapestryFormTemplateName = inTapestryFormTemplateName;
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
        buffer.append("GenerateTapestryFormMojo[");
        buffer.append("tapestryFormTemplateName = ").append(tapestryFormTemplateName);
        buffer.append("\n webFormFilePattern = ").append(webFormFilePattern);
        buffer.append("\n webPackageName = ").append(webPackageName);
        buffer.append("]");

        return buffer.toString();
    }
}
