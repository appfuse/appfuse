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
        UserForm nextPage = (UserForm) cycle.getPage("UserForm");
        Object[] parameters = cycle.getListenerParameters();
        Long id = (Long) parameters[0];

        log.debug("fetching user with id: " + id);

        User user = getUserManager().getUser(""+id);
        user.setConfirmPassword(user.getPassword());
        nextPage.setUser(user);
        nextPage.setFrom("list");
        cycle.activate(nextPage);
    }
}
