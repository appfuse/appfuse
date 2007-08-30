package org.appfuse.dao.ibatis;

import java.util.List;

import org.appfuse.dao.LookupDao;
import org.appfuse.model.Role;

/**
 * iBatis implementation of LookupDao.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class LookupDaoiBatis extends UniversalDaoiBatis implements LookupDao {

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<Role> getRoles() {
        log.debug("retrieving all role names...");

        return getSqlMapClientTemplate().queryForList("getRoles", null);
    }
}
