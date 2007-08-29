package org.appfuse.webapp.action;

import org.subethamail.wiser.Wiser;
import org.appfuse.service.UserManager;
import org.appfuse.service.RoleManager;
import org.appfuse.service.MailEngine;
import org.springframework.mail.SimpleMailMessage;

public class PasswordHintTest extends BasePageTestCase {
    private PasswordHint bean;

    @Override
    public void onSetUp() throws Exception {
        super.onSetUp();
        bean = new PasswordHint();
        bean.setUserManager((UserManager) applicationContext.getBean("userManager"));
        bean.setMessage((SimpleMailMessage) applicationContext.getBean("mailMessage"));
        bean.setMailEngine((MailEngine) applicationContext.getBean("mailEngine"));
        bean.setTemplateName("accountCreated.vm");
    }

    public void testExecute() throws Exception {
        // start SMTP Server
        Wiser wiser = new Wiser();
        wiser.setPort(2525);
        wiser.start();

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
