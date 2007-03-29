package org.appfuse.service.impl;

import java.util.List;

import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.appfuse.dao.UserDao;
import org.appfuse.model.User;
import org.appfuse.service.UserExistsException;
import org.appfuse.service.UserManager;
import org.springframework.dao.DataIntegrityViolationException;

import javax.persistence.EntityExistsException;


/**
 * Implementation of UserManager interface.</p>
 * 
 * <p>
 * <a href="UserManagerImpl.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class UserManagerImpl extends UniversalManagerImpl implements UserManager {
    private UserDao dao;

    /**
     * Set the Dao for communication with the data layer.
     * @param dao the UserDao that communicates with the database
     */
    public void setUserDao(UserDao dao) {
        this.dao = dao;
    }

    /**
     * @see org.appfuse.service.UserManager#getUser(java.lang.String)
     */
    public User getUser(String userId) {
        return dao.get(new Long(userId));
    }

    /**
     * @see org.appfuse.service.UserManager#getUsers(org.appfuse.model.User)
     */
    public List getUsers(User user) {
        return dao.getUsers();
    }

    /**
     * @see org.appfuse.service.UserManager#saveUser(org.appfuse.model.User)
     */
    public User saveUser(User user) throws UserExistsException {
        // if new user, lowercase userId
        if (user.getVersion() == null) {
            user.setUsername(user.getUsername().toLowerCase());
        }
        
        try {
            return dao.saveUser(user);
        } catch (DataIntegrityViolationException e) {
            throw new UserExistsException("User '" + user.getUsername() + "' already exists!");
        } catch (EntityExistsException e) { // needed for JPA
            throw new UserExistsException("User '" + user.getUsername() + "' already exists!");
        }
    }

    /**
     * @see org.appfuse.service.UserManager#removeUser(java.lang.String)
     */
    public void removeUser(String userId) {
        log.debug("removing user: " + userId);
        dao.remove(new Long(userId));
    }

    public User getUserByUsername(String username) throws UsernameNotFoundException {
        return (User) dao.loadUserByUsername(username);
    }
}
