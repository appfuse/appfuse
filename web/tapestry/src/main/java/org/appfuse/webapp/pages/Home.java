package org.appfuse.webapp.pages;

import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.appfuse.model.User;
import org.appfuse.webapp.services.SecurityContext;
import org.slf4j.Logger;


/**
 * Main entry point of the application
 *
 * @author Serge Eby
 * @version $Id: Home.java 5 2008-08-30 09:59:21Z serge.eby $
 */
public class Home {

    @Inject
    private Logger logger;

    @Inject
    private Messages messages;

    @Inject
    private SecurityContext securityContext;

    @Inject
    private AlertManager alertManager;

    @InjectPage
    private UserEdit userEdit;

    @Persist
    @Property
    private User currentUser;

    Object onActionFromEditProfile() {
        logger.debug("Editing current user's profile");
        currentUser = securityContext.getUser();
        if (currentUser == null) {
            logger.debug("Current User is null - this is unexpected");
            return this;
        }
        logger.debug(String.format("Current User is %s", currentUser.getUsername()));
        currentUser.setConfirmPassword(currentUser.getPassword());
        return userEdit.initialize(currentUser, "main", messages.get("userProfile.message"));
    }
}
