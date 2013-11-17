package org.appfuse.webapp.pages;

import org.apache.tapestry5.dom.Document;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.func.Predicate;
import org.junit.Test;
import org.subethamail.wiser.Wiser;

import static org.junit.Assert.assertTrue;

public class PasswordRecoveryTokenTest extends BasePageTestCase {

    @Test
    public void testActivate() throws Exception {
        // start SMTP Server
        Wiser wiser = new Wiser();
        wiser.setPort(getSmtpPort());
        wiser.start();

        doc = tester.renderPage("passwordRecoveryToken/admin");

        // verify an account information e-mail was sent
        wiser.stop();
        assertTrue(wiser.getMessages().size() == 1);

        assertTextPresent(doc, "A password reset link was sent to your registered email address.");
    }

}
