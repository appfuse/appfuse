<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<#assign setIdMethodName = 'set' + pojo.getPropertyName(pojo.identifierProperty)>
package ${basepackage}.webapp.controller;

import ${basepackage}.webapp.controller.BaseControllerTestCase;
import ${pojo.packageName}.${pojo.shortName};
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.beans.factory.annotation.Autowired;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ${pojo.shortName}FormControllerTest extends BaseControllerTestCase {
    @Autowired
    private ${pojo.shortName}FormController form;
    private ${pojo.shortName} ${pojoNameLower};
    private MockHttpServletRequest request;

    @Test
    public void testEdit() throws Exception {
        log.debug("testing edit...");
        request = newGet("/${pojoNameLower}form");
        request.addParameter("${pojo.identifierProperty.name}", "-1");

        ${pojoNameLower} = form.showForm(request);
        assertNotNull(${pojoNameLower});
    }

    @Test
    public void testSave() throws Exception {
        request = newGet("/${pojoNameLower}form");
        request.addParameter("${pojo.identifierProperty.name}", "-1");

        ${pojoNameLower} = form.showForm(request);
        assertNotNull(${pojoNameLower});

        request = newPost("/${pojoNameLower}form");

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

    @Test
    public void testRemove() throws Exception {
        request = newPost("/${pojoNameLower}form");
        request.addParameter("delete", "");
        ${pojoNameLower} = new ${pojo.shortName}();
        ${pojoNameLower}.${setIdMethodName}(-2L);

        BindingResult errors = new DataBinder(${pojoNameLower}).getBindingResult();
        form.onSubmit(${pojoNameLower}, errors, request, new MockHttpServletResponse());

        assertNotNull(request.getSession().getAttribute("successMessages"));
    }
}
