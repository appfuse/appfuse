package org.appfuse.service;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.beanutils.converters.StringConverter;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.BaseObject;
import org.appfuse.util.CurrencyConverter;
import org.appfuse.util.DateConverter;
import org.appfuse.util.DateUtil;

import java.beans.PropertyDescriptor;
import java.util.Date;


/**
 * Base class for Business Services - includes registered converters
 * for BeanUtils.
 *
 * <p>
 * <a href="BaseManager.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @version $Revision: 1.2 $ $Date: 2004/03/07 22:06:53 $
 */
public class BaseManager {
    //~ Static fields/initializers =============================================

    private static Log log = LogFactory.getLog(BaseManager.class);
    private static Long defaultLong = null;

    static {
        ConvertUtils.register(new CurrencyConverter(), Double.class);
        ConvertUtils.register(new DateConverter(), Date.class);
        ConvertUtils.register(new LongConverter(defaultLong), Long.TYPE);
        ConvertUtils.register(new LongConverter(defaultLong), Long.class);
        ConvertUtils.register(new StringConverter(), String.class);

        if (log.isDebugEnabled()) {
            log.debug("Converters registered...");
        }
    }

    /**
     * This method loops through all the Date methods and formats them for the
     * UI.
     *
     * @param obj
     * @param form
     * @return a Form for the web
     */
    protected Object convertDates(Object obj, Object form) {
        if (obj == null || form == null) {
            return null;
        }
        // loop through all the Date methods and format them for the UI
        PropertyDescriptor[] origDescriptors =
                PropertyUtils.getPropertyDescriptors(obj);

        for (int i = 0; i < origDescriptors.length; i++) {
            String name = origDescriptors[i].getName();

            if (origDescriptors[i].getPropertyType().equals(Date.class)) {
                if (PropertyUtils.isWriteable(form, name)) {
                    try {
                        Date date =
                                (Date) PropertyUtils.getSimpleProperty(obj, name);
                        PropertyUtils.setSimpleProperty(form, name,
                                DateUtil.getDate(date));
                    } catch (Exception e) {
                        log.error("Error converting date from object to form");
                    }
                }
            }
        }

        return form;
    }

    /**
     * This method inspects a POJO or Form and figures out its pojo/form
     * equivalent.
     *
     * @param o the object to inspect
     * @return the Class of the persistable object
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    protected Object getObject(Object o) throws ClassNotFoundException,
                                                InstantiationException,
                                                IllegalAccessException {
        String name = o.getClass().getName();
        if (o instanceof BaseObject) {
            if (log.isDebugEnabled()) {
                log.debug("getting form equivalent of pojo...");
            }

            name = StringUtils.replace(name, "model", "webapp.form");
            name += "Form";
        } else {
            if (log.isDebugEnabled()) {
                log.debug("getting pojo equivalent of form...");
            }
            name = StringUtils.replace(name, "webapp.form", "model");
            name = name.substring(0, name.indexOf("Form"));
        }

        Class obj = Class.forName(name);

        if (log.isDebugEnabled()) {
            log.debug("returning className: " + obj.getName());
        }

        return obj.newInstance();
    }

    /**
     * Convenience method to convert a form to a POJO and back again
     *
     * @param o the object to tranfer properties from
     * @return converted object
     */
    protected Object convert(Object o) throws Exception {
        if (o == null) {
            return null;
        }
        Object target = getObject(o);
        BeanUtils.copyProperties(target, o);
        return target;
    }
}
