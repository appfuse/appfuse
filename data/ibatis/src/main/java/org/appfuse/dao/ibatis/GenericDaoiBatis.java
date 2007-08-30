package org.appfuse.dao.ibatis;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.dao.GenericDao;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.util.ClassUtils;

import java.io.Serializable;
import java.util.List;

/**
 * This class serves as the Base class for all other DAOs - namely to hold
 * common CRUD methods that they might all use. You should only need to extend
 * this class when your require custom CRUD logic.
 *
 * <p>To register this class in your Spring context file, use the following XML.
 * <pre>
 *      &lt;bean id="fooDao" class="org.appfuse.dao.ibatis.GenericDaoiBatis"&gt;
 *          &lt;constructor-arg value="org.appfuse.model.Foo"/&gt;
 *          &lt;property name="sessionFactory" ref="sessionFactory"/&gt;
 *      &lt;/bean&gt;
 * </pre>
 *
 * @author Bobby Diaz, Bryan Noll
 * @param <T> a type variable
 * @param <PK> the primary key for that type
 */
public class GenericDaoiBatis<T, PK extends Serializable> extends SqlMapClientDaoSupport implements GenericDao<T, PK> {
    /**
     * Log variable for all child classes. Uses LogFactory.getLog(getClass()) from Commons Logging
     */
    protected final Log log = LogFactory.getLog(getClass());
    private Class<T> persistentClass;

    /**
     * Constructor that takes in a class to see which type of entity to persist
     * @param persistentClass the class type you'd like to persist
     */
    public GenericDaoiBatis(final Class<T> persistentClass) {
        this.persistentClass = persistentClass;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<T> getAll() {
        return getSqlMapClientTemplate().queryForList(
                iBatisDaoUtils.getSelectQuery(ClassUtils.getShortName(this.persistentClass)), null);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public T get(PK id) {
        T object = (T) getSqlMapClientTemplate().queryForObject(
                iBatisDaoUtils.getFindQuery(ClassUtils.getShortName(this.persistentClass)), id);
        if (object == null) {
            log.warn("Uh oh, '" + this.persistentClass + "' object with id '" + id + "' not found...");
            throw new ObjectRetrievalFailureException(ClassUtils.getShortName(this.persistentClass), id);
        }
        return object;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public boolean exists(PK id) {
        T object = (T) getSqlMapClientTemplate().queryForObject(
                iBatisDaoUtils.getFindQuery(ClassUtils.getShortName(this.persistentClass)), id);
        return object != null;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public T save(final T object) {
        String className = ClassUtils.getShortName(object.getClass());
        Object primaryKey = iBatisDaoUtils.getPrimaryKeyValue(object);
        Class primaryKeyClass = iBatisDaoUtils.getPrimaryKeyFieldType(object);
        String keyId = null;

        // check for null id
        if (primaryKey != null) {
            keyId = primaryKey.toString();
        }

        // check for new record
        if (StringUtils.isBlank(keyId)) {
            iBatisDaoUtils.prepareObjectForSaveOrUpdate(object);
            primaryKey = getSqlMapClientTemplate().insert(iBatisDaoUtils.getInsertQuery(className), object);
            iBatisDaoUtils.setPrimaryKey(object, primaryKeyClass, primaryKey);
        } else {
            iBatisDaoUtils.prepareObjectForSaveOrUpdate(object);
            getSqlMapClientTemplate().update(iBatisDaoUtils.getUpdateQuery(className), object);
        }

        // check for null id
        if (iBatisDaoUtils.getPrimaryKeyValue(object) == null) {
            throw new ObjectRetrievalFailureException(className, object);
        } else {
            return object;
        }
    }

    /**
     * {@inheritDoc}
     */
    public void remove(PK id) {
        getSqlMapClientTemplate().update(
                iBatisDaoUtils.getDeleteQuery(ClassUtils.getShortName(this.persistentClass)), id);
    }
}
