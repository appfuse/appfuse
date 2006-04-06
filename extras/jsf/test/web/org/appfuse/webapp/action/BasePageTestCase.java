package org.appfuse.webapp.action;

import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.webapp.FacesServlet;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import junit.framework.TestCase;
import org.apache.myfaces.webapp.StartupServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.Constants;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.appfuse.webapp.util.FacesUtils;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public abstract class BasePageTestCase extends TestCase {
    protected final transient Log log = LogFactory.getLog(getClass());
    protected static final String MESSAGES = "ApplicationResources";
    protected static FacesContext facesContext;
    protected static MockServletConfig config;
    protected static MockServletContext servletContext;
    protected static WebApplicationContext ctx;
    protected static User user;

    // This static block ensures that Spring's BeanFactory and JSF's 
    // FacesContext is only loaded once for all tests. If there's something
    // wrong with this approach - please let me know!
    static {
        String pkg = ClassUtils.classPackageAsResourcePath(Constants.class);
        servletContext = new MockServletContext("");
        servletContext.addInitParameter(BasePage.jstlBundleParam, MESSAGES);
        servletContext.addInitParameter(ContextLoader.CONFIG_LOCATION_PARAM,
                "classpath*:/" + pkg + "/dao/applicationContext-*.xml," +
                "classpath*:META-INF/applicationContext-*.xml");

        ServletContextListener contextListener = new ContextLoaderListener();
        ServletContextEvent event = new ServletContextEvent(servletContext);
        contextListener.contextInitialized(event);

        ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
        config = new MockServletConfig(servletContext);
        facesContext = performFacesContextConfig();
    }
    
    protected void setUp() throws Exception {
        // populate the userForm and place into session
        UserManager userMgr = (UserManager) ctx.getBean("userManager");
        user = userMgr.getUserByUsername("tomcat");
        
        // change the port on the mailSender so it doesn't conflict with an 
        // existing SMTP server on localhost
        JavaMailSenderImpl mailSender = (JavaMailSenderImpl) ctx.getBean("mailSender");
        mailSender.setPort(2525);
        mailSender.setHost("localhost");
    }

    protected static FacesContext performFacesContextConfig() {
        StartupServletContextListener facesListener =
            new StartupServletContextListener();
        ServletContextEvent event = new ServletContextEvent(servletContext);
        facesListener.contextInitialized(event);

        LifecycleFactory lifecycleFactory =
            (LifecycleFactory) FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
        Lifecycle lifecycle = lifecycleFactory.getLifecycle(getLifecycleId());
        FacesContextFactory facesCtxFactory =
            (FacesContextFactory) FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
        FacesContext ctx =
            facesCtxFactory.getFacesContext(servletContext, new MockHttpServletRequest(), new MockHttpServletResponse(),
                                            lifecycle);

        return ctx;
    }

    protected static String getLifecycleId() {
        String lifecycleId =
            servletContext.getInitParameter(FacesServlet.LIFECYCLE_ID_ATTR);

        return (lifecycleId != null) ? lifecycleId
                                     : LifecycleFactory.DEFAULT_LIFECYCLE;
    }

    protected Object getManagedBean(String name) {
        return FacesUtils.getManagedBean(name);
    }
}
