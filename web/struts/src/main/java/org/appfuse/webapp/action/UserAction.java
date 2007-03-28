package org.appfuse.webapp.action;

import org.acegisecurity.Authentication;
import org.acegisecurity.AuthenticationTrustResolver;
import org.acegisecurity.AuthenticationTrustResolverImpl;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.apache.struts2.ServletActionContext;
import org.appfuse.Constants;
import org.appfuse.model.Role;
import org.appfuse.model.User;
import org.appfuse.service.UserExistsException;
import org.appfuse.util.StringUtil;
import org.appfuse.webapp.util.RequestUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.opensymphony.xwork2.Preparable;

public class UserAction extends BaseAction implements Preparable {
    private static final long serialVersionUID = 6776558938712115191L;
    private List users;
    private User user;
    private String id;

    public void prepare() throws Exception {
        if (getRequest().getMethod().equalsIgnoreCase("post")) {
            if (!"".equals(getRequest().getParameter("user.id"))) {
                // prevent failures on new
                user = userManager.getUser(getRequest().getParameter("user.id"));
            }
        }
    }
    
    public List getUsers() {
        return users;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String delete() throws IOException {       
        userManager.removeUser(user.getId().toString());
        List<String> args = new ArrayList<String>();
        args.add(user.getFullName());
        saveMessage(getText("user.deleted", args));

        return SUCCESS;
    }

    public String edit() throws IOException {
        HttpServletRequest request = getRequest();
        boolean editProfile = (request.getRequestURI().indexOf("editProfile") > -1);

        // if URL is "editProfile" - make sure it's the current user
        if (editProfile) {
            // reject if id passed in or "list" parameter passed in
            // someone that is trying this probably knows the AppFuse code
            // but it's a legitimate bug, so I'll fix it. ;-)
            if ((request.getParameter("id") != null) || (request.getParameter("from") != null)) {
                ServletActionContext.getResponse().sendError(HttpServletResponse.SC_FORBIDDEN);
                log.warn("User '" + request.getRemoteUser() + "' is trying to edit user '" +
                         request.getParameter("id") + "'");

                return null;
            }
        }

        // if a user's id is passed in
        if (id != null) {
            // lookup the user using that id
            user = userManager.getUser(id);
        } else if (editProfile) {
            user = userManager.getUserByUsername(request.getRemoteUser());
        } else {
            user = new User();
            user.addRole(new Role(Constants.USER_ROLE));
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
                    saveMessage(getText("userProfile.cookieLogin"));
                }
            }
        }
       
        return SUCCESS;
    }

    public String execute() {        
        return SUCCESS;
    }

    public String cancel() {
        if (!"list".equals(from)) {
            return "mainMenu";
        }
        return "cancel";
    }

    public String save() throws Exception {
        Boolean encrypt = (Boolean) getConfiguration().get(Constants.ENCRYPT_PASSWORD);

        if ("true".equals(getRequest().getParameter("encryptPass")) && (encrypt != null && encrypt)) {
            String algorithm = (String) getConfiguration().get(Constants.ENC_ALGORITHM);

            if (algorithm == null) { // should only happen for test case
                log.debug("assuming testcase, setting algorithm to 'SHA'");
                algorithm = "SHA";
            }

            user.setPassword(StringUtil.encodePassword(user.getPassword(), algorithm));
        }

        Integer originalVersion = user.getVersion();

        boolean isNew = ("".equals(getRequest().getParameter("user.version")));
        // only attempt to change roles if user is admin
        // for other users, prepare() method will handle populating
        if (getRequest().isUserInRole(Constants.ADMIN_ROLE)) {
            String[] userRoles = getRequest().getParameterValues("userRoles");

            for (int i = 0; userRoles != null && i < userRoles.length; i++) {
                String roleName = userRoles[i];
                user.addRole(roleManager.getRole(roleName));
            }
        }

        try {
            user = userManager.saveUser(user);
        } catch (UserExistsException e) {
            log.warn(e.getMessage());
            List<String> args = new ArrayList<String>();
            args.add(user.getUsername());
            args.add(user.getEmail());
            addActionError(getText("errors.existing.user", args));

            // reset the version # to what was passed in
            user.setVersion(originalVersion);
            // redisplay the unencrypted passwords
            user.setPassword(user.getConfirmPassword());
            return INPUT;
        } 

        if (!"list".equals(from)) {
            // add success messages
            saveMessage(getText("user.saved"));
            return "mainMenu";
        } else {
            // add success messages
            List<String> args = new ArrayList<String>();
            args.add(user.getFullName());
            if (isNew) {
                saveMessage(getText("user.added", args));
                // Send an account information e-mail
                mailMessage.setSubject(getText("signup.email.subject"));
                sendUserMessage(user, getText("newuser.email.message", args),
                                RequestUtil.getAppURL(getRequest()));
                return SUCCESS;
            } else {
                saveMessage(getText("user.updated.byAdmin", args));
                return INPUT;
            }
        }
    }

    public String list() {
        users = userManager.getUsers(new User());
        return SUCCESS;
    }
}
