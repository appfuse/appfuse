package org.appfuse.service;

import java.io.FileInputStream;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.sql.DataSource;

import junit.framework.TestCase;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.util.ConvertUtil;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.XmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class BaseManagerTestCase extends TestCase {
    //~ Static fields/initializers =============================================

    protected transient final Log log = LogFactory.getLog(getClass());
    protected final static ApplicationContext ctx;
    protected ResourceBundle rb;
    private IDatabaseConnection conn = null;
    private IDataSet dataSet = null;

    // This static block ensures that Spring's BeanFactory is only loaded
    // once for all tests
    static {
        String[] paths = {"/WEB-INF/applicationContext*.xml"};
        ctx = new ClassPathXmlApplicationContext(paths);
    }

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
    
    protected void setUp() throws Exception {
        DataSource ds = (DataSource) ctx.getBean("dataSource");
        conn = new DatabaseConnection(ds.getConnection());
        dataSet = new XmlDataSet(new FileInputStream("metadata/sql/sample-data.xml"));
        // clear table and insert only sample data
        DatabaseOperation.CLEAN_INSERT.execute(conn, dataSet);
    }
    
    protected void tearDown() throws Exception {
        // clear out database
        DatabaseOperation.DELETE.execute(conn, dataSet);
        conn.close();
        conn = null;
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
        Map map = ConvertUtil.convertBundleToMap(rb);

        BeanUtils.copyProperties(obj, map);

        return obj;
    }
}
