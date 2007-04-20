package org.appfuse.service;

import org.acegisecurity.AccessDeniedException;
import org.acegisecurity.Authentication;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.context.SecurityContextImpl;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.appfuse.Constants;
import org.appfuse.dao.UserDao;
import org.appfuse.model.Role;
import org.appfuse.model.User;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class UserSecurityAdviceTest extends MockObjectTestCase {
    Mock userDao = null;
    ApplicationContext ctx = null;
    SecurityContext initialSecurityContext = null;

    protected void setUp() throws Exception {
        super.setUp();
        
        // store initial security context for later restoration
        initialSecurityContext = SecurityContextHolder.getContext();
        
        SecurityContext context = new SecurityContextImpl();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("user",
                "password",
                new GrantedAuthority[] {new GrantedAuthorityImpl(Constants.USER_ROLE)});
        context.setAuthentication(token);
        SecurityContextHolder.setContext(context);
    }

    protected void tearDown() {
        SecurityContextHolder.setContext(initialSecurityContext);
    }
    
    public void testAddUserWithoutAdminRole() throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertTrue(auth.isAuthenticated());
        UserManager userManager = makeInterceptedTarget();
        User user = new User("admin");

        try {
            userManager.saveUser(user);
            fail("AccessDeniedException not thrown");
        } catch (AccessDeniedException expected) {
            assertNotNull(expected);
            assertEquals(expected.getMessage(), UserSecurityAdvice.ACCESS_DENIED);
        }
    }

    public void testAddUserAsAdmin() throws Exception {
        SecurityContext context = new SecurityContextImpl();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("admin",
                "password",
                new GrantedAuthority[] {new GrantedAuthorityImpl(Constants.ADMIN_ROLE)});
        context.setAuthentication(token);
        SecurityContextHolder.setContext(context);

        UserManager userManager = makeInterceptedTarget();
        User user = new User("admin");

        userDao.expects(once()).method("saveUser");
        userManager.saveUser(user);
        userDao.verify();
    }

    public void testUpdateUserProfile() throws Exception {
        UserManager userManager = makeInterceptedTarget();
        User user = new User("user");
        user.getRoles().add(new Role(Constants.USER_ROLE));

        userDao.expects(once()).method("saveUser");
        userManager.saveUser(user);
        userDao.verify();
    }

    // Test fix to http://issues.appfuse.org/browse/APF-96
    public void testChangeToAdminRoleFromUserRole() throws Exception {
        UserManager userManager = makeInterceptedTarget();
        User user = new User("user");
        user.getRoles().add(new Role(Constants.ADMIN_ROLE));

        try {
            userManager.saveUser(user);
            fail("AccessDeniedException not thrown");
        } catch (AccessDeniedException expected) {
            assertNotNull(expected);
            assertEquals(expected.getMessage(), UserSecurityAdvice.ACCESS_DENIED);
        }
    }

    // Test fix to http://issues.appfuse.org/browse/APF-96
    public void testAddAdminRoleWhenAlreadyHasUserRole() throws Exception {
        UserManager userManager = makeInterceptedTarget();
        User user = new User("user");
        user.getRoles().add(new Role(Constants.ADMIN_ROLE));
        user.getRoles().add(new Role(Constants.USER_ROLE));

        try {
            userManager.saveUser(user);
            fail("AccessDeniedException not thrown");
        } catch (AccessDeniedException expected) {
            assertNotNull(expected);
            assertEquals(expected.getMessage(), UserSecurityAdvice.ACCESS_DENIED);
        }
    }

        // Test fix to http://issues.appfuse.org/browse/APF-96
    public void testAddUserRoleWhenHasAdminRole() throws Exception {
        SecurityContext context = new SecurityContextImpl();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("user",
                "password",
                new GrantedAuthority[] {new GrantedAuthorityImpl(Constants.ADMIN_ROLE)});
        context.setAuthentication(token);
        SecurityContextHolder.setContext(context);

        UserManager userManager = makeInterceptedTarget();
        User user = new User("user");
        user.getRoles().add(new Role(Constants.ADMIN_ROLE));
        user.getRoles().add(new Role(Constants.USER_ROLE));

        userDao.expects(once()).method("saveUser");
        userManager.saveUser(user);
        userDao.verify();
    }

    // Test fix to http://issues.appfuse.org/browse/APF-96
    public void testUpdateUserWithUserRole() throws Exception {
        UserManager userManager = makeInterceptedTarget();
        User user = new User("user");
        user.getRoles().add(new Role(Constants.USER_ROLE));

        userDao.expects(once()).method("saveUser");
        userManager.saveUser(user);
        userDao.verify();
    }

    private UserManager makeInterceptedTarget() {
        ctx = new ClassPathXmlApplicationContext("/applicationContext-test.xml");

        UserManager userManager = (UserManager) ctx.getBean("target");

        // Mock the userDao
        userDao = new Mock(UserDao.class);
        userManager.setUserDao((UserDao) userDao.proxy());
        return userManager;
    }
}
