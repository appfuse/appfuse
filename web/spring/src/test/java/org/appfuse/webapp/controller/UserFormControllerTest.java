package org.appfuse.webapp.controller;

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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.servlet.Filter;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ContextConfiguration(locations = {
    "classpath:/applicationContext-resources.xml",
    "classpath:/applicationContext-dao.xml", "classpath:/applicationContext-service.xml",
    "/WEB-INF/applicationContext*.xml", "/WEB-INF/dispatcher-servlet.xml", "/WEB-INF/security.xml"})
@Transactional
@WebAppConfiguration
public class UserFormControllerTest extends BaseControllerTestCase {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private Filter springSecurityFilterChain;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
            .addFilters(springSecurityFilterChain).build();
    }

    @Test
    public void testAdd() throws Exception {
        log.debug("testing add new user...");

        HttpSession session = mockMvc.perform(post("/j_security_check")
            .param("j_username", "admin").param("j_password", "admin"))
            .andExpect(status().is(HttpStatus.FOUND.value()))
            .andExpect(redirectedUrl("/"))
            .andReturn()
            .getRequest()
            .getSession();

        mockMvc.perform((get("/userform").session((MockHttpSession) session))
            .param("method", "Add"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("user"))
            .andExpect(model().attribute("user", hasProperty("username", nullValue())));
    }

    @Test
    public void testAddWithoutPermission() throws Exception {
        log.debug("testing add new user...");

        HttpSession session = mockMvc.perform(post("/j_security_check")
            .param("j_username", "user").param("j_password", "user"))
            .andExpect(status().is(HttpStatus.FOUND.value()))
            .andExpect(redirectedUrl("/"))
            .andReturn()
            .getRequest()
            .getSession();

        mockMvc.perform((get("/userform").session((MockHttpSession) session))
            .param("method", "Add"))
            .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    public void testCancel() throws Exception {
        log.debug("testing cancel...");

        mockMvc.perform((post("/userform")
            .param("cancel", "")))
            .andExpect(redirectedUrl("/home"));
    }

    @Test
    public void testEdit() throws Exception {
        log.debug("testing edit...");

        HttpSession session = mockMvc.perform(post("/j_security_check")
            .param("j_username", "admin").param("j_password", "admin"))
            .andExpect(status().is(HttpStatus.FOUND.value()))
            .andExpect(redirectedUrl("/"))
            .andReturn()
            .getRequest()
            .getSession();

        mockMvc.perform((get("/userform").session((MockHttpSession) session)
            .param("id", "-1")))
            .andExpect(model().attributeExists("user"))
            .andExpect(model().attribute("user", hasProperty("fullName", is("Tomcat User"))));
    }

    @Test
    public void testEditWithoutPermission() throws Exception {
        log.debug("testing edit...");

        HttpSession session = mockMvc.perform(post("/j_security_check")
            .param("j_username", "user").param("j_password", "user"))
            .andExpect(status().is(HttpStatus.FOUND.value()))
            .andExpect(redirectedUrl("/"))
            .andReturn()
            .getRequest()
            .getSession();

        mockMvc.perform((get("/userform").session((MockHttpSession) session))
            .param("id", "-1"))
            .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    public void testEditProfile() throws Exception {
        log.debug("testing edit profile...");

        HttpSession session = mockMvc.perform(post("/j_security_check")
            .param("j_username", "user").param("j_password", "user"))
            .andExpect(status().is(HttpStatus.FOUND.value()))
            .andExpect(redirectedUrl("/"))
            .andReturn()
            .getRequest()
            .getSession();

        mockMvc.perform((get("/userform").session((MockHttpSession) session)))
            .andExpect(status().isOk())
            .andExpect(model().attribute("user", hasProperty("fullName", is("Tomcat User"))));
    }

    @Test
    public void testSave() throws Exception {
        // set updated properties first since adding them later will
        // result in multiple parameters with the same name getting sent
        User user = ((UserManager) context.getBean("userManager")).getUser("-1");
        user.setConfirmPassword(user.getPassword());
        user.setLastName("Updated Last Name");

        HttpSession session = mockMvc.perform(post("/j_security_check")
            .param("j_username", "user").param("j_password", "user"))
            .andExpect(status().is(HttpStatus.FOUND.value()))
            .andExpect(redirectedUrl("/"))
            .andReturn()
            .getRequest()
            .getSession();

        mockMvc.perform((post("/userform")
            .param("id", user.getId().toString())
            .param("username", user.getUsername())
            .param("firstName", user.getFirstName())
            .param("lastName", user.getLastName())
            .param("email", user.getEmail())
            .param("passwordHint", user.getPasswordHint())
            .session((MockHttpSession) session)))
            .andExpect(status().is3xxRedirection())
            .andExpect(model().hasNoErrors());
    }

    @Test
    public void testAddWithMissingFields() throws Exception {
        mockMvc.perform((post("/userform").param("firstName", "Jack")))
            .andExpect(status().isOk())
            .andExpect(model().hasErrors())
            .andExpect(model().errorCount(4));
    }

    @Test
    public void testRemove() throws Exception {
        HttpSession session = mockMvc.perform(post("/j_security_check")
            .param("j_username", "admin").param("j_password", "admin"))
            .andExpect(status().is(HttpStatus.FOUND.value()))
            .andExpect(redirectedUrl("/"))
            .andReturn()
            .getRequest()
            .getSession();

        session = mockMvc.perform((post("/userform"))
            .session((MockHttpSession) session)
            .param("delete", "").param("id", "-2"))
            .andExpect(status().is3xxRedirection())
            .andExpect(model().hasNoErrors())
            .andReturn().getRequest().getSession();

        assertNotNull(session.getAttribute("successMessages"));
    }
}
