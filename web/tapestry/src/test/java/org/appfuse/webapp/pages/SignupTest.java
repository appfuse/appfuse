package org.appfuse.webapp.pages;

import org.apache.tapestry5.dom.Element;
import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.security.core.context.SecurityContextHolder;
import org.subethamail.wiser.Wiser;

public class SignupTest extends BasePageTestCase {

    @Test
    public void testSignup() {
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

        doc = tester.submitForm(form, fieldValues);

        assertFalse(doc.toString().contains("exception"));

        // verify an account information e-mail was sent
        assertEquals(1, wiser.getMessages().size());
        wiser.stop();

        SecurityContextHolder.getContext().setAuthentication(null);
    }
}
