package org.appfuse.webapp.action;

import org.springframework.mock.web.MockHttpServletRequest;

import com.dumbster.smtp.SimpleSmtpServer;
import com.opensymphony.webwork.ServletActionContext;


public class PasswordHintActionTest extends BaseActionTestCase {
    private PasswordHintAction action;

    protected void setUp() throws Exception {    
        super.setUp();
        action = (PasswordHintAction) ctx.getBean("passwordHintAction");
        ServletActionContext.setRequest(new MockHttpServletRequest());
    }
    
    protected void tearDown() throws Exception {
        super.tearDown();
        action = null;
    }

    public void testExecute() throws Exception {
        action.setUsername("tomcat");
        
        SimpleSmtpServer server = SimpleSmtpServer.start(2525);
        
        assertEquals(action.execute(), "success");
        assertFalse(action.hasActionErrors());

        // verify an account information e-mail was sent
        server.stop();
        assertTrue(server.getReceivedEmailSize() == 1);
        
        // verify that success messages are in the request
        assertNotNull(action.getSession().getAttribute("messages"));
    }
}
