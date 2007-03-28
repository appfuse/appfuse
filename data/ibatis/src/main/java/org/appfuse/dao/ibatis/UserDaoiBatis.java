package org.appfuse.dao.ibatis;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UserDetailsService;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.appfuse.dao.UserDao;
import org.appfuse.model.Role;
import org.appfuse.model.User;
import org.springframework.orm.ObjectRetrievalFailureException;

/**
 * This class interacts with iBatis's SQL Maps to save and retrieve User
 * related objects.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class UserDaoiBatis extends GenericDaoiBatis<User, Long>implements UserDao, UserDetailsService {
    
    public UserDaoiBatis() {
        super(org.appfuse.model.User.class);
    }
    
    /**
     * Get user by id.
     *
     * @param userId the user's id
     * @return a populated user object
     */
    @Override
    public User get(Long userId) {
        User user = (User) getSqlMapClientTemplate().queryForObject("getUser", userId);

        if (user == null) {
            logger.warn("uh oh, user not found...");
            throw new ObjectRetrievalFailureException(User.class, userId);
        } else {
            List roles = getSqlMapClientTemplate().queryForList("getUserRoles", user);
            user.setRoles(new HashSet<Role>(roles));
        }

        return user;
    }

    /**
     * @see org.appfuse.dao.UserDao#getUsers()
     */
    public List<User> getUsers() {
        List users = getSqlMapClientTemplate().queryForList("getUsers", null);

        // get the roles for each user
        for (int i = 0; i < users.size(); i++) {
            User user = (User) users.get(i);

            List roles =  getSqlMapClientTemplate().queryForList("getUserRoles", user);
            user.setRoles(new HashSet<Role>(roles));
            users.set(i, user);
        }

        return users;
    }

    /**
     * Convenience method to delete roles
     * @param userId the id of the user to delete
     */
    private void deleteUserRoles(final Long userId) {
        getSqlMapClientTemplate().update("deleteUserRoles", userId);
    }

    private void addUserRoles(final User user) {
        if (user.getRoles() != null) {
            for (Role role : user.getRoles()) {
                Map<String, Long> newRole = new HashMap<String, Long>();
                newRole.put("userId", user.getId());
                newRole.put("roleId", role.getId());

                List userRoles = getSqlMapClientTemplate().queryForList("getUserRoles", user);
                if (userRoles.isEmpty()) {
                    getSqlMapClientTemplate().update("addUserRole", newRole);
                }
            }
        }
    }

    /**
     * @see org.appfuse.dao.UserDao#saveUser(org.appfuse.model.User)
     */
    public User saveUser(final User user) {
        iBatisDaoUtils.prepareObjectForSaveOrUpdate(user);
        
        if (user.getId() == null) {
            Long id = (Long) getSqlMapClientTemplate().insert("addUser", user);
            user.setId(id);
            addUserRoles(user);
        } else {
            getSqlMapClientTemplate().update("updateUser", user);
            deleteUserRoles(user.getId());
            addUserRoles(user);
        }
        return user;
    }


    @Override
    public void remove(Long userId) {
        deleteUserRoles(userId);
        getSqlMapClientTemplate().update("deleteUser", userId);
    }
    
    /** 
     * @see org.acegisecurity.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
     */
     public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
         User user = (User) getSqlMapClientTemplate().queryForObject("getUserByUsername", username);

         if (user == null) {
             logger.warn("uh oh, user not found...");
             throw new UsernameNotFoundException("user '" + username + "' not found...");
         } else {
             List roles = getSqlMapClientTemplate().queryForList("getUserRoles", user);
             user.setRoles(new HashSet<Role>(roles));
         }

         return user;
     }
}
