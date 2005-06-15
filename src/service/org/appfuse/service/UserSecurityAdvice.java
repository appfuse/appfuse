package org.appfuse.service;

import org.springframework.aop.MethodBeforeAdvice;
import org.appfuse.model.User;
import org.appfuse.Constants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Method;

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
            String username = null;
            boolean removeUser = false;
            if (args[0] instanceof User) {
                username = ((User) args[0]).getUsername(); // saveUser
            } else {
                username = (String) args[0]; // removeUser
                removeUser = true;
            }

            String currentUser = null;
            if (auth.getPrincipal() instanceof UserDetails) {
                currentUser = ((UserDetails) auth.getPrincipal()).getUsername();
            } else {
                currentUser = String.valueOf(auth.getPrincipal());
            }

            if (auth.isAuthenticated() && (!username.equals(currentUser))) {
                AuthenticationTrustResolver resolver = new AuthenticationTrustResolverImpl();
//              allow new users to signup - this is OK b/c Signup doesn't allow setting of roles
                boolean signupUser = resolver.isAnonymous(auth);
                if (!signupUser || removeUser) {
                    if (log.isDebugEnabled()) {
                        log.debug("Verifying that '" + currentUser + "' can modify '" + username + "'");
                    }
                    boolean allowedToModify = false;
                    GrantedAuthority[] roles = auth.getAuthorities();
                    for (int i=0; i < roles.length; i++) {
                        if (roles[i].getAuthority().equals(Constants.ADMIN_ROLE)) {
                            allowedToModify = true;
                            break;
                        }
                    }
                    if (!allowedToModify) {
                        log.warn("Access Denied: '" + currentUser + "' tried to modify '" + username + "'!");
                        throw new AccessDeniedException(ACCESS_DENIED);
                    }
                } else {
                    if (log.isDebugEnabled()) {
                        log.debug("Registering new user '" + username + "'");
                    }
                }
            }
        }
    }
}
