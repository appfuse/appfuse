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

import org.appfuse.mojo.PojoMojoBase;
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
public class GenerateManagerMojo extends PojoMojoBase
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
     * This method is called to add properties to the task for use inside the freemarker template.
     *
     * @param  inProperties  The properties objects to use in the template.
     */
    public void addProperties(final Properties inProperties)
    {
        super.addProperties(inProperties);
        inProperties.setProperty("managerpackagename", this.getManagerPackageName());
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
        inTask.setOutputFilePattern(this.getManagerFilePattern());
        inTask.setPackageName(this.getManagerPackageName());
        inTask.setTemplateName(this.getManagerTemplateName());
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
