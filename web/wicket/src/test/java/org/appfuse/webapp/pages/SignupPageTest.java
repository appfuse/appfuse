package org.appfuse.webapp.pages;

import de.agilecoders.wicket.markup.html.bootstrap.common.NotificationMessage;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.util.tester.FormTester;
import org.appfuse.model.User;
import org.appfuse.service.MailEngine;
import org.appfuse.service.RoleManager;
import org.appfuse.service.UserExistsException;
import org.appfuse.service.UserManager;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.web.context.support.StaticWebApplicationContext;

import java.io.Serializable;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * org.appfuse.webapp.pages.SignupPageTest
 *
 * @author Marcin Zajączkowski, 2011-06-19
 */
public class SignupPageTest extends BasePageTest {

    private static final String TEST_USERNAME = "Username";
    private static final String TEST_PASSWORD = "Password";
    private static final String TEST_CONFIRM_PASSWORD = "Password";
    private static final String TEST_PASSWORD_HINT = "Password hint";
    private static final String TEST_FIRST_NAME = "First name";
    private static final String TEST_LAST_NAME = "Last name";
    private static final String TEST_EMAIL = "email@example.com";
    private static final String TEST_WEBSITE = "website";
    private static final String TEST_CITY = "City";
    private static final String TEST_PROVINCE = "Province";
    private static final String TEST_POSTAL_CODE = "Postal code";
    private static final String TEST_PHONE_NUMBER = "Phone number";
    private static final String TEST_ADDRESS_STREET = "Address";

    @Mock
    private RoleManager roleManager;
    @Mock
    private MailEngine mailEngine;
    @Mock
    private UserManager userManager;

    @Override
    protected void initSpringBeans(StaticWebApplicationContext context) {
        super.initSpringBeans(context);

        context.getBeanFactory().registerSingleton("roleManager", roleManager);
        context.getBeanFactory().registerSingleton("mailEngine", mailEngine);
        context.getBeanFactory().registerSingleton("userManager", userManager);
    }

    @Test
    public void shouldHaveComponentUsingOnRegisteredUsedEditHidden() {
        //given

        //when
        tester.startPage(Signup.class);

        //then
        tester.assertRenderedPage(Signup.class);
        tester.assertInvisible("userEditForm:userEditPanel:buttonsGroup:deleteButton");
        tester.assertInvisible("userEditForm:userEditPanel:accountSettingsGroup");
        tester.assertInvisible("userEditForm:userEditPanel:displayRolesGroup");
    }

    @Test
    public void shouldPassValidationAndRedirectToLoginPageWhenAllRequiredFieldsAreFilled() throws UserExistsException {
        //given
        given(userManager.saveUser(Matchers.<User>anyObject())).willAnswer(createAnswerThatJustReturnPassedUser());
        goToPageAndAssertIfRendered(Signup.class);
        FormTester signupForm = tester.newFormTester("userEditForm");

        //when
        fillRequiredFields(signupForm);
        signupForm.submit("userEditPanel:buttonsGroup:saveButton");

        //then
        tester.assertNoErrorMessage();
        tester.assertRenderedPage(Login.class);
    }

    @Test
    @Ignore //Not implemented - https://agilezen.com/project/23613/story/13
    public void shouldReportErrorWhenPasswordAndConfirmPasswordMismatch() {
        //given
        goToPageAndAssertIfRendered(Signup.class);
        FormTester signupForm = tester.newFormTester("userEditForm");

        //when
        fillRequiredFields(signupForm);
        signupForm.setValue("userEditPanel:passwordGroup:confirmPassword", "mismatch");
        signupForm.submit("userEditPanel:buttonsGroup:saveButton");

        //then
        assertRenderedLoginPageWithErrorMessages(Signup.class, "<<some new message>>");
    }

    @Test
    public void shouldSaveUserWithEnteredValues() throws UserExistsException {
        //given
        given(userManager.saveUser(Matchers.<User>anyObject())).willAnswer(createAnswerThatJustReturnPassedUser());
        goToPageAndAssertIfRendered(Signup.class);
        FormTester signupForm = tester.newFormTester("userEditForm");

        //when
        fillRequiredFields(signupForm);
        fillOptionalFields(signupForm);
        signupForm.submit("userEditPanel:buttonsGroup:saveButton");

        //then
        tester.assertNoErrorMessage();
//        Note: there is an additional space at the end of a setence in *.properties file
        assertInfoMessage("You have successfully registered for access to this application. ");

        tester.assertRenderedPage(Login.class);
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userManager).saveUser(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        verifyEqualityOnRequiredFields(savedUser);
        verifyEqualityOnOptionalFields(savedUser);
//        //Temporarily disabled in code to prevent sending spam
//        verify(mailEngine).sendMessage(...);
    }

