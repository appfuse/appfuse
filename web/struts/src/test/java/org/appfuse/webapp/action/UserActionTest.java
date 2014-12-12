package org.appfuse.webapp.action;

import org.apache.struts2.ServletActionContext;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.Assert.*;

public class UserActionTest extends BaseActionTestCase {
    @Autowired
    private UserAction action;
    @Autowired
    private UserManager userManager;

    @Test
    public void testCancel() throws Exception {
        assertEquals(action.cancel(), "home");
        assertFalse(action.hasActionErrors());

        action.setFrom("list");
        assertEquals("cancel", action.cancel());
    }

    @Test
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

    @Test
    public void testSave() throws Exception {
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

    @Test
    public void testSaveConflictingUser() throws Exception {
        User user = userManager.getUserByUsername("user");
        user.setPassword("user");
        user.setConfirmPassword("user");
        // e-mail address from existing user
        User existingUser = (User) userManager.getUserByUsername("admin");
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

    @Test
    public void testListUsers() throws Exception {
        assertNull(action.getUsers());
        assertEquals("success", action.list());
        assertNotNull(action.getUsers());
        assertFalse(action.hasActionErrors());
    }

    @Test
    public void testRemove() throws Exception {
        User user = new User("admin");
        user.setId(-2L);
        action.setUser(user);
        assertEquals("success", action.delete());
        assertFalse(action.hasActionErrors());
    }

    @Test
    public void testSearch() throws Exception {
        // regenerate search index
        userManager.reindex();

        action.setQ("admin");
        assertEquals("success", action.list());
        assertNotNull(action.getUsers());
        assertTrue(action.getUsers().size() >= 1);
        assertFalse(action.hasActionErrors());
    }
}
