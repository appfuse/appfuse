package org.appfuse.webapp.controller;

import org.acegisecurity.AccessDeniedException;
import org.appfuse.Constants;
import org.appfuse.service.UserManager;
import org.appfuse.model.User;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

public class UserFormControllerTest extends BaseControllerTestCase {
    private UserFormController c = null;
    private MockHttpServletRequest request;
    private ModelAndView mv;

    public void setUserFormController(UserFormController form) {
        this.c = form;
    }

    public void testAdd() throws Exception {
        log.debug("testing add new user...");
        request = newGet("/userform.html");
        request.addParameter("method", "Add");
        request.addUserRole(Constants.ADMIN_ROLE);

        mv = c.handleRequest(request, new MockHttpServletResponse());
        User user = (User) mv.getModel().get(c.getCommandName());
        assertNull(user.getUsername());
    }

    public void testAddWithoutPermission() throws Exception {
        log.debug("testing add new user...");
        request = newGet("/userform.html");
        request.addParameter("method", "Add");

        try {
            mv = c.handleRequest(request, new MockHttpServletResponse());
            fail("AccessDeniedException not thrown...");
        } catch (AccessDeniedException ade) {
            assertNotNull(ade.getMessage());
        }     
    }
    
    public void testCancel() throws Exception {
        log.debug("testing cancel...");
        request = newPost("/userform.html");
        request.addParameter("cancel", "");

        mv = c.handleRequest(request, new MockHttpServletResponse());

        assertEquals("redirect:mainMenu.html", mv.getViewName());
    }

    public void testEdit() throws Exception {
        log.debug("testing edit...");
        request = newGet("/userform.html");
        request.addParameter("id", "-1"); // regular user
        request.addUserRole(Constants.ADMIN_ROLE);

        mv = c.handleRequest(request, new MockHttpServletResponse());

        assertEquals("userForm", mv.getViewName());
        User user = (User) mv.getModel().get(c.getCommandName());
        assertEquals("Tomcat User", user.getFullName());
    }

    public void testEditWithoutPermission() throws Exception {
        log.debug("testing edit...");
        request = newGet("/userform.html");
        request.addParameter("id", "-1"); // regular user

        try {
            mv = c.handleRequest(request, new MockHttpServletResponse());
            fail("AccessDeniedException not thrown...");
        } catch (AccessDeniedException ade) {
            assertNotNull(ade.getMessage());
        }
    }

    public void testEditProfile() throws Exception {
        log.debug("testing edit profile...");
        request = newGet("/userform.html");
        request.setRemoteUser("user");

        mv = c.handleRequest(request, new MockHttpServletResponse());

        assertEquals("userForm", mv.getViewName());
        User userform = (User) mv.getModel().get(c.getCommandName());
        assertEquals("Tomcat User", userform.getFullName());
    }

    public void testSave() throws Exception {
        request = newPost("/userform.html");
        // set updated properties first since adding them later will
        // result in multiple parameters with the same name getting sent
        User user = ((UserManager) applicationContext.getBean("userManager")).getUser("-1");
        user.setConfirmPassword(user.getPassword());
        user.setLastName("Updated Last Name");
        super.objectToRequestParameters(user, request);
        
        mv = c.handleRequest(request, new MockHttpServletResponse());

        log.debug(mv.getModel());
        Errors errors = (Errors) mv.getModel().get(BindException.MODEL_KEY_PREFIX + "user");
        assertNull(errors);
        assertNotNull(request.getSession().getAttribute("successMessages"));
    }
    
    public void testAddWithMissingFields() throws Exception {
        request = newPost("/userform.html");
        request.addParameter("firstName", "Jack");
        request.setRemoteUser("user");

        mv = c.handleRequest(request, new MockHttpServletResponse());

        Errors errors = (Errors) mv.getModel().get(BindException.MODEL_KEY_PREFIX + "user");
        assertTrue(errors.getAllErrors().size() == 10);
    }
    
    public void testRemove() throws Exception {
        request = newPost("/userform.html");
        request.addParameter("delete", "");
        request.addParameter("id", "-2");

        mv = c.handleRequest(request, new MockHttpServletResponse());
        
        assertNotNull(request.getSession().getAttribute("successMessages"));
    }
}
