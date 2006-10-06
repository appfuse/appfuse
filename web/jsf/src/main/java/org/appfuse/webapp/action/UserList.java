package org.appfuse.webapp.action;

import java.io.Serializable;
import java.util.List;

public class UserList extends BasePage implements Serializable {
    private static final long serialVersionUID = -2370422643417612459L;

    public List getUsers() {
        return userManager.getUsers(null);
    }
}
