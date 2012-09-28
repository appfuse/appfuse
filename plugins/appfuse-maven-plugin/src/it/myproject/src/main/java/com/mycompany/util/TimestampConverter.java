package com.mycompany.util;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.lang.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class is converts a java.util.Date to a String and a String to a
 * java.util.Date for use as a Timestamp. It is used by BeanUtils when copying
 * properties.
 * 
 * @author <a href="mailto:dan@getrolling.com">Dan Kibler</a>
 */
public class TimestampConverter extends DateConverter {
    /**
     * i18n-ized timestamp format - based on values in ApplicationResources.properties
     */
    public static final String TS_FORMAT = DateUtil.getDatePattern() + " HH:mm:ss.S";

    /**
     * Convert a String to a date
     * @param type java.util.Date
     * @param value the String value
     * @return a converted date
     */
    protected Object convertToDate(Class type, Object value) {
        DateFormat df = new SimpleDateFormat(TS_FORMAT);
        if (value instanceof String) {
            try {
                if (StringUtils.isEmpty(value.toString())) {
                    return null;
                }

                return df.parse((String) value);
            } catch (Exception pe) {
                throw new ConversionException("Error converting String to Timestamp");
            }
        }

        throw new ConversionException("Could not convert "
                + value.getClass().getName() + " to " + type.getName());
    }

    /**
     * Convert from a java.util.Date to a String
     * @param type java.lang.String
     * @param value the date instance
     * @return string version of date using default date pattern
     */
    protected Object convertToString(Class type, Object value) {
        DateFormat df = new SimpleDateFormat(TS_FORMAT);
        if (value instanceof Date) {
            try {
                return df.format(value);
            } catch (Exception e) {
                throw new ConversionException("Error converting Timestamp to String");
            }
        }

        return value.toString();
    }
}