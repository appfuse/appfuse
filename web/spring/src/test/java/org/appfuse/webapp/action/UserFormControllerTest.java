package org.appfuse.webapp.action;

import org.appfuse.model.User;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

public class UserFormControllerTest extends BaseControllerTestCase {
    private UserFormController c;
    private MockHttpServletRequest request;
    private ModelAndView mv;

    protected void setUp() throws Exception {
        // needed to initialize a user
        super.setUp();
        c = (UserFormController) ctx.getBean("userFormController");
    }

    protected void tearDown() {
        c = null;
    }

    public void testCancel() throws Exception {
        log.debug("testing cancel...");
        request = newPost("/editUser.html");
        request.addParameter("cancel", "");

        mv = c.handleRequest(request, new MockHttpServletResponse());

        assertEquals("redirect:mainMenu.html", mv.getViewName());
    }

    public void testEdit() throws Exception {
        log.debug("testing edit...");
        request = newGet("/editUser.html");
        request.addParameter("username", "tomcat");

        mv = c.handleRequest(request, new MockHttpServletResponse());

        assertEquals("userForm", mv.getViewName());
        User editUser = (User) mv.getModel().get(c.getCommandName());
        assertEquals("Tomcat User", editUser.getFullName());
    }

    public void testSave() throws Exception {
        request = newPost("/editUser.html");
        // set updated properties first since adding them later will
        // result in multiple parameters with the same name getting sent
        user.setConfirmPassword(user.getPassword());
        user.setLastName("Updated Last Name");
        super.objectToRequestParameters(user, request);
        
        mv = c.handleRequest(request, new MockHttpServletResponse());

        log.debug(mv.getModel());
        Errors errors = (Errors) mv.getModel().get(BindException.ERROR_KEY_PREFIX + "user");
        assertNull(errors);
        assertNotNull(request.getSession().getAttribute("messages"));
    }
    
    public void testAddWithMissingFields() throws Exception {
        request = newPost("/editUser.html");
        request.addParameter("firstName", "Julie");
        request.setRemoteUser("tomcat");

        mv = c.handleRequest(request, new MockHttpServletResponse());

        Errors errors = (Errors) mv.getModel().get(BindException.MODEL_KEY_PREFIX + "user");
        assertTrue(errors.getAllErrors().size() == 10);
    }
    
    public void testRemove() throws Exception {
        request = newPost("/editUser.html");
        request.addParameter("delete", "");
        request.addParameter("id", "2");

        mv = c.handleRequest(request, new MockHttpServletResponse());
        
        assertNotNull(request.getSession().getAttribute("messages"));
    }
}
