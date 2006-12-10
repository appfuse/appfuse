package org.appfuse.command;

import org.appfuse.utility.PropertyUtility;
import org.appfuse.engine.ApplicationData;
import org.appfuse.ui.SplashScreen;

import java.io.IOException;
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
public class NewCommand extends Command implements Runnable {
    private static SplashScreen splash;

    /**
     * Construction data used by command
     */
    private ApplicationData data;

    /**
     * Constructor takes java bean with all data needed to create
     * and run this threaded command
     * @param data
     */
    public NewCommand(ApplicationData data) {
        super("NewCommand");
        this.data = data;
    }

    /**
     * Run method for command execution
     */
    public void run() {

        /**
         *  let the user the know what is going to happen
         */
        showExecutionStart();

        /**
         * create an archetype for the new application
         */
        executeArchetypeCommand();

        /**
         * Provide some delay to allow thread to create the archetype
         */
        splash = new SplashScreen();

        for (int i = 0; i < 7; i++) {
            updateStatus(i);
            try {
                Thread.sleep(500);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }

        hideSplashScreen();
        splash = null;

        /**
         *  let the user the know what is going to happen
         */
        showExecutionEnd();
    }

    /**
     * Method to hide Splash screen
     */
    private static void hideSplashScreen() {
        if (splash != null) {
            splash.dispose();
            splash = null;
        }
    }

    /**
     * Method to increment Splash progress indicator
     */
    private static void updateStatus(final int increment) {
        if (splash != null) {
            splash.advance();
        }
    }

    private void showExecutionEnd() {
        System.out.println("##############################################################");
        System.out.println("Application created: Press Cancel to end dialog");
        System.out.println("##############################################################");
    }

    /**
     * Execute maven archetype create goal for determined operating system
     */
    private void executeArchetypeCommand() {
        /*
        * Maven embedder:doit
        *
        */

        String[] commands = {"mvn","archetype:create",
                            "-DarchetypeGroupId=org.appfuse",
                            "-DarchetypeArtifactId=appfuse-archetype-basic",
                            "-DarchetypeVersion=1.0-SNAPSHOT",
                            "-DgroupId=" + data.getPackageName(),
                            "-DartifactId=" + data.getApplicationName()};


        String path = PropertyUtility.getInstance().getSystemProperties().getProperty("user.dir");
        System.out.println(path);
        
        runCommand(commands, null, new File("../"));

        //buffer.append("firefox http://localhost/mediawiki");

    }

    private void runCommand(String[] commandArray, String[] envP, File location) {
        try {
            Runtime.getRuntime().exec(commandArray, envP, location);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Todo refactor for log4j
     * Send console message to acknowledge command start.
     *
     */
    private void showExecutionStart() {
        System.out.println("##############################################################");
        System.out.println("Creating a new web application with the following parameters: ");
        System.out.println("##############################################################");
        System.out.println("Name: " + data.getApplicationName());
        System.out.println("Package: " + data.getPackageName());
        System.out.println("Database Choice: " + data.getDatabaseChoice());
        System.out.println("Database Name: " + data.getDatabaseName());
        System.out.println("Persistence Module: " + data.getPersistenceChoice());
        System.out.println("Web Module: " + data.getWebChoice());
    }
}
