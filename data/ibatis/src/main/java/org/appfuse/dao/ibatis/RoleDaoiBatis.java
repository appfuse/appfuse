package org.appfuse.dao.ibatis;

import org.appfuse.dao.RoleDao;
import org.appfuse.model.Role;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * This class interacts with iBatis's SQL Maps to save/delete and
 * retrieve Role objects.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
@Repository
public class RoleDaoiBatis extends GenericDaoiBatis<Role, Long> implements RoleDao {

    /**
     * Constructor to create a Generics-based version using Role as the entity
     */
    public RoleDaoiBatis() {
        super(Role.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Role> getAll() {
        return getSqlMapClientTemplate().queryForList("getRoles", null);
    }

    /**
     * {@inheritDoc}
     */
    public Role getRoleByName(String name) {
        return (Role) getSqlMapClientTemplate().queryForObject("getRoleByName", name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Role save(final Role role) {
        if (role.getId() == null) {
            getSqlMapClientTemplate().insert("addRole", role);
        } else {
            getSqlMapClientTemplate().update("updateRole", role);
        }
        return role;
    }

    /**
     * {@inheritDoc}
     */
    public void removeRole(String rolename) {
        getSqlMapClientTemplate().update("deleteRole", rolename);
    }

}
