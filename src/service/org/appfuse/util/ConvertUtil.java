package org.appfuse.util;

import java.beans.PropertyDescriptor;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.BaseObject;


/**
 * Utility class to convert one object to another.
 * 
 * <p>
 * <a href="ConvertUtil.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @version $Revision: 1.7 $ $Date: 2004/08/19 05:11:41 $
 */
public final class ConvertUtil {
    //~ Static fields/initializers =============================================

    private static Log log = LogFactory.getLog(ConvertUtil.class);

    //~ Methods ================================================================

    /**
     * Method to convert a ResourceBundle to a Map object.
     * @param rb a given resource bundle
     * @return Map a populated map
     */
    public static Map convertBundleToMap(ResourceBundle rb) {
        Map map = new HashMap();

        for (Enumeration keys = rb.getKeys(); keys.hasMoreElements();) {
            String key = (String) keys.nextElement();
            map.put(key, rb.getString(key));
        }

        return map;
    }

    /**
     * Method to convert a ResourceBundle to a Properties object.
     * @param rb a given resource bundle
     * @return Properties a populated properties object
     */
    public static Properties convertBundleToProperties(ResourceBundle rb) {
        Properties props = new Properties();

        for (Enumeration keys = rb.getKeys(); keys.hasMoreElements();) {
            String key = (String) keys.nextElement();
            props.put(key, rb.getString(key));
        }

        return props;
    }

    /**
     * Convenience method used by tests to populate an object from a
     * ResourceBundle
     * @param obj an initialized object
     * @param rb a resource bundle
     * @return a populated object
     */
    public static Object populateObject(Object obj, ResourceBundle rb) {
        try {
            Map map = convertBundleToMap(rb);

            BeanUtils.copyProperties(obj, map);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Exception occured populating object: " + e.getMessage());
        }

        return obj;
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
    public static Object getOpposingObject(Object o) throws ClassNotFoundException,
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
            name = name.substring(0, name.lastIndexOf("Form"));
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
    public static Object convert(Object o) throws Exception {
        if (o == null) {
        	return null;
        }
        Object target = getOpposingObject(o);
        BeanUtils.copyProperties(target, o);
        return target;
    }

    /**
     * Convenience method to convert Lists (in a Form) from POJOs to Forms.
     * Also checks for and formats dates.
     *
     * @param o
     * @return
     * @throws Exception
     */
    public static Object convertLists(Object o) throws Exception {
        if (o == null) {
            return null;
        }

        Object target = null;

        PropertyDescriptor[] origDescriptors =
                PropertyUtils.getPropertyDescriptors(o);

        for (int i = 0; i < origDescriptors.length; i++) {
            String name = origDescriptors[i].getName();

            if (origDescriptors[i].getPropertyType().equals(List.class)) {
                List list = (List) PropertyUtils.getProperty(o, name);
                for (int j=0; j < list.size(); j++) {
                    Object origin = list.get(j);
                    target = convert(origin);
                    list.set(j, target);
                }
                PropertyUtils.setProperty(o, name, list);
            }
        }
        return o;
    }
}
