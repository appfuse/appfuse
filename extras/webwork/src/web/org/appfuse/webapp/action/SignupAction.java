package org.appfuse.webapp.action;


import java.util.ArrayList;
import java.util.List;

import org.appfuse.Constants;
import org.appfuse.model.User;
import org.appfuse.util.StringUtil;
import org.appfuse.webapp.util.RequestUtil;
import org.springframework.dao.DataIntegrityViolationException;

import com.opensymphony.webwork.ServletActionContext;

public class SignupAction extends BaseAction {
    private User user;
    private String cancel;
    
    public void setCancel(String cancel) {
        this.cancel = cancel;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    public User getUser() {
        return user;
    }

    public String execute() {
        if (cancel != null) {
            return CANCEL;
        }
        if (ServletActionContext.getRequest().getMethod().equals("GET")) {
            return INPUT;
        }
        return SUCCESS;
    }
    
    public String doDefault() {
        return INPUT;
    }
    
    public String save() throws Exception {
        String algorithm =
            (String) getConfiguration().get(Constants.ENC_ALGORITHM);

        if (algorithm == null) { // should only happen for test case
            if (log.isDebugEnabled()) {
                log.debug("assuming testcase, setting algorithm to 'SHA'");
            }
            algorithm = "SHA";
        }
        
        user.setPassword(StringUtil.encodePassword(user.getPassword(), algorithm));

        // Set the default user role on this new user
        user.addRole(roleManager.getRole(Constants.USER_ROLE));

        try {
            userManager.saveUser(user);
        } catch (DataIntegrityViolationException e) {
            log.warn("User already exists: " + e.getMessage());
            List args = new ArrayList();
            args.add(user.getUsername());
            args.add(user.getEmail());
            addActionError(getText("errors.existing.user", args));

            // redisplay the unencrypted passwords
            user.setPassword(user.getConfirmPassword());
            return INPUT;
        }

        // Set cookies for auto-magical login ;-)
        String loginCookie = userManager.createLoginCookie(user.getUsername());
        RequestUtil.setCookie(ServletActionContext.getResponse(), 
                              Constants.LOGIN_COOKIE, loginCookie,
                              ServletActionContext.getRequest().getContextPath());

        saveMessage(getText("user.registered"));
        getSession().setAttribute(Constants.REGISTERED, Boolean.TRUE);

        // Send an account information e-mail
        message.setSubject(getText("signup.email.subject"));
        sendUserMessage(user, getText("signup.email.message"), 
                        RequestUtil.getAppURL(getRequest()));
        return SUCCESS;
    }
}