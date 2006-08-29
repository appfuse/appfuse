package org.appfuse.dao.ibatis;

import java.util.List;

import org.appfuse.dao.RoleDao;
import org.appfuse.model.Role;

/**
 * This class interacts with iBatis's SQL Maps to save/delete and
 * retrieve Role objects.
 *
 * <p>
 * <a href="RoleDaoiBatis.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class RoleDaoiBatis extends BaseDaoiBATIS implements RoleDao {

    public List getRoles(Role role) {
        return getSqlMapClientTemplate().queryForList("getRoles", null);
    }
    
    public Role getRoleByName(String name) {
        return (Role) getSqlMapClientTemplate().queryForObject("getRoleByName", name);
    }

    public void saveRole(final Role role) {
        if (role.getId() == null) {
            getSqlMapClientTemplate().update("addRole", role);
        } else {
            getSqlMapClientTemplate().update("updateRole", role);
        }
    }

    public void removeRole(String rolename) {
        getSqlMapClientTemplate().update("deleteRole", rolename);
    }

}
