package com.mycompany.util;

import junit.framework.TestCase;
import org.springframework.context.i18n.LocaleContextHolder;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class DateConverterTest extends TestCase {

    private final DateConverter converter = new DateConverter();

    public void testInternationalization() throws Exception {
        final List<Locale> locales = new ArrayList<Locale>() {

            private static final long serialVersionUID = 1L;
            {
                add(Locale.US);
                add(Locale.GERMANY);
                add(Locale.FRANCE);
                add(Locale.CHINA);
                add(Locale.ITALY);
            }
        };

        for (final Locale locale : locales) {
            LocaleContextHolder.setLocale(locale);
            testConvertStringToDate();
            testConvertDateToString();
            testConvertStringToTimestamp();
            testConvertTimestampToString();
        }
    }

    public void testConvertStringToDate() throws Exception {
        final Date today = new Date();
        final Calendar todayCalendar = new GregorianCalendar();
        todayCalendar.setTime(today);
        final String datePart = DateUtil.convertDateToString(today);
        // test empty time
        Date date = (Date) converter.convert(Date.class, "");
        assertNull(date);
        date = (Date) converter.convert(Date.class, datePart);

        final Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        assertEquals(todayCalendar.get(Calendar.YEAR), cal.get(Calendar.YEAR));
        assertEquals(todayCalendar.get(Calendar.MONTH), cal.get(Calendar.MONTH));
        assertEquals(todayCalendar.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.DAY_OF_MONTH));
    }

    public void testConvertDateToString() throws Exception {
        final Calendar cal = new GregorianCalendar(2005, 0, 16);
        final String date = (String) converter.convert(String.class, cal.getTime());
        assertEquals(DateUtil.convertDateToString(cal.getTime()), date);
    }

    public void testConvertStringToString() throws Exception {
        assertEquals(converter.convert(String.class, "anystring"), "anystring");
    }

    public void testConvertDateWithNull() throws Exception {
        final String date = (String) converter.convert(String.class, null);
        assertNull(date);
    }

    public void testConvertNotSupported() throws Exception {
        final Calendar cal = new GregorianCalendar(2005, 0, 16);
        try {
            converter.convert(Object.class, cal.getTime());
            fail("Object.class is not supported");
        } catch (final Exception e) {
            // expected
        }
        try {
            converter.convertToDate(Object.class, cal.getTime(), "");
            fail("Object.class is not supported");
        } catch (final Exception e) {
            // expected
        }
    }

    public void testConvertStringToTimestamp() throws Exception {
        final Date today = new Date();
        final Calendar todayCalendar = new GregorianCalendar();
        todayCalendar.setTime(today);
        final String datePart = DateUtil.convertDateToString(today);

        final Timestamp time = (Timestamp) converter.convert(Timestamp.class, datePart + " 01:02:03.4");
        final Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(time.getTime());
        assertEquals(todayCalendar.get(Calendar.YEAR), cal.get(Calendar.YEAR));
        assertEquals(todayCalendar.get(Calendar.MONTH), cal.get(Calendar.MONTH));
        assertEquals(todayCalendar.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.DAY_OF_MONTH));
    }

    public void testConvertTimestampToString() throws Exception {
        final Timestamp timestamp = Timestamp.valueOf("2005-03-10 01:02:03.4");
        final String time = (String) converter.convert(String.class, timestamp);
        assertEquals(DateUtil.getDateTime(DateUtil.getDateTimePattern(), timestamp), time);
    }

}
