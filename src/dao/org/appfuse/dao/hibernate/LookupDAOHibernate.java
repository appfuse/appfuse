package org.appfuse.dao.hibernate;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.dao.LookupDAO;


/**
 * Hibernate implementation of LookupDAO.
 *
 * <p>
 * <a href="LookupDAOHibernate.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class LookupDAOHibernate extends BaseDAOHibernate implements LookupDAO {
    private Log log = LogFactory.getLog(LookupDAOHibernate.class);

    /**
     * @see org.appfuse.dao.LookupDAO#getRoles()
     */
    public List getRoles() {
        if (log.isDebugEnabled()) {
            log.debug("retrieving all role names...");
        }

        return getHibernateTemplate().find("from Role r order by r.name");
    }
}
