package org.appfuse.webapp.action;

import org.appfuse.Constants;
import org.appfuse.model.Address;
import org.appfuse.model.User;

import com.dumbster.smtp.SimpleSmtpServer;

public class SignupFormTest extends BasePageTestCase {
    private SignupForm bean;
    
    public void setUp() throws Exception {
        super.setUp();        
        bean = (SignupForm) getManagedBean("signupForm");
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
        bean.setUser(user);
        
        // start SMTP Server
        SimpleSmtpServer server = SimpleSmtpServer.start(2525);
        
        assertEquals(bean.save(), "mainMenu");
        assertFalse(bean.hasErrors());
        
        // verify an account information e-mail was sent
        server.stop();
        assertTrue(server.getReceivedEmailSize() == 1);

        // verify that success messages are in the session
        assertNotNull(bean.getSession().getAttribute(Constants.REGISTERED));
    }
}
