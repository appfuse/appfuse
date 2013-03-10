package org.appfuse.webapp;

import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.request.Request;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.appfuse.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;

import java.util.ArrayList;
import java.util.List;

/**
 * org.appfuse.webapp.SSAuthenticatedWebSession
 *
 * Based on: https://cwiki.apache.org/WICKET/spring-security-and-wicket-auth-roles.html
 *
 * @author Marcin Zajączkowski, 2011-02-05
 */
public class SSAuthenticatedWebSession extends AuthenticatedWebSession {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @SpringBean(name = "authenticationManager")
    private AuthenticationManager authenticationManager;

    @SpringBean
    private SessionRegistry sessionRegistry;

    public SSAuthenticatedWebSession(Request request) {
        super(request);

        Injector.get().inject(this);
        if (authenticationManager == null) {
            throw new IllegalStateException("AdminSession requires an authenticationManager.");
        }
        
    }

    @Override
    public boolean authenticate(String username, String password) {
        boolean authenticated;
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            //A hack to allow to track logged users without using SessionManagementFilter which is problematic in Wicket
            sessionRegistry.registerNewSession(getId(), authentication.getPrincipal());
            authenticated = authentication.isAuthenticated();
        } catch (AuthenticationException e) {
            log.warn("User '{}' failed to login. Reason: {}", username, e.getMessage());
            authenticated = false;
        }
        return authenticated;
    }

    //FIXME: MZA: Modification of returning object - it would be better if roles were returned
    @Override
    public Roles getRoles() {
        Roles roles = new Roles();
        getRolesIfSignedIn(roles);
        return roles;
    }

    private void getRolesIfSignedIn(Roles roles) {
        if (isSignedIn()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            addRolesFromAuthentication(roles, authentication);
        }
    }

    private void addRolesFromAuthentication(Roles roles, Authentication authentication) {
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            roles.add(authority.getAuthority());
        }
    }

    public List<User> getActiveUsers() {
        //RequestLogger is an alternative, but it keeps only session, not related principals
        List<Object> allPrincipals = sessionRegistry.getAllPrincipals();
        List<User> users = new ArrayList<User>(allPrincipals.size());
        for (Object principal : allPrincipals) {
            users.add((User)principal);
        }
        return users;
    }
}
