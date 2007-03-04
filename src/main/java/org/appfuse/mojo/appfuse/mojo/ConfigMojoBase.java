package org.appfuse.mojo.appfuse.mojo;

import org.apache.maven.plugin.MojoExecutionException;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.LoadFile;
import org.apache.tools.ant.taskdefs.Property;
import org.apache.tools.ant.taskdefs.Replace;
import org.apache.tools.ant.taskdefs.optional.ReplaceRegExp;

import org.appfuse.mojo.MojoBase;
import org.appfuse.mojo.appfuse.utility.AntUtilities;

import java.io.File;
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


/**
 * This class is a base class for all appfuse plugin configuration mojos.
 *
 * @author       <a href="mailto:dlwhitehurst@gmail.com">David Whitehurst</a>
 * @version      $Id$
 * @description  Base mojo for use by appfuse config mojos.
 */
public abstract class ConfigMojoBase extends MojoBase
{
    /**
     * This method will execute a set of methods to insert configuration snippets into a production
     * configuration file using a series a programmatically called ANT tasks. This was handled in
     * earlier versions of AppFuse by an ANT build in the /extras area of the project.
     *
     * @throws  MojoExecutionException  Thrown if we fail to obtain an appfuse resource.
     */
    public void execute() throws MojoExecutionException
    {
        if (getLog().isInfoEnabled())
        {
            getLog().info("Running the " + this.getMojoName() + " mojo with properties " + this);
        }

        // instance tasks
        // LoadFile loadFileTask = this.createLoadFileTask(this.getConfigInFile(),
        // this.getConfigPropertyName());
        // Property propertyTask = this.createPropertyTask();
        Replace replaceStartTagTask = this.createReplaceTask();
        Replace replaceEndTagTask = this.createReplaceTask();
        ReplaceRegExp replaceRegExpTask = this.createRegExpTask();
        Replace replaceStrategicComment = this.createReplaceTask();

        // load the file
        // loadFileTask.perform();
        // set property with destination file
        createPropertyTask();

        // replace start piece
        replaceStartTagTask.perform();

        // replace end piece
        replaceEndTagTask.perform();

        // regex replace (unsure?)
        replaceRegExpTask.perform();

        // insert/replace snippet
        replaceStrategicComment.perform();
    }

    /**
     * This method will create an ANT based LoadFile task based on an infile and a property name.
     * The property will be loaded with the infile for use later by the Replace task.
     *
     * @param   inFile    DOCUMENT ME!
     * @param   propName  DOCUMENT ME!
     *
     * @return  The ANT LoadFile task that loads a property with a file
     */
    protected LoadFile createLoadFileTask(String inFile, String propName)
    {
        Project project = AntUtilities.createProject();
        LoadFile loadFileTask = (LoadFile) project.createTask("loadfile");
        loadFileTask.init();
        loadFileTask.setProperty(propName);
        getLog().info("Using filename " + inFile + " to drive the configuration");
        loadFileTask.setSrcFile(new File(inFile));

        return loadFileTask;
    }

    /**
     * This method will create an ANT based Replace task based on a file, token, and value
     * attribute.
     *
     * @return  The ANT Replace Task to insert/replace configurations
     */
    protected Replace createReplaceTask()
    {
        Project project = AntUtilities.createProject();
        Replace replaceTask = (Replace) project.createTask("replace");

        return replaceTask;
    }

    /**
     * This method will create an ANT based Property task.
     *
     * @return  The ANT Property Task
     */
    protected Property createPropertyTask()
    {
        Project project = AntUtilities.createProject();
        Property propertyTask = (Property) project.createTask("property");

        return propertyTask;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected ReplaceRegExp createRegExpTask()
    {
        // <replaceregexp file="${applicationContext}"
        // match="REGULAR-START(?s:.)*REGULAR-END"
        // replace=""
        // flags="g"/>
        Project project = AntUtilities.createProject();
        ReplaceRegExp regExpTask = (ReplaceRegExp) project.createTask("regexp");

        return regExpTask;
    }
}
