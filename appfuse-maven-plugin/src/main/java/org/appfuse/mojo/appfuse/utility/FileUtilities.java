package org.appfuse.mojo.appfuse.utility;

/*
 * Copyright 2006 The Apache Software Foundation.
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

/**
 * This class provides a set of static helper classes for file manipulation and file name generation.
 * 
 * @author <a href="mailto:scott@theryansplace.com">Scott Ryan</a>
 */
public class FileUtilities
{

    /**
     * Creates a new FileUtilities object. Utility classes do not have public contructors.
     */
    protected FileUtilities()
    {
        throw new UnsupportedOperationException( "Utility classes do not have public contructors." );
    }

    /**
     * This method will convert a package name to a relative file locatiion for processing. For Example com.appfuse.data
     * would be converted to com/appfuse/data.
     * 
     * @param inPackageName
     *            The package name to convert.
     * 
     * @return The converted package name to a a file location.
     */
    public static String convertPackageNameToFileLocation( final String inPackageName )
    {
        String fileLocation = inPackageName.replace( '.', '/' );

        return fileLocation;
    }
}
