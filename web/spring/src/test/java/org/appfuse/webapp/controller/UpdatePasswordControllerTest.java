package org.appfuse.webapp.controller;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.RandomStringUtils;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.servlet.ModelAndView;
import org.subethamail.wiser.Wiser;

@ContextConfiguration(locations = {
 "classpath:/applicationContext-resources.xml",
        "classpath:/applicationContext-dao.xml", "classpath:/applicationContext-service.xml",
        "classpath*:/applicationContext.xml", // for modular archetypes
        "/WEB-INF/applicationContext*.xml", "/WEB-INF/dispatcher-servlet.xml", "/WEB-INF/security.xml" })
public class UpdatePasswordControllerTest extends BaseControllerTestCase {
    @Autowired
    private UpdatePasswordController controller;

    @Autowired
    private UserManager userManager;

    @Test
    public void testRequestRecoveryToken() throws Exception {
        String username = "admin";
        MockHttpServletRequest request = newGet("/updatePassword");
        request.addParameter("username", username);

        // start SMTP Server
        Wiser wiser = new Wiser();
        wiser.setPort(getSmtpPort());
        wiser.start();

        controller.requestRecoveryToken(username, request);

        // verify an account information e-mail was sent
        wiser.stop();
        assertTrue(wiser.getMessages().size() == 1);

        // verify that success messages are in the session
        assertNotNull(request.getSession().getAttribute(BaseFormController.MESSAGES_KEY));
    }

    @Test
    public void testShowUpdatePasswordForm() throws Exception {
        String username = "admin";
        MockHttpServletRequest request = newGet("/updatePassword");
        request.addParameter("username", username);

        ModelAndView mav = controller.showForm(username, null, request);

        assertEquals("updatePasswordForm", mav.getViewName());

    }

    @Test
    public void testShowResetPasswordForm() throws Exception {
        String username = "admin";
        User user = userManager.getUserByUsername(username);
        String token = userManager.generateRecoveryToken(user);

        MockHttpServletRequest request = newGet("/updatePassword");
        request.addParameter("username", username);
        request.addParameter("token", token);

        ModelAndView mav = controller.showForm(username, token, request);

        assertEquals("updatePasswordForm", mav.getViewName());
        assertNull(request.getSession().getAttribute(BaseFormController.ERRORS_MESSAGES_KEY));
    }

    @Test
    public void testShowResetPasswordFormBadToken() throws Exception {
        String username = "admin";
        String badtoken = RandomStringUtils.random(32);

        MockHttpServletRequest request = newGet("/updatePassword");
        request.addParameter("username", username);
        request.addParameter("token", badtoken);

        ModelAndView mav = controller.showForm(username, badtoken, request);

        assertNotNull(request.getSession().getAttribute(BaseFormController.ERRORS_MESSAGES_KEY));
    }

    @Test
    public void testResetPassword() throws Exception {
        String username = "admin";
        User user = userManager.getUserByUsername(username);
        String token = userManager.generateRecoveryToken(user);
        String password = "new-pass";

        MockHttpServletRequest request = newGet("/updatePassword");
        request.addParameter("username", username);
        request.addParameter("token", token);
        request.addParameter("password", password);

        Wiser wiser = new Wiser();
        wiser.setPort(getSmtpPort());
        wiser.start();

        ModelAndView mav = controller.onSubmit(username, token, null, password, request);

        wiser.stop();
        assertTrue(wiser.getMessages().size() == 1);

        assertNotNull(request.getSession().getAttribute(BaseFormController.MESSAGES_KEY));
        assertNull(request.getSession().getAttribute(BaseFormController.ERRORS_MESSAGES_KEY));
    }

    @Test
    public void testResetPasswordBadToken() throws Exception {
        String username = "admin";
        String badToken = RandomStringUtils.random(32);
        String password = "new-pass";

        MockHttpServletRequest request = newGet("/updatePassword");
        request.addParameter("username", username);
        request.addParameter("token", badToken);
        request.addParameter("password", password);

        ModelAndView mav = controller.onSubmit(username, badToken, null, password, request);

        assertNull(request.getSession().getAttribute(BaseFormController.MESSAGES_KEY));
        assertNotNull(request.getSession().getAttribute(BaseFormController.ERRORS_MESSAGES_KEY));
    }

    @Test
    public void testUpdatePassword() throws Exception {
        String username = "admin";
        String currentPassword = "admin";
        String password = "new-pass";

        MockHttpServletRequest request = newGet("/updatePassword");
        request.setRemoteUser(username);// user must ge logged in
        request.addParameter("username", username);
        request.addParameter("currentPassword", currentPassword);
        request.addParameter("password", password);

        ModelAndView mav = controller.onSubmit(username, null, currentPassword, password, request);

        assertNotNull(request.getSession().getAttribute(BaseFormController.MESSAGES_KEY));
        assertNull(request.getSession().getAttribute(BaseFormController.ERRORS_MESSAGES_KEY));
    }

    @Test
    public void testUpdatePasswordBadCurrentPassword() throws Exception {
        String username = "admin";
        String currentPassword = "bad";
        String password = "new-pass";

        MockHttpServletRequest request = newGet("/updatePassword");
        request.setRemoteUser(username);// user must ge logged in
        request.addParameter("username", username);
        request.addParameter("currentPassword", currentPassword);
        request.addParameter("password", password);

        ModelAndView mav = controller.onSubmit(username, null, currentPassword, password, request);

        assertNull(request.getSession().getAttribute(BaseFormController.MESSAGES_KEY));
        assertNotNull(request.getSession().getAttribute(BaseFormController.ERRORS_MESSAGES_KEY));
    }
}
