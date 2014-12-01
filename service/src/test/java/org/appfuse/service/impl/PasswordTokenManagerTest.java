package org.appfuse.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.subethamail.wiser.Wiser;

import javax.transaction.Transactional;
import java.util.Random;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "classpath:/applicationContext-resources.xml",
    "classpath:/applicationContext-dao.xml",
    "classpath:/applicationContext-service.xml",
    "classpath:/applicationContext-test.xml"
})
public class PasswordTokenManagerTest {
    protected transient final Log log = LogFactory.getLog(getClass());
    private int smtpPort;
    private UserManager userManager;
    private PasswordTokenManager passwordTokenManager;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    @Qualifier("userManager")
    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    @Autowired
    @Qualifier("passwordTokenManager")
    public void setPasswordTokenManager(PasswordTokenManager passwordTokenManager) {
        this.passwordTokenManager = passwordTokenManager;
    }

    @Before
    public void before() throws Exception {
        smtpPort = (new Random().nextInt(9999 - 1000) + 1000);
        log.debug("SMTP Port set to: " + smtpPort);
        // change the port on the mailSender so it doesn't conflict with an
        // existing SMTP server on localhost
        final JavaMailSenderImpl mailSender = (JavaMailSenderImpl) applicationContext.getBean("mailSender");
        mailSender.setPort(smtpPort);
        mailSender.setHost("localhost");

        // create new user so conflicts don't occur with other tests
        User user = new User("token-test");
        user.setPassword("foobar");
        user.setFirstName("Token");
        user.setLastName("Test");
        user.setEmail("token-test@appfuse.org");
        userManager.saveUser(user);
    }

    @After
    public void after() {
        userManager.removeUser(userManager.getUserByUsername("token-test"));
    }

    @Test
    public void testGenerateRecoveryToken() {
        final User user = userManager.getUserByUsername("token-test");
        final String token = passwordTokenManager.generateRecoveryToken(user);
        assertNotNull(token);
        assertTrue(passwordTokenManager.isRecoveryTokenValid(user, token));
    }

    @Test
    public void testConsumeRecoveryToken() throws Exception {
        final User user = userManager.getUserByUsername("token-test");
        final Integer version = user.getVersion();

        final String token = passwordTokenManager.generateRecoveryToken(user);
        assertNotNull(token);
        assertTrue(passwordTokenManager.isRecoveryTokenValid(user, token));

        // start SMTP Server
        final Wiser wiser = new Wiser();
        wiser.setPort(smtpPort);
        wiser.start();

        User updated = userManager.updatePassword(user.getUsername(), null, token, "user", "");

        wiser.stop();
        assertTrue(wiser.getMessages().size() == 1);

        assertTrue(updated.getVersion() > version);
        assertFalse(passwordTokenManager.isRecoveryTokenValid(updated, token));
    }

}
