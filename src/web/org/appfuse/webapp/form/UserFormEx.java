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
 * @version $Revision: 1.4 $ $Date: 2004/05/16 02:17:03 $
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

    public ActionErrors validate(ActionMapping mapping,
                                 HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        MessageResources resources =
            (MessageResources) request.getAttribute(Globals.MESSAGES_KEY);
        UserFormEx userForm =
            (UserFormEx) request.getAttribute(Constants.USER_EDIT_KEY);

        // Perform validator framework validations
        if (mapping != null) {
            errors = super.validate(mapping, request);
        } else {
            // Do manual validations
            if (StringUtils.isEmpty(userForm.getUsername())) {
                errors.add(ActionMessages.GLOBAL_MESSAGE,
                           new ActionMessage("errors.required",
                                           resources.getMessage("userFormEx.username")));
            }

            if (StringUtils.isEmpty(userForm.getPassword())) {
                errors.add(ActionMessages.GLOBAL_MESSAGE,
                           new ActionMessage("errors.required",
                                           resources.getMessage("userFormEx.password")));
            }

            if (StringUtils.isEmpty(userForm.getConfirmPassword())) {
                errors.add(ActionMessages.GLOBAL_MESSAGE,
                           new ActionMessage("errors.required",
                                           resources.getMessage("userFormEx.confirmPassword")));
            }

            if (StringUtils.isEmpty(userForm.getFirstName())) {
                errors.add(ActionMessages.GLOBAL_MESSAGE,
                           new ActionMessage("errors.required",
                                           resources.getMessage("userFormEx.firstName")));
            }

            if (StringUtils.isEmpty(userForm.getLastName())) {
                errors.add(ActionMessages.GLOBAL_MESSAGE,
                           new ActionMessage("errors.required",
                                           resources.getMessage("userFormEx.lastName")));
            }

            if (StringUtils.isEmpty(userForm.getCity())) {
                errors.add(ActionMessages.GLOBAL_MESSAGE,
                           new ActionMessage("errors.required",
                                           resources.getMessage("userFormEx.city")));
            }

            if (StringUtils.isEmpty(userForm.getProvince())) {
                errors.add(ActionMessages.GLOBAL_MESSAGE,
                           new ActionMessage("errors.required",
                                           resources.getMessage("userFormEx.province")));
            }

            if (StringUtils.isEmpty(userForm.getCountry())) {
                errors.add(ActionMessages.GLOBAL_MESSAGE,
                           new ActionMessage("errors.required",
                                           resources.getMessage("userFormEx.country")));
            }

            if (StringUtils.isEmpty(userForm.getPostalCode())) {
                errors.add(ActionMessages.GLOBAL_MESSAGE,
                           new ActionMessage("errors.required",
                                           resources.getMessage("userFormEx.postalCode")));
            }

            if (StringUtils.isEmpty(userForm.getEmail())) {
                errors.add(ActionMessages.GLOBAL_MESSAGE,
                           new ActionMessage("errors.required",
                                           resources.getMessage("userFormEx.email")));
            }

            if (StringUtils.isEmpty(userForm.getWebsite())) {
                errors.add(ActionMessages.GLOBAL_MESSAGE,
                           new ActionMessage("errors.website",
                                           resources.getMessage("userFormEx.website")));
            }

            if (StringUtils.isEmpty(userForm.getPasswordHint())) {
                errors.add(ActionMessages.GLOBAL_MESSAGE,
                           new ActionMessage("errors.required",
                                           resources.getMessage("userFormEx.passwordHint")));
            }
        }

        return errors;
    }
}
