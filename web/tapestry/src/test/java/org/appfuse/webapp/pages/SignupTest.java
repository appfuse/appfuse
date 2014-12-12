package org.appfuse.webapp.pages;

import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.internal.test.TestableResponse;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.subethamail.wiser.Wiser;

import static org.junit.Assert.*;

public class SignupTest extends BasePageTestCase {

    @Before
    @After
    public void removeSignupUser() {
        UserManager userManager = applicationContext.getBean(UserManager.class);
        try {
            User signup = userManager.getUserByUsername("self-registered");
            userManager.removeUser(signup);
        } catch (UsernameNotFoundException e) {
            // OK: ignore
        }
    }

    @Test
    public void testNewUserSignup() {
        doc = tester.renderPage("Signup");

        Element form = doc.getElementById("form");
        assertNotNull("form exists", form);

        fieldValues.put("username", "self-registered");
        fieldValues.put("password", "Password1");
        fieldValues.put("confirmPassword", "Password1");
        fieldValues.put("firstName", "First");
        fieldValues.put("lastName", "Last");

        fieldValues.put("email", "self-registered@raibledesigns.com");
        fieldValues.put("website", "http://raibledesigns.com");
        fieldValues.put("passwordHint", "Password is one with you.");

        fieldValues.put("city", "Denver");
        fieldValues.put("state", "CO");
        fieldValues.put("country", "US");
        fieldValues.put("postalCode", "80210");

        // start SMTP Server
        Wiser wiser = startWiser(getSmtpPort());

        TestableResponse response = tester.submitFormAndReturnResponse(form, fieldValues);

        assertFalse(response.getOutput().contains("exception"));
        // verify an account information e-mail was sent
        assertEquals(1, wiser.getMessages().size());
        wiser.stop();

        SecurityContextHolder.getContext().setAuthentication(null);
    }

    @Test
    public void testExistingUserSignup() {
        doc = tester.renderPage("Signup");

        Element form = doc.getElementById("form");
        assertNotNull("form exists", form);

        fieldValues.put("username", "user");
        fieldValues.put("password", "Password1");
        fieldValues.put("confirmPassword", "Password1");
        fieldValues.put("firstName", "First");
        fieldValues.put("lastName", "Last");

        fieldValues.put("email", "self-registered@raibledesigns.com");
        fieldValues.put("website", "http://raibledesigns.com");
        fieldValues.put("passwordHint", "Password is one with you.");

        fieldValues.put("city", "Denver");
        fieldValues.put("state", "CO");
        fieldValues.put("country", "US");
        fieldValues.put("postalCode", "80210");

        // start SMTP Server
        Wiser wiser = startWiser(getSmtpPort());

        TestableResponse response = tester.submitFormAndReturnResponse(form, fieldValues);
        assertEquals(response.getRedirectURL(), "signup");

        // verify no account information e-mail was sent
        assertEquals(0, wiser.getMessages().size());
        wiser.stop();

        SecurityContextHolder.getContext().setAuthentication(null);
    }

}
