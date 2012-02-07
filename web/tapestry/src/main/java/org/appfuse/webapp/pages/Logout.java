package org.appfuse.webapp.pages;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Cookies;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Session;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

/**
 * Logout Page
 * 
 * @author Serge Eby
 * @version $Id: Logout.java 5 2008-08-30 09:59:21Z serge.eby $
 *
 */
public class Logout {

    @Inject
    private Request request;

    @Inject
    private Cookies cookies;

    Object onActivate() {

        // Clear session
        Session session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // Remove RememberMe cookie
        cookies.removeCookieValue(TokenBasedRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY);

        return Home.class;
    }
}
