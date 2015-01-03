package org.appfuse.webapp.server.services;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.appfuse.model.User;
import org.appfuse.service.UserExistsException;
import org.springframework.security.access.prepost.PreAuthorize;

@Path("/usersService")
public interface UserRequestService {

    /**
     * 
     * @return
     */
    @GET
    @Path("currentUser")
    User getCurrentUser();

    /**
     * 
     * @return
     */
    @GET
    @Path("signUp")
    User signUp();

    /**
     * 
     * @param user
     * @return
     * @throws Exception
     */
    @POST
    @Path("signUp")
    User signUp(User user) throws Exception;

    /**
     * 
     * @return
     */
    @GET
    @Path("editProfile")
    @PreAuthorize("isAuthenticated()")
    User editProfile();

    /**
     * 
     * @param user
     * @return
     * @throws Exception
     */
    @POST
    @Path("editProfile")
    @PreAuthorize("isAuthenticated()")
    User editProfile(User user) throws Exception;

    /**
     * 
     * @param userId
     * @return
     */
    @GET
    @Path("user/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    User getUser(@PathParam("id") Long userId);

    /**
     * 
     * @param user
     * @return
     * @throws Exception
     */
    @POST
    @Path("saveUser")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    User saveUser(User user) throws Exception;

    /**
     * 
     * @param searchCriteria
     * @return
     */
    @POST
    @Path("countUsers")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    long countUsers(UsersSearchCriteria searchCriteria);

    /**
     * 
     * @param searchCriteria
     * @param firstResult
     * @param maxResults
     * @return
     */
    @POST
    @Path("searchUsers")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    List<User> searchUsers(UsersSearchCriteria searchCriteria, int firstResult, int maxResults);

    /**
     * 
     * @param searchCriteria
     * @param firstResult
     * @param maxResults
     * @param sortProperty
     * @param ascending
     * @return
     */
    @POST
    @Path("searchUsers")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    List<User> searchUsers(UsersSearchCriteria searchCriteria, int firstResult, int maxResults, String sortProperty, boolean ascending);

    /**
     * 
     * @param user
     */
    @DELETE
    @Path("removeUser/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    void removeUser(@PathParam("id") Long userId);

    /**
     * 
     * @param username
     * @return
     */
    @POST
    @Path("passwordHint")
    String sendPasswordHint(String username);

    /**
     * 
     * @param username
     * @return
     */
    @POST
    @Path("/requestRecoveryToken")
    String requestRecoveryToken(String username);

    @POST
    @Path("/updatePassword/{username}")
    User updatePassword(@PathParam("username") String username, String token, String currentPassword, String password) throws UserExistsException;

    /**
     * 
     * @return
     */
    @GET
    @Path("activeUsers")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    List<User> getActiveUsers();

    @GET
    @Path("/logout")
    boolean logout();

}