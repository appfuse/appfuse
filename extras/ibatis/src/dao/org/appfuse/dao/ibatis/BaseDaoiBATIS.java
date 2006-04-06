package org.appfuse.dao.ibatis;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.dao.Dao;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.util.ClassUtils;

/**
 * @author Bobby Diaz
 * @version 1.0
 */
public class BaseDaoiBATIS extends SqlMapClientDaoSupport implements Dao {
    protected final Log log = LogFactory.getLog(getClass());

    public List getObjects(Class clazz) {
        return getSqlMapClientTemplate().queryForList(getSelectQuery(ClassUtils.getShortName(clazz)), null);
    }

    public Object getObject(Class clazz, Serializable primaryKey) {
        Object object = getSqlMapClientTemplate().queryForObject(getFindQuery(ClassUtils.getShortName(clazz)), primaryKey);
        if (object == null) {
            throw new ObjectRetrievalFailureException(ClassUtils.getShortName(clazz), primaryKey);
        }
        return object;
    }

    public void saveObject(final Object object) {
        String className = ClassUtils.getShortName(object.getClass());
        Object primaryKey = getPrimaryKeyValue(object);
        String keyId = null;

        // check for null id
        if (primaryKey != null) {
            keyId = primaryKey.toString();
        }

        // check for new record
        if (StringUtils.isBlank(keyId)) {  
            prepareObjectForSaveOrUpdate(object);
            primaryKey = getSqlMapClientTemplate().insert(getInsertQuery(className), object);

            // check for null id
            if (primaryKey != null) {
                keyId = primaryKey.toString();
            }
        } else {
            prepareObjectForSaveOrUpdate(object);
            getSqlMapClientTemplate().update(getUpdateQuery(className), object);
        }

        // check for null id
        if (getPrimaryKeyValue(object) == null) {
            throw new ObjectRetrievalFailureException(className, object);
        }
    }

    public void removeObject(Class clazz, Serializable primaryKey) {
        getSqlMapClientTemplate().update(getDeleteQuery(ClassUtils.getShortName(clazz)), primaryKey);
    }
    
    private String getPrimaryKeyFieldName(Object o) {
        Field fieldlist[] = o.getClass().getDeclaredFields();
        String fieldName = null;
        for (int i = 0; i < fieldlist.length; i++) {
            Field fld = fieldlist[i];
            if (fld.getName().equals("id") || fld.getName().indexOf("Id") > -1 || fld.getName().equals("version")) {
                fieldName = fld.getName();
                break;
            }
        }
        return fieldName;
    }

    protected Object getPrimaryKeyValue(Object o) {
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
    
    protected void prepareObjectForSaveOrUpdate(Object o) {
        try {
            String fieldName = getPrimaryKeyFieldName(o);
            if (fieldName.equals("version")) {
                String setterMethod = "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
                Method setMethod = o.getClass().getMethod(setterMethod, new Class[]{Integer.class});
                Object value = getPrimaryKeyValue(o);
                if (value == null) {
                    setMethod.invoke(o, new Object[]{new Integer(1)});
                } else {
                    setMethod.invoke(o, new Object[]{new Integer(((Integer) value).intValue()+1)});
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Could not prepare '" + ClassUtils.getShortName(o.getClass()) + "' for insert/update");
        }
    }

    /**
     * @return Returns the select query name.
     */
    public String getSelectQuery(String className) {
        return "get" + className + "s";
    }

    /**
     * @return Returns the find query name.
     */
    public String getFindQuery(String className) {
        return "get" + className;
    }

    /**
     * @return Returns the insert query name.
     */
    public String getInsertQuery(String className) {
        return "add" + className;
    }

    /**
     * @return Returns the update query name.
     */
    public String getUpdateQuery(String className) {
        return "update" + className;
    }

    /**
     * @return Returns the delete query name.
     */
    public String getDeleteQuery(String className) {
        return "delete" + className;
    }
}
