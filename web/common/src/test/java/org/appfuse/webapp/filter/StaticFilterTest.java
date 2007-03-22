package org.appfuse.webapp.filter;

import junit.framework.TestCase;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class StaticFilterTest extends TestCase {
    private StaticFilter filter = null;

    protected void setUp() throws Exception {
        filter = new StaticFilter();
        MockFilterConfig config = new MockFilterConfig();
        config.addInitParameter("includes", "/scripts/*");
        filter.init(config);
    }

    public void testFilterDoesntForwardWhenPathMatches() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/scripts/dojo/test.html");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertNull(chain.getForwardURL());
    }

    public void testFilterForwardsWhenPathDoesntMatch() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/editProfile.html");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertNotNull(chain.getForwardURL());
    }
}

