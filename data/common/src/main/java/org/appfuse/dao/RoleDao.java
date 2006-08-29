package org.appfuse.dao;

import java.util.List;

import org.appfuse.model.Role;

/**
 * Role Data Access Object (DAO) interface.
 *
 * <p><a href="RoleDao.java.html"><i>View Source</i></a></p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public interface RoleDao extends Dao {
    /**
     * Gets role information based on rolename
     * @param rolename the rolename
     * @return role populated role object
     */
    public Role getRoleByName(String rolename);

    /**
     * Gets a list of roles based on parameters passed in.
     *
     * @return List populated list of roles
     */
    public List getRoles(Role role);

    /**
     * Saves a role's information
     * @param role the object to be saved
     */
    public void saveRole(Role role);

    /**
     * Removes a role from the database by name
     * @param rolename the role's rolename
     */
    public void removeRole(String rolename);
}
