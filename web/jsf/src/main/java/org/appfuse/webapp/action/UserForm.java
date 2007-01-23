package org.appfuse.webapp.action;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.acegisecurity.Authentication;
import org.acegisecurity.AuthenticationTrustResolver;
import org.acegisecurity.AuthenticationTrustResolverImpl;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.apache.commons.lang.StringUtils;
import org.appfuse.Constants;
import org.appfuse.model.Role;
import org.appfuse.model.User;
import org.appfuse.service.RoleManager;
import org.appfuse.service.UserExistsException;
import org.appfuse.util.ConvertUtil;
import org.appfuse.util.StringUtil;
import org.appfuse.webapp.util.RequestUtil;

/**
 * JSF Page class to handle editing a user with a form.
 *
 * @author mraible
 */
public class UserForm extends BasePage implements Serializable {
    private static final long serialVersionUID = -1141119853856863204L;
    private RoleManager roleManager;
    private String id;
    private User user = new User();
    private Map<String, String> availableRoles;
    private String[] userRoles;

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setRoleManager(RoleManager roleManager) {
        this.roleManager = roleManager;
    }

    public String add() {
        user = new User();
        user.addRole(new Role(Constants.USER_ROLE));
        return "editProfile";
    }

    public String cancel() {
        if (log.isDebugEnabled()) {
            log.debug("Entering 'cancel' method");
        }

        if (!StringUtils.equals(getParameter("from"), "list")) {
            return "mainMenu";
        } else {
            return "cancel";
        }
    }

    public String edit() {
        HttpServletRequest request = getRequest();

        // for some reason, the id is not set from the user list - setting username works fine
        if (id == null) {
            id = request.getParameter("id");
        }
        
        // if a user's id is passed in
        if (id != null) {
            // lookup the user using that id
            user = userManager.getUser(id);
        } else if (request.getParameter("add") == null) {
            user = userManager.getUserByUsername(request.getRemoteUser());
        } 

        if (user.getUsername() != null) {
            user.setConfirmPassword(user.getPassword());

            // if user logged in with remember me, display a warning that they can't change passwords
            log.debug("checking for remember me login...");

            AuthenticationTrustResolver resolver = new AuthenticationTrustResolverImpl();
            SecurityContext ctx = SecurityContextHolder.getContext();

            if (ctx != null) {
                Authentication auth = ctx.getAuthentication();

                if (resolver.isRememberMe(auth)) {
                    getSession().setAttribute("cookieLogin", "true");
                    log.trace("User '" + user.getUsername() + "' logged in with cookie");
                    addMessage("userProfile.cookieLogin");
                }
            }
        }

        return "editProfile";
    }

    public String save() {
        String password = user.getPassword();
        String originalPassword = getParameter("userForm:originalPassword");

        Boolean encrypt = (Boolean) getConfiguration().get(Constants.ENCRYPT_PASSWORD);
        boolean doEncrypt = (encrypt != null) && encrypt;
        
        if (doEncrypt && (StringUtils.equals(getParameter("encryptPass"), "true") ||
                !StringUtils.equals(password, originalPassword))) {
            String algorithm = (String) getConfiguration().get(Constants.ENC_ALGORITHM);

            if (algorithm == null) { // should only happen for test case
                log.debug("assuming testcase, setting algorigthm to 'SHA'");
                algorithm = "SHA";
            }

            user.setPassword(StringUtil.encodePassword(password, algorithm));
        }
        
        // workaround for plain ol' HTML input tags that don't seem to set
        // properties on the managed bean
        setUserRoles(getRequest().getParameterValues("userForm:userRoles"));

        for (int i = 0; (userRoles != null) && (i < userRoles.length); i++) {
            String roleName = userRoles[i];
            user.addRole(roleManager.getRole(roleName));
        }
        
        Integer originalVersion = user.getVersion();

        try {
            userManager.saveUser(user);
        } catch (UserExistsException e) {
            log.warn(e.getMessage());
            addError("errors.existing.user",
                     new Object[] { user.getUsername(), user.getEmail() });

            // reset the version # to what was passed in
            user.setVersion(originalVersion);
            return "editProfile";
        }

        if (!StringUtils.equals(getParameter("from"), "list")) {
            // add success messages
            addMessage("user.saved");

            // return to main Menu
            return "mainMenu";
        } else {
            // add success messages
            if ("".equals(getParameter("userForm:version"))) {
                addMessage("user.added", user.getFullName());

                sendUserMessage(user, getText("newuser.email.message",
                                user.getFullName()), RequestUtil.getAppURL(getRequest()));

                return "list"; // return to list screen
            } else {
                addMessage("user.updated.byAdmin", user.getFullName());
                return "editProfile"; // return to current page
            }
        }
    }

    public String delete() {
        userManager.removeUser(getUser().getId().toString());
        addMessage("user.deleted", getUser().getFullName());

        return "list";
    }

    /**
     * Convenience method to determine if the user came from the list screen
     * @return String
     */
    public String getFrom() {
        if ((id != null) || (getParameter("editUser:add") != null) ||
                ("list".equals(getParameter("from")))) {
            return "list";
        }

        return "";
    }

    // Form Controls ==========================================================
    public Map<String, String> getAvailableRoles() {
        if (availableRoles == null) {
            List roles = (List) getServletContext().getAttribute(Constants.AVAILABLE_ROLES);
            availableRoles = ConvertUtil.convertListToMap(roles);
        }

        return availableRoles;
    }

    public String[] getUserRoles() {
        userRoles = new String[user.getRoles().size()];

        int i = 0;

        if (userRoles.length > 0) {
            for (Role role : user.getRoles()) {
                userRoles[i] = role.getName();
                i++;
            }
        }

        return userRoles;
    }

    public void setUserRoles(String[] userRoles) {
        this.userRoles = userRoles;
    }
    
    public String getCountry() {
        return getUser().getAddress().getCountry();
    }
    
    // for some reason, the country drop-down won't do 
    // getUser().getAddress().setCountry(value)
    public void setCountry(String country) {
        getUser().getAddress().setCountry(country);
    }
}
