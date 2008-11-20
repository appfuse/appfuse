package org.appfuse.service.impl;

import java.util.List;

import org.appfuse.dao.RoleDao;
import org.appfuse.dao.UserDao;
import org.appfuse.model.Role;
import org.appfuse.service.RoleManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of RoleManager interface.
 *
 * @author <a href="mailto:dan@getrolling.com">Dan Kibler</a>
 */
@Service("roleManager")
public class RoleManagerImpl extends GenericManagerImpl<Role, Long> implements RoleManager {
    @Autowired
    RoleDao roleDao;

    /**
     * {@inheritDoc}
     */
    public List<Role> getRoles(Role role) {
        return roleDao.getAll();
    }

    /**
     * {@inheritDoc}
     */
    public Role getRole(String rolename) {
        return roleDao.getRoleByName(rolename);
    }

    /**
     * {@inheritDoc}
     */
    public Role saveRole(Role role) {
        return dao.save(role);
    }

    /**
     * {@inheritDoc}
     */
    public void removeRole(String rolename) {
        roleDao.removeRole(rolename);
    }
}