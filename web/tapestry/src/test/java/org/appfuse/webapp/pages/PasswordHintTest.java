package org.appfuse.webapp.pages;

import org.junit.Test;
import org.subethamail.wiser.Wiser;

import static org.junit.Assert.assertTrue;

public class PasswordHintTest extends BasePageTestCase {

    @Test
    public void testActivate() throws Exception {
        // start SMTP Server
        Wiser wiser = startWiser(getSmtpPort());

        doc = tester.renderPage("passwordHint/admin");

        // verify an account information e-mail was sent
        wiser.stop();
        assertTrue(wiser.getMessages().size() == 1);

        assertTrue(doc.toString().contains("The password hint for admin has been sent to"));
    }
}
