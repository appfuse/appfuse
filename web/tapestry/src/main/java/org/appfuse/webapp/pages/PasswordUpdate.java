package org.appfuse.webapp.pages;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.alerts.Duration;
import org.apache.tapestry5.alerts.Severity;
import org.apache.tapestry5.annotations.ActivationRequestParameter;
import org.apache.tapestry5.annotations.Log;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.appfuse.model.User;
import org.appfuse.service.UserExistsException;
import org.appfuse.service.UserManager;
import org.appfuse.webapp.util.RequestUtil;
import org.slf4j.Logger;
import org.springframework.security.access.AccessDeniedException;

import javax.servlet.http.HttpServletRequest;

/**
 * Updates a registered user's password.
 *
 * @author Serge Eby
 */
public class PasswordUpdate {
    @Inject
    private Logger logger;

    @Inject
    private Messages messages;

    @Inject
    private UserManager userManager;


    @Inject
    private AlertManager alertManager;

    @Inject
    private HttpServletRequest request;

    @Inject
    private PageRenderLinkSource pageRenderLinkSource;

    @ActivationRequestParameter(value = "username")
    @Property
    private String username;

    @ActivationRequestParameter(value = "token")
    @Property
    private String token;

    @Property
    private String currentPassword;


    @Property
    @Persist(PersistenceConstants.FLASH)
    private String newPassword;


    void setupRender() {
        if (StringUtils.isBlank(username)) {
            username = request.getRemoteUser();
        }
    }

    @Log
    void onValidateFromPasswordUpdate() {

        // Validate token
        if (StringUtils.isNotBlank(token) && !userManager.isRecoveryTokenValid(username, token)) {
            alertManager.error(messages.get("updatePassword.invalidToken"));
        }

        // Validate user access (if logged in)
        String remoteUser = request.getRemoteUser();
        if (remoteUser != null && !username.equals(remoteUser)) {
            throw new AccessDeniedException("You do not have permission to modify other users password.");
        }


        // Ensure new password is not empty
        if (StringUtils.isEmpty(newPassword)) {
            alertManager.error(messages.format("errors.required", messages.get("updatePassword.newPassword.label")));
        }

    }

    Object onSuccess() throws UserExistsException {

        final User user = userManager.updatePassword(username, currentPassword, token, newPassword,
                RequestUtil.getAppURL(request));
        if (user != null) {
            alertManager.alert(Duration.TRANSIENT,
                    Severity.SUCCESS,
                    messages.format("updatePassword.success", username));

        } else {
            String errorMessageKey = StringUtils.isNotBlank(token) ?
                    "updatePassword.invalidToken" : "updatePassword.invalidPassword";

            alertManager.error(messages.get(errorMessageKey));
            return this;
        }

        return Home.class;

    }

    Object onCancel() {
        return Home.class;
    }

    Object onFailure() {
        return this;
    }


}
