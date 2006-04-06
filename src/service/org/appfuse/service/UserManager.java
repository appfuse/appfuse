package org.appfuse.service;

import java.util.List;

import org.appfuse.dao.UserDao;
import org.appfuse.model.User;


/**
 * Business Service Interface to handle communication between web and
 * persistence layer.
 *
 * <p><a href="UserManager.java.html"><i>View Source</i></a></p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 *  Modified by <a href="mailto:dan@getrolling.com">Dan Kibler </a> 
 */
public interface UserManager {
    //~ Methods ================================================================

    public void setUserDao(UserDao dao);
    
    /**
     * Retrieves a user by username.  An exception is thrown if now user 
     * is found.
     *
     * @param username
     * @return User
     */
    public User getUser(String username);

    /**
     * Retrieves a list of users, filtering with parameters on a user object
     * @param user parameters to filter on
     * @return List
     */
    public List getUsers(User user);

    /**
     * Saves a user's information
     *
     * @param user the user's information
     * @throws UserExistsException
     */
    public void saveUser(User user) throws UserExistsException;

    /**
     * Removes a user from the database by their username
     *
     * @param username the user's username
     */
    public void removeUser(String username);
}
