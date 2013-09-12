package org.appfuse.webapp.util;

import static org.junit.Assert.assertTrue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.subethamail.wiser.Wiser;

@ContextConfiguration(
        locations = {"classpath:/applicationContext-resources.xml",
                "classpath:/applicationContext-dao.xml",
                "classpath:/applicationContext-service.xml"})
public class PasswordRecoveryManagerTest extends AbstractTransactionalJUnit4SpringContextTests {
    protected transient final Log log = LogFactory.getLog(getClass());
    private int smtpPort = 25250;

    @Autowired
    private UserManager userManager;
    //@Autowired
    private PasswordRecoveryManager passwordRecoveryManager;
    
    @Before
    public void onSetUp() {
        smtpPort = smtpPort + (int) (Math.random() * 100);
        // change the port on the mailSender so it doesn't conflict with an
        // existing SMTP server on localhost
        JavaMailSenderImpl mailSender = (JavaMailSenderImpl) applicationContext.getBean("mailSender");
        mailSender.setPort(smtpPort);
        mailSender.setHost("localhost");
        
        passwordRecoveryManager = new PasswordRecoveryManager();
        applicationContext.getAutowireCapableBeanFactory().autowireBean(passwordRecoveryManager);
    }


    @Test
    public void testGenerateRecoveryToken() {
    	User user = userManager.getUserByUsername("admin");
    	String token = passwordRecoveryManager.generateRecoveryToken(user);
    	Assert.assertNotNull(token);
    	Assert.assertTrue(passwordRecoveryManager.isRecoveryTokenValid(user, token));
    }
    
    @Test
    public void testConsumeRecoveryToken() throws Exception {
    	User user = userManager.getUserByUsername("admin");
    	Integer version = user.getVersion();
    	
    	String token = passwordRecoveryManager.generateRecoveryToken(user);
    	Assert.assertNotNull(token);
    	Assert.assertTrue(passwordRecoveryManager.isRecoveryTokenValid(user, token));
    	
        // start SMTP Server
        Wiser wiser = new Wiser();
        wiser.setPort(smtpPort);
        wiser.start();     
    	
    	passwordRecoveryManager.updatePassword(user.getUsername(), token, "admin", "");
    	
        wiser.stop();
        assertTrue(wiser.getMessages().size() == 1);
    	
    	Assert.assertTrue(user.getVersion() > version);
    	Assert.assertFalse(passwordRecoveryManager.isRecoveryTokenValid(user, token));
    }
    
}
