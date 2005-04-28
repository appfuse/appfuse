package org.appfuse.webapp.action;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.ApplicationServlet;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.junit.MockEngine;
import org.apache.tapestry.junit.TapestryTestCase;
import org.apache.tapestry.junit.mock.MockContext;
import org.apache.tapestry.junit.mock.MockServletConfig;
import org.apache.tapestry.request.RequestContext;

import org.appfuse.Constants;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public abstract class BasePageTestCase extends TapestryTestCase {
    protected final transient Log log = LogFactory.getLog(getClass());
    protected static final String MESSAGES = Constants.BUNDLE_KEY;
    protected static WebApplicationContext ctx;
    protected User user;
    protected MockEngine engine;
    protected MockHttpServletRequest request = new MockHttpServletRequest();
    protected MockHttpServletResponse response = new MockHttpServletResponse();

    // This static block ensures that Spring's BeanFactory is only loaded
    // once for all tests
    static {
        String pkg = ClassUtils.classPackageAsResourcePath(Constants.class);
        MockServletContext servletContext = new MockServletContext("");
        servletContext.addInitParameter(ContextLoader.CONFIG_LOCATION_PARAM,
                "classpath*:/" + pkg + "/dao/applicationContext-*.xml," +
                "classpath*:META-INF/applicationContext-*.xml");

        ServletContextListener contextListener = new ContextLoaderListener();
        ServletContextEvent event = new ServletContextEvent(servletContext);
        contextListener.contextInitialized(event);

        ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
    }

    protected void setUp() throws Exception {
        // populate the userForm and place into session
        UserManager userMgr = (UserManager) ctx.getBean("userManager");
        user = (User) userMgr.getUser("tomcat");
        
        // change the port on the mailSender so it doesn't conflict with an 
        // existing SMTP server on localhost
        JavaMailSenderImpl mailSender = (JavaMailSenderImpl) ctx.getBean("mailSender");
        mailSender.setPort(2525);
        mailSender.setHost("localhost");
    }

    protected Object getPage(Class clazz) {
        AbstractInstantiator i = new AbstractInstantiator();

        return i.getInstance(clazz);
    }

    protected IRequestCycle getCycle(HttpServletRequest request, HttpServletResponse response)
    throws Exception {
        MockContext servletContext = new MockContext();
        MockServletConfig config =
            new MockServletConfig("servlet", servletContext);
        ApplicationServlet servlet = new ApplicationServlet();

        servlet.init(config);

        engine = new MockEngine();
        //engine.setComponentStringsSource(new MockComponentMessageSource("ApplicationResources"));
        //engine.setServletPath(servletPath);

        RequestContext context = new RequestContext(servlet, request, response);

        return new org.appfuse.webapp.action.MockRequestCycle(engine, context);
    }
}
