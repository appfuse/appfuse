package org.appfuse.webapp.action;

import org.subethamail.wiser.Wiser;

public class PasswordHintTest extends BasePageTestCase {
    private PasswordHint bean;
    
    public void setUp() throws Exception {
        super.setUp();        
        bean = (PasswordHint) getManagedBean("passwordHint");
    }
    
    public void testExecute() throws Exception {
        bean.setUsername("tomcat");

        Wiser wiser = new Wiser();
        wiser.setPort(2500);
        wiser.start();
        
        assertEquals(bean.execute(), "success");
        assertFalse(bean.hasErrors());

        // verify an account information e-mail was sent
        assertTrue(wiser.getMessages().size() == 1);
        wiser.stop();
        
        // verify that success messages are in the request
        assertNotNull(bean.getSession().getAttribute("messages"));
    }
}
