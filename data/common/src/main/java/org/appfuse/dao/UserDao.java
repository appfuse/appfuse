package org.appfuse.dao;

import java.util.List;

import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.appfuse.model.User;

/**
 * User Data Access Object (Dao) interface.
 *
 * <p>
 * <a href="UserDao.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public interface UserDao extends Dao {
    /**
     * Gets users information based on user id.
     * @param userId the user's id
     * @return user populated user object
     */
    public User getUser(Long userId);

    /**
     * Gets users information based on login name.
     * @param username the user's username
     * @return userDetails populated userDetails object
     */
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
    
    /**
     * Gets a list of users based on parameters passed in.
     *
     * @return List populated list of users
     */
    public List getUsers(User user);

    /**
     * Saves a user's information
     * @param user the object to be saved
     */
    public void saveUser(User user);

    /**
     * Removes a user from the database by id
     * @param userId the user's id
     */
    public void removeUser(Long userId);
}
