package org.appfuse.persistence.hibernate;

import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.persistence.DAO;
import org.appfuse.persistence.DAOException;
import org.springframework.orm.hibernate.support.HibernateDaoSupport;


/**
 * This class serves as the Base class for all other DAOs - namely to hold
 * common methods that they might all use.</p>
 *
 * <p><a href="BaseDAOHibernate.java.html"><i>View Source</i></a></p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @version $Revision: 1.4 $ $Date: 2004/05/16 02:16:48 $
 */
public class BaseDAOHibernate extends HibernateDaoSupport
        implements DAO {
    private Log log = LogFactory.getLog(BaseDAOHibernate.class);

    /**
     * @see org.appfuse.persistence.DAO#getObject(java.lang.Object)
     */
    public Object getObject(Object o) throws DAOException {
        Long id = null;
        // use reflection to get the id and fetch the object
        try {
            Method meth = o.getClass().getMethod("getId", new Class[0]);
            id = (Long) meth.invoke(o, new Object[0]);
            o = getHibernateTemplate().get(o.getClass(), id);
        } catch (Exception e) {
            throw new DAOException("exception loading '"
                    + o.getClass().getName() + "' with id '" + id + "'");
        }
        return o;
    }

    /**
     * @see org.appfuse.persistence.DAO#saveObject(java.lang.Object)
     */
    public Object saveObject(Object o) throws DAOException {
        getHibernateTemplate().saveOrUpdateCopy(o);
        return o;
    }

    /**
     * @see org.appfuse.persistence.DAO#removeObject(java.lang.Object)
     */
    public void removeObject(Object o) throws DAOException {
        if (log.isDebugEnabled()) {
            log.debug("loading object to delete....");
        }

        o = getObject(o);
        if (o != null) {
            getHibernateTemplate().delete(o);
        }
    }
}
