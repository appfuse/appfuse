package org.appfuse.service;

import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import junit.framework.TestCase;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.util.ConvertUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class BaseManagerTestCase extends TestCase {
    //~ Static fields/initializers =============================================

    private static Log log = LogFactory.getLog(BaseManagerTestCase.class);

    //~ Instance fields ========================================================

    protected ResourceBundle rb = null;
    protected static ApplicationContext ctx = 
        new ClassPathXmlApplicationContext("/applicationContext.xml");

    //~ Constructors ===========================================================

    public BaseManagerTestCase() {
        // Since a ResourceBundle is not required for each class, just
        // do a simple check to see if one exists
        String className = this.getClass().getName();

        try {
            rb = ResourceBundle.getBundle(className);
        } catch (MissingResourceException mre) {
            //log.warn("No resource bundle found for: " + className);
        }
    }

    //~ Methods ================================================================

    /**
     * Utility method to populate a javabean-style object with values
     * from a Properties file
     *
     * @param obj
     * @return
     * @throws Exception
     */
    protected Object populate(Object obj) throws Exception {
        // loop through all the beans methods and set its properties from
        // its .properties file
        Map map = ConvertUtils.convertBundleToMap(rb);

        BeanUtils.copyProperties(obj, map);

        return obj;
    }
}
