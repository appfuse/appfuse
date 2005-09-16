package org.appfuse.webapp.action;


import java.util.ArrayList;
import java.util.List;

import org.appfuse.Constants;
import org.appfuse.model.User;
import org.appfuse.util.StringUtil;
import org.appfuse.service.UserExistsException;
import org.appfuse.webapp.util.RequestUtil;

import com.opensymphony.webwork.ServletActionContext;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import net.sf.acegisecurity.context.security.SecureContext;
import net.sf.acegisecurity.context.ContextHolder;
import net.sf.acegisecurity.Authentication;
import net.sf.acegisecurity.GrantedAuthority;
import net.sf.acegisecurity.GrantedAuthorityImpl;
import net.sf.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import net.sf.acegisecurity.providers.ProviderManager;

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
            List args = new ArrayList();
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
        Authentication auth = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getConfirmPassword());
        try {
            ApplicationContext ctx = 
                WebApplicationContextUtils.getWebApplicationContext(getSession().getServletContext());
            if (ctx != null) {
                ProviderManager authenticationManager = (ProviderManager) ctx.getBean("authenticationManager");
                SecureContext secureCtx = (SecureContext) ContextHolder.getContext();
                secureCtx.setAuthentication(authenticationManager.doAuthentication(auth));
            }
        } catch (NoSuchBeanDefinitionException n) {
            // ignore, should only happen when testing
        }
        
        
        // Send an account information e-mail
        message.setSubject(getText("signup.email.subject"));
        sendUserMessage(user, getText("signup.email.message"), RequestUtil.getAppURL(getRequest()));

        return SUCCESS;
    }
}
