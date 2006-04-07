package org.appfuse.webapp.action;

import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.springframework.mock.web.MockHttpServletRequest;

import com.opensymphony.webwork.ServletActionContext;

public class UserActionTest extends BaseActionTestCase {
    private UserAction action;

    protected void setUp() throws Exception {    
        super.setUp();
        action = (UserAction) ctx.getBean("userAction");
    }
    
    protected void tearDown() throws Exception {
        super.tearDown();
        action = null;
    }
    
    public void testCancel() throws Exception {
        assertEquals(action.cancel(), "mainMenu");
        assertFalse(action.hasActionErrors());

        action.setFrom("list");
        assertEquals(action.cancel(), "cancel");
    }
    
    public void testEdit() throws Exception {
        // so request.getRequestURL() doesn't fail
        request = new MockHttpServletRequest("GET", "/editUser.html");
        ServletActionContext.setRequest(request);
        
        action.setUsername("tomcat");
        assertNull(action.getUser());
        assertEquals(action.edit(), "success");
        assertNotNull(action.getUser());
        assertFalse(action.hasActionErrors());
    }

    public void testSave() throws Exception {
        UserManager userManager = (UserManager) ctx.getBean("userManager");
        User user = userManager.getUserByUsername("tomcat");
        user.setPassword("tomcat");
        user.setConfirmPassword("tomcat");
        action.setUser(user);
        action.setFrom("list");
        
        request.addParameter("encryptPass", "true");
        ServletActionContext.setRequest(request);

        assertEquals(action.save(), "input");
        assertNotNull(action.getUser());
        assertFalse(action.hasActionErrors());
    }

    public void testSearch() throws Exception {
        assertNull(action.getUsers());
        assertEquals(action.list(), "success");
        assertNotNull(action.getUsers());
        assertFalse(action.hasActionErrors());
    }

    public void testRemove() throws Exception {
        User user = new User("mraible");
        user.setId(new Long(2));
        action.setUser(user);
        assertEquals(action.delete(), "success");
        assertFalse(action.hasActionErrors());
    }
}
