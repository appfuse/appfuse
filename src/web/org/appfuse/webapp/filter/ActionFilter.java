package org.appfuse.webapp.filter;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.jsp.jstl.core.Config;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.appfuse.Constants;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.appfuse.webapp.util.RequestUtil;
import org.appfuse.webapp.util.SslUtil;
import org.springframework.web.context.WebApplicationContext;


/**
 * This class is used to filter all requests to the <code>Action</code>
 * servlet and detect if a user is authenticated.  If a user is authenticated,
 * but no user object exists, this class populates the <code>UserForm</code>
 * from the user store.
 *
 * <p><a href="ActionFilter.java.html"><i>View Source</i></a></p>
 *
 * @author  Matt Raible
 * @version $Revision: 1.4 $ $Date: 2004/04/16 04:22:28 $
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
    private static Log log = LogFactory.getLog(ActionFilter.class);
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

        // keep JSTL and Struts Locale's in synch
        Locale locale = (Locale) session.getAttribute(Globals.LOCALE_KEY);
        if (locale == null) {
            locale = request.getLocale();
        }
        if (request.getParameter("locale") != null) {
            locale = new Locale(request.getParameter("locale"));
        }
        session.setAttribute(Globals.LOCALE_KEY, locale);
        Config.set(session, Config.FMT_LOCALE, locale);

        User user = (User) session.getAttribute(Constants.USER_KEY);
        ServletContext ctx = config.getServletContext();
        String username = request.getRemoteUser();

        try {
            // user authenticated, empty user object
            if ((username != null) && (user == null)) {
                WebApplicationContext context =
                    (WebApplicationContext) ctx.getAttribute(
                            WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
                UserManager mgr = (UserManager) context.getBean("userManager");
                user = (User) mgr.getUser(username);
                session.setAttribute(Constants.USER_KEY, user);

                // if user wants to be remembered, create a remember me cookie
                if (session.getAttribute(Constants.LOGIN_COOKIE) != null) {
                    session.removeAttribute(Constants.LOGIN_COOKIE);
                
                    String loginCookie = mgr.createLoginCookie(username);
                    RequestUtil.setCookie(response, Constants.LOGIN_COOKIE,
                                          loginCookie, request.getContextPath());
                } 

                // check to see if the user has just registered
                if (session.getAttribute(Constants.REGISTERED) != null) {
                    session.removeAttribute(Constants.REGISTERED);
                    request.setAttribute(Globals.MESSAGE_KEY,
                                         session.getAttribute(Globals.MESSAGE_KEY));
                    session.removeAttribute(Globals.MESSAGE_KEY);
                }
            }
        } catch (Exception e) {
            log.error("Error getting user's information " + e);
            e.printStackTrace();

            ActionErrors errors = new ActionErrors();
            errors.add(ActionMessages.GLOBAL_MESSAGE,
                       new ActionMessage("errors.general"));

            StringBuffer sb = new StringBuffer();

            if (e.getCause() == null) {
                sb.append(e.getMessage());
            } else {
                while (e.getCause() != null) {
                    sb.append(e.getMessage());
                    sb.append("\n");
                    e = (Exception) e.getCause();
                }
            }

            errors.add(ActionMessages.GLOBAL_MESSAGE,
                       new ActionMessage("errors.detail", sb.toString()));

            request.setAttribute(Globals.ERROR_KEY, errors);

            RequestDispatcher dispatcher =
                request.getRequestDispatcher("/error.jsp");
            dispatcher.forward(request, response);

            return;
        }

        chain.doFilter(request, response);
    }
}
