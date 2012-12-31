package org.appfuse.service.impl;

import org.appfuse.Constants;
import org.appfuse.dao.UserDao;
import org.appfuse.model.Role;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.appfuse.service.UserSecurityAdvice;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class UserSecurityAdviceTest {

    @Mock
    private UserDao userDao;

    ApplicationContext ctx = null;
    SecurityContext initialSecurityContext = null;

    @Before
    public void setUp() throws Exception {
        // store initial security context for later restoration
        initialSecurityContext = SecurityContextHolder.getContext();

        SecurityContext context = new SecurityContextImpl();
        User user = new User("user");
        user.setId(1L);
        user.setPassword("password");
        user.addRole(new Role(Constants.USER_ROLE));

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
        token.setDetails(user);
        context.setAuthentication(token);
        SecurityContextHolder.setContext(context);
    }

    @After
    public void tearDown() {
        SecurityContextHolder.setContext(initialSecurityContext);
    }

    @Test
    public void testAddUserWithoutAdminRole() throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertTrue(auth.isAuthenticated());
        UserManager userManager = makeInterceptedTarget();
        User user = new User("admin");
        user.setId(2L);

        try {
            userManager.saveUser(user);
            fail("AccessDeniedException not thrown");
        } catch (AccessDeniedException expected) {
            assertNotNull(expected);
            Assert.assertEquals(expected.getMessage(), UserSecurityAdvice.ACCESS_DENIED);
        }
    }

    @Test
    public void testAddUserAsAdmin() throws Exception {
        SecurityContext securityContext = new SecurityContextImpl();
        User user = new User("admin");
        user.setId(2L);
        user.setPassword("password");
        user.addRole(new Role(Constants.ADMIN_ROLE));
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
        token.setDetails(user);
        securityContext.setAuthentication(token);
        SecurityContextHolder.setContext(securityContext);

        UserManager userManager = makeInterceptedTarget();
        final User adminUser = new User("admin");
        adminUser.setId(2L);

        given(userDao.saveUser(adminUser)).willReturn(adminUser);

        userManager.saveUser(adminUser);
    }

    @Test
    public void testUpdateUserProfile() throws Exception {
        UserManager userManager = makeInterceptedTarget();
        final User user = new User("user");
        user.setId(1L);
        user.getRoles().add(new Role(Constants.USER_ROLE));

        given(userDao.saveUser(user)).willReturn(user);

        userManager.saveUser(user);
    }

    // Test fix to http://issues.appfuse.org/browse/APF-96
    @Test
    public void testChangeToAdminRoleFromUserRole() throws Exception {
        UserManager userManager = makeInterceptedTarget();
        User user = new User("user");
        user.setId(1L);
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
    @Test
    public void testAddAdminRoleWhenAlreadyHasUserRole() throws Exception {
        UserManager userManager = makeInterceptedTarget();
        User user = new User("user");
        user.setId(1L);
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
    @Test
    public void testAddUserRoleWhenHasAdminRole() throws Exception {
        SecurityContext securityContext = new SecurityContextImpl();
        User user1 = new User("user");
        user1.setId(1L);
        user1.setPassword("password");
        user1.addRole(new Role(Constants.ADMIN_ROLE));
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(user1.getUsername(), user1.getPassword(), user1.getAuthorities());
        token.setDetails(user1);
        securityContext.setAuthentication(token);
        SecurityContextHolder.setContext(securityContext);

        UserManager userManager = makeInterceptedTarget();
        final User user = new User("user");
        user.setId(1L);
        user.getRoles().add(new Role(Constants.ADMIN_ROLE));
        user.getRoles().add(new Role(Constants.USER_ROLE));

        given(userDao.saveUser(user)).willReturn(user);

        userManager.saveUser(user);
    }

    // Test fix to http://issues.appfuse.org/browse/APF-96
    @Test
    public void testUpdateUserWithUserRole() throws Exception {
        UserManager userManager = makeInterceptedTarget();
        final User user = new User("user");
        user.setId(1L);
        user.getRoles().add(new Role(Constants.USER_ROLE));

        given(userDao.saveUser(user)).willReturn(user);

        userManager.saveUser(user);
    }

    private UserManager makeInterceptedTarget() {
        ctx = new ClassPathXmlApplicationContext("/applicationContext-test.xml");

        UserManager userManager = (UserManager) ctx.getBean("target");
        userManager.setUserDao(userDao);
        return userManager;
    }
}