    private void assertInfoMessage(String expectedMessage) {
//        //assertInfoMessages cannot be used with NotificationMessage messages - a quick hack can be made more generic
//        tester.assertInfoMessages("You have successfully registered for access to this application.");
        List<Serializable> actualMessages = tester.getMessages(FeedbackMessage.INFO);
        assertEquals(1, actualMessages.size());
        assertEquals(NotificationMessage.class, actualMessages.get(0).getClass());
        NotificationMessage actualMessage = (NotificationMessage) actualMessages.get(0);
        assertEquals(expectedMessage, actualMessage.message().getObject());
    }

    private Answer<User> createAnswerThatJustReturnPassedUser() {
        return new Answer<User>() {
            public User answer(InvocationOnMock invocation) throws Throwable {
                return (User)invocation.getArguments()[0];
            }
        };
    }

    private void fillRequiredFields(FormTester signupForm) {
        signupForm.setValue("userEditPanel:username", TEST_USERNAME);
        signupForm.setValue("userEditPanel:passwordGroup:password", TEST_PASSWORD);
        signupForm.setValue("userEditPanel:passwordGroup:confirmPassword", TEST_CONFIRM_PASSWORD);
        signupForm.setValue("userEditPanel:passwordHint", TEST_PASSWORD_HINT);
        signupForm.setValue("userEditPanel:firstName", TEST_FIRST_NAME);
        signupForm.setValue("userEditPanel:lastName", TEST_LAST_NAME);
        signupForm.setValue("userEditPanel:email", TEST_EMAIL);
        signupForm.setValue("userEditPanel:website", TEST_WEBSITE);

        String addressFormIdPrefix = "userEditPanel:collapsibleAddress:tabs:0:body:content:";
        signupForm.setValue(addressFormIdPrefix + "city", TEST_CITY);
        signupForm.setValue(addressFormIdPrefix + "province", TEST_PROVINCE);
        signupForm.setValue(addressFormIdPrefix + "postalCode", TEST_POSTAL_CODE);
        signupForm.select(addressFormIdPrefix + "country", 1);
    }

    private void fillOptionalFields(FormTester signupForm) {
        signupForm.setValue("userEditPanel:phoneNumber", TEST_PHONE_NUMBER);
        signupForm.setValue("userEditPanel:collapsibleAddress:tabs:0:body:content:" + "address", TEST_ADDRESS_STREET);
    }

    private void verifyEqualityOnRequiredFields(User savedUser) {
        assertEquals(TEST_USERNAME, savedUser.getUsername());
        assertEquals(TEST_PASSWORD, savedUser.getPassword());
        assertEquals(TEST_CONFIRM_PASSWORD, savedUser.getConfirmPassword());
        assertEquals(TEST_PASSWORD_HINT, savedUser.getPasswordHint());
        assertEquals(TEST_FIRST_NAME, savedUser.getFirstName());
        assertEquals(TEST_LAST_NAME, savedUser.getLastName());
        assertEquals(TEST_EMAIL, savedUser.getEmail());
        assertEquals(TEST_WEBSITE, savedUser.getWebsite());
        assertNotNull("address", savedUser.getAddress());
        assertEquals(TEST_CITY, savedUser.getAddress().getCity());
        assertEquals(TEST_PROVINCE, savedUser.getAddress().getProvince());
        assertEquals(TEST_POSTAL_CODE, savedUser.getAddress().getPostalCode());
        //TODO: MZA: Currently taken from UserEditPanel - change to new value when read from database
        assertEquals("Poland", savedUser.getAddress().getCountry());
    }

    private void verifyEqualityOnOptionalFields(User savedUser) {
        assertEquals(TEST_PHONE_NUMBER, savedUser.getPhoneNumber());
        assertEquals(TEST_ADDRESS_STREET, savedUser.getAddress().getAddress());
    }
}
