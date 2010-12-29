package org.appfuse.webapp.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;

public abstract class BaseControllerTestCase extends AbstractTransactionalDataSourceSpringContextTests {
    protected transient final Log log = LogFactory.getLog(getClass());
    private int smtpPort = 25250;

    protected String[] getConfigLocations() {
        setAutowireMode(AUTOWIRE_BY_NAME);
        return new String[] {
                "classpath:/applicationContext-resources.xml",
                "classpath:/applicationContext-dao.xml",
                "classpath:/applicationContext-service.xml",
                "classpath*:/applicationContext.xml", // for modular archetypes
                "/WEB-INF/applicationContext*.xml",
                "/WEB-INF/dispatcher-servlet.xml"
            };
    }

    @Override
    protected void onSetUpBeforeTransaction() throws Exception {
        smtpPort = smtpPort + (int) (Math.random() * 100);
        // change the port on the mailSender so it doesn't conflict with an
        // existing SMTP server on localhost
        JavaMailSenderImpl mailSender = (JavaMailSenderImpl) applicationContext.getBean("mailSender");
        mailSender.setPort(getSmtpPort());
        mailSender.setHost("localhost");
    }

    protected int getSmtpPort() {
        return smtpPort;
    }

    /**
     * Subclasses can invoke this to get a context key for the given location.
     * This doesn't affect the applicationContext instance variable in this class.
     * Dependency Injection cannot be applied from such contexts.
     */
    protected ConfigurableApplicationContext loadContextLocations(String[] locations) {
        if (logger.isInfoEnabled()) {
            logger.info("Loading additional configuration from: " + StringUtils.arrayToCommaDelimitedString(locations));
        }
        XmlWebApplicationContext ctx = new XmlWebApplicationContext();
        ctx.setConfigLocations(locations);
        ctx.setServletContext(new MockServletContext());
        ctx.refresh();
        return ctx;
    }
    
    /**
     * Convenience methods to make tests simpler
     * @return a MockHttpServletRequest with a POST to the specified URL
     * @param url the URL to post to
     */
    public MockHttpServletRequest newPost(String url) {
        return new MockHttpServletRequest("POST", url);
    }

    public MockHttpServletRequest newGet(String url) {
        return new MockHttpServletRequest("GET", url);
    }
}
