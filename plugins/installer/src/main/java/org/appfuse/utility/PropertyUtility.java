package org.appfuse.utility;

import java.util.Properties;
import java.util.Iterator;

/**
 * <p> This program is open software. It is licensed using the Apache Software
 * Foundation, version 2.0 January 2004
 * </p>
 * <a
 * href="mailto:dlwhitehurst@gmail.com">dlwhitehurst@gmail.com</a>
 *
 * @author David L Whitehurst
 */
public class PropertyUtility {

    private static Properties properties;
    private static PropertyUtility instance;


    /**
     * private constructor so getInstance() must be used
     */
    private PropertyUtility() {
        properties = System.getProperties();
    }

    public static PropertyUtility getInstance() {
        if (instance == null) {
            instance = new PropertyUtility();
        }
        return instance;
    }

    /**
     * Getter for system properties
     * @return
     */
    public Properties getSystemProperties() {
        if (properties == null) {

        }
        return properties;
    }

    /**
     * Setter for system properties
     * @param properties
     */
    public void setSystemProperties(Properties properties) {
        PropertyUtility.properties = properties;
    }
}
