package org.appfuse.webapp.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.appfuse.model.Role;

/**
 * This class is an extension of {@link UserForm}to allow for UI specific
 * setters/getters.
 * 
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible </a>
 *  Modified by <a href="mailto:dan@getrolling.com">Dan Kibler </a> 
 * 
 * @struts.form name="userFormEx"
 */
public class UserFormEx extends UserForm implements Serializable {
    
    protected transient List userRoles = new ArrayList();

    public String[] getUserRoles() {
        Role role;
        String[] userRoles = new String[roles.size()];
        Iterator iter = roles.iterator();
        int i = 0;
        while (iter.hasNext()) {
            role = (Role) iter.next();
            userRoles[i] = role.getName();
            i++;
        }
        return userRoles;
    }

    /**
     * Note that this is not used - it's just needed by Struts.  If you look
     * in UserAction - you'll see that request.getParameterValues("userRoles")
     * is used instead.
     * 
     * @param roles
     */
    public void setUserRoles(String[] roles) {
        for (int i = 0; i < roles.length; i++) {
            this.userRoles.add(roles[i]);
        }
    }
}