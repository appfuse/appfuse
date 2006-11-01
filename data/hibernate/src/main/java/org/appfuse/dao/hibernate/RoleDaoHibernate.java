package org.appfuse.dao.hibernate;

import java.util.List;

import org.appfuse.dao.RoleDao;
import org.appfuse.model.Role;


/**
 * This class interacts with Spring's HibernateTemplate to save/delete and
 * retrieve Role objects.
 *
 * @author <a href="mailto:bwnoll@gmail.com">Bryan Noll</a> 
 */
public class RoleDaoHibernate extends GenericDaoHibernate<Role, Long> implements RoleDao {

    public RoleDaoHibernate() {
        super(Role.class);
    }

    public Role getRoleByName(String rolename) {
        List roles = getHibernateTemplate().find("from Role where name=?", rolename);
        if (roles.isEmpty()) {
            return null;
        } else {
            return (Role) roles.get(0);
        }
    }
    
    public void removeRole(String rolename) {
        Object role = getRoleByName(rolename);
        getHibernateTemplate().delete(role);
    }
}
