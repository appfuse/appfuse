package org.appfuse.util;

import java.util.Date;
import java.util.Locale;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.sql.Timestamp;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.i18n.LocaleContextHolder;

public class DateConverterTest extends TestCase {
    //~ Instance fields ========================================================
    private final Log log = LogFactory.getLog(DateConverterTest.class);
    private DateConverter converter = new DateConverter();

    public void testConvertStringToDate() throws Exception {
        Date date = (Date) converter.convert(Date.class, "01/05/2005");
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        assertEquals(2005, cal.get(Calendar.YEAR));
        assertEquals(Calendar.JANUARY, cal.get(Calendar.MONTH));
        assertEquals(05, cal.get(Calendar.DAY_OF_MONTH));
    }

    public void testConvertDateToString() throws Exception {
        Calendar cal = new GregorianCalendar(2005, 00, 16);
        String date = (String) converter.convert(String.class, cal.getTime());
        assertEquals(DateUtil.convertDateToString(cal.getTime()), date);
    }

    public void testConvertStringToTimestamp() throws Exception {
        Timestamp time = (Timestamp) converter.convert(Timestamp.class, "01/05/2005 01:02:03.4");
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(time.getTime());
        assertEquals(2005, cal.get(Calendar.YEAR));
        assertEquals(Calendar.JANUARY, cal.get(Calendar.MONTH));
        assertEquals(05, cal.get(Calendar.DAY_OF_MONTH));
    }

    public void testConvertTimestampToString() throws Exception {
        Timestamp timestamp = Timestamp.valueOf("2005-03-10 01:02:03.4");
        String time = (String) converter.convert(String.class, timestamp);
        assertEquals(DateUtil.getDateTime(DateConverter.TS_FORMAT, timestamp), time);
    }
}
