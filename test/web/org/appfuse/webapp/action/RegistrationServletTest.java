package org.appfuse.webapp.action;

import org.apache.cactus.ServletTestCase;
import org.apache.cactus.WebRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.appfuse.Constants;


public class RegistrationServletTest extends ServletTestCase {
    //~ Instance fields ========================================================

    private Log log = LogFactory.getLog(RegistrationServletTest.class);

    RegistrationServlet servlet = null;

    //~ Constructors ===========================================================

    public RegistrationServletTest(String name) {
        super(name);
    }

    //~ Methods ================================================================
    protected void setUp() throws Exception {
        super.setUp();
        servlet = new RegistrationServlet();
        servlet.init(config);
    }

    protected void tearDown() throws Exception {
        servlet = null;
        super.tearDown();
    }

    public void beginAddUser(WebRequest wRequest) {
        wRequest.addParameter("username", "self-registered");
        wRequest.addParameter("password", "Password1");
        wRequest.addParameter("confirmPassword", "Password1");
        wRequest.addParameter("firstName", "First");
        wRequest.addParameter("lastName", "Last");
        wRequest.addParameter("city", "Denver");
        wRequest.addParameter("province", "Colorado");
        wRequest.addParameter("country", "USA");
        wRequest.addParameter("postalCode", "80210");
        wRequest.addParameter("email", "self-registered@raibledesigns.com");
        wRequest.addParameter("website", "http://raibledesigns.com");
        wRequest.addParameter("passwordHint", "Password is one with you.");
    }

    /**
     * This method tests adding a new user
     * @throws Exception
     */
    public void testAddUser() throws Exception {
        servlet.execute(request, response);

        // no errors
        assertTrue(request.getAttribute(Globals.ERROR_KEY) == null);

        // verify that success messages are in the request
        assertTrue(request.getSession().getAttribute(Constants.REGISTERED) != null);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(RegistrationServletTest.class);
    }
}
