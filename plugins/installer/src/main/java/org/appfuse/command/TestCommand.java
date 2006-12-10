package org.appfuse.command;
/**
 * <p> This program is open software. It is licensed using the Apache Software
 * Foundation, version 2.0 January 2004
 * </p>
 * <a
 * href="mailto:dlwhitehurst@gmail.com">dlwhitehurst@gmail.com</a>
 *
 * @author David L Whitehurst
 */

public class TestCommand extends Command implements Runnable {

    public TestCommand() {
        super("HelloCommand");
    }
    public final void run() {
        System.out.println("TestCommand there.");
        super.setStatus(true);

    }
}
