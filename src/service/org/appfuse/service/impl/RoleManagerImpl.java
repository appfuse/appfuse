/*
 * Created on Aug 12, 2004
 *
 */
package org.appfuse.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.dao.RoleDAO;
import org.appfuse.model.Role;
import org.appfuse.service.RoleManager;

/**
 * Implementation of RoleManager interface. This basically transforms POJOs ->
 * Forms and back again.
 * </p>
 * 
 * <p>
 * <a href="RoleManagerImpl.java.html"> <i>View Source </i> </a>
 * </p>
 * 
 * @author <a href="mailto:dan@getrolling.com">Dan Kibler </a>
 * @version $Revision: 1.1 $ $Date: 2004/10/05 07:20:13 $
 */
public class RoleManagerImpl implements RoleManager {
    private static Log log = LogFactory.getLog(RoleManagerImpl.class);

    private RoleDAO dao;

    public void setRoleDAO(RoleDAO dao) {
        this.dao = dao;
    }

    public List getRoles(Role role) {
        return dao.getRoles(role);
    }

    public Role getRole(String rolename) {
        return dao.getRole(rolename);
    }

    public void saveRole(Role role) {

        dao.saveRole(role);
    }

    public void removeRole(String rolename) {
        dao.removeRole(rolename);
    }
}