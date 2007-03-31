package org.appfuse.webapp.action;


import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.apache.struts2.ServletActionContext;
import org.appfuse.Constants;
import org.appfuse.model.User;
import org.appfuse.service.UserExistsException;
import org.appfuse.util.StringUtil;
import org.appfuse.webapp.util.RequestUtil;

import java.util.ArrayList;
import java.util.List;

public class SignupAction extends BaseAction {
    private static final long serialVersionUID = 6558317334878272308L;
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
        Boolean encrypt = (Boolean) getConfiguration().get(Constants.ENCRYPT_PASSWORD);

        if (encrypt != null && encrypt) {
            String algorithm = (String) getConfiguration().get(Constants.ENC_ALGORITHM);

            if (algorithm == null) { // should only happen for test case
                if (log.isDebugEnabled()) {
                    log.debug("assuming testcase, setting algorithm to 'SHA'");
                }
                algorithm = "SHA";
            }

            user.setPassword(StringUtil.encodePassword(user.getPassword(), algorithm));
        }

        user.setEnabled(true);

        // Set the default user role on this new user
        user.addRole(roleManager.getRole(Constants.USER_ROLE));

        try {
            user = userManager.saveUser(user);
        } catch (UserExistsException e) {
            log.warn(e.getMessage());
            List<String> args = new ArrayList<String>();
            args.add(user.getUsername());
            args.add(user.getEmail());
            addActionError(getText("errors.existing.user", args));

            // redisplay the unencrypted passwords
            user.setPassword(user.getConfirmPassword());
            return INPUT;
        }

        saveMessage(getText("user.registered"));
        getSession().setAttribute(Constants.REGISTERED, Boolean.TRUE);

        // log user in automatically
        Authentication auth = new UsernamePasswordAuthenticationToken(
                user.getUsername(), user.getConfirmPassword(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Send an account information e-mail
        mailMessage.setSubject(getText("signup.email.subject"));
        sendUserMessage(user, getText("signup.email.message"), RequestUtil.getAppURL(getRequest()));

        return SUCCESS;
    }
}
