package org.appfuse.dao;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.persistence.EntityManagerFactory;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.junit.AfterClass;

/**
 * Base class for running DAO tests.
 * @author mraible
 */
@ContextConfiguration(
    locations={"classpath:/applicationContext-resources.xml",
               "classpath:/applicationContext-dao.xml",
               "classpath*:/applicationContext.xml",
               "classpath:**/applicationContext*.xml"})
public abstract class BaseDaoTestCase extends AbstractTransactionalJUnit4SpringContextTests {
    /**
     * Log variable for all child classes. Uses LogFactory.getLog(getClass()) from Commons Logging
     */
    protected final Log log = LogFactory.getLog(getClass());
    /**
     * ResourceBundle loaded from src/test/resources/${package.name}/ClassName.properties (if exists)
     */
    protected ResourceBundle rb;

    /**
     * Default constructor - populates "rb" variable if properties file exists for the class in
     * src/test/resources.
     */
    public BaseDaoTestCase() {
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
     * @param obj the model object to populate
     * @return Object populated object
     * @throws Exception if BeanUtils fails to copy properly
     */
    protected Object populate(Object obj) throws Exception {
        // loop through all the beans methods and set its properties from its .properties file
        Map<String, String> map = new HashMap<String, String>();

        for (Enumeration<String> keys = rb.getKeys(); keys.hasMoreElements();) {
            String key = keys.nextElement();
            map.put(key, rb.getString(key));
        }

        BeanUtils.copyProperties(obj, map);

        return obj;
    }

    /**
     * Flush search indexes, to be done after a reindex() or reindexAll() operation
     */
    public void flushSearchIndexes() {
        EntityManagerFactory entityManagerFactory = (EntityManagerFactory) applicationContext.getBean("entityManagerFactory");
        FullTextEntityManager fullTextEntityMgr = Search.getFullTextEntityManager(entityManagerFactory.createEntityManager());
        fullTextEntityMgr.flushToIndexes();
    }
}
