package org.appfuse.webapp.action;

import org.apache.commons.beanutils.BeanUtils;
import org.appfuse.Constants;
import org.appfuse.webapp.form.UserFormEx;


public class UserActionTest extends BaseStrutsTestCase {

    public UserActionTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        getMockRequest().setUserRole("admin");
    }
    
    public void testCancel() throws Exception {
        setRequestPathInfo("/editUser");
        addRequestParameter("method", "Cancel");
        actionPerform();

        verifyForward("mainMenu");
        verifyNoActionErrors();
    }
    
    public void testEdit() throws Exception {
        // set fake requestURL so getRequestURL() doesn't fail in UserAction
        getMockRequest().setRequestURL("http://foo:8080/bar/editUser.html");
        setRequestPathInfo("/editUser");
        addRequestParameter("method", "Edit");
        addRequestParameter("username", "tomcat");
        actionPerform();

        verifyForward("edit");
        assertTrue(getRequest().getAttribute(Constants.USER_EDIT_KEY) != null);
        verifyNoActionErrors();
    }

    public void testSave() throws Exception {
        UserFormEx ex = new UserFormEx();
        BeanUtils.copyProperties(ex, user);
        ex.setPassword("tomcat");
        ex.setConfirmPassword(ex.getPassword());
        getRequest().setAttribute(Constants.USER_EDIT_KEY, ex);

        setRequestPathInfo("/saveUser");
        addRequestParameter("encryptPass", "true");
        addRequestParameter("method", "Save");
        addRequestParameter("from", "list");
        actionPerform();

        verifyForward("edit");
        assertTrue(getRequest().getAttribute(Constants.USER_EDIT_KEY) != null);
        verifyNoActionErrors();
    }

    public void testSearch() throws Exception {
        setRequestPathInfo("/editUser");
        addRequestParameter("method", "Search");
        actionPerform();

        verifyForward("list");
        assertTrue(getRequest().getAttribute(Constants.USER_LIST) != null);
        verifyNoActionErrors();
    }

    public void testRemove() throws Exception {
        setRequestPathInfo("/editUser");
        addRequestParameter("method", "Delete");
        addRequestParameter("username", "mraible");
        actionPerform();

        verifyForward("viewUsers");
        verifyNoActionErrors();
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(UserActionTest.class);
    }
}
