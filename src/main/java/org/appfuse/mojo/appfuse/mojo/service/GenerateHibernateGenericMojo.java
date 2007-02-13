package org.appfuse.mojo.appfuse.mojo.service;

/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.appfuse.mojo.appfuse.mojo.ConfigMojoBase;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.tools.ant.taskdefs.LoadFile;

/**
 * This class will generate and apply generic hibernate dao and manager extensions for your
 * Spring applicationContext file based on a specified collection of Pojos.
 *
 * @author <a href="mailto:dlwhitehurst@gmail.com">David Whitehurst</a>
 * @version $Id$
 * @description Base mojo for use by appfuse config mojos.
 * @goal genhibernategeneric
 */
public class GenerateHibernateGenericMojo extends ConfigMojoBase {

    /**
     * Creates a new GenerateHibernateGenericMojo object.
     */
    public GenerateHibernateGenericMojo()
    {
        super();
        this.setMojoName( "GenerateHibernateGenericMojo" );
    }

    /**
     *
     * @throws MojoExecutionException
     */
    public void execute() throws MojoExecutionException {

        if ( getLog().isInfoEnabled() )
        {
            getLog().info( "Running the " + this.getMojoName() + " mojo with properties " + this );
        }

        
        LoadFile loadFileTask = this.createLoadFileTask();

        // Generate Generic Manager snippets in XML files
        
        // Configure the files
        loadFileTask.perform();
    }

    /**
     * This method will create an Ant based loadfile task based on a processing pattern.
     *
     * @return The Ant loadfile task to insert/replace configurations
     */
    protected LoadFile createLoadFileTask()
    {
        // TODO - implement using output directory, filePattern, and application context as destination
        return null;

    }

}
