package org.appfuse.webapp.pages;

import org.appfuse.Constants;
import org.appfuse.model.Address;
import org.appfuse.model.User;
import org.subethamail.wiser.Wiser;

import java.util.HashMap;
import java.util.Map;

public class SignupFormTest extends BasePageTestCase {
    private SignupForm page;

    @Override
    protected void onSetUpBeforeTransaction() throws Exception {
        super.onSetUpBeforeTransaction();
        // these can be mocked if you want a more "pure" unit test
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userManager", applicationContext.getBean("userManager"));
        map.put("roleManager", applicationContext.getBean("roleManager"));
        map.put("mailMessage", applicationContext.getBean("mailMessage"));
        map.put("mailEngine", applicationContext.getBean("mailEngine"));
        page = (SignupForm) getPage(SignupForm.class, map);
    }

    @Override
    protected void onTearDownAfterTransaction() throws Exception {
        super.onTearDownAfterTransaction();
        page = null;
    }

    public void testExecute() throws Exception {
        User user = new User();
        user.setUsername("self-registered");
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
        page.setUser(user);

        // start SMTP Server
        Wiser wiser = new Wiser();
        wiser.setPort(2525);
        wiser.start();

        page.save(new MockRequestCycle());

        assertFalse(page.hasErrors());

        // verify an account information e-mail was sent
        wiser.stop();
        assertTrue(wiser.getMessages().size() == 1);

        // verify that success messages are in the session
        assertNotNull(page.getSession().getAttribute(Constants.REGISTERED));
    }
}
