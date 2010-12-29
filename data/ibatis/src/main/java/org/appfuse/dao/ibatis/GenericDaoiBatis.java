package org.appfuse.dao.ibatis;

import com.ibatis.sqlmap.client.SqlMapClient;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.dao.GenericDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.util.ClassUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * This class serves as the Base class for all other DAOs - namely to hold
 * common CRUD methods that they might all use. You should only need to extend
 * this class when your require custom CRUD logic.
 * <p/>
 * <p>To register this class in your Spring context file, use the following XML.
 * <pre>
 *      &lt;bean id="fooDao" class="org.appfuse.dao.ibatis.GenericDaoiBatis"&gt;
 *          &lt;constructor-arg value="org.appfuse.model.Foo"/&gt;
 *          &lt;property name="sqlMapClient" ref="sqlMapClient"/&gt;
 *      &lt;/bean&gt;
 * </pre>
 *
 * @author Bobby Diaz, Bryan Noll
 * @param <T> a type variable
 * @param <PK> the primary key for that type
 */
public class GenericDaoiBatis<T, PK extends Serializable> implements GenericDao<T, PK> {
    /**
     * Log variable for all child classes. Uses LogFactory.getLog(getClass()) from Commons Logging
     */
    protected final Log log = LogFactory.getLog(getClass());
    private Class<T> persistentClass;
    private SqlMapClientTemplate sqlMapClientTemplate = new SqlMapClientTemplate();

    /**
     * Constructor that takes in a class to see which type of entity to persist.
     * Use this constructor when subclassing or using dependency injection.
     *
     * @param persistentClass the class type you'd like to persist
     */
    public GenericDaoiBatis(final Class<T> persistentClass) {
        this.persistentClass = persistentClass;
    }

    /**
     * Constructor that takes in a class to see which type of entity to persist
     * Use this constructor when manually creating a new instance.
     *
     * @param persistentClass the class type you'd like to persist
     * @param sqlMapClient    the configured SqlMapClient
     */
    public GenericDaoiBatis(final Class<T> persistentClass, SqlMapClient sqlMapClient) {
        this.persistentClass = persistentClass;
        this.sqlMapClientTemplate.setSqlMapClient(sqlMapClient);
    }

    /**
     * Set the iBATIS Database Layer SqlMapClient to work with.
     * Either this or a "sqlMapClientTemplate" is required.
     *
     * @param sqlMapClient the configured SqlMapClient
     */
    @Autowired
    @Required
    public final void setSqlMapClient(SqlMapClient sqlMapClient) {
        this.sqlMapClientTemplate.setSqlMapClient(sqlMapClient);
    }

    /**
     * Return the SqlMapClientTemplate for this DAO,
     * pre-initialized with the SqlMapClient or set explicitly.
     *
     * @return an initialized SqlMapClientTemplate
     */
    public final SqlMapClientTemplate getSqlMapClientTemplate() {
        return this.sqlMapClientTemplate;
    }

    public Class<T> getPersistentClass() {
        return persistentClass;
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
    public List<T> getAllDistinct() {
        Collection result = new LinkedHashSet(getAll());
        return new ArrayList(result);
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
