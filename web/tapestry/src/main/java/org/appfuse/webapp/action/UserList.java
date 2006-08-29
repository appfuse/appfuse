package org.appfuse.webapp.action;

import org.apache.tapestry.IRequestCycle;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;

import java.util.List;

public abstract class UserList extends BasePage {
    public abstract UserManager getUserManager();

    public List getUsers() {
        return getUserManager().getUsers(null);
    }
    
    public void edit(IRequestCycle cycle) {
        UserForm nextPage = (UserForm) cycle.getPage("userForm");
        Object[] parameters = cycle.getListenerParameters();
        String username = (String) parameters[0];

        if (log.isDebugEnabled()) {
            log.debug("fetching user with username: " + username);
        }

        User user = getUserManager().getUserByUsername(username);
        user.setConfirmPassword(user.getPassword());
        nextPage.setUser(user);
        nextPage.setFrom("list");
        cycle.activate(nextPage);
    }
}
