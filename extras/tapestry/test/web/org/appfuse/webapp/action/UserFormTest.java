package org.appfuse.webapp.action;

import java.util.ResourceBundle;

import org.apache.tapestry.event.PageEvent;
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
    
    public void testEdit() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        page.setUser(user);
        
        PageEvent event = new PageEvent(page, page.getRequestCycle());
        page.pageBeginRender(event);
        
        assertNotNull(page.getUser().getUsername());
        assertFalse(page.hasErrors());
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
        // this is necessary because the MockRequestCycle instantiates pages
        // by uppercasing the first letter and then loading the class.  Since
        // I don't want the page/URL to be "userList", so that's why this hack
        // exists.
        page.setFrom("test");
        page.delete(getCycle(request, response));
        assertFalse(page.hasErrors());
    }
}
