package org.appfuse.webapp.server.services;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.appfuse.model.User;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.subethamail.wiser.WiserMessage;

/**
 *	
 *	@author ivangsa
 *
 */
public abstract class UserRequestServiceTest extends BaseServiceTestCase {

    @Autowired private UserRequestService userRequestService;

    @After
    public void tearDown() throws Exception {
        logout();
    }

    @Test public void testGetCurrentUser() {
        User user = userRequestService.getCurrentUser();
        Assert.assertNull(user);
        login("admin");
        user = userRequestService.getCurrentUser();
        Assert.assertNotNull(user);
        logout();
        user = userRequestService.getCurrentUser();
        Assert.assertNull(user);
    }

    @Test public void testSignUp() throws Exception {
        User user = userRequestService.signUp();
        Assert.assertTrue(user.getId() == null);

        user.setUsername("signuptest");
        user.setPassword("signuptest");
        user.setPasswordHint("signuptest");
        user.setFirstName("signuptest");
        user.setLastName("signuptest");
        user.setEmail("signuptest@email.com");

        startSmtpServer();
        user = userRequestService.signUp(user);
        Assert.assertTrue(user.getId() != null);
        Assert.assertTrue(getReceivedMailMessages(true).size() == 1);
    }

    @Test public void testEditProfile() throws Exception {
        login("admin");
        User user = userRequestService.editProfile();
        final int version = user.getVersion();
        user.setPhoneNumber("555555");
        user = userRequestService.editProfile(user);
        Assert.assertEquals(version + 1, (int) user.getVersion());
    }

    @Test public void testGetUser() {
        try {
            userRequestService.getUser(-2L);
            Assert.fail("Expected AuthenticationException");
        } catch (final AuthenticationException e) {
        }

        login("user");
        try {
            userRequestService.getUser(-2L);
            Assert.fail("Expected AccessDeniedException");
        } catch (final AccessDeniedException e) {
        }

        login("admin");
        final User user = userRequestService.getUser(-1L);
        Assert.assertNotNull(user);
    }

    @Test public void testSaveUser() throws Exception {
        login("admin");
        User user = userRequestService.getUser(-2L);
        final int version = user.getVersion();
        user.setPhoneNumber("aaaaa");
        user = userRequestService.saveUser(user);
        Assert.assertEquals(version + 1, (int) user.getVersion());
    }

    @Test public void testCountUsers() {
        login("admin");
        final UsersSearchCriteria searchCriteria = new UsersSearchCriteria();
        final long count = userRequestService.countUsers(searchCriteria);
        Assert.assertTrue(count > 0);
    }

    @Test public void testSearchUsers() {
        login("admin");
        final UsersSearchCriteria searchCriteria = new UsersSearchCriteria();
        final long count = userRequestService.countUsers(searchCriteria);
        List<User> results = userRequestService.searchUsers(searchCriteria, 0, (int) count);
        Assert.assertEquals(count, results.size());
        results = userRequestService.searchUsers(searchCriteria, 0, (int) count, "username", true);
        Assert.assertEquals(count, results.size());
    }

    @Test public void testSendPasswordHint() {
        startSmtpServer();
        userRequestService.sendPasswordHint("admin");
        Assert.assertEquals(1, getReceivedMailMessages(true).size());
    }

    @Test public void testRequestRecoveryToken() {
        startSmtpServer();
        userRequestService.requestRecoveryToken("admin");
        final List<WiserMessage> messages = getReceivedMailMessages(true);
        Assert.assertEquals(1, messages.size());
        log.debug(ToStringBuilder.reflectionToString(messages.get(0)));
    }

    @Test public void testUpdatePassword() {

    }


}
