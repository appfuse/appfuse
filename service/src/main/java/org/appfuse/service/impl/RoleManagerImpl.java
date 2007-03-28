package org.appfuse.service.impl;

import java.util.List;

import org.appfuse.dao.RoleDao;
import org.appfuse.model.Role;
import org.appfuse.service.RoleManager;

/**
 * Implementation of RoleManager interface.
 * 
 * @author <a href="mailto:dan@getrolling.com">Dan Kibler</a>
 */
public class RoleManagerImpl extends UniversalManagerImpl implements RoleManager {
    private RoleDao dao;

    public void setRoleDao(RoleDao dao) {
        this.dao = dao;
    }

    public List<Role> getRoles(Role role) {
        return dao.getAll();
    }

    public Role getRole(String rolename) {
        return dao.getRoleByName(rolename);
    }

    public Role saveRole(Role role) {
        return dao.save(role);
    }

    public void removeRole(String rolename) {
        dao.removeRole(rolename);
    }
}