package org.appfuse.webapp.action;

import java.io.Serializable;
import java.util.List;

public class UserList extends BasePage implements Serializable {
    private static final long serialVersionUID = 972359310602744018L;
    
    public UserList() {
        setSortColumn("username");
    }
    
    public List getUsers() {
        return sort(userManager.getUsers(null));
    }
}
