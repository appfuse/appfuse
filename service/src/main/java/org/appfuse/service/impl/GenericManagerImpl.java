package org.appfuse.service.impl;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.appfuse.dao.GenericDao;
import org.appfuse.service.GenericManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;
import java.util.List;

/**
 * This class serves as the Base class for all other Managers - namely to hold
 * common CRUD methods that they might all use. You should only need to extend
 * this class when your require custom CRUD logic.
 *
 * <p>To register this class in your Spring context file, use the following XML.
 * <pre>
 *     &lt;bean id="userManager" class="org.appfuse.service.impl.GenericManagerImpl"&gt;
 *         &lt;constructor-arg&gt;
 *             &lt;bean class="org.appfuse.dao.hibernate.GenericDaoHibernate"&gt;
 *                 &lt;constructor-arg value="org.appfuse.model.User"/&gt;
 *                 &lt;property name="sessionFactory" ref="sessionFactory"/&gt;
 *             &lt;/bean&gt;
 *         &lt;/constructor-arg&gt;
 *     &lt;/bean&gt;
 * </pre>
 *
 * <p>If you're using iBATIS instead of Hibernate, use:
 * <pre>
 *     &lt;bean id="userManager" class="org.appfuse.service.impl.GenericManagerImpl"&gt;
 *         &lt;constructor-arg&gt;
 *             &lt;bean class="org.appfuse.dao.ibatis.GenericDaoiBatis"&gt;
 *                 &lt;constructor-arg value="org.appfuse.model.User"/&gt;
 *                 &lt;property name="dataSource" ref="dataSource"/&gt;
 *                 &lt;property name="sqlMapClient" ref="sqlMapClient"/&gt;
 *             &lt;/bean&gt;
 *         &lt;/constructor-arg&gt;
 *     &lt;/bean&gt;
 * </pre>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class GenericManagerImpl<T, PK extends Serializable> implements GenericManager<T, PK> {
    protected final Log log = LogFactory.getLog(getClass());
    private GenericDao<T, PK> genericDao;

    public GenericManagerImpl(GenericDao<T, PK> genericDao) {
        this.genericDao = genericDao;
    }

    public List<T> getAll() {
        return genericDao.getAll();
    }

    public T get(PK id) {
        return genericDao.get(id);
    }

    public void save(T object) {
        genericDao.save(object);
    }

    public void remove(PK id) {
        genericDao.remove(id);
    }
}
