package org.appfuse.service;

import java.util.List;


/**
 * Business Delegate (Proxy) Interface to handle communication between web and
 * persistence layer.
 *
 * <p>
 * <a href="UserManager.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @version $Revision: 1.1 $ $Date: 2004/03/01 06:19:10 $
 */
public interface UserManager {
    //~ Methods ================================================================

    /**
     * Retrieves a user by name, and can create a new one
     *
     * @param username
     * @return User
     * @throws Exception
     */
    public Object getUser(String username) throws Exception;

    /**
     * Retrieves a list of users, filtering with parameters on a user object
     * @param user parameters to filter on
     * @return List
     * @throws Exception
     */
    public List getUsers(Object user) throws Exception;

    /**
     * Saves a user's information
     *
     * @param user the user's information
     * @return updated user information
     * @throws Exception
     */
    public Object saveUser(Object user) throws Exception;

    /**
     * Removes a user from the database by id
     *
     * @param user the user's information
     * @throws Exception
     */
    public void removeUser(Object user) throws Exception;
    
    /**
     * Validates a user based on a cookie value.  If successfull, it returns
     * a new cookie String.  If not, then it returns null.
     * 
     * @param value (in format username|guid)
     * @return indicator that this is a valid login
     * @throws Exception
     */
    public String checkLoginCookie(String value) throws Exception;
 
    /**
     * Creates a cookie string using a username - designed for use when
     * a user logs in and wants to be remembered.
     * @param username
     * @return String to put in a cookie for remembering user
     * @throws Exception
     */
    public String createLoginCookie(String username) throws Exception;
    
    /**
     * Deletes all cookies for user.
     * @param username
     */
    public void removeLoginCookies(String username);
}
