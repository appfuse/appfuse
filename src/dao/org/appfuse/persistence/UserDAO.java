package org.appfuse.persistence;

import java.util.List;

import org.appfuse.model.User;
import org.appfuse.model.UserCookie;

/**
 * User Data Access Object (DAO) interface.
 * 
 * <p>
 * <a href="UserDAO.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @version $Revision: 1.5 $ $Date: 2004/05/25 06:27:18 $
 */
public interface UserDAO extends DAO {
    //~ Methods ================================================================    
    
    /**
     * Gets users information based on login name.
     * @param username the current username
     * @return user populated user object
     * @throws DAOException if no user is found
     */
    public User getUser(String username) throws DAOException;

	/**
	 * Gets a list of users based on parameters passed in.
	 *
	 * @return List populated list of users
	 */
	public List getUsers(User user);
	
    /**
     * Saves a user's information
     * @param user the object to be saved
     * @return User the updated user object
     * @throws DAOException is save fails
     */
    public User saveUser(User user) throws DAOException;

    /**
     * Removes a user from the database by id
     * @param username the user's username
     */
    public void removeUser(String username) throws DAOException;
    
    /**
     * Gets a userCookie object from the database, based on cookieId
     * @param cookieId
     */
    public UserCookie getUserCookie(String cookieId);
    
    /**
     * Saves a userCookie object to the database
     * @param cookie
     */
    public void saveUserCookie(UserCookie cookie);
    
    /**
     * Removes all cookies for a specified username
     * @param username
     */
    public void removeUserCookies(String username);
}
