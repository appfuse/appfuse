package org.appfuse.service.impl;

import java.util.List;

import javax.jws.WebService;

import org.appfuse.dao.UserDao;
import org.appfuse.model.User;
import org.appfuse.service.UserExistsException;
import org.appfuse.service.UserManager;
import org.appfuse.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


/**
 * Implementation of UserManager interface.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
@Service("userManager")
@WebService(serviceName = "UserService", endpointInterface = "org.appfuse.service.UserService")
public class UserManagerImpl extends GenericManagerImpl<User, Long> implements UserManager, UserService {
    private PasswordEncoder passwordEncoder;
    private UserDao userDao;
    @Autowired(required = false)
    private SaltSource saltSource;

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.dao = userDao;
        this.userDao = userDao;
    }

    /**
     * {@inheritDoc}
     */
    public User getUser(String userId) {
    	User user = userDao.get(new Long(userId));
    	if(user != null) {
    		user.setConfirmPassword(user.getPassword());
    	}
        return user;
    }

    /**
     * {@inheritDoc}
     */
    public List<User> getUsers() {
        return userDao.getAllDistinct();
    }

    /**
     * {@inheritDoc}
     */
    public User saveUser(User user) throws UserExistsException {

        if (user.getVersion() == null) {
            // if new user, lowercase userId
            user.setUsername(user.getUsername().toLowerCase());
        }

        // Get and prepare password management-related artifacts
        boolean passwordChanged = false;
        if (passwordEncoder != null) {
            // Check whether we have to encrypt (or re-encrypt) the password
            if (user.getVersion() == null) {
                // New user, always encrypt
                passwordChanged = true;
            } else {
                // Existing user, check password in DB
                String currentPassword = userDao.getUserPassword(user.getId());
                if (currentPassword == null) {
                    passwordChanged = true;
                } else {
                    if (!currentPassword.equals(user.getPassword())) {
                        passwordChanged = true;
                    }
                }
            }

            // If password was changed (or new user), encrypt it
            if (passwordChanged) {
                if (saltSource == null) {
                    // backwards compatibility
                    user.setPassword(passwordEncoder.encodePassword(user.getPassword(), null));
                    log.warn("SaltSource not set, encrypting password w/o salt");
                } else {
                    user.setPassword(passwordEncoder.encodePassword(user.getPassword(),
                            saltSource.getSalt(user)));
                }
                //copy to confirmPassword so JSR-303 BeanValidators can compare these two fields
                user.setConfirmPassword(user.getPassword()); 
            }
        } else {
            log.warn("PasswordEncoder not set, skipping password encryption...");
        }

        try {
            return userDao.saveUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.getMessage());
            throw new UserExistsException("User '" + user.getUsername() + "' already exists!");
        }
    }

    /**
     * {@inheritDoc}
     */
    public void removeUser(User user) {
        log.debug("removing user: " + user);
        userDao.remove(user);
    }

    /**
     * {@inheritDoc}
     */
    public void removeUser(String userId) {
        log.debug("removing user: " + userId);
        userDao.remove(new Long(userId));
    }

    /**
     * {@inheritDoc}
     *
     * @param username the login name of the human
     * @return User the populated user object
     * @throws UsernameNotFoundException thrown when username not found
     */
    public User getUserByUsername(String username) throws UsernameNotFoundException {
    	User user = (User) userDao.loadUserByUsername(username);
    	if(user != null) {
    		user.setConfirmPassword(user.getPassword());
    	}
        return user;
    }

    /**
     * {@inheritDoc}
     */
    public List<User> search(String searchTerm) {
        return super.search(searchTerm, User.class);
    }
}
