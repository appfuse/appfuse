package org.appfuse.webapp.action;

import java.io.Serializable;

import org.appfuse.Constants;
import org.appfuse.model.User;
import org.appfuse.service.RoleManager;
import org.appfuse.service.UserExistsException;
import org.appfuse.util.StringUtil;
import org.appfuse.webapp.util.RequestUtil;

/**
 * JSF Page class to handle signing up a new user.
 *
 * @author mraible
 */
public class SignupForm extends BasePage implements Serializable {
    private User user = new User();
    private RoleManager roleManager;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    public void setRoleManager(RoleManager roleManager) {
        this.roleManager = roleManager;
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
        user.setEnabled(Boolean.TRUE);
        
        // Set the default user role on this new user
        user.addRole(roleManager.getRole(Constants.USER_ROLE));

        try {
            userManager.saveUser(user);
        } catch (UserExistsException e) {
            log.warn(e.getMessage());
            addMessage("errors.existing.user", 
                    new Object[] { user.getUsername(), user.getEmail() });

            // redisplay the unencrypted passwords
            user.setPassword(user.getConfirmPassword());
            return null;
        }

        // Set cookies for auto-magical login ;-)
        String loginCookie = userManager.createLoginCookie(user.getUsername());
        RequestUtil.setCookie(getResponse(), Constants.LOGIN_COOKIE, loginCookie,
                              getRequest().getContextPath());

        addMessage("user.registered");
        getSession().setAttribute(Constants.REGISTERED, Boolean.TRUE);

        // Send an account information e-mail
        message.setSubject(getText("signup.email.subject"));
        sendUserMessage(user, getText("signup.email.message"), 
                        RequestUtil.getAppURL(getRequest()));
        
        return "mainMenu";
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
