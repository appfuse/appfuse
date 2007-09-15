package org.appfuse.webapp.action;

import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.springframework.mock.web.MockHttpServletRequest;

import org.apache.struts2.ServletActionContext;

public class UserActionTest extends BaseActionTestCase {
    private UserAction action;

    public void setUserAction(UserAction action) {
        this.action = action;
    }
    
    public void testCancel() throws Exception {
        assertEquals(action.cancel(), "mainMenu");
        assertFalse(action.hasActionErrors());

        action.setFrom("list");
        assertEquals("cancel", action.cancel());
    }
    
    public void testEdit() throws Exception {
        // so request.getRequestURL() doesn't fail
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/editUser.html");
        ServletActionContext.setRequest(request);
        
        action.setId("-1"); // regular user
        assertNull(action.getUser());
        assertEquals("success", action.edit());
        assertNotNull(action.getUser());
        assertFalse(action.hasActionErrors());
    }

    public void testSave() throws Exception {
        UserManager userManager = (UserManager) applicationContext.getBean("userManager");
        User user = userManager.getUserByUsername("user");
        user.setPassword("user");
        user.setConfirmPassword("user");
        action.setUser(user);
        action.setFrom("list");
        
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("encryptPass", "true");
        ServletActionContext.setRequest(request);

        assertEquals("input", action.save());
        assertNotNull(action.getUser());
        assertFalse(action.hasActionErrors());
    }
    
    public void testSaveConflictingUser() throws Exception {
        UserManager userManager = (UserManager) applicationContext.getBean("userManager");
        User user = userManager.getUserByUsername("user");
        user.setPassword("user");
        user.setConfirmPassword("user");
        // e-mail address from existing user
        User existingUser = (User) userManager.getUsers(null).get(0);
        user.setEmail(existingUser.getEmail());
        action.setUser(user);
        action.setFrom("list");
        
        Integer originalVersionNumber = user.getVersion();
        log.debug("original version #: " + originalVersionNumber);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("encryptPass", "true");
        ServletActionContext.setRequest(request);

        assertEquals("input", action.save());
        assertNotNull(action.getUser());
        assertEquals(originalVersionNumber, user.getVersion());
        assertTrue(action.hasActionErrors());
    }

    public void testSearch() throws Exception {
        assertNull(action.getUsers());
        assertEquals("success", action.list());
        assertNotNull(action.getUsers());
        assertFalse(action.hasActionErrors());
    }

    public void testRemove() throws Exception {
        User user = new User("admin");
        user.setId(-2L);
        action.setUser(user);
        assertEquals("success", action.delete());
        assertFalse(action.hasActionErrors());
    }
}
