package org.appfuse.webapp.action;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;


public class PasswordHintControllerTest extends BaseControllerTestCase {
    private PasswordHintController c;

    protected void setUp() throws Exception {
        // needed to initialize a user
        super.setUp();
        c = (PasswordHintController) ctx.getBean("passwordHintController");
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        c = null;
    }

    public void testExecute() throws Exception {
        MockHttpServletRequest request = newGet("/passwordHint.html");
        request.addParameter("username", "tomcat");

        c.handleRequest(request, new MockHttpServletResponse());
        
        // verify that success messages are in the session
        assertNotNull(request.getSession().getAttribute("messages"));
    }
}
