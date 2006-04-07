package org.appfuse.webapp.action;

import org.acegisecurity.Authentication;
import org.acegisecurity.AuthenticationTrustResolver;
import org.acegisecurity.AuthenticationTrustResolverImpl;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.apache.commons.lang.StringUtils;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.valid.IValidationDelegate;
import org.apache.tapestry.valid.ValidationConstraint;
import org.appfuse.Constants;
import org.appfuse.model.Role;
import org.appfuse.model.User;
import org.appfuse.service.MailEngine;
import org.appfuse.service.RoleManager;
import org.appfuse.service.UserExistsException;
import org.appfuse.service.UserManager;
import org.appfuse.util.StringUtil;
import org.appfuse.webapp.util.RequestUtil;
import org.springframework.mail.SimpleMailMessage;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class UserForm extends BasePage implements PageBeginRenderListener {
    public abstract IPropertySelectionModel getAvailableRoles();
    public abstract void setAvailableRoles(IPropertySelectionModel model);
    public abstract List getUserRoles();
    public abstract void setUserRoles(List roles);
    public abstract IPropertySelectionModel getCountries(); 
    public abstract void setCountries(IPropertySelectionModel model);
    public abstract MailEngine getMailEngine();
    public abstract SimpleMailMessage getMailMessage();
    public abstract UserManager getUserManager();
    public abstract RoleManager getRoleManager();
    public abstract void setUser(User user);
    public abstract User getUser();
    public abstract void setFrom(String from);
    public abstract String getFrom();

    public void pageBeginRender(PageEvent event) {        
        // if user doing an add, create an empty user with default settings
        if ((getUser() == null) && !event.getRequestCycle().isRewinding()) {
            setUser(new User()); 
            setFrom("list"); // shows role selection
            getUser().addRole(new Role(Constants.USER_ROLE));
        } else if (event.getRequestCycle().isRewinding()) { // before population
            setUser(new User());
        }

        // initialize drop-downs
        if (getAvailableRoles() == null) {
            List roles = (List) getServletContext().getAttribute(Constants.AVAILABLE_ROLES);
            setAvailableRoles(new RoleModel(roles));
        }
        
        List selectedRoles = new ArrayList(getUser().getRoles().size());

        for (Iterator it = getUser().getRoles().iterator();
                 (it != null) && it.hasNext();) {
            Role role = (Role) it.next();
            selectedRoles.add(role.getName());
        }
        setUserRoles(selectedRoles);
        
        if (getCountries() == null) {
            setCountries(new CountryModel(getLocale()));
        }
        
        // if user logged in with remember me, display a warning that they can't change passwords
        log.debug("checking for remember me login...");

        AuthenticationTrustResolver resolver = new AuthenticationTrustResolverImpl();
        SecurityContext ctx = SecurityContextHolder.getContext();

        if (ctx != null) {
            Authentication auth = ctx.getAuthentication();

            if (resolver.isRememberMe(auth)) {
                getSession().setAttribute("cookieLogin", "true");
                
                // add warning message
                setMessage(getText("userProfile.cookieLogin"));
            }
        }
    }

    public ILink cancel(IRequestCycle cycle) {
        if (log.isDebugEnabled()) {
            log.debug("Entering 'cancel' method");
        }

        if (getFrom() != null && getFrom().equalsIgnoreCase("list")) {
            return getEngineService().getLink(false, "users");
        } else {
            return getEngineService().getLink(false, "mainMenu");
        }
    }

    public ILink save(IRequestCycle cycle) throws UserExistsException {
        if (log.isDebugEnabled()) {
            log.debug("entered save method");
        }

        HttpServletRequest request = getRequest();
        
        // make sure the password fields match
        IValidationDelegate delegate = getDelegate();

        if (!StringUtils.equals(getUser().getPassword(), getUser().getConfirmPassword())) {
            addError(delegate, "confirmPasswordField", getMessages().format("errors.twofields",
                     getText("user.confirmPassword"), getText("user.password")),
                     ValidationConstraint.CONSISTENCY);
        }

        if (delegate.getHasErrors()) {
            return null;
        }

        String password = getUser().getPassword();
        String originalPassword = getRequest().getParameter("originalPassword");
        
        Boolean encrypt = (Boolean) getConfiguration().get(Constants.ENCRYPT_PASSWORD);
        boolean doEncrypt = (encrypt != null) && encrypt.booleanValue();
                
        if (doEncrypt && (StringUtils.equals(getRequest().getParameter("encryptPass"), "true") ||
                !StringUtils.equals("S"+password, originalPassword)) || 
                ("X".equals(request.getParameter(("version"))))) {
            String algorithm = (String) getConfiguration().get(Constants.ENC_ALGORITHM);

            if (algorithm == null) { // should only happen for test case
                log.debug("assuming testcase, setting algorigthm to 'SHA'");
                algorithm = "SHA";
            }

            getUser().setPassword(StringUtil.encodePassword(password, algorithm));
        }

        // workaround for input tags that don't aren't set by Tapestry (who knows why)
        boolean fromList = StringUtils.equals(getFrom(), "list");
        String[] userRoles;

        if (fromList) {
            userRoles = getRequest().getParameterValues("userRoles");
        } else {
            userRoles = getRequest().getParameterValues("hiddenUserRoles");
        }

        User user = getUser();
        UserManager userManager = getUserManager();

        user.getRoles().clear();
        for (int i = 0; (userRoles != null) && (i < userRoles.length); i++) {
            String roleName = userRoles[i];
            user.addRole(getRoleManager().getRole(roleName));
        }

        try {
            userManager.saveUser(user);
        } catch (UserExistsException e) {
            log.warn(e.getMessage());
            addError(delegate, "emailField",
                     getMessages().format("errors.existing.user", user.getUsername(),
                            user.getEmail()), ValidationConstraint.CONSISTENCY);
            getUser().setPassword(user.getConfirmPassword());
            getUser().setVersion(null);
            return null;
        }

        if (!fromList && user.getUsername().equals(getRequest().getRemoteUser())) {
            // add success messages
            MainMenu nextPage = (MainMenu) cycle.getPage("mainMenu");
            nextPage.setMessage(getText("user.saved", user.getFullName()));
            return getEngineService().getLink(false, nextPage.getPageName());
        } else {
            // add success messages
            if ("X".equals(request.getParameter(("version")))) {                
                sendNewUserEmail(request, user);
                UserList nextPage = (UserList) cycle.getPage("users");
                nextPage.setMessage(getText("user.added", user.getFullName()));
                //cycle.activate(nextPage); // return to the list screen
                return getEngineService().getLink(false, nextPage.getPageName());
            } else {
                setMessage(getText("user.updated.byAdmin", user.getFullName()));
                return null; // return to current page
            }
        }
    }

    public ILink delete(IRequestCycle cycle) {
        if (log.isDebugEnabled()) {
            log.debug("entered delete method");
        }

        getUserManager().removeUser(getUser().getId().toString());

        UserList nextPage = (UserList) cycle.getPage("users");
        nextPage.setMessage(getText("user.deleted", getUser().getFullName()));
        return getEngineService().getLink(false, nextPage.getPageName());
    }

    private void sendNewUserEmail(HttpServletRequest request, User user) {
        // Send user an e-mail
        if (log.isDebugEnabled()) {
            log.debug("Sending user '" + user.getUsername() + "' an account information e-mail");
        }
        
        SimpleMailMessage message = getMailMessage();
        message.setTo(user.getFullName() + "<" + user.getEmail() + ">");

        StringBuffer msg = new StringBuffer();
        msg.append(getText("newuser.email.message", user.getFullName()));
        msg.append("\n\n" + getText("user.username"));
        msg.append(": " + user.getUsername() + "\n");
        msg.append(getText("user.password") + ": ");
        msg.append(user.getPassword());
        msg.append("\n\nLogin at: " + RequestUtil.getAppURL(request));
        message.setText(msg.toString());

        message.setSubject(getText("signup.email.subject"));
        getMailEngine().send(message);
    }
}
