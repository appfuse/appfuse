package org.appfuse.webapp.action;

import java.util.HashMap;
import java.util.Map;

import org.appfuse.service.MailEngine;
import org.appfuse.service.UserManager;
import org.springframework.mail.MailMessage;

import com.dumbster.smtp.SimpleSmtpServer;


public class PasswordHintTest extends BasePageTestCase {
    private PasswordHint page;
    
    protected void onSetUp() throws Exception {
        super.onSetUp();        
        // these can be mocked if you want a more "pure" unit test
        Map map = new HashMap();
        map.put("userManager", applicationContext.getBean("userManager"));
        map.put("mailEngine", applicationContext.getBean("mailEngine"));
        map.put("mailMessage", applicationContext.getBean("mailMessage"));
        page = (PasswordHint) getPage(PasswordHint.class, map);
    }
    
    protected void onTearDown() throws Exception {
        super.onTearDown();
        page = null;
    }
    
    public void testExecute() throws Exception {
        SimpleSmtpServer server = SimpleSmtpServer.start(2525);
        page.execute("tomcat");
        
        assertFalse(page.hasErrors());

        // verify an account information e-mail was sent
        server.stop();
        assertTrue(server.getReceivedEmailSize() == 1);
        
        // verify that success messages are in the request
        assertNotNull(page.getSession().getAttribute("message"));
    }

}
