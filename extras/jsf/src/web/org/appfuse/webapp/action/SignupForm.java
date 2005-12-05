package org.appfuse.webapp.action;

import java.io.Serializable;

import org.appfuse.Constants;
import org.appfuse.model.User;
import org.appfuse.service.RoleManager;
import org.appfuse.service.UserExistsException;
import org.appfuse.util.StringUtil;
import org.appfuse.webapp.util.RequestUtil;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.Authentication;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.acegisecurity.providers.ProviderManager;

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
        Boolean encrypt = (Boolean) getConfiguration().get(Constants.ENCRYPT_PASSWORD);
        
        if (encrypt != null && encrypt.booleanValue()) {
            String algorithm = (String) getConfiguration().get(Constants.ENC_ALGORITHM);
    
            if (algorithm == null) { // should only happen for test case
                if (log.isDebugEnabled()) {
                    log.debug("assuming testcase, setting algorithm to 'SHA'");
                }
                algorithm = "SHA";
            }
            
            user.setPassword(StringUtil.encodePassword(user.getPassword(), algorithm));
        }
        
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

        addMessage("user.registered");
        getSession().setAttribute(Constants.REGISTERED, Boolean.TRUE);

        // log user in automatically
        Authentication auth = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getConfirmPassword());
        try {
            ApplicationContext ctx = 
                WebApplicationContextUtils.getWebApplicationContext(getSession().getServletContext());
            if (ctx != null) {
                ProviderManager authenticationManager = (ProviderManager) ctx.getBean("authenticationManager");
                SecurityContextHolder.getContext().setAuthentication(authenticationManager.doAuthentication(auth));
            }
        } catch (NoSuchBeanDefinitionException n) {
            // ignore, should only happen when testing
        }
        
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
