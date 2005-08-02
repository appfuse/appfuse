package org.appfuse.service;

import org.springframework.aop.MethodBeforeAdvice;
import org.appfuse.model.User;
import org.appfuse.model.Role;
import org.appfuse.Constants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.collections.CollectionUtils;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

import net.sf.acegisecurity.context.security.SecureContext;
import net.sf.acegisecurity.context.ContextHolder;
import net.sf.acegisecurity.*;

public class UserSecurityAdvice implements MethodBeforeAdvice {
    public final static String ACCESS_DENIED = "Access Denied: Only administrators are allowed to modify other users.";
    protected final Log log = LogFactory.getLog(UserSecurityAdvice.class);

    public void before(Method method, Object[] args, Object target) throws Throwable {
        SecureContext ctx = (SecureContext) ContextHolder.getContext();

        if (ctx != null) {
            Authentication auth = ctx.getAuthentication();
            boolean administrator = false;
            GrantedAuthority[] roles = auth.getAuthorities();
            for (int i=0; i < roles.length; i++) {
                if (roles[i].getAuthority().equals(Constants.ADMIN_ROLE)) {
                    administrator = true;
                    break;
                }
            }

            User user = (User) args[0];
            String username = user.getUsername();

            String currentUser = null;
            if (auth.getPrincipal() instanceof UserDetails) {
                currentUser = ((UserDetails) auth.getPrincipal()).getUsername();
            } else {
                currentUser = String.valueOf(auth.getPrincipal());
            }

            if (!username.equals(currentUser)) {
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
            // don't allow users with "user" role to upgrade to "admin" role
            else if (username.equalsIgnoreCase(currentUser) && !administrator) {

                // get the list of roles the user is trying add
                Set userRoles = new HashSet();
                if (user.getRoles() != null) {
                    for (Iterator it = user.getRoles().iterator(); it.hasNext();) {
                        Role role = (Role) it.next();
                        userRoles.add(role.getName());
                    }
                }

                // get the list of roles the user currently has
                Set authorizedRoles = new HashSet();
                for (int i=0; i < roles.length; i++) {
                    authorizedRoles.add(roles[i].getAuthority());
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
}
