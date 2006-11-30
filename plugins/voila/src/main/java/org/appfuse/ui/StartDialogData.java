package org.appfuse.ui;
/**
 * <p> This program is open software. It is licensed using the Apache Software
 * Foundation, version 2.0 January 2004
 * </p>
 * <a
 * href="mailto:dlwhitehurst@gmail.com">dlwhitehurst@gmail.com</a>
 *
 * @author David L Whitehurst
 */

public class StartDialogData {
    private String applicationName;
    private String packageName;
    private String databaseChoice;
    private String databaseName;
    private String persistenceChoice;
    private String webAChoice;

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getDatabaseChoice() {
        return databaseChoice;
    }

    public void setDatabaseChoice(String databaseChoice) {
        this.databaseChoice = databaseChoice;
    }

    public String getPersistenceChoice() {
        return persistenceChoice;
    }

    public void setPersistenceChoice(String persistenceChoice) {
        this.persistenceChoice = persistenceChoice;
    }

    public String getWebAChoice() {
        return webAChoice;
    }

    public void setWebAChoice(String webAChoice) {
        this.webAChoice = webAChoice;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }
}
