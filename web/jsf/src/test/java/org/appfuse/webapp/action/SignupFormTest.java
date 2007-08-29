package org.appfuse.webapp.action;

import org.appfuse.Constants;
import org.appfuse.service.UserManager;
import org.appfuse.service.RoleManager;
import org.appfuse.service.MailEngine;
import org.appfuse.model.Address;
import org.appfuse.model.User;

import org.subethamail.wiser.Wiser;
import org.acegisecurity.context.SecurityContextHolder;
import org.springframework.mail.SimpleMailMessage;

public class SignupFormTest extends BasePageTestCase {
    private SignupForm bean;

    @Override
    public void onSetUp() throws Exception {
        super.onSetUp();
        bean = new SignupForm();
        bean.setUserManager((UserManager) applicationContext.getBean("userManager"));
        bean.setRoleManager((RoleManager) applicationContext.getBean("roleManager"));
        bean.setMessage((SimpleMailMessage) applicationContext.getBean("mailMessage"));
        bean.setMailEngine((MailEngine) applicationContext.getBean("mailEngine"));
        bean.setTemplateName("accountCreated.vm");
    }

    public void testExecute() throws Exception {
        User user = new User("self-registered");
        user.setPassword("Password1");
        user.setConfirmPassword("Password1");
        user.setFirstName("First");
        user.setLastName("Last");

        Address address = new Address();
        address.setCity("Denver");
        address.setProvince("CO");
        address.setCountry("USA");
        address.setPostalCode("80210");
        user.setAddress(address);

        user.setEmail("self-registered@raibledesigns.com");
        user.setWebsite("http://raibledesigns.com");
        user.setPasswordHint("Password is one with you.");
        bean.setUser(user);

       // start SMTP Server
        Wiser wiser = new Wiser();
        wiser.setPort(2525);
        wiser.start();

        assertEquals("mainMenu", bean.save());
        assertFalse(bean.hasErrors());

        // verify an account information e-mail was sent
        wiser.stop();
        assertTrue(wiser.getMessages().size() == 1);

        // verify that success messages are in the session
        assertNotNull(bean.getSession().getAttribute(Constants.REGISTERED));

        SecurityContextHolder.getContext().setAuthentication(null);
    }
}
