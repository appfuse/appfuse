package org.appfuse.dao;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import junit.framework.TestCase;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Base class for running DAO tests.
 * @author mraible
 */
public class BaseDAOTestCase extends TestCase {
    protected final Log log = LogFactory.getLog(getClass());
    protected final static ApplicationContext ctx;
    protected ResourceBundle rb;
    
    // This static block ensures that Spring's BeanFactory is only loaded
    // once for all tests
    static {
        // the dao.type is written to the database.properties file
        // in properties.xml
        ResourceBundle db = ResourceBundle.getBundle("database");
        String daoType = db.getString("dao.type");
        String[] paths = {"/WEB-INF/applicationContext-resources.xml",
                          "/WEB-INF/applicationContext-" + daoType + ".xml"};
        ctx = new ClassPathXmlApplicationContext(paths);
    }

    public BaseDAOTestCase() {
        // Since a ResourceBundle is not required for each class, just
        // do a simple check to see if one exists
        String className = this.getClass().getName();

        try {
            rb = ResourceBundle.getBundle(className);
        } catch (MissingResourceException mre) {
            //log.warn("No resource bundle found for: " + className);
        }
    }
    
    /**
     * Utility method to populate a javabean-style object with values
     * from a Properties file
     * @param obj
     * @return
     * @throws Exception
     */
    protected Object populate(Object obj) throws Exception {
        // loop through all the beans methods and set its properties from
        // its .properties file
        Map map = new HashMap();

        for (Enumeration keys = rb.getKeys(); keys.hasMoreElements();) {
            String key = (String) keys.nextElement();
            map.put(key, rb.getString(key));
        }

        BeanUtils.copyProperties(obj, map);

        return obj;
    }
}
