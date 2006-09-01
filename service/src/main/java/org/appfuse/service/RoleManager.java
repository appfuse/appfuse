/**
 * Business Service Interface to handle communication between web and
 * persistence layer.
 *
 * <p><a href="RoleManager.java.html"><i>View Source</i></a></p>
 *
 * @author <a href="mailto:dan@getrolling.com">Dan Kibler </a>
 */
package org.appfuse.service;

import org.appfuse.dao.RoleDao;
import org.appfuse.model.Role;

import java.util.List;

public interface RoleManager extends Manager {
    public void setRoleDao(RoleDao dao);
    public List getRoles(Role role);
    public Role getRole(String rolename);
    public void saveRole(Role role);
    public void removeRole(String rolename);
}
