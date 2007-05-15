package org.appfuse.mojo.installer;
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

import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.FileSet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * This class will provide a set of methods to interact with the ant build system at a java level.
 *
 * @author <a href="mailto:scott@theryansplace.com">Scott Ryan</a>
 */
public class AntUtils {

    /**
     * Creates a new AntUtilities object. Utility classes do not have public contructors.
     */
    protected AntUtils() {
        throw new UnsupportedOperationException("Utility classes do not have public contructors.");
    }

    /**
     * This method will create an ant antProject object.
     *
     * @return The ant antProject for use by tasks.
     */
    public static Project createProject() {
        Project project = new Project();
        project.init();
        project.getBaseDir();

        return project;
    }

    /**
     * This method will take an ant based search pattern and a starting directory and return all the file names that
     * match that pattern.
     *
     * @param inDirectory The starting directory to use to locate files.
     * @param inPattern   The pattern to use in matching file names.
     * @return A list of file names that match the pattern in the target directory
     */
    public static List generateFileNameListFromPattern(final String inDirectory, final String inPattern) {
        List<String> fileNames = new ArrayList<String>();
        DirectoryScanner directoryScanner = new DirectoryScanner();
        directoryScanner.setBasedir(inDirectory);

        String[] patterns = new String[1];
        patterns[0] = inPattern;
        directoryScanner.setIncludes(patterns);
        directoryScanner.scan();

        String[] filesIncluded = directoryScanner.getIncludedFiles();

        for (String aFilesIncluded : filesIncluded) {
            fileNames.add(inDirectory + "/" + aFilesIncluded);
        }

        return fileNames;
    }

    /**
     * This method will create a fileset of files to be processed based on a diretory location and a set of include and
     * exclude patterns.
     *
     * @param inDirectory       The directory containing the files to be maintained in the fileset.
     * @param inIncludePattern  The list of patterns to match and include in the fileset.
     * @param inExcludePatterns The list of patterns to match and exclude in the fileset.
     * @return The file set to process.
     */
    public static FileSet createFileset(final String inDirectory, final String inIncludePattern,
                                        final List inExcludePatterns) {
        FileSet fileSet = new FileSet();

        // Set the search directory
        fileSet.setDir(new File(inDirectory));

        // Set the includes

        fileSet.setIncludes(inIncludePattern);

        // Set the excludes
        for (Object inExcludePattern : inExcludePatterns) {
            fileSet.setExcludes((String) inExcludePattern);
        }

        return fileSet;
    }
}
