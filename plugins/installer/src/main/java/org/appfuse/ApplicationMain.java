package org.appfuse;
/**
 * <p> This program is open software. It is licensed using the Apache Software
 * Foundation, version 2.0 January 2004
 * </p>
 * <a
 * href="mailto:dlwhitehurst@gmail.com">dlwhitehurst@gmail.com</a>
 *
 * @author David L Whitehurst
 */

import org.appfuse.engine.graphical.SwingApplicationGenerator;


/**
 * This class is used to start the AppFuse QuickStart application
 */
public class ApplicationMain {

    public static void main(String[] args) {
        SwingApplicationGenerator swingGenerator = new SwingApplicationGenerator();
        swingGenerator.createApplication();
        System.exit(0);
    }
}

