package org.appfuse.webapp.pages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.appfuse.Constants;
import org.appfuse.model.Role;
import org.appfuse.model.User;
import org.appfuse.service.UserExistsException;
import org.appfuse.webapp.components.UserForm;
import org.appfuse.webapp.pages.admin.UserList;
import org.appfuse.webapp.services.ServiceFacade;
import org.appfuse.webapp.util.RequestUtil;
import org.slf4j.Logger;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.AccessDeniedException;
import org.springframework.security.Authentication;
import org.springframework.security.AuthenticationTrustResolver;
import org.springframework.security.AuthenticationTrustResolverImpl;
import org.springframework.security.context.SecurityContext;
import org.springframework.security.context.SecurityContextHolder;

/**
 * Allow adding new users or viewing/updating existing users
 *
 * @author Serge Eby
 * @version $Id: UserEdit.java 5 2008-08-30 09:59:21Z serge.eby $
 */
public class UserEdit extends BasePage {
    @Inject
    private Logger logger;

    @Persist
    private User user;

    @Property @Persist
    private List<String> selectedRoles;

    private List<String> userRoles;

    @Inject
    private PageRenderLinkSource linker;

    @Inject
    private ServiceFacade serviceFacade;

    @InjectPage
    private UserList userList;

    @InjectPage
    private MainMenu mainMenu;

    @Persist
    private String from;

    @Persist
    private Link linkBack;

    @Component(id = "edit")
    private UserForm form;

    private boolean delete = false;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<String> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(List<String> userRoles) {
        this.userRoles = userRoles;
    }

    public Boolean isRememberMe() {
        AuthenticationTrustResolver resolver = new AuthenticationTrustResolverImpl();
        SecurityContext ctx = SecurityContextHolder.getContext();

        if (ctx != null) {
            Authentication auth = ctx.getAuthentication();
            return resolver.isRememberMe(auth);
        }
        return false;
    }

    public Boolean getCookieLogin() {
        return isRememberMe();
    }

    void beginRender() {
        if (user == null) {
            logger.debug("Initializing user object");
            user = new User();
            // Add default role
            user.addRole(new Role(Constants.USER_ROLE));
        }

        selectedRoles = new ArrayList<String>(user.getRoles().size());

        for (Role role : user.getRoles()) {
            logger.debug("Adding Role: " + role.getName());
            selectedRoles.add(role.getName());
        }

        setUserRoles(selectedRoles);

        // if user logged in with remember me, display a warning that they
        // can't change passwords
        logger.debug("checking for remember me login...");

        if (isRememberMe()) {
            // add warning message
            setMessage(getText("userProfile.cookieLogin"));
        }
    }


    // ~ --- Event Handlers

    Object onCancel() {
        logger.debug("Entering 'cancel' method");

        if (from != null && from.equalsIgnoreCase("list")) {
            return linker.createPageRenderLink("admin/UserList");
        } else {
            return linker.createPageRenderLink("MainMenu");
        }
    }

    void onValidateForm() {
        if (!StringUtils.equals(user.getPassword(), user.getConfirmPassword())) {
            addError(form.getForm(), form.getConfirmPasswordField(), "errors.twofields", true,
                    getMessageText("user.confirmPassword"), getMessageText("user.password"));
        }
    }

    Object onSuccess() throws UserExistsException, IOException {
        logger.debug("*** entering onSuccess method ***");

        // Delete Button Clicked
        if (delete) {
            return onDelete();
        }

        HttpServletRequest request = getRequest();

        if (selectedRoles != null && !selectedRoles.isEmpty()) {
            user.getRoles().clear();
            for (String roleName : selectedRoles) {
                logger.debug("Adding Role --> " + roleName);
                user.addRole(serviceFacade.getRoleManager().getRole(roleName));
            }
        }
        Integer originalVersion = user.getVersion();

        try {
            user = serviceFacade.getUserManager().saveUser(user);
        } catch (AccessDeniedException ade) {
            // thrown by UserSecurityAdvice configured in aop:advisor userManagerSecurity
            logger.warn(ade.getMessage());
            return AccessDenied.class;
        } catch (UserExistsException e) {
            addError(form.getForm(), form.getEmailField(), "errors.existing.user", true,
                    user.getUsername(), user.getEmail());
            user.setPassword(user.getConfirmPassword());
            user.setVersion(originalVersion);
            return null;
        }

        if (!form.isFromList() &&
                (request != null && user.getUsername().equals(request.getRemoteUser()))) {
            // add success messages
            mainMenu.addInfo("user.saved", true, user.getFullName());
            return mainMenu;
        } else {
            // add success messages
            if (originalVersion == null) {
                sendNewUserEmail(request, user);
                userList.addInfo("user.added", true, user.getFullName());
                return userList;
            } else {
                addInfo("user.updated.byAdmin", true, user.getFullName());
                return null; // return to current pages
            }
        }
    }

    void onSelectedFromEdit() {
        delete = true;
    }

    Object onDelete() {
        logger.debug("entered delete method");
        // Save full name before deletion
        String fullName = user.getFullName();
        serviceFacade.getUserManager().removeUser(user.getId().toString());
        userList.addInfo("user.deleted", true, fullName);
        logger.debug("After deletion.. ready to return userList object");
        return userList;
    }

    void cleanupRender() {
        //user = null;
//         validationError = null;
    }

    // ~ Helper methods
    private void sendNewUserEmail(HttpServletRequest request, User user) {
        // Send user an e-mail
        if (logger.isDebugEnabled()) {
            logger.debug("Sending user '" + user.getUsername()
                    + "' an account information e-mail");
        }

        SimpleMailMessage message = serviceFacade.getMailMessage();
        message.setTo(user.getFullName() + "<" + user.getEmail() + ">");

        StringBuffer msg = new StringBuffer();
        msg.append(getText("newuser.email.message", user.getFullName()));
        msg.append("\n\n").append(getText("user.username"));
        msg.append(": ").append(user.getUsername()).append("\n");
        msg.append(getText("user.password")).append(": ");
        msg.append(user.getPassword());
        msg.append("\n\nLogin at: ").append(RequestUtil.getAppURL(request));
        message.setText(msg.toString());

        message.setSubject(getText("signup.email.subject"));

        try {
            serviceFacade.getMailEngine().send(message);
        } catch (MailException me) {
            //getSession().setAttribute("error", me.getCause().getLocalizedMessage());
            addError(me.getCause().getLocalizedMessage(), false);
        }
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
