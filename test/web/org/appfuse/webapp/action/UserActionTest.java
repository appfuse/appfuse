package org.appfuse.webapp.action;

import org.apache.commons.beanutils.BeanUtils;
import org.appfuse.Constants;
import org.appfuse.webapp.form.UserFormEx;


public class UserActionTest extends BaseStrutsTestCase {
    //~ Constructors ===========================================================

    public UserActionTest(String name) {
        super(name);
    }

    //~ Methods ================================================================

    public void testCancel() throws Exception {
        setRequestPathInfo("/editUser");
        addRequestParameter("action", "Cancel");
        actionPerform();

        verifyForward("mainMenu");
        verifyNoActionErrors();
    }
    
    public void testEdit() throws Exception {
        setRequestPathInfo("/editUser");
        addRequestParameter("action", "Edit");
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
        request.setAttribute(Constants.USER_EDIT_KEY, ex);

        setRequestPathInfo("/saveUser");
        addRequestParameter("encryptPass", "true");
        addRequestParameter("action", "Save");
        addRequestParameter("from", "list");
        actionPerform();

        verifyForward("edit");
        assertTrue(getRequest().getAttribute(Constants.USER_EDIT_KEY) != null);
        verifyNoActionErrors();
    }

    public void testSearch() throws Exception {
        setRequestPathInfo("/editUser");
        addRequestParameter("action", "Search");
        actionPerform();

        verifyForward("list");
        assertTrue(getRequest().getAttribute(Constants.USER_LIST) != null);
        verifyNoActionErrors();
    }

    public void testRemove() throws Exception {
        setRequestPathInfo("/editUser");
        addRequestParameter("action", "Delete");
        addRequestParameter("username", "mraible");
        actionPerform();

        verifyForward("viewUsers");
        verifyNoActionErrors();
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(UserActionTest.class);
    }
}
