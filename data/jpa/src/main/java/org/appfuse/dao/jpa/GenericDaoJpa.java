package org.appfuse.dao.jpa;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.dao.GenericDao;
import org.appfuse.dao.SearchException;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.util.Version;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;

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
 *      Updated by jgarcia: update hibernate3 to hibernate4
 * @author jgarcia (update: added full text search + reindexing)
 * @param <T> a type variable
 * @param <PK> the primary key for that type
 */
public class GenericDaoJpa<T, PK extends Serializable> implements GenericDao<T, PK> {
    /**
     * Log variable for all child classes. Uses LogFactory.getLog(getClass()) from Commons Logging
     */
    protected final Log log = LogFactory.getLog(getClass());

    public static final String PERSISTENCE_UNIT_NAME = "ApplicationEntityManager";

    /**
     * Entity manager, injected by Spring using @PersistenceContext annotation on setEntityManager()
     */
    @PersistenceContext(unitName=PERSISTENCE_UNIT_NAME)
    private EntityManager entityManager;
    private Class<T> persistentClass;
    private Analyzer defaultAnalyzer;

    /**
     * Constructor that takes in a class to see which type of entity to persist.
     * Use this constructor when subclassing or using dependency injection.
     * @param persistentClass the class type you'd like to persist
     */
    public GenericDaoJpa(final Class<T> persistentClass) {
        this.persistentClass = persistentClass;
        defaultAnalyzer = new StandardAnalyzer(Version.LUCENE_36);
    }

    /**
     * Constructor that takes in a class to see which type of entity to persist.
     * Use this constructor when subclassing or using dependency injection.
     * @param persistentClass the class type you'd like to persist
     * @param entityManager the configured EntityManager for JPA implementation.
     */
    public GenericDaoJpa(final Class<T> persistentClass, EntityManager entityManager) {
        this.persistentClass = persistentClass;
        this.entityManager = entityManager;
        defaultAnalyzer = new StandardAnalyzer(Version.LUCENE_36);
    }

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<T> getAll() {
        return this.entityManager.createQuery(
                "select obj from " + this.persistentClass.getName() + " obj")
                .getResultList();
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
    public T get(PK id) {
        T entity = this.entityManager.find(this.persistentClass, id);

        if (entity == null) {
            String msg = "Uh oh, '" + this.persistentClass + "' object with id '" + id + "' not found...";
            log.warn(msg);
            throw new EntityNotFoundException(msg);
        }

        return entity;
    }

    /**
     * {@inheritDoc}
     */
    public boolean exists(PK id) {
        T entity = this.entityManager.find(this.persistentClass, id);
        return entity != null;
    }

    /**
     * {@inheritDoc}
     */
    public T save(T object) {
        return this.entityManager.merge(object);
    }

    /**
     * {@inheritDoc}
     */
    public void remove(T object) {
        entityManager.remove(entityManager.contains(object) ? object : entityManager.merge(object)); 
    } 

    /**
     * {@inheritDoc}
     */
    public void remove(PK id) {
        this.entityManager.remove(this.get(id));
    }

    public List<T> search(String searchTerm) throws SearchException {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        org.apache.lucene.search.Query qry;
        try {
            qry = HibernateSearchJpaTools.generateQuery(searchTerm, this.persistentClass, entityManager, defaultAnalyzer);
        } catch (ParseException ex) {
            throw new SearchException(ex);
        }
        org.hibernate.search.jpa.FullTextQuery hibQuery = fullTextEntityManager.createFullTextQuery(qry, this.persistentClass);
        // filter search results by owner.id value:
        // hibQuery.enableFullTextFilter("owned").setParameter("ownerId", owner.getId().toString());
        return hibQuery.getResultList();
    }

    /**
     * {@inheritDoc}
     */
    public void reindex() {
        HibernateSearchJpaTools.reindex(persistentClass, getEntityManager());
    }


    /**
     * {@inheritDoc}
     */
    public void reindexAll(boolean async) {
        HibernateSearchJpaTools.reindexAll(async, getEntityManager());
    }
}
