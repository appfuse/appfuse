package org.appfuse.webapp.action;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.Constants;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;

public class LoginServletTest extends TestCase {
    private final Log log = LogFactory.getLog(LoginServletTest.class);
    private LoginServlet servlet = null;
    private MockServletConfig config = null;

    public LoginServlet createInstance() throws Exception {
        return new LoginServlet();
    }

    protected void setUp() throws Exception {
        super.setUp();
        servlet = new LoginServlet();

        // set initialization parameters
        config = new MockServletConfig(new MockServletContext());
        config.addInitParameter(Constants.AUTH_URL, "j_security_check");
        config.addInitParameter(Constants.HTTP_PORT, "80");
        config.addInitParameter(Constants.HTTPS_PORT, "443");
        config.addInitParameter("isSecure", "true");
        config.addInitParameter(Constants.ENC_ALGORITHM, "SHA");
        servlet.init(config);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        servlet = null;
    }

    /**
     * Test that initialization parameters were set
     *
     * @throws Exception
     */
    public void testInit() throws Exception {
        // check all parameters from web.xml
        String authURL = config.getInitParameter(Constants.AUTH_URL);

        assertTrue((authURL != null) && authURL.equals("j_security_check"));

        Map appConfig =
            (HashMap) servlet.getServletContext().getAttribute(Constants.CONFIG);
        assertEquals(appConfig.get(Constants.HTTP_PORT), "80");
        assertEquals(appConfig.get(Constants.HTTPS_PORT), "443");
        assertEquals(appConfig.get(Constants.SECURE_LOGIN),
                     Boolean.valueOf("true"));
        assertEquals(appConfig.get(Constants.ENC_ALGORITHM), "SHA");
    }

    public void testExecute() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("j_username", "tomcat");
        request.addParameter("j_password", "tomcat");

        MockHttpServletResponse response = new MockHttpServletResponse();
        servlet.execute(request, response);

        assertTrue(response.getStatus() == MockHttpServletResponse.SC_OK);
    }
}
