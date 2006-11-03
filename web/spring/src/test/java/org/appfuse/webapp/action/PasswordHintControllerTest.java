package org.appfuse.webapp.action;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import org.subethamail.wiser.Wiser;

public class PasswordHintControllerTest extends BaseControllerTestCase {
    private PasswordHintController c;

    protected void setUp() throws Exception {
        super.setUp(); // needed to initialize a user
        c = (PasswordHintController) ctx.getBean("passwordHintController");
    }
    
    protected void tearDown() throws Exception {
        super.tearDown();
        c = null;
    }

    public void testExecute() throws Exception {
        MockHttpServletRequest request = newGet("/passwordHint.html");
        request.addParameter("username", "tomcat");

       // start SMTP Server
        Wiser wiser = new Wiser();
        wiser.setPort(2525);
        wiser.start();
        
        c.handleRequest(request, new MockHttpServletResponse());
        
        // verify an account information e-mail was sent
        wiser.stop();
        assertTrue(wiser.getMessages().size() == 1);
        
        // verify that success messages are in the session
        assertNotNull(request.getSession().getAttribute("messages"));
    }
}
