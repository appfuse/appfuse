package org.appfuse.webapp.action;

import org.appfuse.Constants;
import org.appfuse.model.Address;
import org.appfuse.model.User;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.dumbster.smtp.SimpleSmtpServer;
import com.opensymphony.webwork.ServletActionContext;


public class SignupActionTest extends BaseActionTestCase {
    private SignupAction action;

    protected void setUp() throws Exception {    
        super.setUp();
        action = (SignupAction) ctx.getBean("signupAction");
    }
    
    protected void tearDown() throws Exception {
        super.tearDown();
        action = null;
    }
    
    public void testDisplayForm() throws Exception {
        request = new MockHttpServletRequest(null, "GET", "/signup.html");
        ServletActionContext.setRequest(request);
        assertEquals(action.execute(), "input");
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
        action.setUser(user);

        // set mock response so setting cookies doesn't fail
        ServletActionContext.setResponse(new MockHttpServletResponse());
        
        // start SMTP Server
        SimpleSmtpServer server = SimpleSmtpServer.start(2525);
        
        assertEquals(action.save(), "success");
        assertFalse(action.hasActionErrors());
        
        // verify an account information e-mail was sent
        server.stop();
        assertTrue(server.getReceivedEmailSize() == 1);

        // verify that success messages are in the session
        assertNotNull(action.getSession().getAttribute(Constants.REGISTERED));
    }
}
