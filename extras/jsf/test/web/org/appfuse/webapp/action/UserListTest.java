package org.appfuse.webapp.action;

public class UserListTest extends BasePageTestCase {
    private UserList bean;

    protected void setUp() throws Exception {    
        super.setUp();
        bean = (UserList) getManagedBean("userList");
    }
    
    public void testSearch() throws Exception {
        assertTrue(bean.getUsers().size() >= 1);
        assertFalse(bean.hasErrors());
    }

}
