package org.appfuse.webapp.action;

import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class UserFormTest extends BasePageTestCase {
    private UserForm bean;
    @Autowired
    private UserManager userManager;

    @Override
    @Before
    public void onSetUp() {
        super.onSetUp();
        bean = new UserForm();
        bean.setUserManager(userManager);
        assertNotNull(bean);
    }

    @Override
    @After
    public void onTearDown() {
        super.onTearDown();
        bean = null;
    }

    @Test
    public void testEdit() throws Exception {
        bean.setId("-1");
        assertEquals("editProfile", bean.edit());
        assertNotNull(bean.getUser().getUsername());
        assertFalse(bean.hasErrors());
    }

    @Test
    public void testSave() throws Exception {
        User user = userManager.getUser("-1");
        user.setPassword("user");
        user.setConfirmPassword("user");
        bean.setUser(user);

        assertEquals("home", bean.save());
        assertNotNull(bean.getUser());
        assertFalse(bean.hasErrors());
    }

    @Test
    public void testRemove() throws Exception {
        User user2Delete = new User();
        user2Delete.setId(-2L);
        bean.setUser(user2Delete);
        assertEquals("list", bean.delete());
        assertFalse(bean.hasErrors());
    }
}
