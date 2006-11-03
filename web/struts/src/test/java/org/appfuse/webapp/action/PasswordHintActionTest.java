package org.appfuse.webapp.action;

import org.subethamail.wiser.Wiser;

public class PasswordHintActionTest extends BaseActionTestCase {
    private PasswordHintAction action;

    protected void setUp() throws Exception {
        super.setUp();
        action = (PasswordHintAction) ctx.getBean("passwordHintAction");
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        action = null;
    }

    public void testExecute() throws Exception {
        // start SMTP Server
        Wiser wiser = new Wiser();
        wiser.setPort(2525);
        wiser.start();
        
        action.setUsername("tomcat");
        assertEquals(action.execute(), "success");
        assertFalse(action.hasActionErrors());

        // verify an account information e-mail was sent
        wiser.stop();
        assertTrue(wiser.getMessages().size() == 1);

        // verify that success messages are in the request
        assertNotNull(action.getSession().getAttribute("messages"));
    }
}
