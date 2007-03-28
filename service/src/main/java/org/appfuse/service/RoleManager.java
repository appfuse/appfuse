package org.appfuse.service;

import org.appfuse.model.Role;

import java.util.List;

/**
 * Business Service Interface to handle communication between web and
 * persistence layer.
 *
 * @author <a href="mailto:dan@getrolling.com">Dan Kibler </a>
 */
public interface RoleManager extends UniversalManager {
    public List getRoles(Role role);
    public Role getRole(String rolename);
    public Role saveRole(Role role);
    public void removeRole(String rolename);
}
