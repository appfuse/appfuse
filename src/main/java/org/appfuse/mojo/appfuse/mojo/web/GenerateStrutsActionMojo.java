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
 * This mojo class will create Struts Action classes for web pages using a set of pre-existing
 * hbm.xml files. These hbm.xml Hibernate mapping files can be generated as well using the dbtoxml
 * goal. Once the backing beans are created they can be copied into the working project using the
 * copywebfiles goal.
 *
 * @author                        <a href="mailto:dlwhitehurst@gmail.com">David L. Whitehurst</a>
 * @version                       $Id: $
 * @description                   Generate Struts Action module for the AppFuse Pojo List.*
 * @goal                          genstrutsaction
 * @requiresDependencyResolution  compile
 */
public class GenerateStrutsActionMojo extends WebMojoBase
{
    /**
     * This is the package name for the web objects.
     *
     * @parameter  expression="${appfuse.web.package.name}" default-value="${project.groupId}.web"
     */
    private String webPackageName;

    /**
     * This is the output file pattern for struts action objects. The package name will be added to
     * the beginning of the pattern with . replaced with slashes.
     *
     * @parameter  expression="${appfuse.web.action.output.file.pattern}"
     *             default-value="{class-name}Action.java"
     */
    private String webActionFilePattern;

    /**
     * This is the template name used to generate the struts action objects.
     *
     * @parameter  expression="${appfuse.struts.action.template.name}"
     *             default-value="/appfuseweb/StrutsAction.ftl"
     */
    private String strutsActionTemplateName;

    /**
     * Creates a new GenerateJSFFormMojo object.
     */
    public GenerateStrutsActionMojo()
    {
        super();
        this.setMojoName("GenerateStrutsActionMojo");
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
        inTask.setOutputFilePattern(this.getWebActionFilePattern());
        inTask.setPackageName(this.getWebPackageName());
        inTask.setTemplateName(this.getStrutsActionTemplateName());
    }

    /**
     * Getter for property struts action template name.
     *
     * @return  The value of struts action template name.
     */
    public String getStrutsActionTemplateName()
    {
        return this.strutsActionTemplateName;
    }

    /**
     * Setter for the struts action template name.
     *
     * @param  inStrutsActionTemplateName  The value of struts action template name.
     */
    public void setStrutsActionTemplateName(final String inStrutsActionTemplateName)
    {
        this.strutsActionTemplateName = inStrutsActionTemplateName;
    }

    /**
     * Getter for property web action file pattern.
     *
     * @return  The value of web action file pattern.
     */
    public String getWebActionFilePattern()
    {
        return this.webActionFilePattern;
    }

    /**
     * Setter for the web action file pattern.
     *
     * @param  inWebActionFilePattern  The value of web action file pattern.
     */
    public void setWebActionFilePattern(final String inWebActionFilePattern)
    {
        this.webActionFilePattern = inWebActionFilePattern;
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
        buffer.append("GenerateStrutsActionMojo[");
        buffer.append("strutsActionTemplateName = ").append(strutsActionTemplateName);
        buffer.append("\n webActionFilePattern = ").append(webActionFilePattern);
        buffer.append("\n webPackageName = ").append(webPackageName);
        buffer.append("]");

        return buffer.toString();
    }
}
