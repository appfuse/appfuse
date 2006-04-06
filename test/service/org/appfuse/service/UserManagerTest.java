package org.appfuse.service;

import java.util.ArrayList;
import java.util.List;

import org.appfuse.Constants;
import org.appfuse.dao.RoleDao;
import org.appfuse.dao.UserDao;
import org.appfuse.model.Role;
import org.appfuse.model.User;
import org.appfuse.service.impl.RoleManagerImpl;
import org.appfuse.service.impl.UserManagerImpl;
import org.jmock.Mock;
import org.springframework.dao.DataIntegrityViolationException;


public class UserManagerTest extends BaseManagerTestCase {
    //~ Instance fields ========================================================

    private UserManager userManager = new UserManagerImpl();
    private RoleManager roleManager = new RoleManagerImpl();
    private Mock userDao = null;
    private Mock roleDao = null;
    private User user = null;
    private Role role = null;

    //~ Methods ================================================================

    protected void setUp() throws Exception {
        super.setUp();
        userDao = new Mock(UserDao.class);
        userManager.setUserDao((UserDao) userDao.proxy());
        roleDao = new Mock(RoleDao.class);
        roleManager.setRoleDao((RoleDao) roleDao.proxy());
    }
    
    public void testGetUser() throws Exception {
        User testData = new User("tomcat");
        testData.getRoles().add(new Role("user"));
        // set expected behavior on dao
        userDao.expects(once()).method("getUser")
               .with(eq("tomcat")).will(returnValue(testData));
        
        user = userManager.getUser("tomcat");
        assertTrue(user != null);
        assertTrue(user.getRoles().size() == 1);
        userDao.verify();
    }

    public void testSaveUser() throws Exception {
        User testData = new User("tomcat");
        testData.getRoles().add(new Role("user"));
        // set expected behavior on dao
        userDao.expects(once()).method("getUser")
               .with(eq("tomcat")).will(returnValue(testData));
        
        user = userManager.getUser("tomcat");
        user.setPhoneNumber("303-555-1212");
        userDao.verify();
        
        // reset expectations
        userDao.reset();
        userDao.expects(once()).method("saveUser").with(same(user));
        
        userManager.saveUser(user);
        assertTrue(user.getPhoneNumber().equals("303-555-1212"));
        assertTrue(user.getRoles().size() == 1);
        userDao.verify();
    }

    public void testAddAndRemoveUser() throws Exception {
        user = new User();

        // call populate method in super class to populate test data
        // from a properties file matching this class name
        user = (User) populate(user);
        
        // set expected behavior on role dao
        roleDao.expects(once()).method("getRole")
               .with(eq("user")).will(returnValue(new Role("user")));
        
        role = roleManager.getRole(Constants.USER_ROLE);
        roleDao.verify();
        user.addRole(role);

        // set expected behavior on user dao
        userDao.expects(once()).method("saveUser").with(same(user));
        
        userManager.saveUser(user);
        assertTrue(user.getUsername().equals("john"));
        assertTrue(user.getRoles().size() == 1);
        userDao.verify();
        
        // reset expectations
        userDao.reset();
        
        userDao.expects(once()).method("removeUser").with(eq(user.getUsername()));
        userManager.removeUser(user.getUsername());
        userDao.verify();

        // reset expectations
        userDao.reset();
        userDao.expects(once()).method("getUser").will(returnValue(null));
        user = userManager.getUser("john");
        assertNull(user);
        userDao.verify();
    }
    
    public void testUserExistsException() {
        // set expectations
        user = new User("admin");
        user.setEmail("matt@raibledesigns.com");
        List users = new ArrayList();
        
        users.add(user);
        Exception ex = new DataIntegrityViolationException("");
        userDao.expects(once()).method("saveUser").with(same(user))
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
