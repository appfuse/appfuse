package org.appfuse.webapp.action;

import org.appfuse.Constants;
import servletunit.struts.CactusStrutsTestCase;


public class SignupActionTest extends CactusStrutsTestCase {
    //~ Constructors ===========================================================

    public SignupActionTest(String name) {
        super(name);
    }

    //~ Methods ================================================================

    public void testExecute() throws Exception {
        setRequestPathInfo("/signup");

        addRequestParameter("username", "self-registered");
        addRequestParameter("password", "Password1");
        addRequestParameter("confirmPassword", "Password1");
        addRequestParameter("firstName", "First");
        addRequestParameter("lastName", "Last");
        addRequestParameter("city", "Denver");
        addRequestParameter("province", "Colorado");
        addRequestParameter("country", "USA");
        addRequestParameter("postalCode", "80210");
        addRequestParameter("email", "self-registered@raibledesigns.com");
        addRequestParameter("website", "http://raibledesigns.com");
        addRequestParameter("passwordHint", "Password is one with you.");
        actionPerform();

        verifyForward("mainMenu");
        verifyNoActionErrors();

        // verify that success messages are in the request
        assertTrue(getSession().getAttribute(Constants.REGISTERED) != null);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(SignupActionTest.class);
    }
}
