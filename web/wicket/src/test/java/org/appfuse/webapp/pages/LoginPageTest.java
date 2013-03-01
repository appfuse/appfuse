package org.appfuse.webapp.pages;

import org.apache.wicket.util.tester.FormTester;
import org.junit.Test;

import static org.appfuse.webapp.StaticAuthenticatedWebSession.*;

/**
 * org.appfuse.webapp.pages.LoginPageTest
 *
 * @author Marcin Zajączkowski, 2011-06-18
 */
public class LoginPageTest extends BasePageTest {

    @Test
    public void shouldForwardToHomePageOnSuccessfulAuthentication() {
        goToPageAndAssertIfRendered(Login.class);

        //when
        submitLoginFormWithUsernameAndPassword(USERNAME_USER, PASSWORD_USER);

        //then
        tester.assertRenderedPage(tester.getApplication().getHomePage());
        tester.assertNoErrorMessage();
    }

    @Test
    public void shouldStayOnLoginPageAndDisplayErrorMessageOnFailedAuthentication() {
        //given
        goToPageAndAssertIfRendered(Login.class);

        //when
        submitLoginFormWithUsernameAndPassword(USERNAME_USER, "invalid");

        //then
        tester.assertRenderedPage(Login.class);
        tester.assertErrorMessages("Invalid username and/or password, please try again.");
    }

    @Test
    public void shouldDisplayCorrectErrorMessageOnMissingUsername() {
        //given
        goToPageAndAssertIfRendered(Login.class);

        //when
        submitLoginFormWithUsernameAndPassword(null, "bar");

        //then
        assertRenderedLoginPageWithErrorMessages(Login.class, getRequiredErrorMessageByField("username"));
    }

    @Test
    public void shouldDisplayCorrectErrorMessageOnMissingPassword() {
        //given
        goToPageAndAssertIfRendered(Login.class);

        //when
        submitLoginFormWithUsernameAndPassword("foo", null);

        //then
        assertRenderedLoginPageWithErrorMessages(Login.class, getRequiredErrorMessageByField("password"));
    }

    @Test
    public void shouldCorrectErrorMessageOnMissingUsernameAndPassword() {
        //given
        goToPageAndAssertIfRendered(Login.class);

        //when
        submitLoginFormWithUsernameAndPassword(null, null);

        //then
        assertRenderedLoginPageWithErrorMessages(Login.class,
                getRequiredErrorMessageByField("username"), getRequiredErrorMessageByField("password"));
    }

    private void submitLoginFormWithUsernameAndPassword(String username, String password) {
        FormTester loginForm = tester.newFormTester("loginForm");
        //MZA: Strange construction required after upgrade to Wicket 1.5
        loginForm.setValue("border:border_body:username", username);
        loginForm.setValue("password", password);
        loginForm.submit();
    }
}
