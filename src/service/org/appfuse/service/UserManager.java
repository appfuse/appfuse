package org.appfuse.service;

import java.util.List;

import org.appfuse.model.User;


/**
 * Business Delegate (Proxy) Interface to handle communication between web and
 * persistence layer.
 *
 * <p>
 * <a href="UserManager.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @version $Revision: 1.6 $ $Date: 2004/05/25 06:27:20 $
 */
public interface UserManager {
    //~ Methods ================================================================

    /**
     * Retrieves a user by username.  An exception is thrown if now user 
     * is found.
     *
     * @param username
     * @return User
     * @throws ServiceException
     */
    public User getUser(String username) throws ServiceException;

    /**
     * Retrieves a list of users, filtering with parameters on a user object
     * @param user parameters to filter on
     * @param user
     * @return List
     * @throws ServiceException
     */
    public List getUsers(Object user);

    /**
     * Saves a user's information
     *
     * @param user the user's information
     * @return updated user information
     * @throws ServiceException
     */
    public User saveUser(Object user) throws ServiceException;

    /**
     * Removes a user from the database by their username
     *
     * @param user the user's username
     * @throws ServiceException
     */
    public void removeUser(String username) throws ServiceException;
    
    /**
     * Validates a user based on a cookie value.  If successful, it returns
     * a new cookie String.  If not, then it returns null.
     * 
     * @param value (in format username|guid)
     * @return indicator that this is a valid login (null == invalid)
     */
    public String checkLoginCookie(String value);
 
    /**
     * Creates a cookie string using a username - designed for use when
     * a user logs in and wants to be remembered.
     * 
     * @param username
     * @return String to put in a cookie for remembering user
     */
    public String createLoginCookie(String username);
    
    /**
     * Deletes all cookies for user.
     * @param username
     */
    public void removeLoginCookies(String username);
}
