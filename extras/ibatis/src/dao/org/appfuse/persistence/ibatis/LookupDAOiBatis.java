package org.appfuse.persistence.ibatis;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.appfuse.persistence.LookupDAO;

import org.springframework.orm.ibatis.support.SqlMapDaoSupport;


/**
 * iBatis implementation of LookupDAO.
 *
 * <p>
 * <a href="LookupDAOiBatis.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @version $Revision: 1.4 $ $Date: 2004/05/25 06:27:39 $
 *
 */
public class LookupDAOiBatis extends SqlMapDaoSupport implements LookupDAO {
    private Log log = LogFactory.getLog(LookupDAOiBatis.class);

    /**
     * @see org.appfuse.persistence.LookupDAO#getRoles()
     */
    public List getRoles() {
        if (log.isDebugEnabled()) {
            log.debug("retrieving all role names...");
        }

        return getSqlMapTemplate().executeQueryForList("getRoles", null);
    }
}
