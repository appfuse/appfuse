package org.appfuse.mojo.appfuse.mojo;

/*
 * Copyright 2005-2006 The Apache Software Foundation.
 *
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
import java.io.File;
import java.util.ArrayList;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Copy;
import org.apache.tools.ant.types.FileSet;
import org.appfuse.mojo.appfuse.utility.AntUtilities;

/**
 * This class is a base class for all appfuse plugin copy mojos.
 * 
 * @author <a href="mailto:scott@theryansplace.com">Scott Ryan</a>
 * @version $Id$
 * @description Base mojo for use by appfuse copy mojos.
 */
public abstract class CopyMojoBase extends MojoBase
{

    /**
     * This method will copy the files from the generated directory to the final resting place inside the project.
     * 
     * @throws MojoExecutionException
     *             Thrown if we fail to obtain an appfuse resource.
     */
    public void execute() throws MojoExecutionException
    {

        if ( getLog().isInfoEnabled() )
        {
            getLog().info( "Running the " + this.getMojoName() + " mojo with properties " + this );
        }

        Copy copyTask = this.createCopyTask(this.getSourceDirectory());

        // Copy the files
        copyTask.perform();
    }

    /**
     * This method takes the ant based file pattern and adds the proper suffix onto it for the type of processing the
     * mojo needs to do. The default is to add .java to the end however if you need a different suffix or need to
     * manipulate the pattern in some way you can overload this method.
     * 
     * @param inFilePattern
     *            The initial file pattern requested for processing.
     * @return An augmented file pattern with the type .java added to it.
     */
    protected String augmentFilePattern( final String inFilePattern )
    {
        return inFilePattern + ".java";
    }

    /**
     * This method will create an Ant based copy task based on a processing pattern.
     * 
     * @param inFilePattern
     *            The initial pattern to process which should not include any suffix.
     * @return The Ant copy task to copy the proper files
     */
    protected Copy createCopyTask( final String inDestinationDirectory )
    {
        Project project = AntUtilities.createProject();
        Copy copyTask = ( Copy ) project.createTask( "copy" );
        copyTask.init();

        String pattern = this.getUpdatedFilePattern();
        getLog().info( "Using pattern " + pattern + " to drive the file copy" );

        // Create file set to process
        FileSet fileSet = AntUtilities.createFileset( this.getOutputDirectory(), pattern, new ArrayList() );

        getLog().info( "Copying " + fileSet.selectorCount() + " files based on pattern set match" );
        // Set the destination directory
        copyTask.setTodir( new File( inDestinationDirectory ) );

        // Set the file set
        copyTask.addFileset( fileSet );

        return copyTask;

    }

    /**
     * This method will take a bare file pattern and add the proper prefix and suffix to constrain it properly for the
     * copy operation intended by the mojo. If the file pattern is blank and default will be chosen to include all files
     * of that type.
     * 
     * @return A updated file pattern to constrain the copy operation.
     */
    protected String getUpdatedFilePattern()
    {
        // If no file pattern set then use the default
        String pattern = this.getFilePattern();
        if ( ( pattern == null ) || ( pattern.length() == 0 ) )
        {

            String defaultFilePattern = "**/*";
            getLog().info(
                           "The attribute filePattern for this mojo was not set so using default value of "
                                           + defaultFilePattern );
            pattern = defaultFilePattern;

        }
        // Call the method to allow the mojo to add the proper prefix/suffix onto the pattern.
        return augmentFilePattern( pattern );
    }

}
