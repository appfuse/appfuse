package org.appfuse.engine;

import org.appfuse.ui.StartDialog;

/**
 * <p> This program is open software. It is licensed using the Apache Software
 * Foundation, version 2.0 January 2004
 * </p>
 * <a
 * href="mailto:dlwhitehurst@gmail.com">dlwhitehurst@gmail.com</a>
 *
 * @author David L Whitehurst
 */
public abstract class ApplicationGenerator {

    /**
     * private data java bean containing everything needed to create a
     * new web application.  StartDialog uses Hashtable to allow
     * this dataset to be very flexible.
     */
    private ApplicationData applicationData;


    /**
     * Force the creation of an application in a child implementation class
     */
    public abstract void createApplication();

    /**
     * Getter for applicationData
     * @return
     */
    public ApplicationData getApplicationData() {
        return applicationData;
    }

    /**
     * Setter for applicationData
     * @param applicationData
     */
    public void setApplicationData(ApplicationData applicationData) {
        this.applicationData = applicationData;
    }
}
