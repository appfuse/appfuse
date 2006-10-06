package org.appfuse.webapp.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.appfuse.model.User;
import org.appfuse.webapp.listener.UserCounterListener;

public class ActiveUserList extends BasePage implements Serializable {
    private static final long serialVersionUID = -2725378005612769815L;
    private String sort = "username";
    private boolean ascending = false;
    
    public String getSort(){
        return sort;
    }

    public void setSort(String sort){
        this.sort = sort;
    }

    public boolean isAscending(){
        return ascending;
    }

    public void setAscending(boolean ascending){
        this.ascending = ascending;
    } 
    
    public List getUsers() {
        Set users = (Set) getServletContext().getAttribute(UserCounterListener.USERS_KEY);
        if (users != null) {
            List usersAsList = new ArrayList(users);

            Comparator comparator = new Comparator(){
                public int compare(Object o1, Object o2){
                    User u1 = (User) o1;
                    User u2 = (User) o2;
                    if (sort == null){
                        return 0;
                    }
                    if (sort.equals("username")){
                        return !ascending ? u1.getUsername().toLowerCase()
                                .compareTo(u2.getUsername().toLowerCase())
                                : u2.getUsername().toLowerCase()
                                .compareTo(u1.getUsername().toLowerCase());
                    }
                    if (sort.equals("fullName")){
                        return !ascending ? u1.getFullName().toLowerCase()
                                .compareTo(u2.getFullName().toLowerCase())
                                : u2.getFullName().toLowerCase()
                                .compareTo(u1.getFullName().toLowerCase());
                    }
                    return 0;
                }
            };
            Collections.sort(usersAsList, comparator);
            return usersAsList;
        } else {
            return new ArrayList();
        }
    }
}
