package org.appfuse.webapp.pages;

import org.appfuse.Constants;
import org.appfuse.model.Address;
import org.appfuse.model.User;
import org.subethamail.wiser.Wiser;
import org.springframework.security.context.SecurityContextHolder;

import java.util.HashMap;
import java.util.Map;

//public class SignupFormTest extends BasePageTestCase {
//    private Signup page;
//
//    @Override
//    protected void onSetUpBeforeTransaction() throws Exception {
//        super.onSetUpBeforeTransaction();
//        // these can be mocked if you want a more "pure" unit test
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("userManager", applicationContext.getBean("userManager"));
//        map.put("roleManager", applicationContext.getBean("roleManager"));
//        map.put("mailMessage", applicationContext.getBean("mailMessage"));
//        map.put("mailEngine", applicationContext.getBean("mailEngine"));
//        page = (Signup) getPage(Signup.class, map);
//    }
//
//    @Override
//    protected void onTearDownAfterTransaction() throws Exception {
//        super.onTearDownAfterTransaction();
//        page = null;
//    }
//
//    public void testExecute() throws Exception {
//        User user = new User();
//        user.setUsername("self-registered");
//        user.setPassword("Password1");
//        user.setConfirmPassword("Password1");
//        user.setFirstName("First");
//        user.setLastName("Last");
//
//        Address address = new Address();
//        address.setCity("Denver");
//        address.setProvince("CO");
//        address.setCountry("USA");
//        address.setPostalCode("80210");
//        user.setAddress(address);
//
//        user.setEmail("self-registered@raibledesigns.com");
//        user.setWebsite("http://raibledesigns.com");
//        user.setPasswordHint("Password is one with you.");
//        page.setUser(user);
//
//        // start SMTP Server
//        Wiser wiser = new Wiser();
//        wiser.setPort(getSmtpPort());
//        wiser.start();
//
//        page.save(new MockRequestCycle());
//
//        assertFalse(page.hasErrors());
//
//        // verify an account information e-mail was sent
//        wiser.stop();
//        assertTrue(wiser.getMessages().size() == 1);
//
//        // verify that success messages are in the session
//        assertNotNull(page.getSession().getAttribute(Constants.REGISTERED));
//
//        SecurityContextHolder.getContext().setAuthentication(null);
//    }
//}



import org.apache.tapestry5.dom.Document;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.test.AbstractIntegrationTestSuite;
import org.apache.tapestry5.test.PageTester;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;

public class SignupTest extends BasePageTester {
	
	public SignupTest() {
		super();
	//	doc = tester.renderPage("Signup");
	}

    
// //   @Test
//    public void integration_test() throws Exception {
//        open(BASE_URL);
//
//        type("input", "paris in the springtime");
//        clickAndWait("//input[@value='Convert']");
//
//        assertFieldValue("input", "PARIS IN THE SPRINGTIME");
//    }
//
//    @Test
//    public void access_to_spring_context() throws Exception
//    {
//        open(BASE_URL);
//
//        assertTextPresent("[upcase]");
//    }
    
	@Test
	public void test_click_cancel() {
	//	Element link = doc.getElementById("cancel");
	//	doc = tester.clickLink(link);
	//	assertTrue(doc.toString().contains("Login Page"));		
	}
	
	@Test
	public void test_click_signup() {
//		Document doc = tester.renderPage("Signup");
//		Element form = doc.getElementById("signup");
		// Setup form
//	   fieldValues.put("username", "self-registered");
//       fieldValues.put("password", "Password1");
//       fieldValues.put("confirmPassword", "Password1");
//       fieldValues.put("firstName", "First");
//       fieldValues.put("lastName", "Last");
//
//       fieldValues.put("email", "self-registered@raibledesigns.com");
//       fieldValues.put("website", "http://raibledesigns.com");
//       fieldValues.put("passwordHint", "Password is one with you.");
//   
//       fieldValues.put("city", "Denver");
//       fieldValues.put("province","CO");
//       fieldValues.put("country", "USA");
//       fieldValues.put("postalCode", "80210");
//
//       doc = tester.submitForm(form, fieldValues);
//       assertTrue(doc.toString().contains("success"));

     
	}
    
}