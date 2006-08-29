package org.appfuse.dao.hibernate;

import java.util.List;

import org.appfuse.dao.RoleDao;
import org.appfuse.model.Role;


/**
 * This class interacts with Spring's HibernateTemplate to save/delete and
 * retrieve Role objects.
 *
 * <p>
 * <a href="RoleDaoHibernate.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:dan@getrolling.com">Dan Kibler</a> 
 */
public class RoleDaoHibernate extends BaseDaoHibernate implements RoleDao {

    public List getRoles(Role role) {
        return getHibernateTemplate().find("from Role");
    }
    
    public Role getRole(Long roleId) {
        return (Role) getHibernateTemplate().get(Role.class, roleId);
    }

    public Role getRoleByName(String rolename) {
        List roles = getHibernateTemplate().find("from Role where name=?", rolename);
        if (roles.isEmpty()) {
            return null;
        } else {
            return (Role) roles.get(0);
        }
    }

    public void saveRole(Role role) {
        getHibernateTemplate().saveOrUpdate(role);
    }

    public void removeRole(String rolename) {
        Object role = getRoleByName(rolename);
        getHibernateTemplate().delete(role);
    }

}
