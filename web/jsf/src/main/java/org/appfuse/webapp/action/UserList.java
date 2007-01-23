package org.appfuse.webapp.action;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.NullComparator;
import org.apache.commons.collections.comparators.ReverseComparator;

public class UserList extends BasePage implements Serializable {
    private static final long serialVersionUID = 972359310602744018L;
    private String sortColumn = "username";
    private boolean ascending = true;
    private boolean nullsAreHigh = true; 
    
    public String getSortColumn() {
        return sortColumn;
    }

    public void setSortColumn(String sortColumn) {
        this.sortColumn = sortColumn;
    }

    public boolean isAscending() {
        return ascending;
    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    } 
    
    public List getUsers() {
        List users = userManager.getUsers(null);

        Comparator comparator = new BeanComparator(sortColumn, new NullComparator(nullsAreHigh));
        if (!ascending) {
            comparator = new ReverseComparator(comparator);
        }
        Collections.sort(users, comparator);

        return users;
    }
}
