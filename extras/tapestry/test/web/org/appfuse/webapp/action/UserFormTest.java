package org.appfuse.webapp.action;

import java.util.ResourceBundle;

import org.appfuse.service.RoleManager;
import org.appfuse.service.UserManager;
import org.springframework.mock.web.MockHttpServletRequest;


public class UserFormTest extends BasePageTestCase {
    private UserForm page;

    protected void setUp() throws Exception {    
        super.setUp();
        page = (UserForm) getPage(UserForm.class);
        
        // unfortunately this is a required step if you're calling getMessage
        page.setBundle(ResourceBundle.getBundle(MESSAGES));
        page.setValidationDelegate(new Validator());

        // these can be mocked if you want a more "pure" unit test
        page.setUserManager((UserManager) ctx.getBean("userManager"));
        page.setRoleManager((RoleManager) ctx.getBean("roleManager"));
        page.setRequestCycle(getCycle(request, response));
    }
    
    protected void tearDown() throws Exception {
        super.tearDown();
        page = null;
    }
    
    public void testSave() throws Exception {
        user.setPassword("tomcat");
        user.setConfirmPassword("tomcat");
        page.setUser(user);

        page.save(getCycle(request, response));
        assertNotNull(page.getUser());
        assertFalse(page.hasErrors());
    }
    
    public void testRemove() throws Exception {
        user.setUsername("mraible");
        page.setUser(user);
        page.delete(getCycle(request, response));
        assertFalse(page.hasErrors());
    }
}
