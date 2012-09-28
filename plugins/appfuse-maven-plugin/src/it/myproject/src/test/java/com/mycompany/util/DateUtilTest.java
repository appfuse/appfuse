package com.mycompany.util;

import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.i18n.LocaleContextHolder;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

public class DateUtilTest extends TestCase {
    //~ Instance fields ========================================================

    private final Log log = LogFactory.getLog(DateUtilTest.class);

    //~ Constructors ===========================================================

    public DateUtilTest(String name) {
        super(name);
    }

    public void testGetInternationalDatePattern() {
        LocaleContextHolder.setLocale(new Locale("nl"));
        assertEquals("dd-MMM-yyyy", DateUtil.getDatePattern());
       
        LocaleContextHolder.setLocale(Locale.FRANCE);
        assertEquals("dd/MM/yyyy", DateUtil.getDatePattern());
        
        LocaleContextHolder.setLocale(Locale.GERMANY);
        assertEquals("dd.MM.yyyy", DateUtil.getDatePattern());
        
        // non-existant bundle should default to default locale
        LocaleContextHolder.setLocale(new Locale("fi"));
        String fiPattern = DateUtil.getDatePattern();
        LocaleContextHolder.setLocale(Locale.getDefault());
        String defaultPattern = DateUtil.getDatePattern();
        
        assertEquals(defaultPattern, fiPattern);
    }

    public void testGetDate() throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("db date to convert: " + new Date());
        }

        String date = DateUtil.getDate(new Date());

        if (log.isDebugEnabled()) {
            log.debug("converted ui date: " + date);
        }

        assertTrue(date != null);
    }
    
    public void testGetDateTime() {
        if (log.isDebugEnabled()) {
            log.debug("entered 'testGetDateTime' method");
        }
        String now = DateUtil.getTimeNow(new Date());
        assertTrue(now != null);
        log.debug(now);
    }
    public void testGetDateWithNull() {
        final String date = DateUtil.getDate(null);
        assertEquals("", date);
    }

    public void testGetDateTimeWithNull() {
        final String date = DateUtil.getDateTime(null, null);
        assertEquals("", date);
    }

    public void testGetToday() throws ParseException {
        assertNotNull(DateUtil.getToday());
    }
}
