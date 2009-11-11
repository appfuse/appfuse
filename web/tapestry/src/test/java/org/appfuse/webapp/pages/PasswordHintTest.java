package org.appfuse.webapp.pages;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.subethamail.wiser.Wiser;

public class PasswordHintTest extends BasePageTestCase {

    @Test
    public void testActivate() throws Exception {
        // start SMTP Server
        Wiser wiser = new Wiser();
        wiser.setPort(getSmtpPort());
        wiser.start();

        doc = tester.renderPage("passwordHint/admin");

        // verify an account information e-mail was sent
        wiser.stop();
        assertTrue(wiser.getMessages().size() == 1);

        assertTrue(doc.getElementById("successMessages").toString()
                .contains("The password hint for admin has been sent to"));
    }

}
