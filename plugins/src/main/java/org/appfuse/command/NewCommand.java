package org.appfuse.command;

import org.appfuse.utility.FileUtility;
import org.appfuse.ui.StartDialogData;

import java.util.logging.Logger;

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

    private StartDialogData dataModel;

    public NewCommand(StartDialogData dataData) {
        super("NewCommand");
        this.dataModel = dataData;
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
        System.out.println("Name: " + dataModel.getApplicationName());
        System.out.println("Package: " + dataModel.getPackageName());
        System.out.println("Database Choice: " + dataModel.getDatabaseChoice());
        System.out.println("Database Name: " + dataModel.getDatabaseName());
        System.out.println("Persistence Module: " + dataModel.getPersistenceChoice());
        System.out.println("Web Module: " + dataModel.getWebAChoice());

        /**
         * copy and modify AppFuse
         */
        
    }
}
