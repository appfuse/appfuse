package org.appfuse.mojo.appfuse.mojo.data;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.tools.ant.taskdefs.Copy;
import org.appfuse.mojo.appfuse.mojo.CopyMojoBase;

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
 * This mojo class will copy the hbm.xml model files from the generation directory to the source directory.
 * 
 * @author <a href="mailto:scott@theryansplace.com">Scott Ryan</a>
 * @version $Id: $
 * @description Copy hbm.xml model files from the generation directory to the destination directory.
 * @goal copyxmlfiles
 */
public class CopyXMLFilesMojo extends CopyMojoBase
{

    /**
     * Creates a new CopyXMLFilesMojo object.
     */
    public CopyXMLFilesMojo()
    {
        super();
        this.setMojoName( "CopyXMLFilesMojo" );
    }

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

        Copy copyTask = this.createCopyTask( this.getModelDirectory() );

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
        return inFilePattern + ".hbm.xml";
    }

    /**
     * This method creates a String representation of this object.
     * 
     * @return the String representation of this object
     */
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append( super.toString() );
        buffer.append( "CopyXMLFilesMojo[" );
        buffer.append( "]" );
        return buffer.toString();
    }
}
