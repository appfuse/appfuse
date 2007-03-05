package org.appfuse.dao.jpa;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.dao.GenericDao;

/**
 * This class serves as the Base class for all other DAOs - namely to hold
 * common CRUD methods that they might all use. You should only need to extend
 * this class when your require custom CRUD logic.
 *
 * <p>To register this class in your Spring context file, use the following XML.
 * <pre>
 *      &lt;bean id="fooDao" class="org.appfuse.dao.hibernate.GenericDaoJpaHibernate"&gt;
 *          &lt;constructor-arg value="org.appfuse.model.Foo"/&gt;
 *          &lt;property name="sessionFactory" ref="sessionFactory"/&gt;
 *      &lt;/bean&gt;
 * </pre>
 *
 * @author <a href="mailto:bwnoll@gmail.com">Bryan Noll</a>
 */
public class GenericDaoJpa<T, PK extends Serializable> implements GenericDao<T, PK> {
    protected final Log log = LogFactory.getLog(getClass());
    private Class<T> persistentClass;

    public GenericDaoJpa(Class<T> persistentClass) {
        this.persistentClass = persistentClass;
    }
    
    protected EntityManager entityManager;
    
    @PersistenceContext(unitName="ApplicationEntityManager")
    public void setEntityManager(EntityManager entityManager) {
      this.entityManager = entityManager;
    }

    public List<T> getAll() {
        return this.entityManager.createQuery(
                "select obj from " + this.persistentClass.getName() + " obj")
                .getResultList();
    }

    public T get(PK id) {        
        T entity = (T) this.entityManager.find(this.persistentClass, id);

        if (entity == null) {
            String msg = "Uh oh, '" + this.persistentClass + "' object with id '" + id + "' not found..."; 
            log.warn(msg);
            throw new EntityNotFoundException(msg);
        }

        return entity;
    }
    
    public boolean exists(PK id) {
	T entity = (T) this.entityManager.find(this.persistentClass, id);
	
	if (entity == null) {
	    return false;
	} else {
	    return true;
	}
    }

    public void save(T object) {
        Object objId = DaoUtils.getPersistentId(object);
        
        if (objId == null) {
            this.entityManager.persist(object);
        } else {
            this.entityManager.merge(object);
        }
    }

    public void remove(PK id) {
        this.entityManager.remove(this.get(id));
    }
}
