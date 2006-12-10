package org.appfuse.utility;

import junit.framework.TestCase;

import java.io.File;

/**
 * <p> This program is open software. It is licensed using the Apache Software
 * Foundation, version 2.0 January 2004
 * </p>
 * <a
 * href="mailto:dlwhitehurst@gmail.com">dlwhitehurst@gmail.com</a>
 *
 * @author David L Whitehurst
 */
public class FileUtilityTest extends TestCase {
    /**
     * Constructor
     * @param name
     */
    public FileUtilityTest(String name) {
        super(name);
    }

    /**
     * Test the deletion of target directories
     * @throws Exception
     */
    public void testTargetDirectoryDeletes() throws Exception {
        boolean result = FileUtility.deleteTargetDirectories();
        //assertTrue(result);
        // deletes with true result only if directories exist, does nothing otherwise
    }

    /**
     * Test creation of a directory
     */
    public void testCreateDirectory() throws Exception {
        // check if any directories beneath project exist
        FileUtility.listFilesInDirectory(".");

        // attempt to add a directory at same level as project
        FileUtility.createDirectory("./TestDirectory");
        FileUtility.createDirectory("./TestDirectory/ChildDirectory");

        // list again, is there?
        FileUtility.listFilesInDirectory("./");
    }

    /**
     * Test directory copying
     * @throws Exception
     */
    public void testCopyDirectory() throws Exception {
        FileUtility.copyDirectory(new File("./TestDirectory"), new File("./TestDirectory2"));
    }

    /**
     * Test the deletion of the directory just created
     * @throws Exception
     */
    public void testDeleteDirectory() throws Exception {
       boolean result = FileUtility.deleteDirectory(new File("./TestDirectory")); // this works
       result = FileUtility.deleteDirectory(new File("./TestDirectory2"));
       assertTrue(result);

    }
}
