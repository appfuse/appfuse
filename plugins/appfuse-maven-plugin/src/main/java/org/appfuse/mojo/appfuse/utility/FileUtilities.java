package org.appfuse.mojo.appfuse.utility;

import java.io.File;


/**
 * This class provides a set of static helper classes for file manipulation and file name
 * generation.
 *
 * @author  <a href="mailto:scott@theryansplace.com">Scott Ryan</a>
 */
public class FileUtilities
{
    /**
     * Creates a new FileUtilities object. Utility classes do not have public contructors.
     */
    protected FileUtilities()
    {
        throw new UnsupportedOperationException("Utility classes do not have public contructors.");
    }

    /**
     * This method will convert a package name to a relative file location for processing. For
     * Example com.appfuse.data would be converted to com/appfuse/data.
     *
     * @param   inPackageName  The package name to convert.
     *
     * @return  The converted package name to a a file location.
     */
    public static String convertPackageNameToFileLocation(final String inPackageName)
    {
        char seperatorChar = File.separatorChar;
        String fileLocation = inPackageName.replace('.', seperatorChar);

        return fileLocation;
    }
}
