package org.appfuse.webapp;

import org.apache.wicket.request.Request;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;

/**
 * org.appfuse.webapp.StaticAuthenticatedWebSession
 *
 * @author Marcin Zajączkowski, 2011-06-18
 */
public class StaticAuthenticatedWebSession extends AuthenticatedWebSession {

    public static final String USERNAME_USER = "user";
    public static final String PASSWORD_USER = "userpass";

    public StaticAuthenticatedWebSession(Request request) {
        super(request);
    }

    @Override
    public boolean authenticate(String username, String password) {
        return USERNAME_USER.equals(username) && PASSWORD_USER.equals(password);
    }

    @Override
    public Roles getRoles() {
        if (isSignedIn()) {
            return new Roles("ROLE_USER");
        } else {
            return new Roles();
        }
    }
}
