package org.appfuse.webapp.pages;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry5.dom.Document;
import org.apache.tapestry5.test.PageTester;
import org.appfuse.Constants;
import org.appfuse.webapp.services.IntegrationTestModule;
import org.appfuse.webapp.services.ServiceFacade;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.springframework.mail.javamail.JavaMailSenderImpl;

public abstract class BasePageTester extends Assert {
    protected PageTester tester;
    protected Document doc;
    protected Map<String, String> fieldValues;
    protected String testType;
    protected final static String MOCK = "mock";
    protected final static String INTEGRATION = "integration";
	protected final Log log = LogFactory.getLog(getClass());
    protected static final String MESSAGES = Constants.BUNDLE_KEY;
    private int smtpPort = 25250;

    @Before
    public void before() {
        String appPackage = "org.appfuse.webapp";
        String appName = "app";
        tester = new PageTester(appPackage, appName, "src/main/webapp", IntegrationTestModule.class);
        testType = INTEGRATION;
        fieldValues = new HashMap<String, String>();

	    smtpPort = smtpPort + (int) (Math.random() * 100);

        // change the port on the mailSender so it doesn't conflict with an existing SMTP server on localhost
	    ServiceFacade services = tester.getService(ServiceFacade.class);
	    JavaMailSenderImpl mailSender = (JavaMailSenderImpl) services.getMailEngine().getMailSender();
	    mailSender.setPort(getSmtpPort());
        mailSender.setHost("localhost");
    }

    @After
    public void after() {
        if (tester != null) {
            tester.shutdown();
        }
    }

	protected int getSmtpPort() {
        return smtpPort;
    }
}