package org.appfuse.service;

import org.appfuse.Constants;
import org.appfuse.model.User;


public class UserManagerTest extends BaseManagerTestCase {
    //~ Instance fields ========================================================

    private UserManager mgr = null;
    private User user;

    //~ Methods ================================================================

    protected void setUp() throws Exception {
        mgr = (UserManager) ctx.getBean("userManager");
    }

    protected void tearDown() {
        mgr = null;
    }

    public void testGetUser() throws Exception {
        user = mgr.getUser("tomcat");

        if (log.isDebugEnabled()) {
            log.debug(user);
        }

        assertTrue(user != null);
        assertTrue(user.getRoles().size() == 1);
    }

    public void testSaveUser() throws Exception {
        user = (User) mgr.getUser("tomcat");
        user.setPhoneNumber("303-555-1212");

        if (log.isDebugEnabled()) {
            log.debug("saving user with updated phone number: " + user);
        }

        user = mgr.saveUser(user);
        assertTrue(user.getPhoneNumber().equals("303-555-1212"));
        assertTrue(user.getRoles().size() == 1);
    }

    public void testAddAndRemoveUser() throws Exception {
        user = new User();

        // call populate method in super class to populate test data
        // from a properties file matching this class name
        user = (User) populate(user);

        user.addRole(Constants.USER_ROLE);

        user = mgr.saveUser(user);
        assertTrue(user.getUsername().equals("john"));
        assertTrue(user.getRoles().size() == 1);

        if (log.isDebugEnabled()) {
            log.debug("removing user...");
        }

        mgr.removeUser(user.getUsername());

        try {
            user = mgr.getUser("john");
            fail("Expected 'Exception' not thrown");
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug(e);
            }

            assertTrue(e != null);
        }
    }

    public void testLoginWithCookie() {
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
