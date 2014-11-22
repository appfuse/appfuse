package org.appfuse.webapp.action;

import org.appfuse.service.MailEngine;
import org.appfuse.service.UserManager;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mail.SimpleMailMessage;
import org.subethamail.wiser.Wiser;

import static org.junit.Assert.*;


public class PasswordHintTest extends BasePageTestCase {
    private PasswordHint bean;

    @Override
    @Before
    public void onSetUp() {
        super.onSetUp();
        bean = new PasswordHint();
        bean.setUserManager((UserManager) applicationContext.getBean("userManager"));
        bean.setMessage((SimpleMailMessage) applicationContext.getBean("mailMessage"));
        bean.setMailEngine((MailEngine) applicationContext.getBean("mailEngine"));
        bean.setTemplateName("accountCreated.vm");
    }

    @Test
    public void testExecute() throws Exception {
        // start SMTP Server
        Wiser wiser = new Wiser();
        wiser.setPort(getSmtpPort());
        try {
            wiser.start();
        } catch (RuntimeException re) {
            // address already in use, try different port
            wiser.setPort(getSmtpPort() + (int) (Math.random() * 100));
            wiser.start();
        }

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
