package org.appfuse.webapp.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.Constants;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.compass.core.CompassDetachedHits;
import org.compass.core.CompassHit;
import org.compass.core.CompassTemplate;
import org.compass.core.support.search.CompassSearchCommand;
import org.compass.core.support.search.CompassSearchHelper;
import org.compass.core.support.search.CompassSearchResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;


/**
 * Simple class to retrieve a list of users from the database.
 * <p/>
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
    private CompassSearchHelper compass;

    @Autowired
    public void setUserManager(UserManager userManager) {
        this.mgr = userManager;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView handleRequest(@RequestParam(required = false, value = "q") String query) throws Exception {
        if (query != null && !"".equals(query.trim())) {
            return new ModelAndView("admin/userList", Constants.USER_LIST, search(query));
        } else {
            return new ModelAndView("admin/userList", Constants.USER_LIST, mgr.getUsers());
        }
    }

    public List<User> search(String query) {
        List<User> results = new ArrayList<User>();
        CompassSearchCommand command = new CompassSearchCommand(query);
        CompassSearchResults compassResults = compass.search(command);
        CompassHit[] hits = compassResults.getHits();
        log.debug("No. of results for '" + query + "': " + compassResults.getTotalHits());
        for (CompassHit hit : hits) {
            if (hit.data() instanceof User) {
                results.add((User) hit.data());
            }
        }
        return results;
    }
}
