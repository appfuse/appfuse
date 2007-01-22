package org.appfuse.mojo.appfuse.mojo;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.tools.ant.taskdefs.LoadFile;

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

/**
 * This class is a base class for all appfuse plugin configuration mojos.
 *
 * @author <a href="mailto:dlwhitehurst@gmail.com">David Whitehurst</a>
 * @version $Id$
 * @description Base mojo for use by appfuse config mojos.
 */
public abstract class ConfigMojoBase extends MojoBase {

    /**
     * This method will copy the files from the generated directory to the final resting place inside the project.
     *
     * @throws org.apache.maven.plugin.MojoExecutionException
     *             Thrown if we fail to obtain an appfuse resource.
     */
    public void execute() throws MojoExecutionException {

        if ( getLog().isInfoEnabled() )
        {
            getLog().info( "Running the " + this.getMojoName() + " mojo with properties " + this );
        }

        LoadFile loadFileTask = this.createLoadFileTask();

        // Config the files
        loadFileTask.perform();
    }

    /**
     * This method will create an Ant based loadfile task based on a processing pattern.
     *
     * @return The Ant loadfile task to insert/replace configurations
     */
    protected LoadFile createLoadFileTask()
    {
        // TODO - implement
        return null;

    }


}
