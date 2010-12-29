package org.appfuse.util;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.lang.StringUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class converts a java.util.Date to a String and a String to a java.util.Date.
 * 
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @author <a href="mailto:tstrausz@tryllian.nl">Tibor Strausz</a>
 */
public class DateConverter implements Converter {

    /**
     * Convert a date to a String and a String to a Date
     * @param type String, Date or Timestamp
     * @param value value to convert
     * @return Converted value for property population
     */
    @SuppressWarnings("unchecked")
    public Object convert(final Class type, final Object value) {
        if (value == null) {
            return null;
        } else if (type == Timestamp.class) {
            return convertToDate(type, value, DateUtil.getDateTimePattern());
        } else if (type == Date.class) {
            return convertToDate(type, value, DateUtil.getDatePattern());
        } else if (type == String.class) {
            return convertToString(value);
        }

        throw new ConversionException("Could not convert " + value.getClass().getName() + " to " + type.getName());
    }

    /**
     * Convert a String to a Date with the specified pattern.
     * @param type String
     * @param value value of String
     * @param pattern date pattern to parse with
     * @return Converted value for property population
     */
    protected Object convertToDate(final Class<?> type, final Object value, final String pattern) {
        final DateFormat df = new SimpleDateFormat(pattern);
        if (value instanceof String) {
            try {
                if (StringUtils.isEmpty(value.toString())) {
                    return null;
                }

                final Date date = df.parse((String) value);
                if (type.equals(Timestamp.class)) {
                    return new Timestamp(date.getTime());
                }
                return date;
            } catch (final Exception e) {
                throw new ConversionException("Error converting String to Date", e);
            }
        }

        throw new ConversionException("Could not convert " + value.getClass().getName() + " to " + type.getName());
    }

    /**
     * Convert a java.util.Date or a java.sql.Timestamp to a String. Or does a toString
     * @param value value to convert
     * @return Converted value for property population
     */
    protected Object convertToString(final Object value) {
        if (value instanceof Date) {
            DateFormat df = new SimpleDateFormat(DateUtil.getDatePattern());
            if (value instanceof Timestamp) {
                df = new SimpleDateFormat(DateUtil.getDateTimePattern());
            }

            try {
                return df.format(value);
            } catch (final Exception e) {
                throw new ConversionException("Error converting Date to String", e);
            }
        } else {
            return value.toString();
        }
    }
}
