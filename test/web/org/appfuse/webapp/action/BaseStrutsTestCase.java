package org.appfuse.webapp.action;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.Constants;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mock.web.MockServletContext;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import servletunit.struts.MockStrutsTestCase;


/**
 * This class is extended by all ActionTests.  It basically
 * contains common methods that they might use.
 *
 * <p>
 * <a href="BaseStrutsTestCase.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @version $Revision: 1.13 $ $Date: 2005/08/28 21:13:23 $
 */
public abstract class BaseStrutsTestCase extends MockStrutsTestCase {
    //~ Instance fields ========================================================

    protected final Log log = LogFactory.getLog(getClass());
    protected User user = null;
    protected ResourceBundle rb = null;
    protected WebApplicationContext ctx = null;
    
    //~ Constructors ===========================================================

    public BaseStrutsTestCase(String name) {
        super(name);
        // Since a ResourceBundle is not required for each class, just
        // do a simple check to see if one exists
        String className = this.getClass().getName();

        try {
            rb = ResourceBundle.getBundle(className);
        } catch (MissingResourceException mre) {
            //log.warn("No resource bundle found for: " + className);
        }
    }

    //~ Methods ================================================================

    protected void setUp() throws Exception {
        super.setUp();       
        
        // initialize Spring
        String pkg = ClassUtils.classPackageAsResourcePath(Constants.class);
        MockServletContext sc = new MockServletContext("");
        sc.addInitParameter(ContextLoader.CONFIG_LOCATION_PARAM,
                "classpath*:/" + pkg + "/dao/applicationContext-*.xml," + 
                "classpath*:META-INF/applicationContext-*.xml");
        
        ServletContextListener contextListener = new ContextLoaderListener();
        ServletContextEvent event = new ServletContextEvent(sc);
        contextListener.contextInitialized(event);
        
        // magic bridge to make StrutsTestCase aware of Spring's Context
        getSession().getServletContext().setAttribute(
                WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, 
                sc.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE));
        
        ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(
        		    getSession().getServletContext());
        
        // populate the userForm and place into session
        UserManager userMgr = (UserManager) ctx.getBean("userManager");
        user = userMgr.getUser("tomcat");
        getSession().setAttribute(Constants.USER_KEY, user);

        // change the port on the mailSender so it doesn't conflict with an 
        // existing SMTP server on localhost
        JavaMailSenderImpl mailSender = (JavaMailSenderImpl) ctx.getBean("mailSender");
        mailSender.setPort(2525);
        mailSender.setHost("localhost");
    }
    
    public void tearDown() throws Exception {
        super.tearDown();
        ctx = null;
    }
}
