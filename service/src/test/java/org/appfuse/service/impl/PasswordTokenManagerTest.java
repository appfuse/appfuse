package org.appfuse.service.impl;

import static org.junit.Assert.assertTrue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.subethamail.wiser.Wiser;

@ContextConfiguration(
	locations = {"classpath:/applicationContext-resources.xml",
		"classpath:/applicationContext-dao.xml",
                "classpath:/applicationContext-service.xml",
		"classpath:/applicationContext-test.xml"})
public class PasswordTokenManagerTest extends AbstractTransactionalJUnit4SpringContextTests {

    protected transient final Log log = LogFactory.getLog(getClass());
    private int smtpPort = 25250;

    @Autowired
    private UserManager userManager;

    private PasswordTokenManager passwordTokenManager;

    @Autowired
    @Qualifier("passwordTokenManager")
    public void setPasswordTokenManager(PasswordTokenManager passwordTokenManager) {
        this.passwordTokenManager = passwordTokenManager;
    }

    @Before
    public void onSetUp() {
	smtpPort = smtpPort + (int) (Math.random() * 100);
	// change the port on the mailSender so it doesn't conflict with an
	// existing SMTP server on localhost
	final JavaMailSenderImpl mailSender = (JavaMailSenderImpl) applicationContext.getBean("mailSender");
	mailSender.setPort(smtpPort);
	mailSender.setHost("localhost");
    }


    @Test
    public void testGenerateRecoveryToken() {
	final User user = userManager.getUserByUsername("admin");
	final String token = passwordTokenManager.generateRecoveryToken(user);
	Assert.assertNotNull(token);
	Assert.assertTrue(passwordTokenManager.isRecoveryTokenValid(user, token));
    }

    @Test
    public void testConsumeRecoveryToken() throws Exception {
	final User user = userManager.getUserByUsername("admin");
	final Integer version = user.getVersion();

	final String token = passwordTokenManager.generateRecoveryToken(user);
	Assert.assertNotNull(token);
	Assert.assertTrue(passwordTokenManager.isRecoveryTokenValid(user, token));

	// start SMTP Server
	final Wiser wiser = new Wiser();
	wiser.setPort(smtpPort);
	wiser.start();

	userManager.updatePassword(user.getUsername(), null, token, "admin", "");

	wiser.stop();
	assertTrue(wiser.getMessages().size() == 1);

	Assert.assertTrue(user.getVersion() > version);
	Assert.assertFalse(passwordTokenManager.isRecoveryTokenValid(user, token));
    }

}
