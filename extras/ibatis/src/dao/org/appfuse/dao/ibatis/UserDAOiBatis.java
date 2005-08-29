package org.appfuse.dao.ibatis;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.appfuse.dao.UserDAO;
import org.appfuse.model.Role;
import org.appfuse.model.User;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

/**
 * This class interacts with iBatis's SQL Maps to save and retrieve User
 * related objects.
 *
 * <p><a href="UserDAOiBatis.java.html"><i>View Source</i></a></p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class UserDAOiBatis extends SqlMapClientDaoSupport implements UserDAO {
    /**
     * Get user by username.
     *
     * @param username the user's username
     * @return a populated user object
     */
    public User getUser(String username) {
        User user = (User) getSqlMapClientTemplate().queryForObject("getUser", username);

        if (user == null) {
            logger.warn("uh oh, user not found...");
            throw new ObjectRetrievalFailureException(User.class, username);
        } else {
            List roles = getSqlMapClientTemplate().queryForList("getUserRoles", user);
            user.setRoles(new HashSet(roles));
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

            List roles =  getSqlMapClientTemplate().queryForList("getUserRoles", user);
            user.setRoles(new HashSet(roles));
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
            for (Iterator it = user.getRoles().iterator(); it.hasNext();) {
                Role role = (Role) it.next();
                Map newRole = new HashMap();
                newRole.put("username", user.getUsername());
                newRole.put("roleName", role.getName());

                List userRoles = getSqlMapClientTemplate().queryForList("getUserRoles", user.getUsername());

                if (userRoles.isEmpty()) {
                    getSqlMapClientTemplate().update("addUserRole", newRole);
                }
            }
        }
    }

    /**
     * @see org.appfuse.dao.UserDAO#saveUser(org.appfuse.model.User)
     */
    public void saveUser(final User user) {
        if (user.getVersion() == null) {
            user.setVersion(new Integer(1));
            getSqlMapClientTemplate().update("addUser", user);
            addUserRoles(user);
        } else {
            user.setVersion(new Integer(user.getVersion().intValue()+1));
            getSqlMapClientTemplate().update("updateUser", user);
            deleteUserRoles(user);
            addUserRoles(user);
        }
    }

    /**
     * @see org.appfuse.dao.UserDAO#removeUser(java.lang.String)
     */
    public void removeUser(String username) {
        User user = getUser(username);
        deleteUserRoles(user);
        getSqlMapClientTemplate().update("deleteUser", user);
    }
}
