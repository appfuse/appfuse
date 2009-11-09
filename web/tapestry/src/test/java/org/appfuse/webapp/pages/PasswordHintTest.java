package org.appfuse.webapp.pages;

import org.junit.Test;
import org.junit.Ignore;
import org.subethamail.wiser.Wiser;
import org.apache.tapestry5.dom.Element;

public class PasswordHintTest extends BasePageTester {

    @Test
    public void testActivate() throws Exception {
        // start SMTP Server
        Wiser wiser = new Wiser();
        wiser.setPort(getSmtpPort());
        wiser.start();

        doc = tester.renderPage("Login");

        Element hintLink = doc.getElementById("passwordHint");
        assertNotNull("link exists", hintLink);
        doc = tester.clickLink(hintLink);

        // verify an account information e-mail was sent
        wiser.stop();
        assertTrue(wiser.getMessages().size() == 1);
        
        assertTrue(doc.getElementById("successMessages").toString()
                .contains("The password hint for admin has been sent to"));
    }

}
