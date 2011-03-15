<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
package ${basepackage}.webapp.controller;

import ${basepackage}.webapp.controller.BaseControllerTestCase;
import org.compass.gps.CompassGps;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public class ${pojo.shortName}ControllerTest extends BaseControllerTestCase {
    @Autowired
    private CompassGps compassGps;
    @Autowired
    private ${pojo.shortName}Controller controller;

    public void testHandleRequest() throws Exception {
        ModelAndView mav = controller.handleRequest(null);
        ModelMap m = mav.getModelMap();
        assertNotNull(m.get("${pojoNameLower}List"));
        assertTrue(((List) m.get("${pojoNameLower}List")).size() > 0);
    }

    public void testSearch() throws Exception {
        compassGps.index();
        ModelAndView mav = controller.handleRequest("*");
        Map m = mav.getModel();
        List results = (List) m.get("${pojoNameLower}List");
        assertNotNull(results);
        assertTrue(results.size() == 3);
    }
}