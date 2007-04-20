package org.appfuse.webapp.action;

import org.appfuse.model.User;
import org.appfuse.service.UserManager;

public class UserFormTest extends BasePageTestCase {
    private UserForm bean;
    private UserManager userManager;

    protected void setUp() throws Exception {
        super.setUp();
        userManager = (UserManager) applicationContext.getBean("userManager");
        bean = (UserForm) getManagedBean("userForm");
        assertNotNull(bean);
    }
    
    protected void tearDown() throws Exception {
        super.tearDown();
        bean = null;
    }
    
    public void testEdit() throws Exception {
        bean.setId("1");
        assertEquals(bean.edit(), "editProfile");
        assertNotNull(bean.getUser().getUsername());
        assertFalse(bean.hasErrors());
    }

    public void testSave() throws Exception {
        User user = userManager.getUser("1");
        user.setPassword("user");
        user.setConfirmPassword("user");
        bean.setUser(user);

        assertEquals(bean.save(), "mainMenu");
        assertNotNull(bean.getUser());
        assertFalse(bean.hasErrors());
    }
    
    public void testRemove() throws Exception {
        User user2Delete = new User();
        user2Delete.setId(2L);
        bean.setUser(user2Delete);
        assertEquals(bean.delete(), "list");
        assertFalse(bean.hasErrors());
    }
}
