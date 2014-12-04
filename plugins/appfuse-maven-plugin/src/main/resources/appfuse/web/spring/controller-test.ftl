<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<#assign identifierType = pojo.getJavaTypeName(pojo.identifierProperty, jdk5)>
package ${basepackage}.webapp.controller;

<#if genericcore>
import ${appfusepackage}.service.GenericManager;
    <#assign managerClass = 'GenericManager'>
<#else>
import ${basepackage}.service.${pojo.shortName}Manager;
    <#assign managerClass = pojo.shortName + 'Manager'>
</#if>
import ${pojo.packageName}.${pojo.shortName};

import ${basepackage}.webapp.controller.BaseControllerTestCase;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ${pojo.shortName}ControllerTest extends BaseControllerTestCase {
    @Autowired
    private ${managerClass} ${pojoNameLower}Manager;
    @Autowired
    private ${pojo.shortName}Controller controller;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/pages/");
        viewResolver.setSuffix(".jsp");

        mockMvc = MockMvcBuilders.standaloneSetup(controller).setViewResolvers(viewResolver).build();
    }

    @Test
    public void testHandleRequest() throws Exception {
        mockMvc.perform(get("/${util.getPluralForWord(pojoNameLower)}"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("${pojoNameLower}List"))
            .andExpect(view().name("${util.getPluralForWord(pojoNameLower)}"));
    }

    @Test
    public void testSearch() throws Exception {
        // regenerate indexes
        ${pojoNameLower}Manager.reindex();

        Map<String,Object> model = mockMvc.perform((get("/${util.getPluralForWord(pojoNameLower)}")).param("q", "*"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("${pojoNameLower}List"))
            .andReturn()
            .getModelAndView()
            .getModel();

        List results = (List) model.get("${pojoNameLower}List");
        assertNotNull(results);
        assertEquals(3, results.size());
    }
}
