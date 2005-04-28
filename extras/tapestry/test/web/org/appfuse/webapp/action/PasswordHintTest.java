package org.appfuse.webapp.action;

import java.util.ResourceBundle;

import org.appfuse.service.MailEngine;
import org.appfuse.service.UserManager;
import org.springframework.mail.SimpleMailMessage;

import com.dumbster.smtp.SimpleSmtpServer;


public class PasswordHintTest extends BasePageTestCase {
    private PasswordHint page;
    
    public void setUp() throws Exception {
        super.setUp();        
        page = (PasswordHint) getPage(PasswordHint.class);
        
        // these can be mocked if you want a more "pure" unit test
        page.setUserManager((UserManager) ctx.getBean("userManager"));
        page.setMailEngine((MailEngine) ctx.getBean("mailEngine"));
        page.setMailMessage((SimpleMailMessage) ctx.getBean("mailMessage"));
        
        // unfortunately this is a required step if you're calling getMessage
        page.setBundle(ResourceBundle.getBundle(MESSAGES));
    }
    
    public void testExecute() throws Exception {
        request.addParameter("username", "tomcat");
        page.setRequestCycle(getCycle(request, response));
        SimpleSmtpServer server = SimpleSmtpServer.start(2525);
        
        page.execute(page.getRequestCycle());
        
        assertFalse(page.hasErrors());

        // verify an account information e-mail was sent
        server.stop();
        assertTrue(server.getReceivedEmailSize() == 1);
        
        // verify that success messages are in the request
        assertNotNull(page.getSession().getAttribute("message"));
    }
}
