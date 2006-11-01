package org.appfuse.dao.ibatis;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.dao.UniversalDao;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.util.ClassUtils;

/**
 * This class serves as the a class that can CRUD any object witout any
 * Spring configuration. The only downside is it does require casting
 * from Object to the object class.
 *
 * @author Bobby Diaz, Bryan Noll
 */
public class UniversalDaoiBatis extends SqlMapClientDaoSupport implements UniversalDao {
    protected final Log log = LogFactory.getLog(getClass());

    public List getAll(Class clazz) {
        return getSqlMapClientTemplate().queryForList(iBatisDaoUtils.getSelectQuery(ClassUtils.getShortName(clazz)), null);
    }

    public Object get(Class clazz, Serializable primaryKey) {
        Object object = getSqlMapClientTemplate().queryForObject(iBatisDaoUtils.getFindQuery(ClassUtils.getShortName(clazz)), primaryKey);
        if (object == null) {
            throw new ObjectRetrievalFailureException(ClassUtils.getShortName(clazz), primaryKey);
        }
        return object;
    }

    public void save(final Object object) {
        String className = ClassUtils.getShortName(object.getClass());
        Object primaryKey = iBatisDaoUtils.getPrimaryKeyValue(object);
        String keyId = null;

        // check for null id
        if (primaryKey != null) {
            keyId = primaryKey.toString();
        }

        // check for new record
        if (StringUtils.isBlank(keyId)) {  
            iBatisDaoUtils.prepareObjectForSaveOrUpdate(object);
            primaryKey = getSqlMapClientTemplate().insert(iBatisDaoUtils.getInsertQuery(className), object);

            // check for null id
            if (primaryKey != null) {
                keyId = primaryKey.toString();
            }
            iBatisDaoUtils.setPrimaryKey(object, Long.class, new Long(keyId));
        } else {
            iBatisDaoUtils.prepareObjectForSaveOrUpdate(object);
            getSqlMapClientTemplate().update(iBatisDaoUtils.getUpdateQuery(className), object);
        }

        // check for null id
        if (iBatisDaoUtils.getPrimaryKeyValue(object) == null) {
            throw new ObjectRetrievalFailureException(className, object);
        }
    }

    public void remove(Class clazz, Serializable primaryKey) {
        getSqlMapClientTemplate().update(iBatisDaoUtils.getDeleteQuery(ClassUtils.getShortName(clazz)), primaryKey);
    }
}
