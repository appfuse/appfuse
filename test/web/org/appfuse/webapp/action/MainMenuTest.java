package org.appfuse.webapp.action;

import java.util.ResourceBundle;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;


/**
 * HttpUnit test to verify links in the mainMenu.jsp
 */
public class MainMenuTest extends TestCase {
    //~ Instance fields ========================================================

    private Log log = LogFactory.getLog(MainMenuTest.class);
    private ResourceBundle messages = null;
    private ResourceBundle testParams = null;
    private WebConversation conversation = null;

    //~ Constructors ===========================================================

    /**
     * Constructor
     *
     * @param name name of test
     */
    public MainMenuTest(String name) {
        super(name);
    }

    //~ Methods ================================================================

    /**
     * main method
     *
     * @param args any arguments for this test
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    /**
     * declare test suite
     *
     * @return this testSuite
     */
    public static Test suite() {
        return new TestSuite(MainMenuTest.class);
    }

    /**
     * load resources and setup values
     *
     * @throws Exception an exception
     */
    protected void setUp() throws Exception {
        messages = ResourceBundle.getBundle("ApplicationResources");
        testParams = ResourceBundle.getBundle(this.getClass().getName());
        conversation = new WebConversation();
    }

    /**
     * Verifies that trying to access protected resource with http:// presents
     * a login page
     *
     * @throws Exception exception indicating errors
     */
    public void testSecuredResourceWithHTTP() throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("Retrieving URL: " + testParams.getString("unsecureUrl"));
        }

        WebRequest request =
            new GetMethodWebRequest(testParams.getString("unsecureUrl"));

        WebResponse response = conversation.getResponse(request);
        Document document = response.getDOM();
        NodeList nl = document.getElementsByTagName("title");
        assertEquals(messages.getString("webapp.prefix") +
                     messages.getString("login.title"),
                     nl.item(0).getFirstChild().getNodeValue());
    }

    /**
     * Verifies that trying to access protected resource with https:// presents
     * a login page
     *
     * @throws Exception exception indicating errors
     */

    /* This method requires extra configuration settings with httpunit -
       see http://www.httpunit.org/doc/sslfaq.html for more information.
       public void testSecuredResourceWithHTTPS() throws Exception {
           if (log.isDebugEnabled()) {
               log.debug("Retrieving URL: " + testParams.getString("secureUrl"));
           }
           WebRequest request =
               new GetMethodWebRequest(testParams.getString("secureUrl"));
           WebResponse response = conversation.getResponse(request);
           Document document = response.getDOM();
           NodeList nl = document.getElementsByTagName("title");
           assertEquals(messages.getString("login.title"),
                        nl.item(0).getFirstChild().getNodeValue());
       }
     */

    /**
     * Test the login form presented to the user
     *
     * @throws Exception exception indicating errors
     */
    public void testLoginForm() throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("Retrieving URL: " + testParams.getString("unsecureUrl"));
        }

        WebRequest request =
            new GetMethodWebRequest(testParams.getString("unsecureUrl"));

        WebResponse response = conversation.getResponse(request);
        WebForm form =
            response.getFormWithID(testParams.getString("loginFormId"));

        // obtain the desired form
        assertNotNull("No form found with ID 'loginForm'", form);

        // expected elements
        // 1. j_username
        // 2. j_password
        // 3. j_uri
        // 4. rememberMe
        // 5. submit 
        // 6. reset
        String[] elements = form.getParameterNames();
        assertEquals(6, elements.length);

        // verify that there's only one submit button
        assertEquals("Number of submit buttons", 1,
                     form.getSubmitButtons().length);
    }

    /**
     * Login and verify that links show up properly on the MainMenu
     *
     * @throws Exception exception indicating errors
     */
    public void testMainMenu() throws Exception {
        WebRequest request =
            new PostMethodWebRequest(testParams.getString("loginUrl"));
        WebResponse response = conversation.getResponse(request);
        WebForm form =
            response.getFormWithID(testParams.getString("loginFormId"));

        // (1) obtain the desired form
        request = form.getRequest();
        request.setParameter("j_username", testParams.getString("username"));
        request.setParameter("j_password", testParams.getString("password"));
        response = conversation.getResponse(request);

        Document document = response.getDOM();
        NodeList nl = document.getElementsByTagName("title");
        assertEquals(messages.getString("webapp.prefix") +
                     messages.getString("mainMenu.title"),
                     nl.item(0).getFirstChild().getNodeValue());

        /*
           NodeList links = document.getElementsByTagName("ul");
           log.debug("links.getLength()" + links.getLength());
           assertTrue(links.getLength() == 4);
         */
    }
}
