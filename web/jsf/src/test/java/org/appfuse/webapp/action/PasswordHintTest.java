package org.appfuse.webapp.action;

import org.appfuse.service.MailEngine;
import org.appfuse.service.UserManager;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.subethamail.wiser.Wiser;

import static org.junit.Assert.*;


public class PasswordHintTest extends BasePageTestCase {
    private PasswordHint bean;

    @Autowired
    private UserManager userManager;

    @Autowired
    private SimpleMailMessage mailMessage;

    @Autowired
    private MailEngine mailEngine;

    @Override
    @Before
    public void onSetUp() {
        super.onSetUp();
        bean = new PasswordHint();
        bean.setUserManager(userManager);
        bean.setMessage(mailMessage);
        bean.setMailEngine(mailEngine);
        bean.setTemplateName("accountCreated.vm");
    }

    @Test
    public void testExecute() throws Exception {
        // start SMTP Server
        Wiser wiser = startWiser(getSmtpPort());

        bean.setUsername("user");
        assertEquals("success", bean.execute());
        assertFalse(bean.hasErrors());

        // verify an account information e-mail was sent
        wiser.stop();
        assertTrue(wiser.getMessages().size() == 1);

        // verify that success messages are in the request
        assertNotNull(bean.getSession().getAttribute("messages"));
    }
}
