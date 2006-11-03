package org.appfuse.webapp.action;

import org.subethamail.wiser.Wiser;

public class PasswordHintTest extends BasePageTestCase {
    private PasswordHint bean;

    public void setUp() throws Exception {
        super.setUp();
        bean = (PasswordHint) getManagedBean("passwordHint");
    }

    public void testExecute() throws Exception {
       // start SMTP Server
        Wiser wiser = new Wiser();
        wiser.setPort(2525);
        wiser.start();

        bean.setUsername("tomcat");
        assertEquals(bean.execute(), "success");
        assertFalse(bean.hasErrors());

        // verify an account information e-mail was sent
        wiser.stop();
        assertTrue(wiser.getMessages().size() == 1);
        
        // verify that success messages are in the request
        assertNotNull(bean.getSession().getAttribute("messages"));
    }
}
