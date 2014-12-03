package org.appfuse.webapp.action;

import com.opensymphony.xwork2.Action;
import org.apache.struts2.ServletActionContext;
import org.appfuse.Constants;
import org.appfuse.model.Address;
import org.appfuse.model.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.subethamail.wiser.Wiser;

import static org.junit.Assert.*;

public class SignupActionTest extends BaseActionTestCase {
    @Autowired
    private SignupAction action;

    @Test
    public void testDisplayForm() throws Exception {
        final MockHttpServletRequest request = new MockHttpServletRequest(null, "GET", "/signup.html");
        ServletActionContext.setRequest(request);
        assertEquals("input", action.execute());
    }

    @Test
    public void testExecutePost() throws Exception {
        final MockHttpServletRequest request = new MockHttpServletRequest(null, "POST", "/signup.html");
        ServletActionContext.setRequest(request);
        assertEquals(Action.SUCCESS, action.execute());
    }

    @Test
    public void testExecuteCancel() throws Exception {
        action.setCancel(BaseAction.CANCEL);
        assertEquals(BaseAction.CANCEL, action.execute());
    }

    @Test
    public void testDefault() throws Exception {
        action.setCancel(BaseAction.CANCEL);
        assertEquals(BaseAction.INPUT, action.doDefault());
    }

    @Test
    public void testSave() throws Exception {
        final User user = createUser();
        final User user2 = createUser();
        action.setUser(user);

        // set mock response so setting cookies doesn't fail
        ServletActionContext.setResponse(new MockHttpServletResponse());

        // start SMTP Server
        final Wiser wiser = startWiser(getSmtpPort());

        assertNull(action.getUser().getId());
        assertEquals("success", action.save());
        assertFalse(action.hasActionErrors());
        assertNotNull(action.getUser().getId());

        // verify an account information e-mail was sent
        wiser.stop();
        assertTrue(wiser.getMessages().size() == 1);

        // verify that success messages are in the session
        assertNotNull(action.getSession().getAttribute(Constants.REGISTERED));
        // try it again with same user
        action.setUser(user2);
        assertEquals(Action.INPUT, action.save());
        assertTrue(action.hasActionErrors());

        SecurityContextHolder.getContext().setAuthentication(null);
    }

    private User createUser() {
        final User user = new User();

        user.setUsername("self-registered");
        user.setPassword("Password1");
        user.setConfirmPassword("Password1");
        user.setFirstName("First");
        user.setLastName("Last");

        final Address address = new Address();
        address.setCity("Denver");
        address.setProvince("CO");
        address.setCountry("USA");
        address.setPostalCode("80210");
        user.setAddress(address);

        user.setEmail("self-registered@raibledesigns.com");
        user.setWebsite("http://raibledesigns.com");
        user.setPasswordHint("Password is one with you.");

        return user;
    }
}
