package org.appfuse.webapp.action;

import org.apache.commons.beanutils.BeanUtils;
import org.appfuse.Constants;
import org.appfuse.webapp.form.UserForm;


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
        // set requestURI so getRequestURI() doesn't fail in UserAction
        getMockRequest().setRequestURI("/editUser.html");
        setRequestPathInfo("/editUser");
        addRequestParameter("method", "Edit");
        addRequestParameter("username", "tomcat");
        actionPerform();

        verifyForward("edit");
        assertTrue(getRequest().getAttribute(Constants.USER_KEY) != null);
        verifyNoActionErrors();
    }

    public void testSave() throws Exception {
        UserForm userForm = new UserForm();
        BeanUtils.copyProperties(userForm, user);
        userForm.setPassword("tomcat");
        userForm.setConfirmPassword(userForm.getPassword());
        getRequest().setAttribute(Constants.USER_KEY, userForm);

        setRequestPathInfo("/saveUser");
        addRequestParameter("encryptPass", "true");
        addRequestParameter("method", "Save");
        addRequestParameter("from", "list");
        actionPerform();

        verifyForward("edit");
        assertTrue(getRequest().getAttribute(Constants.USER_KEY) != null);
        verifyNoActionErrors();
    }

    public void testSearch() throws Exception {
        setRequestPathInfo("/users");
        addRequestParameter("method", "Search");
        actionPerform();

        verifyForward("list");
        assertTrue(getRequest().getAttribute(Constants.USER_LIST) != null);
        verifyNoActionErrors();
    }

    public void testRemove() throws Exception {
        setRequestPathInfo("/editUser");
        addRequestParameter("method", "Delete");
        addRequestParameter("id", "2");
        actionPerform();

        verifyForward("viewUsers");
        verifyNoActionErrors();
    }
}
