package org.appfuse.webapp.action;

import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.dumbster.smtp.SimpleSmtpServer;


public class PasswordHintActionTest extends BaseStrutsTestCase {
    
    public PasswordHintActionTest(String name) {
        super(name);
    }
    
    public void setUp() throws Exception {
        super.setUp();
        // change the port on the mailSender so it doesn't conflict with an 
        // existing SMTP server on localhost
        JavaMailSenderImpl mailSender = (JavaMailSenderImpl) ctx.getBean("mailSender");
        mailSender.setPort(2525);
        mailSender.setHost("localhost");
    }

    public void testExecute() throws Exception {
        setRequestPathInfo("/passwordHint");
        addRequestParameter("username", "tomcat");

        SimpleSmtpServer server = SimpleSmtpServer.start(2525);
        
        actionPerform();

        // verify an account information e-mail was sent
        server.stop();
        assertTrue(server.getReceivedEmailSize() == 1);
        
        verifyForward("previousPage");
        verifyNoActionErrors();
    }
}
