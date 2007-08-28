<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
package ${basepackage}.webapp.controller;

import ${appfusepackage}.webapp.controller.BaseControllerTestCase;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

public class ${pojo.shortName}ControllerTest extends BaseControllerTestCase {
    private ${pojo.shortName}Controller controller;

    public void set${pojo.shortName}Controller(${pojo.shortName}Controller controller) {
        this.controller = controller;
    }

    public void testHandleRequest() throws Exception {
        ModelAndView mav = controller.handleRequest(null, null);
        ModelMap m = mav.getModelMap();
        assertNotNull(m.get("${pojoNameLower}List"));
        assertTrue(((List) m.get("${pojoNameLower}List")).size() > 0);
    }
}