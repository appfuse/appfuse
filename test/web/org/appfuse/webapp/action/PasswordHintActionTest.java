package org.appfuse.webapp.action;

import com.dumbster.smtp.SimpleSmtpServer;


public class PasswordHintActionTest extends BaseStrutsTestCase {
    
    public PasswordHintActionTest(String name) {
        super(name);
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
