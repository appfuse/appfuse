package org.appfuse.service.impl;

import org.appfuse.Constants;
import org.appfuse.dao.RoleDao;
import org.appfuse.dao.UserDao;
import org.appfuse.model.Role;
import org.appfuse.model.User;
import org.appfuse.service.UserExistsException;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

public class UserManagerImplTest extends BaseManagerMockTestCase {
    //~ Instance fields ========================================================

    @Mock
    private UserDao userDao;

    @Mock
    private RoleDao roleDao;

    @InjectMocks
    private UserManagerImpl userManager = new UserManagerImpl();

    @InjectMocks
    private RoleManagerImpl roleManager;


    //~ Methods ================================================================

    @Test
    public void testGetUser() throws Exception {
        //given
        final User testData = new User("1");
        testData.getRoles().add(new Role("user"));

        given(userDao.get(1L)).willReturn(testData);

        //then
        User user = userManager.getUser("1");

        //then
        assertTrue(user != null);
        assert user != null;
        assertTrue(user.getRoles().size() == 1);
    }

    @Test
    public void testSaveUser() throws Exception {
        //given
        final User testData = new User("1");
        testData.getRoles().add(new Role("user"));

        given(userDao.get(1L)).willReturn(testData);


        final User user = userManager.getUser("1");
        user.setPhoneNumber("303-555-1212");

        given(userDao.saveUser(user)).willReturn(user);


        //when
        User returned = userManager.saveUser(user);

        //then
        assertTrue(returned.getPhoneNumber().equals("303-555-1212"));
        assertTrue(returned.getRoles().size() == 1);
    }

    @Test
    public void testAddAndRemoveUser() throws Exception {
        //given
        User user = new User();

        // call populate method in super class to populate test data
        // from a properties file matching this class name
        user = (User) populate(user);

        given(roleDao.getRoleByName("ROLE_USER")).willReturn(new Role("ROLE_USER"));

        Role role = roleManager.getRole(Constants.USER_ROLE);
        user.addRole(role);

        final User user1 = user;
        given(userDao.saveUser(user1)).willReturn(user1);


        //when
        user = userManager.saveUser(user);

        //then
        assertTrue(user.getUsername().equals("john"));
        assertTrue(user.getRoles().size() == 1);

        //given
        willDoNothing().given(userDao).remove(5L);
        userManager.removeUser("5");

        given(userDao.get(5L)).willReturn(null);

        //when
        user = userManager.getUser("5");

        //then
        assertNull(user);
        verify(userDao).remove(5L);
    }

    @Test
    public void testUserExistsException() {
        // set expectations
        final User user = new User("admin");
        user.setEmail("matt@raibledesigns.com");

        willThrow(new DataIntegrityViolationException("")).given(userDao).saveUser(user);

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
