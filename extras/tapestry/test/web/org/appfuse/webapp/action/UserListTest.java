package org.appfuse.webapp.action;

import org.appfuse.service.UserManager;

public class UserListTest extends BasePageTestCase {
    private UserList page;

    protected void setUp() throws Exception {
        super.setUp();
        page = (UserList) getPage(UserList.class);

        // these can be mocked if you want a more "pure" unit test
        page.setUserManager((UserManager) ctx.getBean("userManager"));
        page.setRequestCycle(getCycle(request, response));
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        page = null;
    }

    public void testSearch() throws Exception {
        assertTrue(page.getUserManager().getUsers(null).size() >= 1);
    }
}
