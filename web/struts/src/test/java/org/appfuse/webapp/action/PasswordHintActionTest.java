package org.appfuse.webapp.action;

import org.subethamail.wiser.Wiser;

public class PasswordHintActionTest extends BaseActionTestCase {
    private PasswordHintAction action;

    public void setPasswordHintAction(PasswordHintAction action) {
        this.action = action;
    }

    public void testExecute() throws Exception {
        // start SMTP Server
        Wiser wiser = new Wiser();
        wiser.setPort(getSmtpPort());
        wiser.start();
        
        action.setUsername("user");
        assertEquals("success", action.execute());
        assertFalse(action.hasActionErrors());

        // verify an account information e-mail was sent
        wiser.stop();
        assertTrue(wiser.getMessages().size() == 1);

        // verify that success messages are in the request
        assertNotNull(action.getSession().getAttribute("messages"));
    }
}
