package org.appfuse.webapp.action;

import org.springframework.mock.web.MockHttpServletRequest;

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
        assertEquals(action.execute(), "success");
        assertFalse(action.hasActionErrors());

        // verify that success messages are in the request
        assertNotNull(action.getSession().getAttribute("messages"));
    }
}
