package org.appfuse.util;

import java.text.DecimalFormat;
import java.text.ParseException;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * This class is converts a Double to a double-digit String
 * (and vise-versa) by BeanUtils when copying properties.
 * Registered for use in BaseAction.
 *
 * <p>
 * <a href="CurrencyConverter.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @version $Revision: 1.2 $ $Date: 2004/04/29 08:13:00 $
 */
public class CurrencyConverter implements Converter {
    //~ Instance fields ========================================================

    protected final Log log = LogFactory.getLog(CurrencyConverter.class);
    protected final DecimalFormat formatter = new DecimalFormat("###,###.00");

    //~ Methods ================================================================

    /**
     * Convert a String to a Double and a Double to a String
     *
     * @param type the class type to output
     * @param value the object to convert
     * @return object the converted object (Double or String)
     */
    public final Object convert(final Class type, final Object value) {
        // for a null value, return null
        if (value == null) {
            return null;
        } else {
            if (value instanceof String) {
				if (log.isDebugEnabled()) {
                	log.debug("value (" + value + ") instance of String");
				}

                try {
                    if (StringUtils.isEmpty(String.valueOf(value))) {
                        return null;
                    }

                    if (log.isDebugEnabled()) {
                        log.debug("converting '" + value + "' to a decimal");
                    }
                    //formatter.setDecimalSeparatorAlwaysShown(true);
                    Number num = formatter.parse(String.valueOf(value));
                    return new Double(num.doubleValue());
                } catch (ParseException pe) {
                    pe.printStackTrace();
                }
            } else if (value instanceof Double) {
				if (log.isDebugEnabled()) {
                	log.debug("value (" + value + ") instance of Double");
				}
				log.debug("returning double: " + formatter.format(value));
                return formatter.format(value);
            }
        }

        throw new ConversionException("Could not convert " +
                                      value + " to " +
                                      type.getName() + "!");
    }
}

