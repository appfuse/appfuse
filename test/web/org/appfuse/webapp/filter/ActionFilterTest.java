package org.appfuse.webapp.filter;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import junit.framework.TestCase;

import org.appfuse.Constants;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ContextLoaderListener;

public class ActionFilterTest extends TestCase {
    private ActionFilter filter = null;
    private MockFilterConfig config = null;

    protected void setUp() throws Exception {
        super.setUp();
        filter = new ActionFilter();

        MockServletContext sc = new MockServletContext("");
        Map appConfig = new HashMap();
        appConfig.put(Constants.HTTP_PORT, "80");
        appConfig.put(Constants.HTTPS_PORT, "443");
        sc.setAttribute(Constants.CONFIG, appConfig);
        
        sc.addInitParameter(ContextLoader.CONFIG_LOCATION_PARAM,
                            "/WEB-INF/applicationContext*.xml");
        ServletContextListener listener = new ContextLoaderListener();
        ServletContextEvent event = new ServletContextEvent(sc);
        listener.contextInitialized(event);
        
        // set initialization parameters
        config = new MockFilterConfig(sc);
        config.addInitParameter("isSecure", "false");
        filter.init(config);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        filter = null;
    }

    public void testInit() throws Exception {
        assertTrue(config != null);
        assertEquals(config.getInitParameter("isSecure"), "false");
    }

    public void testDestroy() throws Exception {
        config = null;
        assertTrue(config == null);
    }

    public void testDoFilter() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteUser("tomcat");

        MockHttpServletResponse response = new MockHttpServletResponse();
        filter.doFilter(request, response, new MockFilterChain());
        assertNotNull(request.getSession().getAttribute(Constants.USER_KEY));
    }
}
