/**
 * Business Delegate (Proxy) Interface to handle communication between web and
 * persistence layer.
 * 
 * <p>
 * <a href="RoleManager.java.html"> <i>View Source </i> </a>
 * </p>
 * 
 * @author <a href="mailto:dan@getrolling.com">Dan Kibler </a>
 * @version $Revision: 1.1 $ $Date: 2004/10/05 07:20:12 $
 */
package org.appfuse.service;

import java.util.List;

import org.appfuse.model.Role;

public interface RoleManager {

    public List getRoles(Role role);

    public Role getRole(String rolename);

    public void saveRole(Role role);

    public void removeRole(String rolename);
}