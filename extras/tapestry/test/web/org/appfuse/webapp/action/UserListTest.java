package org.appfuse.webapp.action;

import org.apache.tapestry.event.PageEvent;
import org.appfuse.service.UserManager;
import org.appfuse.webapp.action.MockRequestCycle;
import org.springframework.mock.web.MockHttpServletRequest;

public class UserListTest extends BasePageTestCase {
    private UserList page;

    protected void setUp() throws Exception {
        super.setUp();
        page = (UserList) getPage(UserList.class);

        // this manager can be mocked if you want a more "pure" unit test
        page.setUserManager((UserManager) ctx.getBean("userManager"));
        
        // default request cycle
        page.setRequestCycle(getCycle(request, response));
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        page = null;
    }
    
    public void testEdit() throws Exception {
        MockRequestCycle cycle = (MockRequestCycle) page.getRequestCycle();
        cycle.addServiceParameter("tomcat");

        page.edit(cycle);

        // TODO: Figure out how to verify the next page has been activated
        // and the user object is not null
        assertFalse(page.hasErrors());
    }

    public void testSearch() throws Exception {
        assertTrue(page.getUserManager().getUsers(null).size() >= 1);
    }
}
