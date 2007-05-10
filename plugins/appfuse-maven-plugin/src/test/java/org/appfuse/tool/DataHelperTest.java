package org.appfuse.tool;

import java.util.Date;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author mraible
 */
public class DataHelperTest extends TestCase {
    private Log log = LogFactory.getLog(DataHelperTest.class);

    public void testRandomValuesForDbUnit() throws Exception {
        DataHelper dh = new DataHelper();
        String randomString = dh.getTestValueForDbUnit(String.class.getName());
        assertNotNull(randomString);
        assertTrue(randomString.length() >= 10);
        log.debug("random string: " + randomString);
        
        String todaysDate = dh.getTestValueForDbUnit(Date.class.getName());
        assertNotNull(todaysDate);
        log.debug("today's date: " + todaysDate);
    }
}
