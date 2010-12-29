package org.appfuse.webapp.controller;

import org.appfuse.Constants;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

public class UserControllerTest extends BaseControllerTestCase {
    UserController c = null;

    public void setUserController(UserController controller) {
        this.c = controller;
    }

    public void testHandleRequest() throws Exception {
        ModelAndView mav = c.handleRequest();
        Map m = mav.getModel();
        assertNotNull(m.get(Constants.USER_LIST));
        assertEquals("admin/userList", mav.getViewName());
    }
}
