package org.appfuse.dao.jpa;

import java.util.List;

import javax.persistence.Query;

import org.appfuse.dao.RoleDao;
import org.appfuse.model.Role;


/**
 * This class interacts with Spring's HibernateTemplate to save/delete and
 * retrieve Role objects.
 *
 * @author <a href="mailto:bwnoll@gmail.com">Bryan Noll</a> 
 */
public class RoleDaoJpa extends GenericDaoJpa<Role, Long> implements RoleDao {

    public RoleDaoJpa() {
        super(Role.class);
    }

    public Role getRoleByName(String rolename) {
        Query q = super.entityManager.createQuery("select r from Role r where r.name = ?");
        q.setParameter(1, rolename);
        List roles = q.getResultList();
        
        if (roles.isEmpty()) {
            return null;
        } else {
            return (Role) roles.get(0);
        }
    }
    
    public void removeRole(String rolename) {
        Object role = getRoleByName(rolename);
        super.entityManager.remove(role);
    }
}
