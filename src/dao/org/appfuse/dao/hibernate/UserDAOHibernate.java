package org.appfuse.dao.hibernate;

import java.util.List;

import org.appfuse.dao.UserDAO;
import org.appfuse.model.User;
import org.appfuse.model.UserCookie;
import org.springframework.orm.ObjectRetrievalFailureException;




/**
 * This class interacts with Spring's HibernateTemplate to save/delete and
 * retrieve User objects.
 *
 * <p>
 * <a href="UserDAOHibernate.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
  *  Modified by <a href="mailto:dan@getrolling.com">Dan Kibler </a> 
*/
public class UserDAOHibernate extends BaseDAOHibernate implements UserDAO {

    /**
     * @see org.appfuse.dao.UserDAO#getUser(java.lang.String)
     */

    public User getUser(String username) {
        User user = (User) getHibernateTemplate().get(User.class, username);
        
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
     * @see org.appfuse.dao.UserDAO#saveUser(org.appfuse.model.User)
     */
    public void saveUser(final User user) {
        if (log.isDebugEnabled()) {
            log.debug("user's id: " + user.getUsername());
        }
        getHibernateTemplate().saveOrUpdate(user);
    }

    /**
     * @see org.appfuse.dao.UserDAO#removeUser(java.lang.String)
     */
    public void removeUser(String username) {
        removeUserCookies(username);
        User user = getUser(username);
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
