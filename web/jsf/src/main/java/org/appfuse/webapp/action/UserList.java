package org.appfuse.webapp.action;

import java.io.Serializable;
import java.util.List;

public class UserList extends BasePage implements Serializable {
    private static final long serialVersionUID = 972359310602744018L;

    private String query;

    public void setQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public UserList() {
        setSortColumn("username");
    }

    public List getUsers() {
        if (query != null && !"".equals(query.trim())) {
            return userManager.search(query);
        } else {
            return sort(userManager.getUsers());
        }
    }

    public String search() {
        return "success";
    }
}
