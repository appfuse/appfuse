package org.appfuse.webapp.controller;

import org.apache.commons.lang.RandomStringUtils;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.subethamail.wiser.Wiser;

import javax.servlet.Filter;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ContextConfiguration(locations = {
    "classpath:/applicationContext-resources.xml",
    "classpath:/applicationContext-dao.xml", "classpath:/applicationContext-service.xml",
    "/WEB-INF/applicationContext*.xml", "/WEB-INF/dispatcher-servlet.xml",
    "/WEB-INF/security.xml"})
@Transactional
@WebAppConfiguration
public class UpdatePasswordControllerTest extends BaseControllerTestCase {
    @Autowired
    private UpdatePasswordController controller;

    @Autowired
    private UserManager userManager;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private Filter springSecurityFilterChain;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testRequestRecoveryToken() throws Exception {
        // start SMTP Server
        Wiser wiser = startWiser(getSmtpPort());

        ResultActions update = mockMvc.perform(get("/requestRecoveryToken").param("username", "admin"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/"));

        // verify an account information e-mail was sent
        wiser.stop();
        assertTrue(wiser.getMessages().size() == 1);

        // verify that success messages are in the session
        MvcResult result = update.andReturn();
        MockHttpSession session = (MockHttpSession) result.getRequest().getSession();
        assertNotNull(session.getAttribute(BaseFormController.MESSAGES_KEY));
    }

    @Test
    public void testShowUpdatePasswordForm() throws Exception {
        mockMvc.perform(get("/updatePassword").param("username", "admin"))
            .andExpect(status().isOk())
            .andExpect(view().name("updatePasswordForm"));
    }

    @Test
    public void testShowResetPasswordForm() throws Exception {
        String username = "admin";
        User user = userManager.getUserByUsername(username);
        String token = userManager.generateRecoveryToken(user);

        ResultActions update = mockMvc.perform(get("/updatePassword")
            .param("username", username).param("token", token))
            .andExpect(status().isOk())
            .andExpect(view().name("updatePasswordForm"));

        MvcResult result = update.andReturn();
        MockHttpSession session = (MockHttpSession) result.getRequest().getSession();
        assertNull(session.getAttribute(BaseFormController.ERRORS_MESSAGES_KEY));
    }

    @Test
    public void testShowResetPasswordFormBadToken() throws Exception {
        String username = "admin";
        String badtoken = RandomStringUtils.random(32);

        ResultActions update = mockMvc.perform(get("/updatePassword")
            .param("username", username).param("token", badtoken))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/"));

        MvcResult result = update.andReturn();
        MockHttpSession session = (MockHttpSession) result.getRequest().getSession();
        assertNotNull(session.getAttribute(BaseFormController.ERRORS_MESSAGES_KEY));
    }

    @Test
    public void testResetPassword() throws Exception {
        String username = "admin";
        User user = userManager.getUserByUsername(username);
        String token = userManager.generateRecoveryToken(user);
        String password = "new-pass";

        Wiser wiser = startWiser(getSmtpPort());

        ResultActions update = mockMvc.perform(post("/updatePassword")
            .param("username", username).param("token", token)
            .param("password", password))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/"));

        wiser.stop();
        assertTrue(wiser.getMessages().size() == 1);

        MvcResult result = update.andReturn();
        MockHttpSession session = (MockHttpSession) result.getRequest().getSession();
        assertNotNull(session.getAttribute(BaseFormController.MESSAGES_KEY));
        assertNull(session.getAttribute(BaseFormController.ERRORS_MESSAGES_KEY));
    }

    @Test
    public void testResetPasswordBadToken() throws Exception {
        String username = "admin";
        String badToken = RandomStringUtils.random(32);
        String password = "new-pass";

        ResultActions update = mockMvc.perform(get("/updatePassword")
            .param("username", username).param("token", badToken)
            .param("password", password))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/"));

        MvcResult result = update.andReturn();
        MockHttpSession session = (MockHttpSession) result.getRequest().getSession();
        assertNull(session.getAttribute(BaseFormController.MESSAGES_KEY));
        assertNotNull(session.getAttribute(BaseFormController.ERRORS_MESSAGES_KEY));
    }

    @Test
    public void testUpdatePassword() throws Exception {
        String username = "admin";
        String currentPassword = "admin";
        String password = "new-pass";

        mockMvc = MockMvcBuilders.webAppContextSetup(context)
            .addFilters(springSecurityFilterChain).build();

        // user must ge logged in
        HttpSession session = mockMvc.perform(post("/j_security_check")
            .param("j_username", "admin").param("j_password", "admin"))
            .andExpect(status().is(HttpStatus.FOUND.value()))
            .andExpect(redirectedUrl("/"))
            .andReturn()
            .getRequest()
            .getSession();

        mockMvc.perform(post("/updatePassword").session((MockHttpSession) session)
            .param("username", username)
            .param("currentPassword", currentPassword)
            .param("password", password))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/"));

        assertNotNull(session.getAttribute(BaseFormController.MESSAGES_KEY));
        assertNull(session.getAttribute(BaseFormController.ERRORS_MESSAGES_KEY));
    }

    @Test
    public void testUpdatePasswordBadCurrentPassword() throws Exception {
        String username = "admin";
        String currentPassword = "bad";
        String password = "new-pass";

        mockMvc = MockMvcBuilders.webAppContextSetup(context)
            .addFilters(springSecurityFilterChain).build();

        // user must ge logged in
        HttpSession session = mockMvc.perform(post("/j_security_check")
            .param("j_username", "admin").param("j_password", "admin"))
            .andExpect(status().is(HttpStatus.FOUND.value()))
            .andExpect(redirectedUrl("/"))
            .andReturn()
            .getRequest()
            .getSession();

        mockMvc.perform(post("/updatePassword").session((MockHttpSession) session)
            .param("username", username)
            .param("currentPassword", currentPassword)
            .param("password", password))
            .andExpect(status().isOk());

        assertNull(session.getAttribute(BaseFormController.MESSAGES_KEY));
        assertNotNull(session.getAttribute(BaseFormController.ERRORS_MESSAGES_KEY));
    }
}
