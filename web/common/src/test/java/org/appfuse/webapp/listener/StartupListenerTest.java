package org.appfuse.webapp.listener;

import org.appfuse.Constants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import static org.junit.Assert.*;


/**
 * This class tests the StartupListener class to verify that variables are
 * placed into the servlet context.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class StartupListenerTest {
    private MockServletContext sc = null;
    private ServletContextListener listener = null;
    private ContextLoaderListener springListener = null;

    @Before
    public void setUp() throws Exception {
        sc = new MockServletContext("");

        // initialize Spring
        sc.addInitParameter(ContextLoader.CONFIG_LOCATION_PARAM,
            "classpath:/applicationContext-dao.xml, " +
                "classpath:/applicationContext-service.xml, " +
                "classpath:/applicationContext-resources.xml");

        springListener = new ContextLoaderListener();
        springListener.contextInitialized(new ServletContextEvent(sc));
        listener = new StartupListener();
    }

    @After
    public void tearDown() throws Exception {
        // cleanup: close sessionFactory and related resources (search index locks)
        springListener.closeWebApplicationContext(sc);
        springListener = null;
        listener = null;
        sc = null;
    }

    @Test
    public void testContextInitialized() {
        listener.contextInitialized(new ServletContextEvent(sc));

        assertTrue(sc.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE) != null);
        assertTrue(sc.getAttribute(Constants.AVAILABLE_ROLES) != null);
        assertNotNull(sc.getAttribute(Constants.ASSETS_VERSION));
    }
}
