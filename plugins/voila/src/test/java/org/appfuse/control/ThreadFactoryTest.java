package org.appfuse.control;
/**
 * <p> This program is open software. It is licensed using the Apache Software
 * Foundation, version 2.0 January 2004
 * </p>
 * <a
 * href="mailto:dlwhitehurst@gmail.com">dlwhitehurst@gmail.com</a>
 *
 * @author David L Whitehurst
 */

import junit.framework.TestCase;
import org.appfuse.command.TestCommand;

/**
 * This Class tests the functionality of the ThreadManager
 */
public class ThreadFactoryTest extends TestCase {

    /**
     * Constructor
     * @param name
     */
    public ThreadFactoryTest(String name) {
        super(name);
    }

    /**
     * Test the creation of the ThreadManager
     * @throws Exception
     */
    public void testCreateFactory() throws Exception {
        ThreadManager manager = new ThreadManager();
        assertTrue(manager !=null);

    }

    /**
     * Test a simple threaded command
     * @throws Exception
     */
    public void testDumbCommand() throws Exception {
        TestCommand testCommand = new TestCommand();
        ThreadManager.getInstance().add(testCommand);
        while(!testCommand.getStatus()) {
           //System.out.println("waiting...");
           // waiting ...
        }
        assertTrue(testCommand.getStatus());
    }

    /**
     * Main method
     * @param args
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(ThreadFactoryTest.class);
    }

}
