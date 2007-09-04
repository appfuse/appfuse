package org.appfuse.dao.jpa;

import java.util.List;

import org.appfuse.dao.LookupDao;
import org.appfuse.model.Role;

/**
 * JPA implementation of LookupDao.
 *
 * @author <a href="mailto:bwnoll@gmail.com">Bryan Noll</a>
 */
public class LookupDaoJpa extends UniversalDaoJpa implements LookupDao {

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<Role> getRoles() {
        log.debug("retrieving all role names...");

        return super.entityManager.createQuery(
                "select r from Role r order by name").getResultList();
    }
}
