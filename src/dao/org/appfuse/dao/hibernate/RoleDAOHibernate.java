package org.appfuse.dao.hibernate;

import java.util.List;

import org.appfuse.model.Role;
import org.appfuse.dao.RoleDAO;


/**
 * This class interacts with Spring's HibernateTemplate to save/delete and
 * retrieve Role objects.
 *
 * <p>
 * <a href="RoleDAOHibernate.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:dan@getrolling.com">Dan Kibler</a> 
 */
public class RoleDAOHibernate extends BaseDAOHibernate implements RoleDAO {

    public List getRoles(Role role) {
        return getHibernateTemplate().find("from Role");
    }

    public Role getRole(String rolename) {
        return (Role) getHibernateTemplate().get(Role.class, rolename);
        /*
        try {
            Hibernate.initialize(role);
        } catch (HibernateException e) {
            log.error(e);
        }
        return role;
        */
    }

    public void saveRole(Role role) {
        getHibernateTemplate().saveOrUpdate(role);
    }

    public void removeRole(String rolename) {
        Object role = getHibernateTemplate().load(Role.class, rolename);
        getHibernateTemplate().delete(role);
    }

}
