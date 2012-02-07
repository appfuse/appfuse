package org.appfuse.webapp.services;

import org.appfuse.model.User;
import org.springframework.security.core.userdetails.UserDetails;


public interface SecurityContext {
    /**
     * Return true if user is authenticated, false otherwise
     */
    boolean isLoggedIn();
    UserDetails getUserDetails();

    /**
     * Logged in user
     * @return
     */
    User getUser();

    String getUsername();

    /**
     * Check if user has a role
     * @param roleName
     * @return
     */
    boolean hasRoles(String roleName);

    /**
     * Verify if user has admin role
     * @return
     */
    boolean isAdmin();

    /**
     * Authenticates user
     * @param user
     */
    void login(User user);

    /**
     * logs user out
     */
    void logout();

    /**
     *
     * @return
     */
    boolean isRememberMe();
}
