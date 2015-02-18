package org.appfuse.webapp.pages;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.alerts.Duration;
import org.apache.tapestry5.alerts.Severity;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.appfuse.Constants;
import org.appfuse.model.Role;
import org.appfuse.model.User;
import org.appfuse.service.RoleManager;
import org.appfuse.service.UserExistsException;
import org.appfuse.service.UserManager;
import org.appfuse.webapp.components.UserForm;
import org.appfuse.webapp.pages.admin.UserList;
import org.appfuse.webapp.services.EmailService;
import org.appfuse.webapp.services.SecurityContext;
import org.appfuse.webapp.util.RequestUtil;
import org.slf4j.Logger;
import org.springframework.mail.MailException;
import org.springframework.security.access.AccessDeniedException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Allow adding new users or viewing/updating existing users
 *
 * @author Serge Eby
 * @version $Id: UserEdit.java 5 2008-08-30 09:59:21Z serge.eby $
 */
public class UserEdit {

    @Inject
    private Logger logger;

    @PageActivationContext(passivate = false)
    @Property(write = false)
    private User user;

    @Property
    @Persist
    private List<Role> selectedRoles;

    @Inject
    private PageRenderLinkSource pageRenderLinkSource;

    @Inject
    private Messages messages;

    @Inject
    private SecurityContext securityContext;


    @Inject
    private UserManager userManager;

    @Inject
    private RoleManager roleManager;

    @Inject
    private HttpServletRequest request;

    @Inject
    private AlertManager alertManager;


    @Inject
    private EmailService emailService;

    @InjectPage
    private UserList userList;

    @InjectPage
    private Home home;

    @Persist(PersistenceConstants.FLASH)
    @Property
    private Class goBack;

    @Persist
    @Property(write = false)
    private String from;


    @Persist(PersistenceConstants.FLASH)
    @Property(write = false)
    private String infoMessage;

    @Component(id = "edit")
    private UserForm form;

    private boolean delete = false;

    private boolean cancel = false;

    public void setUser(User user) {
        this.user = user;
    }


    public Object initialize(User user, String from, String infoMessage) {
        this.user = user;
        this.from = from;
        this.infoMessage = infoMessage;

        return this;
    }

    public void setInfoMessage(String infoMessage) {
        this.infoMessage = infoMessage;
    }

    Object[] onPassivate() {
        if (user != null && user.getId() != null) {
            return new Object[]{user};
        }
        return new User[]{};
    }

    void setupRender() {
        if (user == null) {
            user = new User();
            // Add default role
            user.addRole(new Role(Constants.USER_ROLE));
        }

        selectedRoles = new ArrayList<Role>(user.getRoles());

        // if user logged in with remember me, display a warning that they
        // can't change passwords
        logger.debug("checking for remember me login...");

        if (securityContext.isRememberMe()) {
            // add warning message
            alertManager.info(messages.get("userProfile.cookieLogin"));
        }

        // Set info message
        form.setInfoMessage(infoMessage);
    }


    public boolean isCookieLogin() {
        return securityContext.isRememberMe();
    }

    // ~ --- Event Handlers

    @Log
    @DiscardAfter
    Object onCanceledFromEdit() {
        //  return pageRenderLinkSource.createPageRenderLink(goBack);

        if (from != null && from.equalsIgnoreCase("list")) {
            return pageRenderLinkSource.createPageRenderLink(UserList.class);
        } else {
            return pageRenderLinkSource.createPageRenderLink(Home.class);
        }
    }

    @Log
    void onValidatePasswordFromEdit() {
        // Ensure the password fields match
        if (form.isValid()) {
            if (!StringUtils.equals(user.getPassword(), user.getConfirmPassword())) {

                String errorMessage = messages.format("errors.twofields",
                        messages.get("user.confirmPassword"),
                        messages.get("user.password"));

                // form.recordError(passwordField, errorMessage);

                alertManager.alert(Duration.TRANSIENT, Severity.ERROR, errorMessage);
            }
        }
    }

    void onPrepare() {
        if (user == null) {
            user = new User();
        }
    }

    @Log
    @DiscardAfter
    Object onSuccess() throws UserExistsException, IOException {

        // Delete Button Clicked
        if (delete) {
            return onDelete();
        }

        // Only Admins can update roles for other users
        if (securityContext.isAdmin()) {
            if (selectedRoles != null && !selectedRoles.isEmpty()) {
                user.getRoles().clear();
                for (int i = 0; selectedRoles != null && i < selectedRoles.size(); i++) {
                    String roleName = selectedRoles.get(i).getName();
                    user.addRole(roleManager.getRole(roleName));
                }
            }
        }

        Integer originalVersion = user.getVersion();

        try {
            user = userManager.saveUser(user);
        } catch (AccessDeniedException ade) {
            // thrown by UserSecurityAdvice configured in aop:advisor userManagerSecurity
            logger.warn(ade.getMessage());
            return AccessDenied.class;
        } catch (UserExistsException e) {
            // TODO
            //form.recordError(form.getEmailField(), "User exits");
            alertManager.alert(Duration.TRANSIENT, Severity.ERROR,
                    messages.format("errors.existing.user", user.getUsername(), user.getEmail())
            );

            user.setPassword(user.getConfirmPassword());
            user.setVersion(originalVersion);
            return null;
        }

        if (!"list".equalsIgnoreCase(from)) {
            // add success messages
            alertManager.alert(
                    Duration.TRANSIENT,
                    Severity.SUCCESS,
                    messages.format("user.saved", user.getFullName()));
            return Home.class;
        } else {
            // add success messages
            if (originalVersion == null) {
                alertManager.alert(Duration.TRANSIENT, Severity.SUCCESS,
                        messages.format("user.added", user.getFullName()));

                try {
                    String msg = messages.format("newuser.email.message", user.getFullName());
                    String subject = messages.get("signup.email.subject");
                    emailService.send(user, subject, msg, RequestUtil.getAppURL(request), false);
                } catch (MailException me) {
                    alertManager.alert(
                            Duration.TRANSIENT,
                            Severity.ERROR,
                            me.getCause().getLocalizedMessage());
                }
                return UserList.class;
            } else {
                alertManager.alert(Duration.TRANSIENT, Severity.SUCCESS,
                        messages.format("user.updated.byAdmin", user.getFullName()));
            }
        }

        return this;
    }

    @Log
    Object onDelete() {
        // Save full name before deletion
        String fullName = user.getFullName();
        userManager.removeUser(user.getId().toString());
        alertManager.alert(Duration.TRANSIENT, Severity.SUCCESS,
                messages.format("user.deleted", fullName)
        );
        logger.debug("After deletion.. ready to return userList object");
        return UserList.class;
    }


    @Log
    Object onUpdatePassword() {
        Link link = pageRenderLinkSource.createPageRenderLinkWithContext(PasswordUpdate.class);
        link.addParameter("username", user.getUsername());
        return link;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
