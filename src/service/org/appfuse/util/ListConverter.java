package org.appfuse.util;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * This class is converts a List to an ArrayList, used
 * by BeanUtils when copying properties.  Registered
 * for use in BaseManager.
 * 
 * <p>
 * <a href="ListConverter.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @version $Revision: 1.1 $ $Date: 2004/03/01 06:19:12 $
 */
public class ListConverter implements Converter {
    //~ Instance fields ========================================================

    protected Log log = LogFactory.getLog(ListConverter.class);

    //~ Methods ================================================================

    /**
     * Convert a List to an ArrayList
     *
     * @param type the class type to output
     * @param value the object to convert
     */
    public Object convert(Class type, Object value) {
        /*if (log.isDebugEnabled()) {
           log.debug("entering 'convert' method");
           }*/

        // for a null value, return null
        if (value == null) {
            return null;
        } else if (value.getClass().isAssignableFrom(type)) {
            return value;
        } else if (ArrayList.class.isAssignableFrom(type) &&
                       (value instanceof Collection)) {
            return new ArrayList((Collection) value); // List, Set, Collection  -> ArrayList
        } else if (type.isAssignableFrom(Collection.class) &&
                       (value instanceof Collection)) {
            try {
                //most of collections implement this constructor
                Constructor constructor =
                    type.getConstructor(new Class[] { Collection.class });

                return constructor.newInstance(new Object[] { value });
            } catch (Exception e) {
                log.error(e);
            }
        }

        throw new ConversionException("Could not convert " +
                                      value.getClass().getName() + " to " +
                                      type.getName() + "!");
    }
}
