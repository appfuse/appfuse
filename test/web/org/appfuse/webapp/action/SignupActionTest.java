package org.appfuse.webapp.action;

import org.appfuse.Constants;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.context.support.WebApplicationContextUtils;

import servletunit.struts.CactusStrutsTestCase;

import com.dumbster.smtp.SimpleSmtpServer;

public class SignupActionTest extends CactusStrutsTestCase {
    
    public SignupActionTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        // change the port on the mailSender so it doesn't conflict with an 
        // existing SMTP server on localhost
        ApplicationContext ctx = WebApplicationContextUtils
            .getRequiredWebApplicationContext(getSession().getServletContext());
        JavaMailSenderImpl mailSender = (JavaMailSenderImpl) ctx.getBean("mailSender");
        mailSender.setPort(2525);
    }
    
    public void testExecute() throws Exception {
        setRequestPathInfo("/signup");

        SimpleSmtpServer server = SimpleSmtpServer.start(2525);
        
        addRequestParameter("username", "self-registered");
        addRequestParameter("password", "Password1");
        addRequestParameter("confirmPassword", "Password1");
        addRequestParameter("firstName", "First");
        addRequestParameter("lastName", "Last");
        addRequestParameter("addressForm.city", "Denver");
        addRequestParameter("addressForm.province", "Colorado");
        addRequestParameter("addressForm.country", "USA");
        addRequestParameter("addressForm.postalCode", "80210");
        addRequestParameter("email", "self-registered@raibledesigns.com");
        addRequestParameter("website", "http://raibledesigns.com");
        addRequestParameter("passwordHint", "Password is one with you.");
        actionPerform();

        // verify an account information e-mail was sent
        server.stop();
        assertTrue(server.getReceievedEmailSize() == 1);
        
        verifyForward("mainMenu");
        verifyNoActionErrors();

        // verify that success messages are in the request
        assertTrue(getSession().getAttribute(Constants.REGISTERED) != null);
    }
}
