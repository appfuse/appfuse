package org.appfuse.webapp.action;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageRenderListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.valid.IValidationDelegate;
import org.apache.tapestry.valid.ValidationConstraint;
import org.appfuse.Constants;
import org.appfuse.model.User;
import org.appfuse.service.MailEngine;
import org.appfuse.service.RoleManager;
import org.appfuse.service.UserExistsException;
import org.appfuse.service.UserManager;
import org.appfuse.util.StringUtil;
import org.appfuse.webapp.util.RequestUtil;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.context.support.WebApplicationContextUtils;

import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.Authentication;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.acegisecurity.providers.ProviderManager;

public abstract class SignupForm extends BasePage implements PageRenderListener {
    private IPropertySelectionModel countries;  

    public abstract UserManager getUserManager();
    public abstract void setUserManager(UserManager manager);
    public abstract RoleManager getRoleManager();
    public abstract void setRoleManager(RoleManager manager);
    public abstract void setUser(User user);
    public abstract User getUser();
    
    public IPropertySelectionModel getCountries() {
        if (countries == null) {
            countries = new CountryModel(getLocale());
        }
        return countries;
    }
    
    public void pageBeginRender(PageEvent event) {
        if (getUser() == null && !event.getRequestCycle().isRewinding()) {
            setUser(new User());
        } else if (event.getRequestCycle().isRewinding()) {
            setUser(new User());
        }
     }
    
    public void cancel(IRequestCycle cycle) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("entered cancel method");
        }
        getResponse().sendRedirect(getRequest().getContextPath());
    }
    
    public void save(IRequestCycle cycle) throws UserExistsException, IOException {
        if (log.isDebugEnabled()) {
            log.debug("entered save method");
        }
        
        // make sure the password fields match
        IValidationDelegate delegate = getValidationDelegate();
        if (!StringUtils.equals(getUser().getPassword(), getUser().getConfirmPassword())) {
            addError(delegate, "confirmPasswordField", 
                     format("errors.twofields", getMessage("user.confirmPassword"), 
                             getMessage("user.password")),
                     ValidationConstraint.CONSISTENCY);
        }
        
        if (delegate.getHasErrors()) { 
            return; 
        }

        User user = getUser();
        
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
        user.addRole(getRoleManager().getRole(Constants.USER_ROLE));
        
        getUserManager().saveUser(getUser());
        
        try {
            getUserManager().saveUser(user);
        } catch (UserExistsException e) {
            log.warn(e.getMessage());
            addError(delegate, "usernameField",
                     format("errors.existing.user", user.getUsername(),
                            user.getEmail()), ValidationConstraint.CONSISTENCY);
            // redisplay the unencrypted passwords
            user.setPassword(user.getConfirmPassword());
            return;
        }

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
        
        // Send user an e-mail
        if (log.isDebugEnabled()) {
            log.debug("Sending user '" + user.getUsername() + "' an account information e-mail");
        }

        Map global = (Map) getGlobal();
        ApplicationContext ctx = (ApplicationContext) global.get(BaseEngine.APPLICATION_CONTEXT_KEY);
        
        SimpleMailMessage message = (SimpleMailMessage) ctx.getBean("mailMessage");
        message.setTo(user.getFullName() + "<" + user.getEmail() + ">");
        
        StringBuffer msg = new StringBuffer();
        msg.append(getMessage("signup.email.message"));
        msg.append("\n\n" + getMessage("user.username"));
        msg.append(": " + user.getUsername() + "\n");
        msg.append(getMessage("user.password") + ": ");
        msg.append(user.getPassword());
        msg.append("\n\nLogin at: " + RequestUtil.getAppURL(getRequest()));
        message.setText(msg.toString());
        
        message.setSubject(getMessage("signup.email.subject"));
        
        MailEngine engine = (MailEngine) ctx.getBean("mailEngine");
        engine.send(message);

        getSession().setAttribute("message", getMessage("user.registered"));
        getResponse().sendRedirect(getRequest().getContextPath());
    }
}

