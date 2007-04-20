package org.appfuse.service;

import org.acegisecurity.*;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.acegisecurity.userdetails.UserDetails;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.Constants;
import org.appfuse.model.Role;
import org.appfuse.model.User;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class UserSecurityAdvice implements MethodBeforeAdvice, AfterReturningAdvice {
    public final static String ACCESS_DENIED = "Access Denied: Only administrators are allowed to modify other users.";
    protected final Log log = LogFactory.getLog(UserSecurityAdvice.class);

    /**
     * Method to enforce security and only allow administrators to modify users. Regular
     * users are allowed to modify themselves.
     */
    public void before(Method method, Object[] args, Object target) throws Throwable {
        SecurityContext ctx = SecurityContextHolder.getContext();

        if (ctx.getAuthentication() != null) {
            Authentication auth = ctx.getAuthentication();
            boolean administrator = false;
            GrantedAuthority[] roles = auth.getAuthorities();
            for (GrantedAuthority role1 : roles) {
                if (role1.getAuthority().equals(Constants.ADMIN_ROLE)) {
                    administrator = true;
                    break;
                }
            }

            User user = (User) args[0];
            String username = user.getUsername();

            String currentUser;
            if (auth.getPrincipal() instanceof UserDetails) {
                currentUser = ((UserDetails) auth.getPrincipal()).getUsername();
            } else {
                currentUser = String.valueOf(auth.getPrincipal());
            }

            if (username != null && !username.equals(currentUser)) {
                AuthenticationTrustResolver resolver = new AuthenticationTrustResolverImpl();
                // allow new users to signup - this is OK b/c Signup doesn't allow setting of roles
                boolean signupUser = resolver.isAnonymous(auth);
                if (!signupUser) {
                    if (log.isDebugEnabled()) {
                        log.debug("Verifying that '" + currentUser + "' can modify '" + username + "'");
                    }
                    if (!administrator) {
                        log.warn("Access Denied: '" + currentUser + "' tried to modify '" + username + "'!");
                        throw new AccessDeniedException(ACCESS_DENIED);
                    }
                } else {
                    if (log.isDebugEnabled()) {
                        log.debug("Registering new user '" + username + "'");
                    }
                }
            }

            // fix for http://issues.appfuse.org/browse/APF-96
            // don't allow users with "ROLE_USER" role to upgrade to "ROLE_ADMIN" role
            else if (username != null && username.equalsIgnoreCase(currentUser) && !administrator) {

                // get the list of roles the user is trying add
                Set<String> userRoles = new HashSet<String>();
                if (user.getRoles() != null) {
                    for (Object o : user.getRoles()) {
                        Role role = (Role) o;
                        userRoles.add(role.getName());
                    }
                }

                // get the list of roles the user currently has
                Set<String> authorizedRoles = new HashSet<String>();
                for (GrantedAuthority role1 : roles) {
                    authorizedRoles.add(role1.getAuthority());
                }

                // if they don't match - access denied
                // users aren't allowed to change their roles
                if (!CollectionUtils.isEqualCollection(userRoles, authorizedRoles)) {
                    log.warn("Access Denied: '" + currentUser + "' tried to change their role(s)!");
                    throw new AccessDeniedException(ACCESS_DENIED);
                }
            }
        }
    }

    public void afterReturning(Object returnValue, Method method, Object[] args, Object target)
    throws Throwable {
        User user = (User) args[0];

        if (user.getVersion() != null) {
            // reset the authentication object if current user
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getPrincipal() instanceof UserDetails) {
                User currentUser = (User) auth.getPrincipal();
                if (currentUser.getId().equals(user.getId())) {
                    auth = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        }
    }
}
