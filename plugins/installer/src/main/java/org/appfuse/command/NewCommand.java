package org.appfuse.command;

import org.appfuse.utility.PropertyUtility;
import org.appfuse.engine.ApplicationData;
import org.appfuse.ui.SplashScreen;

import java.io.IOException;
import java.io.File;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import static org.apache.log4j.Logger.*;

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
    private static Logger log;

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
        // configure log4j using resource URL APF-548
        DOMConfigurator.configure(getClass().getResource("/META-INF/log4j.xml"));
        log = getLogger(NewCommand.class);

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
                            "-DarchetypeArtifactId=appfuse-archetype-basic-struts",
                            "-DarchetypeVersion=1.0-m2",
                            "-DgroupId=" + data.getPackageName(),
                            "-DartifactId=" + data.getApplicationName(),
                            "-DremoteRepositories=http://oss.sonatype.org/content/repositories/appfuse-releases"};


        String path = PropertyUtility.getInstance().getSystemProperties().getProperty("user.dir");
        log.info(path);
        
        runCommand(commands, null, new File("../"));

        //buffer.append("firefox http://localhost/mediawiki");

    }

    /**
     * Execute command using Java Runtime object
     * @param commandArray
     * @param envP
     * @param location
     */
    private void runCommand(String[] commandArray, String[] envP, File location) {
        try {
            Runtime.getRuntime().exec(commandArray, envP, location);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send console message to acknowledge command start.
     *
     */
    private void showExecutionStart() {
        log.info("##############################################################");
        log.info("Creating a new web application with the following parameters: ");
        log.info("##############################################################");
        log.info("Name: " + data.getApplicationName());
        log.info("Package: " + data.getPackageName());
        log.info("Database Choice: " + data.getDatabaseChoice());
        log.info("Database Name: " + data.getDatabaseName());
        log.info("Persistence Module: " + data.getPersistenceChoice());
        log.info("Web Module: " + data.getWebChoice());
    }

    /**
     * Send console message to acknowledge command end.
     */
    private void showExecutionEnd() {
        log.info("##############################################################");
        log.info("Application created: Press Cancel to end dialog");
        log.info("##############################################################");
    }

}
