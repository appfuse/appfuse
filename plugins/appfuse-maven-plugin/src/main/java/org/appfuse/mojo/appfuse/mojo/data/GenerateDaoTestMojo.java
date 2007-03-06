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
 * This mojo class will create dao interfaces from a set of annotated class files.
 *
 * @author                        <a href="mailto:scott@theryansplace.com">Scott Ryan</a>
 * @version                       $Id: $
 * @description                   Generate one or more dao test objects from the input annotated
 *                                model objects.
 * @goal                          gendaotest
 * @requiresDependencyResolution  compile
 */
public class GenerateDaoTestMojo extends PojoMojoBase
{
    /**
     * This is the package name for the dao objects.
     *
     * @parameter  expression="${appfuse.dao.package.name}" default-value="${project.groupId}.dao"
     */
    private String daoPackageName;

    /**
     * This is the output file pattern for Dao Test objects. The package name will be added to the
     * beginning of the pattern with . replaced with slashes.
     *
     * @parameter  expression="${appfuse.dao.output.file.pattern}"
     *             default-value="{class-name}Test.java"
     */
    private String daoTestFilePattern;

    /**
     * This is the template name used to generate the dao test objects.
     *
     * @parameter  expression="${appfuse.dao.template.name}" default-value="/appfusedao/daotest.ftl"
     */
    private String daoTestTemplateName;

    /**
     * Creates a new GenDaoMojo object.
     */
    public GenerateDaoTestMojo()
    {
        super();
        this.setMojoName("GenerateDaoTestMojo");
    }

    /**
     * This method is called to add properties to the task for use inside the freemarker template.
     *
     * @param  inProperties  The properties objects to use in the template.
     */
    public void addProperties(final Properties inProperties)
    {
        super.addProperties(inProperties);
        inProperties.put("daopackagename", this.getDaoPackageName());
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
        inTask.setOutputFilePattern(this.getDaoTestFilePattern());
        inTask.setPackageName(this.getDaoPackageName());
        inTask.setTemplateName(this.getDaoTestTemplateName());
    }

    /**
     * Getter for property dao package name.
     *
     * @return  The value of dao package name.
     */
    public String getDaoPackageName()
    {
        return this.daoPackageName;
    }

    /**
     * Setter for the dao package name.
     *
     * @param  inDaoPackageName  The value of dao package name.
     */
    public void setDaoPackageName(final String inDaoPackageName)
    {
        this.daoPackageName = inDaoPackageName;
    }

    /**
     * Getter for property dao test file pattern.
     *
     * @return  The value of dao test file pattern.
     */
    public String getDaoTestFilePattern()
    {
        return this.daoTestFilePattern;
    }

    /**
     * Setter for the dao test file pattern.
     *
     * @param  inDaoTestFilePattern  The value of dao test file pattern.
     */
    public void setDaoTestFilePattern(final String inDaoTestFilePattern)
    {
        this.daoTestFilePattern = inDaoTestFilePattern;
    }

    /**
     * Getter for property dao test template name.
     *
     * @return  The value of dao test template name.
     */
    public String getDaoTestTemplateName()
    {
        return this.daoTestTemplateName;
    }

    /**
     * Setter for the dao test template name.
     *
     * @param  inDaoTestTemplateName  The value of dao test template name.
     */
    public void setDaoTestTemplateName(final String inDaoTestTemplateName)
    {
        this.daoTestTemplateName = inDaoTestTemplateName;
    }
}
