package org.appfuse.service;

import java.util.ArrayList;
import java.util.List;

import org.appfuse.Constants;
import org.appfuse.dao.RoleDAO;
import org.appfuse.dao.UserDAO;
import org.appfuse.model.Role;
import org.appfuse.model.User;
import org.appfuse.model.UserCookie;
import org.appfuse.service.impl.RoleManagerImpl;
import org.appfuse.service.impl.UserManagerImpl;
import org.jmock.Mock;
import org.springframework.dao.DataIntegrityViolationException;


public class UserManagerTest extends BaseManagerTestCase {
    //~ Instance fields ========================================================

    private UserManager userManager = new UserManagerImpl();
    private RoleManager roleManager = new RoleManagerImpl();
    private Mock userDAO = null;
    private Mock roleDAO = null;
    private User user = null;
    private Role role = null;

    //~ Methods ================================================================

    protected void setUp() throws Exception {
        super.setUp();
        userDAO = new Mock(UserDAO.class);
        userManager.setUserDAO((UserDAO) userDAO.proxy());
        roleDAO = new Mock(RoleDAO.class);
        roleManager.setRoleDAO((RoleDAO) roleDAO.proxy());
    }
    
    public void testGetUser() throws Exception {
        User testData = new User();
        testData.setUsername("tomcat");
        testData.getRoles().add(new Role("user"));
        // set expected behavior on dao
        userDAO.expects(once()).method("getUser")
               .with(eq("tomcat")).will(returnValue(testData));
        
        user = userManager.getUser("tomcat");
        assertTrue(user != null);
        assertTrue(user.getRoles().size() == 1);
        userDAO.verify();
    }

    public void testSaveUser() throws Exception {
        User testData = new User();
        testData.setUsername("tomcat");
        testData.getRoles().add(new Role("user"));
        // set expected behavior on dao
        userDAO.expects(once()).method("getUser")
               .with(eq("tomcat")).will(returnValue(testData));
        
        user = userManager.getUser("tomcat");
        user.setPhoneNumber("303-555-1212");
        userDAO.verify();
        
        // reset expectations
        userDAO.reset();
        userDAO.expects(once()).method("saveUser").with(same(user));
        
        userManager.saveUser(user);
        assertTrue(user.getPhoneNumber().equals("303-555-1212"));
        assertTrue(user.getRoles().size() == 1);
        userDAO.verify();
    }

    public void testAddAndRemoveUser() throws Exception {
        user = new User();

        // call populate method in super class to populate test data
        // from a properties file matching this class name
        user = (User) populate(user);
        
        // set expected behavior on role dao
        roleDAO.expects(once()).method("getRole")
               .with(eq("tomcat")).will(returnValue(new Role("tomcat")));
        
        role = roleManager.getRole(Constants.USER_ROLE);
        roleDAO.verify();
        user.addRole(role);

        // set expected behavior on user dao
        userDAO.expects(once()).method("saveUser").with(same(user));
        
        userManager.saveUser(user);
        assertTrue(user.getUsername().equals("john"));
        assertTrue(user.getRoles().size() == 1);
        userDAO.verify();
        
        // reset expectations
        userDAO.reset();
        
        userDAO.expects(once()).method("removeUser").with(eq(user.getUsername()));
        userManager.removeUser(user.getUsername());
        userDAO.verify();

        // reset expectations
        userDAO.reset();
        userDAO.expects(once()).method("getUser").will(returnValue(null));
        user = userManager.getUser("john");
        assertNull(user);
        userDAO.verify();
    }

    public void testLoginWithCookie() {
        // set expectations
        userDAO.expects(once()).method("saveUserCookie");
        
        String cookieString = userManager.createLoginCookie("tomcat");

        assertNotNull(cookieString);
        userDAO.verify();
        
        // reset expectations
        userDAO.expects(once()).method("getUserCookie").will(returnValue(new UserCookie()));
        // lookup succeeds, save will be called to generate a new one
        userDAO.expects(once()).method("saveUserCookie");
        String newCookie = userManager.checkLoginCookie(cookieString);
        assertNotNull(newCookie);
        userDAO.verify();
        
        // reset expectations
        userDAO.expects(once()).method("getUserCookie").will(returnValue(null));        
        newCookie = userManager.checkLoginCookie(cookieString);
        assertNull(newCookie);
        userDAO.verify();
    }
    
    public void testUserExistsException() {
        // set expectations
        user = new User();
        user.setUsername("admin");
        user.setEmail("matt@raibledesigns.com");
        List users = new ArrayList();
        
        users.add(user);
        Exception ex = new DataIntegrityViolationException("");
        userDAO.expects(once()).method("saveUser").with(same(user))
               .will(throwException(ex));
        
        // run test
        try {
            userManager.saveUser(user);
            fail("Expected UserExistsException not thrown");
        } catch (UserExistsException e) {
            log.debug("expected exception: " + e.getMessage());
            assertNotNull(e);
        }
    }
}
