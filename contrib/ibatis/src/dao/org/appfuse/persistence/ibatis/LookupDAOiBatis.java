package org.appfuse.persistence.ibatis;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.appfuse.persistence.LookupDAO;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.support.SqlMapDaoSupport;


/**
 * iBatis implementation of LookupDAO.
 *
 * <p>
 * <a href="LookupDAOiBatis.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @version $Revision: 1.1 $ $Date: 2004/03/01 06:17:59 $
 *
 */
public class LookupDAOiBatis extends SqlMapDaoSupport implements LookupDAO {
    private Log log = LogFactory.getLog(LookupDAOiBatis.class);

    /**
     * @see org.appfuse.persistence.LookupDAO#getRoles()
     */
    public List getRoles() throws DataAccessException {
        if (log.isDebugEnabled()) {
            log.debug("retrieving all role names...");
        }

        return getSqlMapTemplate().executeQueryForList("getRoles", null);
    }
}
