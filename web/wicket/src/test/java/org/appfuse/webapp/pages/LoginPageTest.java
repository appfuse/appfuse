package org.appfuse.webapp.pages;

import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.appfuse.webapp.TestWicketApplication;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Locale;

import static org.appfuse.webapp.StaticAuthenticatedWebSession.*;

/**
 * org.appfuse.webapp.pages.LoginPageTest
 *
 * @author Marcin Zajączkowski, 2011-06-18
 */
public class LoginPageTest {

    private static WicketTester tester;

    @BeforeClass
    public static void initClass() {
        ApplicationContext emptyTestContext = new ClassPathXmlApplicationContext();
        tester = new WicketTester(new TestWicketApplication(emptyTestContext));
        //ensure english locale regardless of local system locale
        tester.setupRequestAndResponse();
        tester.getWicketSession().setLocale(Locale.ENGLISH);
    }

    @Test
    public void shouldForwardToHomePageOnSuccessfulAuthentication() {
        goToLoginPage();

        //when
        submitLoginFormWithUsernameAndPassword(USERNAME_USER, PASSWORD_USER);

        //then
        tester.assertRenderedPage(tester.getApplication().getHomePage());
        tester.assertNoErrorMessage();
    }

    @Test
    public void shouldStayOnLoginPageAndDisplayErrorMessageOnFailedAuthentication() {
        //given
        goToLoginPage();

        //when
        submitLoginFormWithUsernameAndPassword(USERNAME_USER, "invalid");

        //then
        tester.assertRenderedPage(Login.class);
        tester.assertErrorMessages(new String[] {"Invalid username and/or password, please try again."});
    }

    @Test
    public void shouldDisplayCorrectErrorMessageOnMissingUsername() {
        //given
        goToLoginPage();

        //when
        submitLoginFormWithUsernameAndPassword(null, "bar");

        //then
        assertRenderedLoginPageAndErrorMessages(getRequiredErrorMessageByField("username"));
    }

    @Test
    public void shouldDisplayCorrectErrorMessageOnMissingPassword() {
        //given
        goToLoginPage();

        //when
        submitLoginFormWithUsernameAndPassword("foo", null);

        //then
        assertRenderedLoginPageAndErrorMessages(getRequiredErrorMessageByField("password"));
    }

    @Test
    public void shouldCorrectErrorMessageOnMissingUsernameAndPassword() {
        //given
        goToLoginPage();

        //when
        submitLoginFormWithUsernameAndPassword(null, null);

        //then
        assertRenderedLoginPageAndErrorMessages(
                getRequiredErrorMessageByField("username"), getRequiredErrorMessageByField("password"));
    }

    private void goToLoginPage() {
        tester.startPage(Login.class);
        tester.assertRenderedPage(Login.class);
    }

    private void submitLoginFormWithUsernameAndPassword(String username, String password) {
        FormTester loginForm = tester.newFormTester("loginForm");
        loginForm.setValue("border:username", username);
        loginForm.setValue("password", password);
        loginForm.submit();
    }

    private String getRequiredErrorMessageByField(String field) {
        return "Field '" + field + "' is required.";
    }

    private void assertRenderedLoginPageAndErrorMessages(String... errorMessage) {
        tester.assertRenderedPage(Login.class);
        tester.assertErrorMessages(errorMessage);
    }
}
