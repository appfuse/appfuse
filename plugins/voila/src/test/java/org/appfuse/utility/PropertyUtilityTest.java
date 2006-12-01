package org.appfuse.utility;

import junit.framework.TestCase;

import java.util.Iterator;
import java.util.Properties;

/**
 * <p> This program is open software. It is licensed using the Apache Software
 * Foundation, version 2.0 January 2004
 * </p>
 * <a
 * href="mailto:dlwhitehurst@gmail.com">dlwhitehurst@gmail.com</a>
 *
 * @author David L Whitehurst
 */
public class PropertyUtilityTest extends TestCase {
    /**
     * Constructor
     * @param name
     */
    public PropertyUtilityTest(String name) {
        super(name);
    }

    /**
     * Test the deletion of target directories
     * @throws Exception
     */
    public void testListSystemProperties() throws Exception {
        Properties properties = PropertyUtility.getInstance().getSystemProperties();
        for (Iterator iter = properties.keySet().iterator(); iter.hasNext();) {
            String key = (String) iter.next();
            String value = properties.getProperty(key);

            System.out.println(key + "=" + value);
        }
    }

}
