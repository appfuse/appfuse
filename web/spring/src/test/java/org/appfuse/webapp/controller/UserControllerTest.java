package org.appfuse.webapp.controller;

import org.appfuse.Constants;
import org.compass.gps.CompassGps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

public class UserControllerTest extends BaseControllerTestCase {
    @Autowired
    private CompassGps compassGps;
    @Autowired
    private UserController c;

    public void testHandleRequest() throws Exception {
        ModelAndView mav = c.handleRequest(null);
        Map m = mav.getModel();
        assertNotNull(m.get(Constants.USER_LIST));
        assertEquals("admin/userList", mav.getViewName());
    }

    public void testSearch() throws Exception {
        compassGps.index();
        ModelAndView mav = c.handleRequest("admin");
        Map m = mav.getModel();
        List results = (List) m.get(Constants.USER_LIST);
        assertNotNull(results);
        assertTrue(results.size() >= 1);
        assertEquals("admin/userList", mav.getViewName());
    }
}
