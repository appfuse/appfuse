package org.appfuse.webapp.form;

import java.io.Serializable;
import java.util.ArrayList;

import org.appfuse.model.UserRole;


/**
 * This class is an extension of {@link UserForm} to allow
 * for UI specific setters/getters.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @version $Revision: 1.6 $ $Date: 2004/08/22 03:27:26 $
 *
 * @struts.form name="userFormEx"
 */
public class UserFormEx extends UserForm implements Serializable {
    //~ Methods ================================================================

    public String[] getUserRoles() {
        String[] userRoles = new String[roles.size()];

        for (int i = 0; i < roles.size(); i++) {
            UserRole userRole = (UserRole) roles.get(i);
            userRoles[i] = userRole.getRoleName();
        }

        return userRoles;
    }

    public void setUserRoles(String[] userRoles) {
        if (this.roles == null) {
            this.roles = new ArrayList();
        }

        for (int i = 0; i < userRoles.length; i++) {
            UserRole userRole = new UserRole();
            userRole.setRoleName(userRoles[i]);
            this.roles.add(userRole);
        }
    }
}
