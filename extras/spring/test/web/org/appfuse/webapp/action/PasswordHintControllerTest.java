package org.appfuse.webapp.action;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.appfuse.Constants;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;


public class PasswordHintControllerTest extends BaseControllerTestCase {
    private PasswordHintController c;

    protected void setUp() throws Exception {
        // needed to initialize a user
        super.setUp();
        c = (PasswordHintController) ctx.getBean("passwordHintController");
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        c = null;
    }

    public void testExecute() throws Exception {
        MockHttpServletRequest request = newGet("/passwordHint.html");
        request.addParameter("username", "tomcat");

        c.handleRequest(request, new MockHttpServletResponse());
        
        // verify that success messages are in the request
        assertNotNull(request.getAttribute("messages"));
    }
}
