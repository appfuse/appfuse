package org.appfuse.persistence.hibernate;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.persistence.LookupDAO;
import org.springframework.dao.DataAccessException;


/**
 * Hibernate implementation of LookupDAO.
 *
 * <p>
 * <a href="LookupDAOHibernate.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @version $Revision: 1.4 $ $Date: 2004/04/14 06:53:12 $
 *
 */
public class LookupDAOHibernate extends BaseDAOHibernate implements LookupDAO {
    private Log log = LogFactory.getLog(LookupDAOHibernate.class);

    /**
     * @see org.appfuse.persistence.LookupDAO#getRoles()
     */
    public List getRoles() throws DataAccessException {
        if (log.isDebugEnabled()) {
            log.debug("retrieving all role names...");
        }

        return getHibernateTemplate().find("from Role r order by r.name");
    }
}
