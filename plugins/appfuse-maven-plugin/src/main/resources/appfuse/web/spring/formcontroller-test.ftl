<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
package ${basepackage}.webapp.controller;

import ${basepackage}.webapp.controller.BaseControllerTestCase;
import ${pojo.packageName}.${pojo.shortName};
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.web.servlet.ModelAndView;

public class ${pojo.shortName}FormControllerTest extends BaseControllerTestCase {
    private ${pojo.shortName}FormController form;
    private ${pojo.shortName} ${pojoNameLower};
    private MockHttpServletRequest request;

    public void set${pojo.shortName}FormController(${pojo.shortName}FormController form) {
        this.form = form;
    }

    public void testEdit() throws Exception {
        log.debug("testing edit...");
        request = newGet("/${pojoNameLower}form.html");
        request.addParameter("${pojo.identifierProperty.name}", "-1");

        ${pojoNameLower} = form.showForm(request);
        assertNotNull(${pojoNameLower});
    }

    public void testSave() throws Exception {
        request = newGet("/${pojoNameLower}form.html");
        request.addParameter("${pojo.identifierProperty.name}", "-1");

        ${pojoNameLower} = form.showForm(request);
        assertNotNull(${pojoNameLower});

        request = newPost("/${pojoNameLower}form.html");

        ${pojoNameLower} = form.showForm(request);
        // update required fields
<#foreach field in pojo.getAllPropertiesIterator()>
    <#foreach column in field.getColumnIterator()>
        <#if !field.equals(pojo.identifierProperty) && !column.nullable && !c2h.isCollection(field) && !c2h.isManyToOne(field) && !c2j.isComponent(field)>
            <#lt/>        ${pojoNameLower}.set${pojo.getPropertyName(field)}(${data.getValueForJavaTest(column)});
        </#if>
    </#foreach>
</#foreach>

        BindingResult errors = new DataBinder(${pojoNameLower}).getBindingResult();
        form.onSubmit(${pojoNameLower}, errors, request, new MockHttpServletResponse());
        assertFalse(errors.hasErrors());
        assertNotNull(request.getSession().getAttribute("successMessages"));
    }

    public void testRemove() throws Exception {
        request = newPost("/${pojoNameLower}form.html");
        request.addParameter("delete", "");
        ${pojoNameLower} = new ${pojo.shortName}();
        ${pojoNameLower}.${pojo.getSetterSignature(pojo.identifierProperty)}(-2L);

        BindingResult errors = new DataBinder(${pojoNameLower}).getBindingResult();
        form.onSubmit(${pojoNameLower}, errors, request, new MockHttpServletResponse());

        assertNotNull(request.getSession().getAttribute("successMessages"));
    }
}