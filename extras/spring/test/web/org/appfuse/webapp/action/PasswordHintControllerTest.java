package org.appfuse.webapp.action;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.dumbster.smtp.SimpleSmtpServer;


public class PasswordHintControllerTest extends BaseControllerTestCase {
    private PasswordHintController c;

    protected void setUp() throws Exception {
        // needed to initialize a user
        super.setUp();
        c = (PasswordHintController) ctx.getBean("passwordHintController");
        // change the port on the mailSender so it doesn't conflict with an 
        // existing SMTP server on localhost
        JavaMailSenderImpl mailSender = (JavaMailSenderImpl) ctx.getBean("mailSender");
        mailSender.setPort(2525);
        mailSender.setHost("localhost");
    }
    
    protected void tearDown() throws Exception {
        super.tearDown();
        c = null;
    }

    public void testExecute() throws Exception {
        MockHttpServletRequest request = newGet("/passwordHint.html");
        request.addParameter("username", "tomcat");

        SimpleSmtpServer server = SimpleSmtpServer.start(2525);
        
        c.handleRequest(request, new MockHttpServletResponse());
        
        // verify an account information e-mail was sent
        server.stop();
        assertTrue(server.getReceivedEmailSize() == 1);
        
        // verify that success messages are in the session
        assertNotNull(request.getSession().getAttribute("messages"));
    }
}
