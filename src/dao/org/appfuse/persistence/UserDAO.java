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
 * @version $Revision: 1.2 $ $Date: 2004/05/04 06:08:55 $
 */
public interface UserDAO extends DAO {
    //~ Methods ================================================================    
    
    /**
     * Gets users information based on login name.
     * @param username the current username
     * @return user populated user object
     */
    public User getUser(String username) throws DAOException;

	/**
	 * Gets a list of users based on parameters passed in.
	 *
	 * @return List populated list of users
	 * @throws DAOException
	 */
	public List getUsers(User user) throws DAOException;
	
    /**
     * Saves a user's information
     * @param user the object to be saved
     * @param user the updated user object
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
     * @throws DAOException
     */
    public UserCookie getUserCookie(String cookieId) throws DAOException;
    
    /**
     * Saves a userCookie object to the database
     * @param cookie
     * @throws DAOException
     */
    public void saveUserCookie(UserCookie cookie) throws DAOException;
    
    /**
     * Removes all cookies for a specified username
     * @param username
     */
    public void removeUserCookies(String username);
}
