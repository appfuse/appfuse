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
import org.apache.maven.plugin.MojoExecutionException;

import org.appfuse.mojo.MojoBase;
import org.appfuse.mojo.appfuse.utility.MojoUtilities;
import org.appfuse.mojo.tasks.GeneratePojoTask;

import org.codehaus.plexus.components.interactivity.PrompterException;

import java.util.Properties;


/**
 * This mojo class will create dao interfaces from a set of hbm.xml files.
 *
 * @author                        <a href="mailto:scott@theryansplace.com">Scott Ryan</a>
 * @version                       $Id: $
 * @description                   Generate one or more dao test objects from the input annotated
 *                                model objects.
 * @goal                          gendaotest
 * @requiresDependencyResolution  compile
 */
public class GenerateDaoTestMojo extends MojoBase
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
            props.put("daopackagename", this.getDaoPackageName());

            // Set all the properties required.
            task.setEjb3(this.isEjb3());
            task.setJdk5(this.isJdk5());
            task.setOutputDirectory(this.getOutputDirectory());
            task.setOutputFilePattern(this.getDaoTestFilePattern());
            task.setInputFilePattern(this.promptForInputPattern());
            task.setPackageName(this.daoPackageName);
            task.setModelPackageName(this.getModelPackageName());
            task.setTemplateProperties(props);
            task.setTemplateName(this.getDaoTestTemplateName());
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

    /**
     * This method will prompt the user for an ant pattern to determine which objects to generate
     * from..
     *
     * @return  The ant pattern to use to find model objects.
     *
     * @throws  PrompterException  Thrown if the user input cannot be generated.
     */
    private String promptForInputPattern() throws PrompterException
    {
        String returnValue = prompter.prompt(
                "Please enter an Ant Pattern to determine which objects to generate. (Default is **/*.java)");

        return returnValue;
    }
}
