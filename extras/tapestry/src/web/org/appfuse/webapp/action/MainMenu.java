package org.appfuse.webapp.action;

import org.apache.tapestry.IRequestCycle;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;

public abstract class MainMenu extends BasePage {
    public abstract UserManager getUserManager();

    public void editProfile(IRequestCycle cycle) {
        UserForm nextPage = (UserForm) cycle.getPage("userForm");
        Object[] parameters = cycle.getServiceParameters();
        String username = getRequest().getRemoteUser();

        if (log.isDebugEnabled()) {
            log.debug("fetching user profile: " + username);
        }

        User user = getUserManager().getUser(username);
        user.setConfirmPassword(user.getPassword());
        nextPage.setUser(user);
        nextPage.setFrom("mainMenu");
        cycle.activate(nextPage);
    }
}
