package org.appfuse.webapp.page;

import org.apache.tapestry.IRequestCycle;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;

public abstract class MainMenu extends BasePage {
    public abstract UserManager getUserManager();

    public void editProfile(IRequestCycle cycle) {
        UserForm nextPage = (UserForm) cycle.getPage("UserForm");
        String username = getRequest().getRemoteUser();

        log.debug("fetching user profile: " + username);

        User user = getUserManager().getUserByUsername(username);
        user.setConfirmPassword(user.getPassword());
        nextPage.setUser(user);
        nextPage.setFrom("mainMenu");
        cycle.activate(nextPage);
    }
}
