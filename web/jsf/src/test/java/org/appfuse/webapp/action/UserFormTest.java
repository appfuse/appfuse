package org.appfuse.webapp.action;

import org.appfuse.model.User;
import org.appfuse.service.UserManager;

public class UserFormTest extends BasePageTestCase {
    private UserForm bean;
    private UserManager userManager;

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    @Override
    protected void onSetUp() throws Exception {
        super.onSetUp();
        bean = new UserForm();
        bean.setUserManager(userManager);
        assertNotNull(bean);
    }

    @Override
    protected void onTearDown() throws Exception {
        super.onTearDown();
        bean = null;
    }
    
    public void testEdit() throws Exception {
        bean.setId("-1");
        assertEquals("editProfile", bean.edit());
        assertNotNull(bean.getUser().getUsername());
        assertFalse(bean.hasErrors());
    }

    public void testSave() throws Exception {
        User user = userManager.getUser("-1");
        user.setPassword("user");
        user.setConfirmPassword("user");
        bean.setUser(user);

        assertEquals("mainMenu", bean.save());
        assertNotNull(bean.getUser());
        assertFalse(bean.hasErrors());
    }
    
    public void testRemove() throws Exception {
        User user2Delete = new User();
        user2Delete.setId(-2L);
        bean.setUser(user2Delete);
        assertEquals("list", bean.delete());
        assertFalse(bean.hasErrors());
    }
}
