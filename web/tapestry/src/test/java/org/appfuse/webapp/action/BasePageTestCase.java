package org.appfuse.webapp.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hivemind.Messages;
import org.apache.hivemind.impl.MessageFormatter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.test.Creator;
import org.appfuse.Constants;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

public abstract class BasePageTestCase extends AbstractDependencyInjectionSpringContextTests {
    protected final Log log = LogFactory.getLog(getClass());
    protected final static String EXTENSION = ".html";
    protected static final String MESSAGES = Constants.BUNDLE_KEY;
    protected User user;

    protected String[] getConfigLocations() {
        return new String[] {"classpath*:/applicationContext-dao.xml",
            "classpath*:/applicationContext-service.xml",
            "/applicationContext-resources.xml"};
    }
    
    protected void onSetUp() throws Exception {
        // populate the userForm and place into session
        UserManager userMgr = (UserManager) applicationContext.getBean("userManager");
        user = userMgr.getUserByUsername("tomcat");
        
        // change the port on the mailSender so it doesn't conflict with an 
        // existing SMTP server on localhost
        JavaMailSenderImpl mailSender = (JavaMailSenderImpl) applicationContext.getBean("mailSender");
        mailSender.setPort(2525);
        mailSender.setHost("localhost");
    }
    
    protected void onTearDown() throws Exception {
        user = null;
    }

    protected IPage getPage(Class clazz) {
        return getPage(clazz, null);
    }
    
    protected IPage getPage(Class clazz, Map properties) {
        Creator creator = new Creator();
        if (properties == null) {
            properties = new HashMap();
        }
        
        Messages messages = new MessageFormatter(log, MESSAGES);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteUser(user.getUsername());
        
        properties.put("engineService", new MockPageService());
        properties.put("messages", messages);
        properties.put("request", request);
        properties.put("response", new MockHttpServletResponse());
        
        return (IPage) creator.newInstance(clazz, properties);
    }
}