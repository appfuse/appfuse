package org.appfuse.webapp.pages;

import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.internal.test.TestableResponse;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.security.core.context.SecurityContextHolder;
import org.subethamail.wiser.Wiser;
import org.subethamail.wiser.WiserMessage;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import static org.junit.Assert.*;

public class SignupTest extends BasePageTestCase {

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
        fieldValues.put("website", "http:raibledesigns.com");
        fieldValues.put("passwordHint", "Password is one with you.");

        fieldValues.put("city", "Denver");
        fieldValues.put("state", "CO");
        fieldValues.put("country", "USA");
        fieldValues.put("postalCode", "80210");

        // start SMTP Server
        Wiser wiser = new Wiser();
        wiser.setPort(getSmtpPort());
        wiser.start();

        TestableResponse response = tester.submitFormAndReturnResponse(form, fieldValues);


        assertFalse(response.getOutput().contains("exception"));

        // verify an account information e-mail was sent
        assertEquals(1, wiser.getMessages().size());
        wiser.stop();

        SecurityContextHolder.getContext().setAuthentication(null);
    }

    // Disabled for now.... UserExistsException not trapped in test mode?
    @Ignore @Test
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
        fieldValues.put("website", "http:raibledesigns.com");
        fieldValues.put("passwordHint", "Password is one with you.");

        fieldValues.put("city", "Denver");
        fieldValues.put("state", "CO");
        fieldValues.put("country", "USA");
        fieldValues.put("postalCode", "80210");

        // start SMTP Server
        Wiser wiser = new Wiser();
        wiser.setPort(getSmtpPort());
        wiser.start();

        TestableResponse response = tester.submitFormAndReturnResponse(form, fieldValues);

        ResourceBundle rb = ResourceBundle.getBundle(MESSAGES);
        String errorMessage = MessageFormat.format(rb.getString("errors.existing.user"),
                fieldValues.get("username"), fieldValues.get("email"));


        assertTrue(response.getRenderedDocument().toString().contains(errorMessage));

        // verify no account information e-mail was sent
        assertEquals(0, wiser.getMessages().size());
        wiser.stop();

        SecurityContextHolder.getContext().setAuthentication(null);
    }

}
