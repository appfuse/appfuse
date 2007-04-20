package org.appfuse.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.Constants;
import org.appfuse.model.User;

public class UserManagerTest extends BaseManagerTestCase {
    //~ Instance fields ========================================================

    private UserManager mgr = null;
    private RoleManager roleManager = null;
    private Log log = LogFactory.getLog(UserManagerTest.class);
    private User user;
    
    public void setUserManager(UserManager userManager) {
        this.mgr = userManager;
    }
    
    public void setRoleManager(RoleManager roleManager) {
        this.roleManager = roleManager;
    }

    public void testGetUser() throws Exception {
        user = mgr.getUserByUsername("user");
        assertNotNull(user);
        
        log.debug(user);
        assertEquals(1, user.getRoles().size());
    }

    public void testSaveUser() throws Exception {
        user = mgr.getUserByUsername("user");
        user.setPhoneNumber("303-555-1212");

        log.debug("saving user with updated phone number: " + user);

        user = mgr.saveUser(user);
        assertEquals("303-555-1212", user.getPhoneNumber());
        assertEquals(1, user.getRoles().size());
    }

    public void testAddAndRemoveUser() throws Exception {
        user = new User();

        // call populate method in super class to populate test data
        // from a properties file matching this class name
        user = (User) populate(user);

        user.addRole(roleManager.getRole(Constants.USER_ROLE));

        user = mgr.saveUser(user);
        assertEquals("john", user.getUsername());
        assertEquals(1, user.getRoles().size());

        log.debug("removing user...");

        mgr.removeUser(user.getId().toString());

        try {
            user = mgr.getUserByUsername("john");
            fail("Expected 'Exception' not thrown");
        } catch (Exception e) {
            log.debug(e);
            assertNotNull(e);
        }
    }
}
