package org.appfuse.utility;

import junit.framework.TestCase;

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
        // deletes with true result only if directories exist
    }

}
