package org.appfuse.util;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * This class is converts a java.util.Date to a String
 * and a String to a java.util.Date. It is used by
 * BeanUtils when copying properties.  Registered
 * for use in BaseManager.
 * 
 * <p>
 * <a href="DateConverter.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @version $Revision: 1.1 $ $Date: 2004/03/01 06:19:11 $
 */
public class DateConverter implements Converter {
    //~ Instance fields ========================================================

    private Log log = LogFactory.getLog(DateConverter.class);

    //~ Methods ================================================================

    /**
     * Convert a String to a Date and a Date to a String
     *
     * @param type the class type to output
     * @param value the object to convert
     * @return object the converted object (Date or String)
     */
    public Object convert(Class type, Object value) {
        if (log.isDebugEnabled()) {
            log.debug("entered 'convert' method...");
        }

        // for a null value, return null
        if (value == null) {
            return null;
        } else {
            if (value instanceof String) {
                log.debug("value (" + value + ") instance of String");

                try {
                    if (StringUtils.isEmpty(value.toString())) {
                        return null;
                    }

                    return DateUtil.convertStringToDate(value.toString());
                } catch (ParseException pe) {
                    pe.printStackTrace();
                }
            } else if (value instanceof Date) {
                log.debug("value (" + value + ") instance of Date");

                return DateUtil.convertDateToString((Date) value);
            }
        }

        throw new ConversionException("Could not convert "
                                      + value.getClass().getName() + " to "
                                      + type.getName() + "!");
    }
}
