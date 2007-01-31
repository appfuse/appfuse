package org.appfuse.webapp.pages;

import java.io.IOException;

import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.providers.ProviderManager;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.apache.commons.lang.StringUtils;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;
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

public abstract class SignupForm extends BasePage implements PageBeginRenderListener {
    private IPropertySelectionModel countries;  

    public abstract UserManager getUserManager();
    public abstract RoleManager getRoleManager();
    public abstract MailEngine getMailEngine();
    public abstract SimpleMailMessage getMailMessage();
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
    
    public void save(IRequestCycle cycle) throws IOException {
        log.debug("entered save method");
        
        // make sure the password fields match
        IValidationDelegate delegate = getDelegate();
        if (!StringUtils.equals(getUser().getPassword(), getUser().getConfirmPassword())) {
            addError(delegate, "confirmPasswordField", 
                     getText("errors.twofields", new Object[] {getText("user.confirmPassword"), 
                                                               getText("user.password")}),
                     ValidationConstraint.CONSISTENCY);
        }
        
        if (delegate.getHasErrors()) { 
            return; 
        }

        User user = getUser();
        
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
        user.addRole(getRoleManager().getRole(Constants.USER_ROLE));
        
        try {
            getUserManager().saveUser(user);
        } catch (UserExistsException e) {
            log.warn(e.getMessage());
            addError(delegate, "usernameField",
                     getMessages().format("errors.existing.user", user.getUsername(),
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
        
        SimpleMailMessage message = getMailMessage();
        message.setTo(user.getFullName() + "<" + user.getEmail() + ">");
        
        StringBuffer msg = new StringBuffer();
        msg.append(getText("signup.email.message"));
        msg.append("\n\n" + getText("user.username"));
        msg.append(": " + user.getUsername() + "\n");
        msg.append(getText("user.password") + ": ");
        msg.append(user.getPassword());
        msg.append("\n\nLogin at: " + RequestUtil.getAppURL(getRequest()));
        message.setText(msg.toString());
        message.setSubject(getText("signup.email.subject"));
        
        getMailEngine().send(message);

        getSession().setAttribute("message", getText("user.registered"));
        getResponse().sendRedirect(getRequest().getContextPath());
    }
}

