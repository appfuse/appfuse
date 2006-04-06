package org.appfuse.service.impl;

import java.util.List;

import org.appfuse.dao.UserDao;
import org.appfuse.model.User;
import org.appfuse.service.UserExistsException;
import org.appfuse.service.UserManager;
import org.springframework.dao.DataIntegrityViolationException;


/**
 * Implementation of UserManager interface.</p>
 * 
 * <p>
 * <a href="UserManagerImpl.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class UserManagerImpl extends BaseManager implements UserManager {
    private UserDao dao;

    /**
     * Set the Dao for communication with the data layer.
     * @param dao
     */
    public void setUserDao(UserDao dao) {
        this.dao = dao;
    }

    /**
     * @see org.appfuse.service.UserManager#getUser(java.lang.String)
     */
    public User getUser(String username) {
        return dao.getUser(username);
    }

    /**
     * @see org.appfuse.service.UserManager#getUsers(org.appfuse.model.User)
     */
    public List getUsers(User user) {
        return dao.getUsers(user);
    }

    /**
     * @see org.appfuse.service.UserManager#saveUser(org.appfuse.model.User)
     */
    public void saveUser(User user) throws UserExistsException {
    	// if new user, lowercase username
    	if (user.getVersion() == null) {
            user.setUsername(user.getUsername().toLowerCase());
    	}
        try {
            dao.saveUser(user);
        } catch (DataIntegrityViolationException e) {
            throw new UserExistsException("User '" + user.getUsername() + "' already exists!");
        }
    }

    /**
     * @see org.appfuse.service.UserManager#removeUser(java.lang.String)
     */
    public void removeUser(String username) {
        if (log.isDebugEnabled()) {
            log.debug("removing user: " + username);
        }

        dao.removeUser(username);
    }
}
