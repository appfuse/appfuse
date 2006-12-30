package org.appfuse.webapp.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.appfuse.Constants;
import org.springframework.web.servlet.ModelAndView;

public class UserControllerTest extends BaseControllerTestCase {

    public void testHandleRequest() throws Exception {
        UserController c = (UserController) applicationContext.getBean("userController");
        ModelAndView mav = c.handleRequest(null, null);
        Map m = mav.getModel();
        assertNotNull(m.get(Constants.USER_LIST));
        assertEquals("userList", mav.getViewName());
    }
}
