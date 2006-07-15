package org.appfuse.webapp.action;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.Constants;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mock.web.MockServletContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;

import com.opensymphony.xwork.ActionContext;
import com.opensymphony.xwork.util.LocalizedTextUtil;
import com.opensymphony.webwork.ServletActionContext;

public abstract class BaseActionTestCase extends TestCase {
    protected transient final Log log = LogFactory.getLog(getClass());
    protected static XmlWebApplicationContext ctx;
    protected MockHttpServletRequest request = new MockHttpServletRequest();

    // This static block ensures that Spring's BeanFactory is only loaded
    // once for all tests
    static {
        String pkg = ClassUtils.classPackageAsResourcePath(Constants.class);
        String[] paths = {
                "classpath*:/" + pkg + "/dao/applicationContext-*.xml",
                "classpath*:META-INF/applicationContext-*.xml",
                "/WEB-INF/action-servlet.xml"
            };
        
        ctx = new XmlWebApplicationContext();
        ctx.setConfigLocations(paths);
        ctx.setServletContext(new MockServletContext(""));
        ctx.refresh();
    }

    protected void setUp() throws Exception {
        LocalizedTextUtil.addDefaultResourceBundle(Constants.BUNDLE_KEY); 
        ActionContext.getContext().setSession(new HashMap());
        
        // change the port on the mailSender so it doesn't conflict with an 
        // existing SMTP server on localhost
        JavaMailSenderImpl mailSender = (JavaMailSenderImpl) ctx.getBean("mailSender");
        mailSender.setPort(2525);
        mailSender.setHost("localhost");

        // populate the request so getRequest().getSession() doesn't fail in BaseAction.java
        ServletActionContext.setRequest(request);
    }
    
    protected void tearDown() throws Exception {
        ActionContext.getContext().setSession(null);   
    }
}
