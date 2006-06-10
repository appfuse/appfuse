package org.appfuse.webapp.action;

import java.util.HashMap;
import java.util.Map;

import org.appfuse.Constants;
import org.appfuse.model.Address;
import org.appfuse.model.User;
import org.appfuse.service.MailEngine;
import org.appfuse.service.RoleManager;
import org.appfuse.service.UserManager;
import org.springframework.mail.SimpleMailMessage;

import com.dumbster.smtp.SimpleSmtpServer;

public class SignupFormTest extends BasePageTestCase {
    private SignupForm page;
    
    protected void onSetUp() throws Exception {
        super.onSetUp();        
        // these can be mocked if you want a more "pure" unit test
        Map map = new HashMap();
        map.put("userManager", applicationContext.getBean("userManager"));
        map.put("roleManager", applicationContext.getBean("roleManager"));
        map.put("mailMessage", applicationContext.getBean("mailMessage"));
        map.put("mailEngine", applicationContext.getBean("mailEngine"));
        page = (SignupForm) getPage(SignupForm.class, map);
    }
    
    protected void onTearDown() throws Exception {
        super.onTearDown();
        page = null;
    }
    
    public void testExecute() throws Exception {
        User user = new User();
        user.setUsername("self-registered");
        user.setPassword("Password1");
        user.setConfirmPassword("Password1");
        user.setFirstName("First");
        user.setLastName("Last");
        
        Address address = new Address();
        address.setCity("Denver");
        address.setProvince("CO");
        address.setCountry("USA");
        address.setPostalCode("80210");        
        user.setAddress(address);
        
        user.setEmail("self-registered@raibledesigns.com");
        user.setWebsite("http://raibledesigns.com");
        user.setPasswordHint("Password is one with you.");
        page.setUser(user);
        
        // start SMTP Server
        SimpleSmtpServer server = SimpleSmtpServer.start(2525);
        
        page.save(new MockRequestCycle());
        
        assertFalse(page.hasErrors());
        
        // verify an account information e-mail was sent
        server.stop();
        assertTrue(server.getReceivedEmailSize() == 1);

        // verify that success messages are in the session
        assertNotNull(page.getSession().getAttribute(Constants.REGISTERED));
    }
}
