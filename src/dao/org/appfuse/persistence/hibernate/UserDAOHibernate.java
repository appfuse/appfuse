package org.appfuse.persistence.hibernate;

import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.appfuse.model.UserCookie;
import org.appfuse.model.UserRole;
import org.appfuse.persistence.DAOException;
import org.appfuse.persistence.UserDAO;
import org.springframework.orm.hibernate.HibernateCallback;


/**
 * This class interacts with Hibernate's Session object to save and
 * retrieve User objects.
 *
 * <p>
 * <a href="UserDAOHibernate.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @version $Revision: 1.5 $ $Date: 2004/05/04 06:08:55 $
 */
public class UserDAOHibernate extends BaseDAOHibernate implements UserDAO {
    private Log log = LogFactory.getLog(UserDAOHibernate.class);

    /**
     * @see org.appfuse.persistence.UserDAO#getUser(java.lang.String)
     */
    public User getUser(String username) throws DAOException {
        User user = null;

        List users =
            getHibernateTemplate().find("from User u where u.username=?",
                                        username);

        if ((users != null) && (users.size() > 0)) {
            user = (User) users.get(0);
        }

        if (user == null) {
            log.warn("uh oh, user not found...");
            throw new DAOException("User '" + username +
                                   "' not found in database!");
        }

        return user;
    }

    /**
     * @see org.appfuse.persistence.UserDAO#getUsers(org.appfuse.model.User)
     */
    public List getUsers(User user) throws DAOException {
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
     * @see org.appfuse.persistence.UserDAO#saveUser(org.appfuse.model.User)
     */
    public User saveUser(final User user) throws DAOException {
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
            for (Iterator it = user.getRoles().iterator(); it.hasNext();) {
                UserRole userRole = (UserRole) it.next();
                userRole.setUserId(user.getId());
                getHibernateTemplate().save(userRole);
            }
        }

        return user;
    }

    /**
     * @see org.appfuse.persistence.UserDAO#removeUser(java.lang.String)
     */
    public void removeUser(String username) throws DAOException {
        removeUserCookies(username);
        User user = getUser(username);
        user.getRoles().clear();
        getHibernateTemplate().delete(user);
    }

    /**
     * @see org.appfuse.persistence.UserDAO#getUserCookie(java.lang.String)
     */
    public UserCookie getUserCookie(String cookieId) throws DAOException {
        List cookies =
            getHibernateTemplate().find("from UserCookie c where c.cookieId=?",
                                        cookieId);

        if (cookies.size() == 0) {
            return null;
        }

        return (UserCookie) cookies.get(0);
    }

    /**
     * @see org.appfuse.persistence.UserDAO#removeUserCookies(java.lang.String)
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
     * @see org.appfuse.persistence.UserDAO#saveUserCookie(org.appfuse.model.UserCookie)
     */
    public void saveUserCookie(UserCookie cookie) throws DAOException {
        getHibernateTemplate().saveOrUpdate(cookie);
    }
}
