package org.appfuse.dao.ibatis;

import java.util.List;

import org.appfuse.dao.RoleDAO;
import org.appfuse.model.Role;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;


/**
 * This class interacts with iBatis's SQL Maps to save/delete and
 * retrieve Role objects.
 *
 * <p>
 * <a href="RoleDAOiBatis.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class RoleDAOiBatis extends SqlMapClientDaoSupport implements RoleDAO {

    public List getRoles(Role role) {
        return getSqlMapClientTemplate().queryForList("getRoles", null);
    }

    public Role getRole(String name) {
        return (Role) getSqlMapClientTemplate().queryForObject("getRole", name);
    }

    public void saveRole(final Role role) {
        if (role.getVersion() == null) {
            role.setVersion(new Integer(1));
            getSqlMapClientTemplate().update("addRole", role);
        } else {
            role.setVersion(new Integer(role.getVersion().intValue()+1));
            getSqlMapClientTemplate().update("updateRole", role);
        }
    }

    public void removeRole(String name) {
        getSqlMapClientTemplate().update("deleteRole", name);
    }

}
