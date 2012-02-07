package org.appfuse.webapp.services.impl;

import org.appfuse.Constants;
import org.appfuse.model.Role;
import org.appfuse.model.User;
import org.appfuse.webapp.services.SecurityContext;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Security context implementation based on Spring Security
 *
 * @author Serge Eby
 */
public class SpringSecurityContext implements SecurityContext {

    private final static Pattern COMMA_PATTERN = Pattern.compile("\\s*,\\s*");

    public boolean isLoggedIn() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() != null) {
            if ("anonymousUser".equals(authentication.getName())) {
                return false;
            }
            return authentication.isAuthenticated();
        }
        return false;
    }

    public UserDetails getUserDetails() {

        UserDetails userDetails = null;
        if (isLoggedIn()) {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetails) {
                userDetails = (UserDetails) principal;
            }
        }
        return userDetails;
    }


    public User getUser() {
        User user = null;
        if (isLoggedIn()) {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetails) {
                user = (User) principal;
            }
        }
        return user;
    }

    public String getUsername() {
        User user = getUser();
        return user != null ? user.getUsername() : null;
    }

    public boolean hasRoles(String roleName) {
        // If no role defined, return true
        if (roleName == null) {
            return true;
        }

        User user = getUser();
        //TODO: User InternalUtils class??
        List<String> allowedRoles = Arrays.asList(COMMA_PATTERN.split(roleName.trim()));
        if (user != null) {
            for (Role role : user.getRoles()) {
                if (allowedRoles.contains(role.getName())) {
                    return true;
                }
            }
        }
        return false;
    }


    public boolean isAdmin() {
        return hasRoles(Constants.ADMIN_ROLE);
    }

    public void logout() {
        // NYI
    }

    public boolean isRememberMe() {
        AuthenticationTrustResolver resolver = new AuthenticationTrustResolverImpl();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return resolver.isRememberMe(authentication);
    }

    public void login(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        UsernamePasswordAuthenticationToken loggedIn = new UsernamePasswordAuthenticationToken(
                user,
                user.getConfirmPassword(),
                user.getAuthorities());

        loggedIn.setDetails(user);
        SecurityContextHolder.getContext().setAuthentication(loggedIn);

    }

}
