package org.appfuse.webapp.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.appfuse.Constants;
import org.appfuse.model.Role;
import org.appfuse.model.User;
import org.appfuse.util.StringUtil;
import org.appfuse.service.UserExistsException;
import org.appfuse.webapp.util.RequestUtil;

import com.opensymphony.webwork.ServletActionContext;

public class UserAction extends BaseAction {
    private List users;
    private User user;
    private String username;
    
    /**
     * Override constructor to set ordered HashMap for errors.
     * NOTE: Currently doesn't work.
     */
    public UserAction() {
        super.setFieldErrors(new LinkedHashMap());
    }
    
    public List getUsers() {
        return users;
    }

    public void setUsername(String id) {
        this.username = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String delete() {
        userManager.removeUser(user.getUsername());
        List args = new ArrayList();
        args.add(user.getUsername());
        saveMessage(getText("user.deleted", args));

        return SUCCESS;
    }

    public String edit() throws IOException {
        HttpServletRequest request = getRequest();
        boolean editProfile =
            (request.getRequestURI().indexOf("editProfile") > -1);

        // if URL is "editProfile" - make sure it's the current user
        if (editProfile) {
            // reject if username passed in or "list" parameter passed in
            // someone that is trying this probably knows the AppFuse code
            // but it's a legitimate bug, so I'll fix it. ;-)
            if ((request.getParameter("username") != null) ||
                    (request.getParameter("from") != null)) {
                ServletActionContext.getResponse().sendError(HttpServletResponse.SC_FORBIDDEN);
                log.warn("User '" + request.getRemoteUser() +
                         "' is trying to edit user '" +
                         request.getParameter("username") + "'");

                return null;
            }
        }

        // if a user's username is passed in
        if (username != null) {
            // lookup the user using that id
            user = userManager.getUser(username);
        } else if (editProfile) {
            user = userManager.getUser(request.getRemoteUser());
        } else {
            user = new User();
            user.addRole(new Role(Constants.USER_ROLE));
        }

        if (user.getUsername() != null) {
            user.setConfirmPassword(user.getPassword());

            // if user logged in with a cookie, display a warning that they
            // can't change passwords
            if (log.isDebugEnabled()) {
                log.debug("checking for cookieLogin...");
            }

            if (getSession().getAttribute("cookieLogin") != null) {
                saveMessage(getText("userProfile.cookieLogin"));
            }
        }
       
        return SUCCESS;
    }

    public String execute() {        
        return SUCCESS;
    }

    public String save() throws Exception {
        if (cancel != null) {
            if (!"list".equals(from)) {
                return "mainMenu";
            }
            return "cancel";
        }

        if (delete != null) {
            return delete();
        }

        if (StringUtils.equals(getRequest().getParameter("encryptPass"), "true")) {
            String algorithm =
                (String) getConfiguration().get(Constants.ENC_ALGORITHM);

            if (algorithm == null) { // should only happen for test case
                log.debug("assuming testcase, setting algorigthm to 'SHA'");
                algorithm = "SHA";
            }

            user.setPassword(StringUtil.encodePassword(user.getPassword(), algorithm));
        }
        
        boolean isNew = ("".equals(getRequest().getParameter("user.version")));
        
        String[] userRoles = getRequest().getParameterValues("user.userRoles");

        for (int i = 0; userRoles != null && i < userRoles.length; i++) {
            String roleName = userRoles[i];
            user.addRole(roleManager.getRole(roleName));
        }

        try {
            userManager.saveUser(user);
        } catch (UserExistsException e) {
            log.warn(e.getMessage());
            List args = new ArrayList();
            args.add(user.getUsername());
            args.add(user.getEmail());
            addActionError(getText("errors.existing.user", args));

            // redisplay the unencrypted passwords
            user.setPassword(user.getConfirmPassword());
            return INPUT;
        }

        HttpServletRequest request = getRequest();

        if (!"list".equals(from)) {
            getSession().setAttribute(Constants.USER_KEY, user);

            // update the user's remember me cookie if they didn't login
            // with a cookie
            if ((RequestUtil.getCookie(request, Constants.LOGIN_COOKIE) != null) &&
                    (request.getSession().getAttribute("cookieLogin") == null)) {
                // delete all user cookies and add a new one
                userManager.removeLoginCookies(user.getUsername());

                String autoLogin =
                    userManager.createLoginCookie(user.getUsername());
                RequestUtil.setCookie(ServletActionContext.getResponse(),
                                      Constants.LOGIN_COOKIE, autoLogin,
                                      request.getContextPath());
            }

            // add success messages
            saveMessage(getText("user.saved"));

            return "mainMenu";
        } else {
            // add success messages
            List args = new ArrayList();
            args.add(user.getFullName());
            if (isNew) {
                saveMessage(getText("user.added", args));
                // Send an account information e-mail
                message.setSubject(getText("signup.email.subject"));
                sendUserMessage(user, getText("newuser.email.message", args), 
                                RequestUtil.getAppURL(getRequest()));
                return "addAnother";
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
