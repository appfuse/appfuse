package org.appfuse.webapp.action;

import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.appfuse.Constants;
import org.appfuse.util.StringUtil;
import org.appfuse.webapp.util.SslUtil;


/**
 * Implementation of <strong>HttpServlet</strong> that is used
 * to get a username and password and encrypt the password
 * before sending to container-managed authentication.
 *
 * <p><a href="LoginServlet.java.html"><i>View Source</i></a></p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @version $Revision: 1.1 $ $Date: 2004/03/01 06:19:14 $
 *
 * @web.servlet
 *     display-name="Login Servlet"
 *     load-on-startup="1"
 *     name="login"
 *
 * @web.servlet-init-param
 *     name="authURL"
 *     value="${form.auth.action}"
 *
 * Change the following value to false if you don't require SSL for login
 *
 * @web.servlet-init-param
 *     name="isSecure"
 *     value="${secure.login}"
 *
 * If you're not using Tomcat, change encrypt-password to true
 *
 * @web.servlet-init-param
 *     name="encrypt-password"
 *     value="${encrypt.password}"
 *
 * @web.servlet-init-param
 *     name="algorithm"
 *     value="${encrypt.algorithm}"
 *
 * @web.servlet-mapping
 *     url-pattern="/authorize/*"
 */
public final class LoginServlet extends HttpServlet {
    private static String authURL = "j_security_check";
    private static String httpsPort = null;
    private static String httpPort = null;
    private static Boolean secure = Boolean.FALSE;
    private static String algorithm = "SHA";
    private static Boolean encrypt = Boolean.FALSE;
    private Log log = LogFactory.getLog(LoginServlet.class);

    /**
     * Initializes the port numbers based on the port init parameters as defined
     * in web.xml
     */
    private static void initializeSchemePorts(ServletContext servletContext) {
        if (httpPort == null) {
            String portNumber =
                servletContext.getInitParameter(SslUtil.HTTP_PORT_PARAM);
            httpPort = ((portNumber == null) ? SslUtil.STD_HTTP_PORT : portNumber);
        }

        if (httpsPort == null) {
            String portNumber =
                servletContext.getInitParameter(SslUtil.HTTPS_PORT_PARAM);
            httpsPort = ((portNumber == null) ? SslUtil.STD_HTTPS_PORT
                                              : portNumber);
        }
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Validates the Init and Context parameters, configures authentication URL
     *
     * @throws ServletException if the init parameters are invalid or any
     * other problems occur during initialisation
     */
    public void init() throws ServletException {
        // Get the container authentication URL for FORM-based Authentication
        // J2EE spec says should be j_security_check
        authURL = getInitParameter(Constants.AUTH_URL);

        // Get the encryption algorithm to use for encrypting passwords before
        // storing in database
        algorithm = getInitParameter(Constants.ENC_ALGORITHM);

        /* This determines if the login uses SSL or not */
        secure = Boolean.valueOf(getInitParameter("isSecure"));

        /* This determines if the password should be encrypted programmatically */
        encrypt = Boolean.valueOf(getInitParameter("encrypt-password"));

        if (log.isDebugEnabled()) {
            log.debug("Authentication URL: " + authURL);
            log.debug("Use SSL for login? " + secure);
            log.debug("Programmatic encryption of password? " + encrypt);
            log.debug("Encryption algorithm: " + algorithm);
        }

        ServletContext ctx = getServletContext();
        initializeSchemePorts(ctx);

        if (log.isDebugEnabled()) {
            log.debug("HTTP Port: " + httpPort);
            log.debug("HTTPS Port: " + httpsPort);
        }

        // Orion starts Servlets before Listeners, so check if the config
        // object already exists
        Map config = (HashMap) ctx.getAttribute(Constants.CONFIG);

        if (config == null) {
            config = new HashMap();
        }

        // update the config object with the init-params from this servlet
        config.put(Constants.HTTP_PORT, httpPort);
        config.put(Constants.HTTPS_PORT, httpsPort);
        config.put(Constants.SECURE_LOGIN, secure);
        config.put(Constants.ENC_ALGORITHM, algorithm);
        config.put(Constants.ENCRYPT_PASSWORD, encrypt);
        ctx.setAttribute(Constants.CONFIG, config);
    }

    /**
     * Route the user to the execute method
     *
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {
        execute(request, response);
    }

    /**
     * Route the user to the execute method
     *
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {
        execute(request, response);
    }

    /**
     * Process the specified HTTP request, and create the corresponding HTTP
     * response (or forward to another web component that will create it).
     *
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {
        // if user is already authenticated, it means they probably bookmarked
        // or typed in the URL to login.jsp directly, route them to the main
        // menu is this is the case
        if (request.getRemoteUser() != null) {
            if (log.isDebugEnabled()) {
                log.debug("User '" + request.getRemoteUser() +
                          "' already logged in, routing to mainMenu");
            }

            response.sendRedirect(request.getContextPath() + "/mainMenu.do");

            return;
        }

        String redirectString =
            SslUtil.getRedirectString(request, getServletContext(),
                                      secure.booleanValue());

        if (redirectString != null) {
            // Redirect the page to the desired URL
            response.sendRedirect(response.encodeRedirectURL(redirectString));

            if (log.isDebugEnabled()) {
                log.debug("switching protocols, redirecting user");
            }
        }

        // Extract attributes we will need
        String username = request.getParameter("j_username");
        String password = request.getParameter("j_password");

        if (request.getParameter("rememberMe") != null) {
            request.getSession().setAttribute(Constants.LOGIN_COOKIE, "true");
        }

        String encryptedPassword = "";

        if (encrypt.booleanValue() &&
                (request.getAttribute("encrypt") == null)) {
            if (log.isDebugEnabled()) {
                log.debug("Encrypting password for user '" + username + "'");
            }

            encryptedPassword = StringUtil.encodePassword(password, algorithm);
        } else {
            encryptedPassword = password;
        }

        if (redirectString == null) {
            // signifies already correct protocol
            if (log.isDebugEnabled()) {
                log.debug("Authenticating user '" + username + "'");
            }

            String req =
                request.getContextPath() + "/" + authURL + "?j_username=" +
                username + "&j_password=" + encryptedPassword + "&j_uri=" +
                request.getParameter("j_uri");

            response.sendRedirect(response.encodeRedirectURL(req));
        }
    }
}
