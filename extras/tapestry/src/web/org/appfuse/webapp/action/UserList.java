package org.appfuse.webapp.action;

import org.apache.tapestry.IRequestCycle;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;

public abstract class UserList extends BasePage {

    public abstract UserManager getUserManager();
    public abstract void setUserManager(UserManager manager);

    public void edit(IRequestCycle cycle) {
        UserForm nextPage = (UserForm) cycle.getPage("userForm");
        Object[] parameters = cycle.getServiceParameters();
        String username = (String) parameters[0];

        if (log.isDebugEnabled()) {
            log.debug("fetching user with username: " + username);
        }

        User user = getUserManager().getUser(username);
        user.setConfirmPassword(user.getPassword());
        nextPage.setUser(user);
        nextPage.setFrom("list");
        cycle.activate(nextPage);
    }
}
