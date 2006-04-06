package org.appfuse.dao;

import java.util.List;

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
     * Gets users information based on login name.
     * @param username the current username
     * @return user populated user object
     */
    public User getUser(String username);

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
     * @param username the user's username
     */
    public void removeUser(String username);
}
