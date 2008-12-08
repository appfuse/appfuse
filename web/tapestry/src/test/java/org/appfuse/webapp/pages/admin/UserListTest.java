package org.appfuse.webapp.pages.admin;

import java.util.HashMap;
import java.util.Map;

import org.appfuse.webapp.pages.BasePageTester;
import org.testng.Assert;

public class UserListTest extends BasePageTester {
    private UserList page;
           /*
    @Override
    protected void onSetUpBeforeTransaction() throws Exception {
        super.onSetUpBeforeTransaction();        
        // these can be mocked if you want a more "pure" unit test
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userManager", applicationContext.getBean("userManager"));
        page = (UserList) getPage(UserList.class, map);
    }

    @Override
    protected void onTearDownAfterTransaction() throws Exception {
        super.onTearDownAfterTransaction();
        page = null;
    }
    
    public void testEdit() throws Exception {
        RequestCycle cycle = new MockRequestCycle();
        cycle.setListenerParameters(new Object[] {-1L}); // regular user
        page.edit(cycle);
        assertFalse(page.hasErrors());
    }

    public void testSearch() throws Exception {
        assertTrue(page.getUserManager().getUsers(null).size() >= 1);
    } */
}
