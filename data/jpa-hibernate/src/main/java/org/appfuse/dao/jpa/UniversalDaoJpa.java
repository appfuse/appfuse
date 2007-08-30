package org.appfuse.dao.jpa;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.dao.UniversalDao;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

/**
 * This class serves as the a class that can CRUD any object witout any
 * Spring configuration. The only downside is it does require casting
 * from Object to the object class.
 *
 * @author Bryan Noll
 */
public class UniversalDaoJpa implements UniversalDao {
    /**
     * Log variable for all child classes. Uses LogFactory.getLog(getClass()) from Commons Logging
     */
    protected final Log log = LogFactory.getLog(getClass());
    /**
     * Entity manager, injected by Spring using @PersistenceContext annotation on setEntityManager()
     */
    protected EntityManager entityManager;

    @PersistenceContext(unitName="ApplicationEntityManager")
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * {@inheritDoc}
     */
    public Object save(Object o) {
        return this.entityManager.merge(o);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public Object get(Class clazz, Serializable id) {
        Object o = this.entityManager.find(clazz, id);

        if (o == null) {
            String msg = "Uh oh, '" + clazz + "' object with id '" + id + "' not found..."; 
            log.warn(msg);
            throw new EntityNotFoundException(msg);
        }

        return o;
    }

    /**
     * {@inheritDoc}
     */
    public List getAll(Class clazz) {
        return this.entityManager.createQuery("select obj from " + clazz + " obj").getResultList();
    }

    /**
     * {@inheritDoc}
     */
    public void remove(Class clazz, Serializable id) {
        this.entityManager.remove(this.get(clazz, id));
    }
}
