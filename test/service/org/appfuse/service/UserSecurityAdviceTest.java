package org.appfuse.service;

import net.sf.acegisecurity.AccessDeniedException;
import net.sf.acegisecurity.Authentication;
import net.sf.acegisecurity.GrantedAuthority;
import net.sf.acegisecurity.GrantedAuthorityImpl;
import net.sf.acegisecurity.context.ContextHolder;
import net.sf.acegisecurity.context.security.SecureContext;
import net.sf.acegisecurity.context.security.SecureContextImpl;
import net.sf.acegisecurity.providers.UsernamePasswordAuthenticationToken;

import org.appfuse.Constants;
import org.appfuse.dao.UserDAO;
import org.appfuse.model.Role;
import org.appfuse.model.User;
import org.jmock.Mock;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class UserSecurityAdviceTest extends BaseManagerTestCase {
    Mock userDAO = null;

    protected void setUp() throws Exception {
        super.setUp();
        SecureContext context = new SecureContextImpl();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("user",
                "password",
                new GrantedAuthority[] {new GrantedAuthorityImpl(Constants.USER_ROLE)});
        token.setAuthenticated(true);
        context.setAuthentication(token);
        ContextHolder.setContext(context);
    }

    public void testAddUserWithoutAdminRole() throws Exception {
        Authentication auth = ((SecureContext) ContextHolder.getContext()).getAuthentication();
        assertTrue(auth.isAuthenticated());
        UserManager userManager = (UserManager) makeInterceptedTarget();
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
        SecureContext context = new SecureContextImpl();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("admin",
                "password",
                new GrantedAuthority[] {new GrantedAuthorityImpl(Constants.ADMIN_ROLE)});
        token.setAuthenticated(true);
        context.setAuthentication(token);
        ContextHolder.setContext(context);

        UserManager userManager = (UserManager) makeInterceptedTarget();
        User user = new User("admin");

        userDAO.expects(once()).method("saveUser");
        userManager.saveUser(user);
        userDAO.verify();
    }

    public void testUpdateUserProfile() throws Exception {
        UserManager userManager = (UserManager) makeInterceptedTarget();
        User user = new User("user");;
        user.getRoles().add(new Role(Constants.USER_ROLE));

        userDAO.expects(once()).method("saveUser");
        userManager.saveUser(user);
        userDAO.verify();
    }

    // Test fix to http://issues.appfuse.org/browse/APF-96
    public void testChangeToAdminRoleFromUserRole() throws Exception {
        UserManager userManager = (UserManager) makeInterceptedTarget();
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
        UserManager userManager = (UserManager) makeInterceptedTarget();
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
        SecureContext context = new SecureContextImpl();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("user",
                "password",
                new GrantedAuthority[] {new GrantedAuthorityImpl(Constants.ADMIN_ROLE)});
        token.setAuthenticated(true);
        context.setAuthentication(token);
        ContextHolder.setContext(context);

        UserManager userManager = (UserManager) makeInterceptedTarget();
        User user = new User("user");
        user.getRoles().add(new Role(Constants.ADMIN_ROLE));
        user.getRoles().add(new Role(Constants.USER_ROLE));

        userDAO.expects(once()).method("saveUser");
        userManager.saveUser(user);
        userDAO.verify();
    }

    // Test fix to http://issues.appfuse.org/browse/APF-96
    public void testUpdateUserWithUserRole() throws Exception {
        UserManager userManager = (UserManager) makeInterceptedTarget();
        User user = new User("user");
        user.getRoles().add(new Role(Constants.USER_ROLE));

        userDAO.expects(once()).method("saveUser");
        userManager.saveUser(user);
        userDAO.verify();
    }

    private UserManager makeInterceptedTarget() {
        ApplicationContext context = new ClassPathXmlApplicationContext(
                "org/appfuse/service/applicationContext-test.xml");

        UserManager userManager = (UserManager) context.getBean("target");

        // Mock the userDAO
        userDAO = new Mock(UserDAO.class);
        userManager.setUserDAO((UserDAO) userDAO.proxy());
        return userManager;
    }
}
