package org.appfuse.webapp.action;

import java.io.Serializable;
import java.util.List;

public class UserList extends BasePage implements Serializable {
    public List getUsers() {
        return userManager.getUsers(null);
    }
}
