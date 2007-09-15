package org.appfuse.webapp.pages;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hivemind.Messages;
import org.apache.hivemind.impl.MessageFormatter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.test.Creator;
import org.appfuse.Constants;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

public abstract class BasePageTestCase extends AbstractTransactionalDataSourceSpringContextTests {
    protected final Log log = LogFactory.getLog(getClass());
    protected final static String EXTENSION = ".html";
    protected static final String MESSAGES = Constants.BUNDLE_KEY;

    protected String[] getConfigLocations() {
        super.setAutowireMode(AUTOWIRE_BY_NAME);
        return new String[] {
                "classpath:/applicationContext-resources.xml",
                "classpath:/applicationContext-dao.xml",
                "classpath:/applicationContext-service.xml",
                "classpath*:/applicationContext.xml", // for modular archetypes
                "/WEB-INF/applicationContext*.xml"
            };
    }

    @Override
    protected void onSetUpBeforeTransaction() throws Exception {
        // change the port on the mailSender so it doesn't conflict with an 
        // existing SMTP server on localhost
        JavaMailSenderImpl mailSender = (JavaMailSenderImpl) applicationContext.getBean("mailSender");
        mailSender.setPort(2525);
        mailSender.setHost("localhost");
    }

    protected IPage getPage(Class clazz) {
        return getPage(clazz, null);
    }
    
    protected IPage getPage(Class clazz, Map<String, Object> properties) {
        Creator creator = new Creator();
        if (properties == null) {
            properties = new HashMap<String, Object>();
        }
        
        Messages messages = new MessageFormatter(log, MESSAGES);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteUser("user");
        
        properties.put("engineService", new MockPageService());
        properties.put("messages", messages);
        properties.put("request", request);
        properties.put("response", new MockHttpServletResponse());
        
        return (IPage) creator.newInstance(clazz, properties);
    }
}