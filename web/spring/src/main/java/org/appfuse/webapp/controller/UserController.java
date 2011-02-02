package org.appfuse.webapp.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.Constants;
import org.appfuse.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


/**
 * Simple class to retrieve a list of users from the database.
 *
 * <p>
 * <a href="UserController.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
@Controller
@RequestMapping("/admin/users*")
public class UserController {
    private transient final Log log = LogFactory.getLog(UserController.class);
    private UserManager mgr = null;

    @Autowired
    public void setUserManager(UserManager userManager) {
        this.mgr = userManager;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView handleRequest() throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'handleRequest' method...");
        }

        return new ModelAndView("admin/userList", Constants.USER_LIST, mgr.getUsers());
    }
}
