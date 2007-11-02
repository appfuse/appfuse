package org.appfuse.tool;

import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.SimpleValue;

import java.util.Date;

/**
 * @author mraible
 */
public class DataHelperTest extends TestCase {
    private Log log = LogFactory.getLog(DataHelperTest.class);

    public void testRandomValuesForDbUnit() throws Exception {
        DataHelper dh = new DataHelper();
        String randomString = dh.getTestValueForDbUnit(createColumn(String.class));
        assertNotNull(randomString);
        assertTrue(randomString.length() >= 10);
        log.debug("random string: " + randomString);
        
        String todaysDate = dh.getTestValueForDbUnit(createColumn(Date.class));
        assertNotNull(todaysDate);
        log.debug("today's date: " + todaysDate);
    }

    private Column createColumn(Class clazz) {
        Column column = new Column();
        SimpleValue sv = new SimpleValue();
        sv.setTypeName(clazz.getName());
        column.setValue(sv);
        return column;
    }
}
