package org.appfuse.webapp.action;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.cactus.ServletTestCase;
import org.apache.cactus.WebRequest;
import org.apache.cactus.WebResponse;
import org.apache.cactus.client.authentication.FormAuthentication;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.Constants;


public class LoginServletTest extends ServletTestCase {
    //~ Static fields/initializers =============================================

    private static ResourceBundle rb = null;

    //~ Instance fields ========================================================

    private final Log log = LogFactory.getLog(LoginServletTest.class);
    private LoginServlet servlet = null;

    //~ Constructors ===========================================================

    public LoginServletTest(String name) {
        super(name);
        rb = ResourceBundle.getBundle(this.getClass().getName());
    }

    //~ Methods ================================================================

    public LoginServlet createInstance() throws Exception {
        return new LoginServlet();
    }

    protected void setUp() throws Exception {
        super.setUp();
        servlet = new LoginServlet();

        // set initialization parameters
        config.setInitParameter(Constants.AUTH_URL, rb.getString("authURL"));
        config.setInitParameter(Constants.HTTP_PORT, rb.getString("httpPort"));
        config.setInitParameter(Constants.HTTPS_PORT, rb.getString("httpsPort"));
        config.setInitParameter("isSecure", rb.getString("isSecure"));
        config.setInitParameter(Constants.ENC_ALGORITHM,
                                rb.getString("algorithm"));

        servlet.init(config);
    }

    protected void tearDown() throws Exception {
        servlet = null;
        super.tearDown();
    }

    /**
     * Test that initialization parameters were set
     *
     * @throws Exception
     */
    public void testInit() throws Exception {
        // check all parameters from web.xml
        String authURL = config.getInitParameter(Constants.AUTH_URL);
        log.debug("authURL: " + authURL);
        assertTrue((authURL != null) &&
                   authURL.equals(rb.getString("authURL")));

        Map config =
            (HashMap) servlet.getServletContext().getAttribute(Constants.CONFIG);
        assertEquals(config.get(Constants.HTTP_PORT),
                     rb.getString("httpPort"));
        assertEquals(config.get(Constants.HTTPS_PORT),
                     rb.getString("httpsPort"));
        assertEquals(config.get(Constants.SECURE_LOGIN),
                     Boolean.valueOf(rb.getString("isSecure")));
        assertEquals(config.get(Constants.ENC_ALGORITHM),
                     rb.getString("algorithm"));
    }

    public void beginFormAuthentication(WebRequest theRequest) {
        theRequest.setRedirectorName("ServletRedirectorSecure");
        theRequest.setAuthentication(new FormAuthentication(rb.getString("username"),
                                                            rb.getString("encryptedPassword")));
    }

    /**
     * Test logging in as user a user
     */
    public void testFormAuthentication() {
        assertEquals(rb.getString("username"),
                     request.getUserPrincipal().getName());
        assertEquals(rb.getString("username"), request.getRemoteUser());
        assertTrue("User not in '" + rb.getString("role") + "' role",
                   request.isUserInRole(rb.getString("role")));
    }

    public void beginExecute(WebRequest wRequest) {
        wRequest.addParameter("j_username", rb.getString("username"));
        wRequest.addParameter("j_password", rb.getString("password"));
    }

    public void testExecute() throws Exception {
        servlet.execute(request, response);
    }

    public void endExecute(WebResponse response) {
        // TODO: get header value, verify Http Status 302
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(LoginServletTest.class);
    }
}
