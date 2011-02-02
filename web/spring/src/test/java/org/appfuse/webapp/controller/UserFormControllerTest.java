package org.appfuse.webapp.controller;

import org.appfuse.Constants;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;

public class UserFormControllerTest extends BaseControllerTestCase {
    private UserFormController c = null;
    private MockHttpServletRequest request;
    private User user;

    public void setUserFormController(UserFormController form) {
        this.c = form;
    }

    public void testAdd() throws Exception {
        log.debug("testing add new user...");
        request = newGet("/userform.html");
        request.addParameter("method", "Add");
        request.addUserRole(Constants.ADMIN_ROLE);

        user = c.showForm(request, new MockHttpServletResponse());
        assertNull(user.getUsername());
    }

    public void testAddWithoutPermission() throws Exception {
        log.debug("testing add new user...");
        request = newGet("/userform.html");
        request.addParameter("method", "Add");

        try {
            c.showForm(request, new MockHttpServletResponse());
            fail("AccessDeniedException not thrown...");
        } catch (AccessDeniedException ade) {
            assertNotNull(ade.getMessage());
        }     
    }
    
    public void testCancel() throws Exception {
        log.debug("testing cancel...");
        request = newPost("/userform.html");
        request.addParameter("cancel", "");

        BindingResult errors = new DataBinder(user).getBindingResult();
        String view = c.onSubmit(user, errors, request, new MockHttpServletResponse());

        assertEquals("redirect:/mainMenu", view);
    }

    public void testEdit() throws Exception {
        log.debug("testing edit...");
        request = newGet("/userform.html");
        request.addParameter("id", "-1"); // regular user
        request.addUserRole(Constants.ADMIN_ROLE);

        User user = c.showForm(request, new MockHttpServletResponse());
        assertEquals("Tomcat User", user.getFullName());
    }

    public void testEditWithoutPermission() throws Exception {
        log.debug("testing edit...");
        request = newGet("/userform.html");
        request.addParameter("id", "-1"); // regular user

        try {
            c.showForm(request, new MockHttpServletResponse());
            fail("AccessDeniedException not thrown...");
        } catch (AccessDeniedException ade) {
            assertNotNull(ade.getMessage());
        }
    }

    public void testEditProfile() throws Exception {
        log.debug("testing edit profile...");
        request = newGet("/userform.html");
        request.setRemoteUser("user");

        user = c.showForm(request, new MockHttpServletResponse());
        assertEquals("Tomcat User", user.getFullName());
    }

    public void testSave() throws Exception {
        request = newPost("/userform.html");
        // set updated properties first since adding them later will
        // result in multiple parameters with the same name getting sent
        User user = ((UserManager) applicationContext.getBean("userManager")).getUser("-1");
        user.setConfirmPassword(user.getPassword());
        user.setLastName("Updated Last Name");

        BindingResult errors = new DataBinder(user).getBindingResult();
        c.onSubmit(user, errors, request, new MockHttpServletResponse());

        assertFalse(errors.hasErrors());
        assertNotNull(request.getSession().getAttribute("successMessages"));
    }
    
    public void testAddWithMissingFields() throws Exception {
        request = newPost("/userform.html");
        user = new User();
        user.setFirstName("Jack");
        request.setRemoteUser("user");

        BindingResult errors = new DataBinder(user).getBindingResult();
        c.onSubmit(user, errors, request, new MockHttpServletResponse());
        
        assertTrue(errors.getAllErrors().size() == 10);
    }
    
    public void testRemove() throws Exception {
        request = newPost("/userform.html");
        request.addParameter("delete", "");
        user = new User();
        user.setId(-2L);

        BindingResult errors = new DataBinder(user).getBindingResult();
        c.onSubmit(user, errors, request, new MockHttpServletResponse());
        
        assertNotNull(request.getSession().getAttribute("successMessages"));
    }
}
