package org.appfuse.persistence.ibatis;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.appfuse.model.User;
import org.appfuse.model.UserCookie;
import org.appfuse.model.UserRole;
import org.appfuse.persistence.DAOException;
import org.appfuse.persistence.UserDAO;

import org.springframework.orm.ibatis.support.SqlMapDaoSupport;


/**
 * This class interacts with iBatis's SQL Maps to save and retrieve User
 * related objects.
 *
 * <p>
 * <a href="UserDAOiBatis.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @version $Revision: 1.1 $ $Date: 2004/04/29 22:22:52 $
 */
public class UserDAOiBatis extends SqlMapDaoSupport implements UserDAO {
    private Log log = LogFactory.getLog(UserDAOiBatis.class);

    /**
     * Get user by username.
     *
     * @param username the user's username
     * @return a populated user object
     * @throws DAOException if anything goes wrong
     */
    public User getUser(String username) throws DAOException {
        User user = new User();
        user.setUsername(username);

        List users = getSqlMapTemplate().executeQueryForList("getUser", user);

        if ((users != null) && (users.size() > 0)) {
            user = (User) users.get(0);

            List roles =
                getSqlMapTemplate().executeQueryForList("getUserRoles", user);
            user.setRoles(roles);
        }

        if ((users != null) && (users.size() == 0)) {
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
        List users = getSqlMapTemplate().executeQueryForList("getUsers", null);

        // get the roles for each user
        for (int i = 0; i < users.size(); i++) {
            user = (User) users.get(i);

            List roles =
                getSqlMapTemplate().executeQueryForList("getUserRoles", user);
            user.setRoles(roles);
            users.set(i, user);
        }

        return users;
    }

    /**
     * Convenience method to delete roles
     * @param user
     */
    private void deleteUserRoles(final User user) {
        getSqlMapTemplate().executeUpdate("deleteUserRoles", user);
    }

    private void addUserRoles(final User user) {
        if (user.getRoles() != null) {
            UserRole userRole = null;

            for (Iterator it = user.getRoles().iterator(); it.hasNext();) {
                userRole = (UserRole) it.next();

                Long pk =
                    (Long) getSqlMapTemplate().executeQueryForObject("getRoleId",
                                                                     null);

                if (pk == null) {
                    pk = new Long(0);
                }

                userRole.setId(new Long(pk.longValue() + 1));

                userRole.setUserId(user.getId());
                userRole.setUsername(user.getUsername());
                getSqlMapTemplate().executeUpdate("addUserRole", userRole);
            }
        }
    }

    /**
     * @see org.appfuse.persistence.UserDAO#saveUser(org.appfuse.model.User)
     */
    public User saveUser(final User user) throws DAOException {
        if (user.getId() == null) {
            Long pk =
                (Long) getSqlMapTemplate().executeQueryForObject("getUserId",
                                                                 null);

            if (pk == null) {
                pk = new Long(0);
            }

            user.setId(new Long(pk.longValue() + 1));

            if (log.isDebugEnabled()) {
                log.debug("user's id: " + user.getId());
            }

            getSqlMapTemplate().executeUpdate("addUser", user);
            addUserRoles(user);
        } else {
            getSqlMapTemplate().executeUpdate("updateUser", user);
            deleteUserRoles(user);
            addUserRoles(user);
        }

        return user;
    }

    /**
     * @see org.appfuse.persistence.UserDAO#removeUser(org.appfuse.model.User)
     */
    public void removeUser(User user) throws DAOException {
        removeUserCookies(user.getUsername());
        deleteUserRoles(user);
        getSqlMapTemplate().executeUpdate("deleteUser", user);
    }

    /**
     * @see org.appfuse.persistence.UserDAO#getUserCookie(java.lang.String)
     */
    public UserCookie getUserCookie(String cookieId) throws DAOException {
        UserCookie c = new UserCookie();
        c.setCookieId(cookieId);

        List cookies =
            getSqlMapTemplate().executeQueryForList("getUserCookies", c);

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
        UserCookie c = new UserCookie();
        c.setUsername(username);

        getSqlMapTemplate().executeUpdate("deleteUserCookies", c);
    }

    /**
     * @see org.appfuse.persistence.UserDAO#saveUserCookie(org.appfuse.model.UserCookie)
     */
    public void saveUserCookie(UserCookie cookie) throws DAOException {
        if (cookie.getId() == null) {
            Long pk =
                (Long) getSqlMapTemplate().executeQueryForObject("getUserCookieId",
                                                                 null);

            if (pk == null) {
                pk = new Long(0);
            }

            cookie.setId(new Long(pk.longValue() + 1));
            getSqlMapTemplate().executeUpdate("addUserCookie", cookie);
        } else {
            getSqlMapTemplate().executeUpdate("updateUserCookie", cookie);
        }
    }
}
