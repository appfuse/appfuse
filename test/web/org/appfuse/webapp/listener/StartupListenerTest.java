package org.appfuse.webapp.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import junit.framework.TestCase;

import org.appfuse.Constants;
import org.springframework.mock.web.MockServletContext;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;


/**
 * This class tests the StartupListener class to
 * verify that variables are placed into the application context.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class StartupListenerTest extends TestCase {
    private MockServletContext sc = null;
    private ServletContextListener listener = null;

    protected void setUp() throws Exception {
        super.setUp();
        listener = new StartupListener();
        
        sc = new MockServletContext("");
        sc.addInitParameter("daoType", "hibernate");
        
        // initialize Spring
        String pkg = ClassUtils.classPackageAsResourcePath(Constants.class);
        sc.addInitParameter(ContextLoader.CONFIG_LOCATION_PARAM,
                "classpath*:/" + pkg + "/dao/applicationContext-*.xml," +
                "classpath*:META-INF/applicationContext-*.xml");
        
        ServletContextListener contextListener = new ContextLoaderListener();
        ServletContextEvent event = new ServletContextEvent(sc);
        contextListener.contextInitialized(event);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        listener = null;
        sc = null;
    }

    public void testContextInitialized() {
        ServletContextEvent event = new ServletContextEvent(sc);
        listener.contextInitialized(event);

        assertTrue(sc.getAttribute(Constants.CONFIG) != null);
        assertTrue(sc.getAttribute(WebApplicationContext
                .ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE) != null);
        assertTrue(sc.getAttribute(Constants.AVAILABLE_ROLES) != null);
    }
}
