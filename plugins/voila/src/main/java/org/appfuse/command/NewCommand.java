package org.appfuse.command;

import org.appfuse.utility.FileUtility;
import org.appfuse.engine.ApplicationData;

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

    private ApplicationData data;

    public NewCommand(ApplicationData data) {
        super("NewCommand");
        this.data = data;
    }

    public void run() {

        /**
         * Remove all build artifacts from existing appfuse/data ,/service ,/web ,/etc
         */

        FileUtility.deleteTargetDirectories();

        /**
         *  let the user the know what is going to happen
         */
        
        System.out.println("##############################################################");
        System.out.println("Creating a new web application with the following parameters: ");
        System.out.println("##############################################################");
        System.out.println("Name: " + data.getApplicationName());
        System.out.println("Package: " + data.getPackageName());
        System.out.println("Database Choice: " + data.getDatabaseChoice());
        System.out.println("Database Name: " + data.getDatabaseName());
        System.out.println("Persistence Module: " + data.getPersistenceChoice());
        System.out.println("Web Module: " + data.getWebAChoice());

        /**
         * copy and modify AppFuse
         */
        
    }
}
