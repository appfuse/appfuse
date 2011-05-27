package org.appfuse.webapp.pages;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebPage;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.wicketstuff.annotation.mount.MountPath;

import javax.servlet.http.Cookie;

/**
 * Page for loging out.
 *
 * @author Marcin Zajączkowski, 2011-02-06
 */
@MountPath(path = "logout")
@AuthorizeInstantiation({"ROLE_ADMIN", "ROLE_USER"})
public class Logout extends WebPage {

    @Override
    protected void onInitialize() {
        super.onInitialize();

        getSession().invalidate();

        removeRememberMeCookie();

        setRedirect(true);
        throw new RestartResponseException(Login.class);
    }

    private void removeRememberMeCookie() {
        getWebRequestCycle().getWebResponse().clearCookie(
                new Cookie(TokenBasedRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY, null));
    }
}
