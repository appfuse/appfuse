package org.appfuse.mojo.appfuse.mojo.web;

import org.appfuse.mojo.appfuse.mojo.CopyMojoBase;


/**
 * This mojo class will copy a base set of webapp files from the AppFuse war
 * deployment to a Maven project directory.
 *
 * @author <a href="mailto:dlwhitehurst@gmail.com">David L. Whitehurst</a>
 * @version $Id: $
 * @description Copy modifiable webapp files to src/main/resources.
 * @goal extract-web
 */
public class ExtractWebMojo extends CopyMojoBase {

    /**
     * Creates a new CopyDaoFilesMojo object.
     */
    public ExtractWebMojo()
    {
        super();
        this.setMojoName( "ExtractWebMojo" );
    }

    /**
     * This method takes the ant based file pattern and adds the proper suffix onto it for the type of processing the
     * mojo needs to do. The default is to add .java to the end however if you need a different suffix or need to
     * manipulate the pattern in some way you can overload this method.
     *
     * @param inFilePattern
     *            The initial file pattern requested for processing.
     * @return An augmented file pattern with the type .xml added to it.
     */
    protected String augmentFilePattern( final String inFilePattern )
    {
        return inFilePattern + ".xml";
    }
    /**
     * This method creates a String representation of this object.
     *
     * @return the String representation of this object
     */
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append( "ExtractWebMojo[" );

        buffer.append( "]" );

        return buffer.toString();
    }
}
