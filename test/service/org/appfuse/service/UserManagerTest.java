package org.appfuse.service;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.Constants;
import org.appfuse.webapp.form.UserForm;
import org.appfuse.webapp.form.UserFormEx;


public class UserManagerTest extends BaseManagerTestCase {
    //~ Instance fields ========================================================

    private UserManager mgr = null;
    private Log log = LogFactory.getLog(UserManagerTest.class);
    private UserForm userForm;

    //~ Methods ================================================================

    protected void setUp() throws Exception {
        mgr = (UserManager) ctx.getBean("userManager");
    }

    protected void tearDown() throws Exception {
        mgr = null;
    }

    public void testGetUser() throws Exception {
        userForm = (UserForm) mgr.getUser("tomcat");

        if (log.isDebugEnabled()) {
            log.debug(userForm);
        }

        assertTrue(userForm != null);
        assertTrue(userForm.getRoles().size() == 1);
    }

    public void testSaveUser() throws Exception {
        userForm = (UserForm) mgr.getUser("tomcat");
        userForm.setPhoneNumber("303-555-1212");

        if (log.isDebugEnabled()) {
            log.debug("saving user with updated phone number: " + userForm);
        }

        userForm = (UserForm) mgr.saveUser(userForm);
        assertTrue(userForm.getPhoneNumber().equals("303-555-1212"));
        assertTrue(userForm.getRoles().size() == 1);
    }

    public void testAddAndRemoveUser() throws Exception {
        UserFormEx userFormEx = new UserFormEx();

        // call populate method in super class to populate test data
        // from a properties file matching this class name
        userFormEx = (UserFormEx) populate(userFormEx);

        String[] roles = { Constants.USER_ROLE };
        userFormEx.setUserRoles(roles);
        userForm = new UserForm();
        BeanUtils.copyProperties(userForm, userFormEx);

        userForm = (UserForm) mgr.saveUser(userForm);
        assertTrue(userForm.getUsername().equals("john"));
        assertTrue(userForm.getRoles().size() == 1);

        if (log.isDebugEnabled()) {
            log.debug("removing user...");
        }

        mgr.removeUser(userForm);

        try {
            userForm = (UserForm) mgr.getUser("john");
            fail("Expected 'Exception' not thrown");
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug(e);
            }

            assertTrue(e != null);
        }
    }

    public void testLoginWithCookie() throws Exception {
        String cookieString = mgr.createLoginCookie("tomcat");

        if (log.isDebugEnabled()) {
            log.debug("loginCookie: " + cookieString);
        }

        assertNotNull(cookieString);

        String newCookie = mgr.checkLoginCookie(cookieString);
        assertNotNull(newCookie);

        // 2nd time should fail because cookieId should change for this user
        newCookie = mgr.checkLoginCookie(cookieString);
        assertNull(newCookie);

        // Delete all user cookies for tomcat?  Nahhh, what's the point?
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(UserManagerTest.class);
    }
}
