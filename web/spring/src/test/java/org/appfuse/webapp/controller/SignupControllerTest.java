package org.appfuse.webapp.controller;

import org.appfuse.Constants;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.subethamail.wiser.Wiser;

import javax.transaction.Transactional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
public class SignupControllerTest extends BaseControllerTestCase {
    @Autowired
    private SignupController controller;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testDisplayForm() throws Exception {
        mockMvc.perform(get("/signup.html"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("user"));
    }

    @Test
    public void testSignupUser() throws Exception {
        // start SMTP Server
        Wiser wiser = startWiser(getSmtpPort());

        ResultActions signup = mockMvc.perform(post("/signup.html")
                .param("address.city", "Denver")
                .param("address.province", "Colorado")
                .param("address.country", "USA")
                .param("address.postalCode", "80210")
                .param("username", "self-registered")
                .param("password", "Password1")
                .param("confirmPassword", "Password1")
                .param("firstName", "First")
                .param("lastName", "Last")
                .param("email", "self-registered@raibledesigns.com")
                .param("website", "http://raibledesigns.com")
                .param("passwordHint", "Password is one with you.")
        )
            .andExpect(status().is3xxRedirection())
            .andExpect(model().hasNoErrors());

        // verify an account information e-mail was sent
        wiser.stop();
        assertTrue(wiser.getMessages().size() == 1);

        MvcResult result = signup.andReturn();
        MockHttpSession session = (MockHttpSession) result.getRequest().getSession();

        // verify that success messages are in the session
        assertNotNull(session.getAttribute("successMessages"));
        assertNotNull(session.getAttribute(Constants.REGISTERED));

        SecurityContextHolder.getContext().setAuthentication(null);
    }
}
