package org.appfuse.webapp.form;

import java.io.Serializable;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;
import org.appfuse.Constants;
import org.appfuse.model.UserRole;


/**
 * This class is an extension of {@link UserForm} to allow
 * for UI specific setters/getters.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @version $Revision: 1.5 $ $Date: 2004/08/15 06:04:22 $
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
