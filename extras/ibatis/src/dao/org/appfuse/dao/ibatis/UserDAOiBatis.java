package org.appfuse.dao.ibatis;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.appfuse.model.User;
import org.appfuse.model.UserCookie;
import org.appfuse.model.UserRole;
import org.appfuse.dao.UserDAO;

import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
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
 */
public class UserDAOiBatis extends SqlMapClientDaoSupport implements UserDAO {

    /**
     * Get user by username.
     *
     * @param username the user's username
     * @return a populated user object
     * @throws DAOException if anything goes wrong
     */
    public User getUser(String username) {
        User user = new User();
        user.setUsername(username);

        List users = getSqlMapClientTemplate().queryForList("getUser", user);

        if ((users != null) && (users.size() > 0)) {
            user = (User) users.get(0);

            List roles =
            	getSqlMapClientTemplate().queryForList("getUserRoles", user);
            user.setRoles(roles);
        }

        if ((users != null) && (users.size() == 0)) {
            logger.warn("uh oh, user not found...");
            throw new ObjectRetrievalFailureException(User.class, username);
        }

        return user;
    }

    /**
     * @see org.appfuse.dao.UserDAO#getUsers(org.appfuse.model.User)
     */
    public List getUsers(User user) {
        List users = getSqlMapClientTemplate().queryForList("getUsers", null);

        // get the roles for each user
        for (int i = 0; i < users.size(); i++) {
            user = (User) users.get(i);

            List roles =
            	getSqlMapClientTemplate().queryForList("getUserRoles", user);
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
    	getSqlMapClientTemplate().update("deleteUserRoles", user);
    }

    private void addUserRoles(final User user) {
        if (user.getRoles() != null) {
            UserRole userRole = null;
            List preventDups = new ArrayList();
            for (Iterator it = user.getRoles().iterator(); it.hasNext();) {
                userRole = (UserRole) it.next();
                if (!preventDups.contains(userRole.getRoleName())) {
                    Long pk = (Long) getSqlMapClientTemplate().queryForObject("getRoleId", null);
    
                    if (pk == null) {
                        pk = new Long(0);
                    }
    
                    userRole.setId(new Long(pk.longValue() + 1));
    
                    userRole.setUserId(user.getId());
                    userRole.setUsername(user.getUsername());
                    getSqlMapClientTemplate().update("addUserRole", userRole);
                    preventDups.add(userRole.getRoleName());
                }                    
            }
        }
    }

    /**
     * @see org.appfuse.dao.UserDAO#saveUser(org.appfuse.model.User)
     */
    public User saveUser(final User user) {
        if (user.getId() == null) {
            Long pk =
                (Long) getSqlMapClientTemplate().queryForObject("getUserId",
                                                                 null);

            if (pk == null) {
                pk = new Long(0);
            }

            user.setId(new Long(pk.longValue() + 1));

            if (logger.isDebugEnabled()) {
                logger.debug("user's id: " + user.getId());
            }

            getSqlMapClientTemplate().update("addUser", user);
            addUserRoles(user);
        } else {
            getSqlMapClientTemplate().update("updateUser", user);
            deleteUserRoles(user);
            addUserRoles(user);
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
        User user = getUser(username);
        removeUserCookies(user.getUsername());
        deleteUserRoles(user);
        getSqlMapClientTemplate().update("deleteUser", user);
    }

    /**
     * @see org.appfuse.dao.UserDAO#getUserCookie(java.lang.String)
     */
    public UserCookie getUserCookie(UserCookie userCookie) {

        List cookies =
        	getSqlMapClientTemplate().queryForList("getUserCookies", userCookie);

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
        UserCookie c = new UserCookie();
        c.setUsername(username);

        getSqlMapClientTemplate().update("deleteUserCookies", c);
    }

    /**
     * @see org.appfuse.dao.UserDAO#saveUserCookie(org.appfuse.model.UserCookie)
     */
    public void saveUserCookie(UserCookie cookie) {
        if (cookie.getId() == null) {
            Long pk =
                (Long) getSqlMapClientTemplate().queryForObject("getUserCookieId",
                                                                 null);

            if (pk == null) {
                pk = new Long(0);
            }

            cookie.setId(new Long(pk.longValue() + 1));
            getSqlMapClientTemplate().update("addUserCookie", cookie);
        } else {
            getSqlMapClientTemplate().update("updateUserCookie", cookie);
        }
    }
}
