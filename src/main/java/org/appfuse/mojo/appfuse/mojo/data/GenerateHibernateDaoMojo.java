package org.appfuse.mojo.appfuse.mojo.data;

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
 * This mojo class will create the hibernate dao objects from a set of hbm.xml files.
 *
 * @author                        <a href="mailto:scott@theryansplace.com">Scott Ryan</a>
 * @version                       $Id: $
 * @description                   Generate one or more hibernate dao objects from the input
 *                                annotated model objects.
 * @goal                          genhibernatedao
 * @requiresDependencyResolution  compile
 */
public class GenerateHibernateDaoMojo extends PojoMojoBase
{
    /**
     * This is the package name for the dao objects.
     *
     * @parameter  expression="${appfuse.hibernate.dao.package.name}"
     *             default-value="${project.groupId}.dao.hibernate"
     */
    private String hibernateDaoPackageName;

    /**
     * This is the output file pattern for Hibernate Dao objects. The package name will be added to
     * the beginning of the pattern with . replaced with slashes.
     *
     * @parameter  expression="${appfuse.hibernate.dao.output.file.pattern}"
     *             default-value="{class-name}HibernateDao.java"
     */
    private String hibernateDaoFilePattern;

    /**
     * This is the template name used to generate the dao test objects.
     *
     * @parameter  expression="${appfuse.hibernate.dao.template.name}"
     *             default-value="/appfusedao/hibernatedao.ftl"
     */
    private String hibernateDaoTemplateName;

    /**
     * Creates a new GenerateHibernateDaoMojo object.
     */
    public GenerateHibernateDaoMojo()
    {
        super();
        this.setMojoName("GenerateHibernateDaoMojo");
    }

    /**
     * This method is called to add properties to the task for use inside the freemarker template.
     *
     * @param  inProperties  The properties objects to use in the template.
     */
    public void addProperties(final Properties inProperties)
    {
        super.addProperties(inProperties);
        inProperties.put("hibernatedaopackagename", this.getHibernateDaoPackageName());
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
        inTask.setOutputFilePattern(this.getHibernateDaoFilePattern());
        inTask.setPackageName(this.getHibernateDaoPackageName());
        inTask.setTemplateName(this.getHibernateDaoTemplateName());
    }

    /**
     * Getter for property hibernate dao file pattern.
     *
     * @return  The value of hibernate dao file pattern.
     */
    public String getHibernateDaoFilePattern()
    {
        return this.hibernateDaoFilePattern;
    }

    /**
     * Setter for the hibernate dao file pattern.
     *
     * @param  inHibernateDaoFilePattern  The value of hibernate dao file pattern.
     */
    public void setHibernateDaoFilePattern(final String inHibernateDaoFilePattern)
    {
        this.hibernateDaoFilePattern = inHibernateDaoFilePattern;
    }

    /**
     * Getter for property hibernate dao package name.
     *
     * @return  The value of hibernate dao package name.
     */
    public String getHibernateDaoPackageName()
    {
        return this.hibernateDaoPackageName;
    }

    /**
     * Setter for the hibernate dao package name.
     *
     * @param  inHibernateDaoPackageName  The value of hibernate dao package name.
     */
    public void setHibernateDaoPackageName(final String inHibernateDaoPackageName)
    {
        this.hibernateDaoPackageName = inHibernateDaoPackageName;
    }

    /**
     * Getter for property hibernate dao template name.
     *
     * @return  The value of hibernate dao template name.
     */
    public String getHibernateDaoTemplateName()
    {
        return this.hibernateDaoTemplateName;
    }

    /**
     * Setter for the hibernate dao template name.
     *
     * @param  inHibernateDaoTemplateName  The value of hibernate dao template name.
     */
    public void setHibernateDaoTemplateName(final String inHibernateDaoTemplateName)
    {
        this.hibernateDaoTemplateName = inHibernateDaoTemplateName;
    }
}
