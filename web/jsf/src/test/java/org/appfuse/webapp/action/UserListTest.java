package org.appfuse.webapp.action;

import org.appfuse.service.UserManager;
import org.compass.gps.CompassGps;
import org.springframework.beans.factory.annotation.Autowired;

public class UserListTest extends BasePageTestCase {
    private UserList bean;
    @Autowired
    private UserManager userManager;
    @Autowired
    private CompassGps compassGps;

    @Override
    protected void onSetUp() throws Exception {
        super.onSetUp();
        bean = new UserList();
        bean.setUserManager(userManager);
    }

    public void testListUsers() throws Exception {
        assertTrue(bean.getUsers().size() >= 1);
        assertFalse(bean.hasErrors());
    }

    public void testSearch() throws Exception {
        compassGps.index();
        bean.setQuery("admin");
        assertEquals("success", bean.search());
        assertTrue(bean.getUsers().size() == 1);
    }
}
