package org.appfuse.dao.ibatis;

import java.util.Date;
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
        if (role.getUpdated() == null) {
            role.setUpdated(new Date());
            getSqlMapClientTemplate().update("addRole", role);
        } else {
            getSqlMapClientTemplate().update("updateRole", role);
        }
    }

    public void removeRole(String name) {
        getSqlMapClientTemplate().update("deleteRole", name);
    }

}
