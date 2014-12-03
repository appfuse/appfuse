package org.appfuse.webapp.controller;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.subethamail.wiser.Wiser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PasswordHintControllerTest extends BaseControllerTestCase {
    @Autowired
    private PasswordHintController controller;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testExecute() throws Exception {
       // start SMTP Server
        Wiser wiser = startWiser(getSmtpPort());

        ResultActions actions = mockMvc.perform(get("/passwordHint.html").param("username", "user"))
            .andExpect(status().is3xxRedirection());

        MvcResult result = actions.andReturn();
        MockHttpSession session = (MockHttpSession) result.getRequest().getSession();
        // verify that success messages are in the session
        assertNotNull(session.getAttribute(BaseFormController.MESSAGES_KEY));

        // verify an account information e-mail was sent
        wiser.stop();
        assertTrue(wiser.getMessages().size() == 1);
    }
}
