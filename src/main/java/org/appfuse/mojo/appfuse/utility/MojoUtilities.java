package org.appfuse.mojo.appfuse.utility;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.project.MavenProject;

import java.util.List;


/**
 * This class contains a set of utility methods to interact with the maven system.
 *
 * @author   <a href="mailto:scott@theryansplace.com">Scott Ryan</a>
 * @version  $Id: $
 */
public class MojoUtilities
{
    /**
     * This method will return the ouput path for the build.
     *
     * @param   inProject  The maven project to investigate.
     *
     * @return  The value of output classpath.
     *
     * @throws  DependencyResolutionRequiredException  Thrown if depedency resolution has not been
     *                                                 called in the mojo at this point.
     */
    public static String getOutputClasspath(MavenProject inProject)
        throws DependencyResolutionRequiredException
    {
        List elements = inProject.getCompileClasspathElements();
        String classpath = "";

        // I am assuming the first one is the ouptut classpath for now.
        // TODO find a better way to do this.
        classpath = (String) elements.get(0);

        return classpath;
    }
}
