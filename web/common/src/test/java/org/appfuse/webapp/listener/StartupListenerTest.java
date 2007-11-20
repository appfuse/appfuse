package org.appfuse.webapp.listener;

import junit.framework.TestCase;
import org.appfuse.Constants;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.ContextLoaderListener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Map;


/**
 * This class tests the StartupListener class to verify that variables are
 * placed into the servlet context.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class StartupListenerTest extends TestCase {
    private MockServletContext sc = null;
    private ServletContextListener listener = null;
    private ContextLoaderListener springListener = null;

    protected void setUp() throws Exception {
        super.setUp();
        sc = new MockServletContext("");
        sc.addInitParameter(Constants.CSS_THEME, "simplicity");
        
        // initialize Spring
        sc.addInitParameter(ContextLoader.CONFIG_LOCATION_PARAM,
                "classpath:/applicationContext-dao.xml, " +
                "classpath:/applicationContext-service.xml, " + 
                "classpath:/applicationContext-resources.xml");

        springListener = new ContextLoaderListener();
        springListener.contextInitialized(new ServletContextEvent(sc));
        listener = new StartupListener();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        springListener = null;
        listener = null;
        sc = null;
    }

    public void testContextInitialized() {
        listener.contextInitialized(new ServletContextEvent(sc));

        assertTrue(sc.getAttribute(Constants.CONFIG) != null);
        Map config = (Map) sc.getAttribute(Constants.CONFIG);
        assertEquals(config.get(Constants.CSS_THEME), "simplicity");
        
        assertTrue(sc.getAttribute(WebApplicationContext
                .ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE) != null);
        assertTrue(sc.getAttribute(Constants.AVAILABLE_ROLES) != null);
    }
}
