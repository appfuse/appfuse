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
 * This mojo class will create context xml entries for the manager dao implementations and dao
 * interfaces from a set of hbm.xml files.
 *
 * @author                        <a href="mailto:scott@theryansplace.com">Scott Ryan</a>
 * @version                       $Id: $
 * @description                   Generate one or more service context entries for the
 *                                applicationContext-service.xml file from input hbm.xml files.*
 * @goal                          genmanagercontext
 * @requiresDependencyResolution  compile
 */
public class GenerateManagerContextMojo extends PojoMojoBase
{
    /**
     * This is the package name for the manager objects.
     *
     * @parameter  expression="${appfuse.manager.package.name}"
     *             default-value="${project.groupId}.manager"
     */
    private String managerPackageName;

    /**
     * This is the name of the transaction proxy for the manager objects.
     *
     * @parameter  expression="${appfuse.manager.transaction.proxy.name}" default-value="txproxy"
     */
    private String managerTransactionProxyName;

    /**
     * This is the output file pattern for manager context objects. The package name will be added
     * to the beginning of the pattern with . replaced with slashes.
     *
     * @parameter  expression="${appfuse.manager.context.output.file.pattern}"
     *             default-value="{class-name}Manager.xml"
     */
    private String managerContextFilePattern;

    /**
     * This is the package name for the managerimpl objects.
     *
     * @parameter  expression="${appfuse.manager.impl.package.name}"
     *             default-value="${project.groupId}.manager.impl"
     */
    private String managerImplPackageName;

    /**
     * This is the template name used to generate the manager context objects.
     *
     * @parameter  expression="${appfuse.manager.context.template.name}"
     *             default-value="/appfusemanager/managercontext.ftl"
     */
    private String managerContextTemplateName;

    /**
     * Creates a new GenerateManagerContextMojo object.
     */
    public GenerateManagerContextMojo()
    {
        super();
        this.setMojoName("GenerateManagerContextMojo");
    }

    /**
     * This method is called to add properties to the task for use inside the freemarker template.
     *
     * @param  inProperties  The properties objects to use in the template.
     */
    public void addProperties(final Properties inProperties)
    {
        super.addProperties(inProperties);
        inProperties.setProperty("managerimplpackagename", this.getManagerImplPackageName());
        inProperties.setProperty("transactionproxyname", this.getManagerTransactionProxyName());
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
        inTask.setOutputFilePattern(this.getManagerContextFilePattern());
        inTask.setPackageName(this.getManagerPackageName());
        inTask.setTemplateName(this.getManagerContextTemplateName());
    }

    /**
     * Getter for property manager context file pattern.
     *
     * @return  The value of manager context file pattern.
     */
    public String getManagerContextFilePattern()
    {
        return this.managerContextFilePattern;
    }

    /**
     * Setter for the manager context file pattern.
     *
     * @param  inManagerContextFilePattern  The value of manager context file pattern.
     */
    public void setManagerContextFilePattern(final String inManagerContextFilePattern)
    {
        this.managerContextFilePattern = inManagerContextFilePattern;
    }

    /**
     * Getter for property manager context template name.
     *
     * @return  The value of manager context template name.
     */
    public String getManagerContextTemplateName()
    {
        return this.managerContextTemplateName;
    }

    /**
     * Setter for the manager context template name.
     *
     * @param  inManagerContextTemplateName  The value of manager context template name.
     */
    public void setManagerContextTemplateName(final String inManagerContextTemplateName)
    {
        this.managerContextTemplateName = inManagerContextTemplateName;
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
     * Getter for property manager transaction proxy name.
     *
     * @return  The value of manager transaction proxy name.
     */
    public String getManagerTransactionProxyName()
    {
        return this.managerTransactionProxyName;
    }

    /**
     * Setter for the manager transaction proxy name.
     *
     * @param  inManagerTransactionProxyName  The value of manager transaction proxy name.
     */
    public void setManagerTransactionProxyName(final String inManagerTransactionProxyName)
    {
        this.managerTransactionProxyName = inManagerTransactionProxyName;
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
     * This method creates a String representation of this object.
     *
     * @return  the String representation of this object
     */
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(super.toString());
        buffer.append("GenerateManagerContextMojo[");
        buffer.append("managerContextFilePattern = ").append(managerContextFilePattern);
        buffer.append("\n managerContextTemplateName = ").append(managerContextTemplateName);
        buffer.append("\n managerPackageName = ").append(managerPackageName);
        buffer.append("\n managerTransactionProxyName = ").append(managerTransactionProxyName);
        buffer.append("\n managerImplPackageName = ").append(managerImplPackageName);
        buffer.append("]");

        return buffer.toString();
    }
}
