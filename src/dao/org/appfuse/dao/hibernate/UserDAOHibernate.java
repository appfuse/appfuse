package org.appfuse.dao.hibernate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import org.appfuse.dao.UserDAO;
import org.appfuse.model.User;
import org.appfuse.model.UserCookie;
import org.appfuse.model.UserRole;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.orm.hibernate.HibernateCallback;


/**
 * This class interacts with Spring's HibernateTemplate to save/delete and
 * retrieve User objects.
 *
 * <p>
 * <a href="UserDAOHibernate.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class UserDAOHibernate extends BaseDAOHibernate implements UserDAO {

    /**
     * @see org.appfuse.dao.UserDAO#getUser(java.lang.String)
     */
    public User getUser(String username) {
        User user = null;

        List users =
            getHibernateTemplate().find("from User u where u.username=?",
                                        username);

        if ((users != null) && (users.size() > 0)) {
            user = (User) users.get(0);
        }

        if (user == null) {
            log.warn("uh oh, user '" + username + "' not found...");
            throw new ObjectRetrievalFailureException(User.class, username);
        }

        return user;
    }

    /**
     * @see org.appfuse.dao.UserDAO#getUsers(org.appfuse.model.User)
     */
    public List getUsers(User user) {
        return getHibernateTemplate().find("from User u order by upper(u.username)");
    }

    /**
     * Convenience method to delete roles
     * @param user
     */
    private void deleteUserRoles(final User user) {
        getHibernateTemplate().execute(new HibernateCallback() {
                String deleteRolesQuery = "from UserRole r where r.username=?";

                public Object doInHibernate(Session ses)
                throws HibernateException {
                    ses.delete(deleteRolesQuery, user.getUsername(),
                               Hibernate.STRING);
                    return null;
                }
            });
    }

    /**
     * @see org.appfuse.dao.UserDAO#saveUser(org.appfuse.model.User)
     */
    public User saveUser(final User user) {
        if (log.isDebugEnabled()) {
            log.debug("user's id: " + user.getId());
        }

        String countUserQuery =
            "select count(*) from User u where u.username=?";

        int count =
            ((Integer) getHibernateTemplate()
                           .find(countUserQuery, user.getUsername()).iterator()
                           .next()).intValue();

        if (count == 0) {
            user.setId(null);
            getHibernateTemplate().save(user);
        } else {
            // existing user, delete and re-add their roles
            getHibernateTemplate().saveOrUpdate(user);
            deleteUserRoles(user);
        }
        
        if (user.getRoles() != null) {
            // prevent duplicates
            List preventDups = new ArrayList();
            for (Iterator it = user.getRoles().iterator(); it.hasNext();) {
                UserRole userRole = (UserRole) it.next();
                // make sure the role hasn't already been added
                if (!preventDups.contains(userRole.getRoleName())) {
                    userRole.setUserId(user.getId());
                    userRole.setUsername(user.getUsername());
                    getHibernateTemplate().save(userRole);
                    preventDups.add(userRole.getRoleName());
                } 
            }
        }

        // Have to call getUser here b/c the user might still have duplicate
        // roles.  The preventDups logic can't remove items b/c it'll throw
        // a java.util.ConcurrentModificationException.  Cleaner solutions
        // welcome. ;-)
        return getUser(user.getUsername());
    }

    /**
     * @see org.appfuse.dao.UserDAO#removeUser(java.lang.String)
     */
    public void removeUser(String username) {
        removeUserCookies(username);
        User user = getUser(username);
        user.getRoles().clear();
        getHibernateTemplate().delete(user);
    }

    /**
     * @see org.appfuse.dao.UserDAO#getUserCookie(org.appfuse.model.UserCookie)
     */
    public UserCookie getUserCookie(final UserCookie cookie) {
        List cookies = getHibernateTemplate().find(
                "from UserCookie c where c.username=? and c.cookieId=?", 
                new Object[]{cookie.getUsername(), cookie.getCookieId()});

        if (cookies.size() == 0) {
            return null;
        }

        return (UserCookie) cookies.get(0);
    }

    /**
     * @see org.appfuse.dao.UserDAO#removeUserCookies(java.lang.String)
     */
    public void removeUserCookies(String username) {
        // delete any cookies associated with this user
        List cookies =
            getHibernateTemplate().find("from UserCookie c where c.username=?",
                                        username);

        if ((cookies.size() > 0) && log.isDebugEnabled()) {
            log.debug("deleting " + cookies.size() + " cookies for user '" +
                      username + "'");
        }

        getHibernateTemplate().deleteAll(cookies);
    }

    /**
     * @see org.appfuse.dao.UserDAO#saveUserCookie(org.appfuse.model.UserCookie)
     */
    public void saveUserCookie(UserCookie cookie) {
        getHibernateTemplate().saveOrUpdate(cookie);
    }
}
