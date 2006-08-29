/**
 * Business Service Interface to handle communication between web and
 * persistence layer.
 *
 * <p><a href="RoleManager.java.html"><i>View Source</i></a></p>
 *
 * @author <a href="mailto:dan@getrolling.com">Dan Kibler </a>
 */
package org.appfuse.service;

import java.util.List;

import org.appfuse.dao.RoleDao;
import org.appfuse.model.Role;

public interface RoleManager {

    public void setRoleDao(RoleDao dao);

    public List getRoles(Role role);

    public Role getRole(String rolename);

    public void saveRole(Role role);

    public void removeRole(String rolename);
}
