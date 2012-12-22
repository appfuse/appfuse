<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<#assign identifierType = pojo.getJavaTypeName(pojo.identifierProperty, jdk5)>
package ${basepackage}.webapp.controller;

<#if genericcore>
import ${appfusepackage}.service.GenericManager;
<#else>
import ${basepackage}.service.${pojo.shortName}Manager;
</#if>
import ${pojo.packageName}.${pojo.shortName};

import ${basepackage}.webapp.controller.BaseControllerTestCase;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import static org.junit.Assert.*;

public class ${pojo.shortName}ControllerTest extends BaseControllerTestCase {
    @Autowired
    private ${pojo.shortName}Controller controller;

    @Test
    public void testHandleRequest() throws Exception {
        Model model = controller.handleRequest(null);
        Map m = model.asMap();
        assertNotNull(m.get("${pojoNameLower}List"));
        assertTrue(((List) m.get("${pojoNameLower}List")).size() > 0);
    }

    @Test
    public void testSearch() throws Exception {
        // regenerate indexes
<#if genericcore>
        GenericManager<${pojo.shortName}, ${identifierType}> ${pojoNameLower}Manager = (GenericManager<${pojo.shortName}, ${identifierType}>) applicationContext.getBean(${'"'}${pojoNameLower}Manager${'"'});
<#else>
        ${pojo.shortName}Manager ${pojoNameLower}Manager = (${pojo.shortName}Manager) applicationContext.getBean(${'"'}${pojoNameLower}Manager${'"'});
</#if>
        ${pojoNameLower}Manager.reindex();

        Model model = controller.handleRequest("*");
        Map m = model.asMap();
        List results = (List) m.get("${pojoNameLower}List");
        assertNotNull(results);
        assertEquals(3, results.size());
    }
}