package org.appfuse.dao.ibatis;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.MessageFormat;

/**
 * General iBATIS Utilities class with rules for primary keys and
 * query names.
 *
 * @author Bobby Diaz, Bryan Noll
 */
public class iBatisDaoUtils {
    
    protected static final Log log = LogFactory.getLog(iBatisDaoUtils.class);
    
    protected static String getPrimaryKeyFieldName(Object o) {
        Field fieldlist[] = o.getClass().getDeclaredFields();
        String fieldName = null;
        for (Field fld : fieldlist) {
            if (fld.getName().equals("id") || fld.getName().indexOf("Id") > -1 || fld.getName().equals("version")) {
                fieldName = fld.getName();
                break;
            }
        }
        return fieldName;
    }

    protected static Object getPrimaryKeyValue(Object o) {
        // Use reflection to find the first property that has the name "id" or "Id"
        String fieldName = getPrimaryKeyFieldName(o);
        String getterMethod = "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1); 
        
        try {
            Method getMethod = o.getClass().getMethod(getterMethod, null);
            return getMethod.invoke(o, null);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Could not invoke method '" + getterMethod + "' on " + ClassUtils.getShortName(o.getClass()));
        }
        return null;
    }
    
    protected static void prepareObjectForSaveOrUpdate(Object o) {
        try {
            Field fieldlist[] = o.getClass().getDeclaredFields();
            for (Field fld : fieldlist) {
                String fieldName = fld.getName();
                if (fieldName.equals("version")) {
                    Method setMethod = o.getClass().getMethod("setVersion", Integer.class);
                    Object value = o.getClass().getMethod("getVersion", null).invoke(o, null);
                    if (value == null) {
                        setMethod.invoke(o, 1);
                    } else {
                        setMethod.invoke(o, (Integer) value + 1);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Could not prepare '" + ClassUtils.getShortName(o.getClass()) + "' for insert/update");
        }
    }

    protected static void setPrimaryKey(Object o, Class clazz, Object value) {
        String fieldName = getPrimaryKeyFieldName(o);
        String setMethodName = "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);

        try {
            Method setMethod = o.getClass().getMethod(setMethodName, clazz);
            if (value != null) {
                setMethod.invoke(o, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(MessageFormat.format("Could not set ''{0}.{1} with value {2}",
                    ClassUtils.getShortName(o.getClass()), fieldName, value));
        }
    }

    /**
     * @return Returns the select query name.
     * @param className the name of the class - returns "get" + className + "s"
     */
    public static String getSelectQuery(String className) {
        return "get" + className + "s";
    }

    /**
     * @return Returns the find query name.
     * @param className the name of the class - returns "get" + className
     */
    public static String getFindQuery(String className) {
        return "get" + className;
    }

    /**
     * @return Returns the insert query name.
     * @param className the name of the class - returns "add" + className
     */
    public static String getInsertQuery(String className) {
        return "add" + className;
    }

    /**
     * @return Returns the update query name.
     * @param className the name of the class - returns "update" + className
     */
    public static String getUpdateQuery(String className) {
        return "update" + className;
    }

    /**
     * @return Returns the delete query name.
     * @param className the name of the class - returns "delete" + className
     */
    public static String getDeleteQuery(String className) {
        return "delete" + className;
    }
}
