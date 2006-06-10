package org.appfuse.webapp.action;

import org.apache.tapestry.engine.ILink;
import org.appfuse.model.User;
import org.appfuse.service.MailEngine;
import org.appfuse.service.RoleManager;
import org.appfuse.service.UserManager;
import org.springframework.mail.SimpleMailMessage;

import java.util.HashMap;
import java.util.Map;


public class UserFormTest extends BasePageTestCase {
    private UserForm page;

    protected void onSetUp() throws Exception {
        super.onSetUp();        
        // these can be mocked if you want a more "pure" unit test
        Map map = new HashMap();
        map.put("userManager", applicationContext.getBean("userManager"));
        map.put("roleManager", applicationContext.getBean("roleManager"));
        map.put("mailMessage", applicationContext.getBean("mailMessage"));
        map.put("mailEngine", applicationContext.getBean("mailEngine"));
        page = (UserForm) getPage(UserForm.class, map);
    }
    
    protected void onTearDown() throws Exception {
        super.onTearDown();
        page = null;
    }
    
    public void testCancel() throws Exception {
        page.setFrom("");
        ILink link = page.cancel(new MockRequestCycle());
        assertEquals("mainMenu" + EXTENSION, link.getURL());
    }
    
    public void testSave() throws Exception {
        user.setPassword("tomcat");
        user.setConfirmPassword("tomcat");
        page.setUser(user);

        ILink link = page.save(new MockRequestCycle());
        assertNotNull(page.getUser());
        assertFalse(page.hasErrors());
        assertNull(page.getFrom());
        assertEquals("mainMenu" + EXTENSION, link.getURL());
    }
    
    public void testRemove() throws Exception {
        User user2Delete = new User();
        user2Delete.setId(new Long(2));
        page.setUser(user2Delete);
        page.delete(new MockRequestCycle());
        assertFalse(page.hasErrors());
    }
}
