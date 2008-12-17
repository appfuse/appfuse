package org.appfuse.webapp.pages;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.Constants;
import org.junit.Before;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

@ContextConfiguration(
        locations = {"classpath:/applicationContext-resources.xml",
                "classpath:/applicationContext-dao.xml",
                "classpath:/applicationContext-service.xml",
                "classpath*:/applicationContext.xml",
                "classpath:**/applicationContext*.xml"})
public abstract class BasePageTestCase extends AbstractTransactionalJUnit4SpringContextTests {
    protected final Log log = LogFactory.getLog(getClass());
    protected static final String MESSAGES = Constants.BUNDLE_KEY;
    private int smtpPort = 25250;

    @Before
    public void setUp() {
        smtpPort = smtpPort + (int) (Math.random() * 100);

        // change the port on the mailSender so it doesn't conflict with an existing SMTP server on localhost
        JavaMailSenderImpl mailSender = (JavaMailSenderImpl) applicationContext.getBean("mailSender");
        mailSender.setPort(getSmtpPort());
        mailSender.setHost("localhost");
    }

    protected int getSmtpPort() {
        return smtpPort;
    }
}