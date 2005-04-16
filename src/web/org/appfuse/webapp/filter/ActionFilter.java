package org.appfuse.webapp.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.Constants;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.appfuse.webapp.util.RequestUtil;
import org.appfuse.webapp.util.SslUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * This class is used to filter all requests to the <code>Action</code>
 * servlet and detect if a user is authenticated.  If a user is authenticated,
 * but no user object exists, this class populates the <code>UserForm</code>
 * from the user store.
 *
 * <p><a href="ActionFilter.java.html"><i>View Source</i></a></p>
 *
 * @author  Matt Raible
 * @version $Revision: 1.13 $ $Date: 2005/04/16 22:17:21 $
 *
 * @web.filter display-name="Action Filter" name="actionFilter"
 *
 * <p>Change this value to true if you want to secure your entire application.
 * This can also be done in web-security.xml by setting <transport-guarantee>
 * to CONFIDENTIAL.</p>
 *
 * @web.filter-init-param name="isSecure" value="${secure.application}"
 */
public class ActionFilter implements Filter {
    private static Boolean secure = Boolean.FALSE;
    private final transient Log log = LogFactory.getLog(ActionFilter.class);
    private FilterConfig config = null;

    public void init(FilterConfig config) throws ServletException {
        this.config = config;

        /* This determines if the application uconn SSL or not */
        secure = Boolean.valueOf(config.getInitParameter("isSecure"));
    }

    /**
     * Destroys the filter.
     */
    public void destroy() {
        config = null;
    }

    public void doFilter(ServletRequest req, ServletResponse resp,
                         FilterChain chain)
    throws IOException, ServletException {
        // cast to the types I want to use
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpSession session = request.getSession(true);
        
        // notify the LocaleContextHolder what locale is being used so
        // service and data layer classes can get the locale
        LocaleContextHolder.setLocale(request.getLocale());

        // do pre filter work here
        // If using https, switch to http
        String redirectString =
            SslUtil.getRedirectString(request, config.getServletContext(),
                                      secure.booleanValue());

        if (redirectString != null) {
            if (log.isDebugEnabled()) {
                log.debug("protocol switch needed, redirecting to '" +
                          redirectString + "'");
            }

            // Redirect the page to the desired URL
            response.sendRedirect(response.encodeRedirectURL(redirectString));

            // ensure we don't chain to requested resource
            return;
        }

        User user = (User) session.getAttribute(Constants.USER_KEY);
        ServletContext context = config.getServletContext();
        String username = request.getRemoteUser();

        // user authenticated, empty user object
        if ((username != null) && (user == null)) {
            ApplicationContext ctx =
                WebApplicationContextUtils.getRequiredWebApplicationContext(context);

            UserManager mgr = (UserManager) ctx.getBean("userManager");
            user = mgr.getUser(username);
            session.setAttribute(Constants.USER_KEY, user);

            // if user wants to be remembered, create a remember me cookie
            if (session.getAttribute(Constants.LOGIN_COOKIE) != null) {
                session.removeAttribute(Constants.LOGIN_COOKIE);

                String loginCookie = mgr.createLoginCookie(username);
                RequestUtil.setCookie(response, Constants.LOGIN_COOKIE,
                                      loginCookie, request.getContextPath());
            }
        }

        chain.doFilter(request, response);
    }
}
