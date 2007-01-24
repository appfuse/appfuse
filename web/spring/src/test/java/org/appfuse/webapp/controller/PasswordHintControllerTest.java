package org.appfuse.webapp.controller;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.subethamail.wiser.Wiser;

public class PasswordHintControllerTest extends BaseControllerTestCase {
    private PasswordHintController c = null;
    
    public void setPasswordHintController(PasswordHintController password) {
        this.c = password;
    }

    public void testExecute() throws Exception {
        MockHttpServletRequest request = newGet("/passwordhint.html");
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
