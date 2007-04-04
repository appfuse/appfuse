package org.appfuse.mojo.appfuse.utility;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.project.MavenProject;

import java.io.File;

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
     * This method will return the compile time classpath for the project.
     *
     * @param   inProject  The maven project to investigate.
     *
     * @return  The value of compile time classpath.
     *
     * @throws  DependencyResolutionRequiredException  Thrown if depedency resolution has not been
     *                                                 called in the mojo at this point.
     */
    public static String getCompileClasspath(MavenProject inProject)
        throws DependencyResolutionRequiredException
    {
        List elements = inProject.getCompileClasspathElements();
        StringBuffer classpath = new StringBuffer();
        String pathSeperator = File.pathSeparator;

        for (int i = 0; i < elements.size(); i++)
        {
            classpath.append((String) elements.get(i));

            if (i < (elements.size() - 1))
            {
                classpath.append(pathSeperator);
            }
        }

        return classpath.toString();
    }
}
