package org.appfuse.service;

import java.util.List;

import org.appfuse.dao.UserDao;
import org.appfuse.model.User;


/**
 * WebService Interface
 *
 * <p><a href="UserWebService.java.html"><i>View Source</i></a></p>
 *
 * @author <a href="mailto:mikagoeckel@codehaus.org">Mika Goeckel</a>
 * @aegis.mapping 
 */
public interface UserWebService {
    //~ Methods ================================================================

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
     * 
     * Tell xfire, which objects it will find in the List
     * @aegis.method
     * @aegis.method-return-type componentType="org.appfuse.model.User"
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
