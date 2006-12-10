package org.appfuse.engine;
/**
 * <p> This program is open software. It is licensed using the Apache Software
 * Foundation, version 2.0 January 2004
 * </p>
 * <a
 * href="mailto:dlwhitehurst@gmail.com">dlwhitehurst@gmail.com</a>
 *
 * @author David L Whitehurst
 */

public class ApplicationData {
    private String applicationName;
    private String packageName;
    private String databaseChoice;
    private String databaseName;
    private String persistenceChoice;
    private String webChoice;
    private int operatingSystem;

    public static final int OS_LINUX = 1;
    public static final int OS_WINDOWS = 2;
    public static final int OS_X = 3;
    
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

    public String getWebChoice() {
        return webChoice;
    }

    public void setWebChoice(String webChoice) {
        this.webChoice = webChoice;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    /**
     * Getter for operating system
     * @return
     */
    public int getOperatingSystem() {
        return operatingSystem;
    }

    /**
     * Setter for operating system
     * @param operatingSystem
     */
    public void setOperatingSystem(int operatingSystem) {
        this.operatingSystem = operatingSystem;
    }
}
