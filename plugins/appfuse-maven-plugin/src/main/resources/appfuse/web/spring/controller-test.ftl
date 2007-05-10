package ${basepackage}.webapp.controller;

import org.appfuse.webapp.controller.BaseControllerTestCase;
import ${basepackage}.model.${pojo.shortName};
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

public class ${pojo.shortName}FormControllerTest extends BaseControllerTestCase {
    private ${pojo.shortName}FormController c;

    public void set${pojo.shortName}FormController(${pojo.shortName}FormController c) {
        this.c = c;
    }

    public void testEdit() throws Exception {
        log.debug("testing edit...");
        MockHttpServletRequest request = newGet("/${pojo.shortName.toLowerCase()}form.html");
        request.addParameter("id", "1");

        ModelAndView mv = c.handleRequest(request, new MockHttpServletResponse());

        ${pojo.shortName} ${pojo.shortName.toLowerCase()} = (${pojo.shortName}) mv.getModel().get(c.getCommandName());
        assertNotNull(${pojo.shortName.toLowerCase()});
    }

    public void testSave() throws Exception {
        MockHttpServletRequest request = newGet("/${pojo.shortName.toLowerCase()}form.html");
        request.addParameter("id", "1");

        ModelAndView mv = c.handleRequest(request, new MockHttpServletResponse());

        ${pojo.shortName} ${pojo.shortName.toLowerCase()} = (${pojo.shortName}) mv.getModel().get(c.getCommandName());
        assertNotNull(${pojo.shortName.toLowerCase()});

        request = newPost("/${pojo.shortName.toLowerCase()}form.html");
        super.objectToRequestParameters(${pojo.shortName.toLowerCase()}, request);
        request.addParameter("lastName", "Updated Last Name");

        mv = c.handleRequest(request, new MockHttpServletResponse());

        Errors errors = (Errors) mv.getModel().get(BindException.MODEL_KEY_PREFIX + "${pojo.shortName.toLowerCase()}");
        assertNull(errors);
        assertNotNull(request.getSession().getAttribute("successMessages"));
    }

    public void testRemove() throws Exception {
        MockHttpServletRequest request = newPost("/${pojo.shortName.toLowerCase()}form.html");
        request.addParameter("delete", "");
        request.addParameter("id", "2");

        c.handleRequest(request, new MockHttpServletResponse());

        assertNotNull(request.getSession().getAttribute("successMessages"));
    }
}