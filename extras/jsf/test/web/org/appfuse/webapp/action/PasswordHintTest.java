package org.appfuse.webapp.action;

import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.dumbster.smtp.SimpleSmtpServer;


public class PasswordHintTest extends BasePageTestCase {
    private PasswordHint bean;
    
    public void setUp() throws Exception {
        super.setUp();        
        bean = (PasswordHint) getManagedBean("passwordHint");
        // change the port on the mailSender so it doesn't conflict with an 
        // existing SMTP server on localhost
        JavaMailSenderImpl mailSender = (JavaMailSenderImpl) ctx.getBean("mailSender");
        mailSender.setPort(2525);
        mailSender.setHost("localhost");
    }
    
    public void testExecute() throws Exception {
        bean.setUsername("tomcat");

        SimpleSmtpServer server = SimpleSmtpServer.start(2525);
        
        assertEquals(bean.execute(), "success");
        assertFalse(bean.hasErrors());

        // verify an account information e-mail was sent
        server.stop();
        assertTrue(server.getReceivedEmailSize() == 1);
        
        // verify that success messages are in the request
        assertNotNull(bean.getSession().getAttribute("messages"));
    }
}
