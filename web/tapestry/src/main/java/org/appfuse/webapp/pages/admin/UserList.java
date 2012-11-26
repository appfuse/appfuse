package org.appfuse.webapp.pages.admin;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.appfuse.dao.SearchException;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.appfuse.webapp.pages.Home;
import org.appfuse.webapp.pages.UserEdit;
import org.slf4j.Logger;

import java.util.List;

/**
 * @author Serge Eby
 * @version $Id: UserList.java 5 2008-08-30 09:59:21Z serge.eby $
 */
public class UserList {
    @Inject
    private Logger logger;

    @Inject
    private Messages messages;

    @Inject
    private UserManager userManager;

    @Inject
    private BeanModelSource beanModelSource;

    @Inject
    private JavaScriptSupport jsSupport;

    @InjectPage
    private UserEdit userEdit;

    @Property
    private User currentUser;

    @Property
    @Persist(PersistenceConstants.FLASH)
    private String q;

    private String infoMessage;

    @Property
    private String errorMessage;

    @Property
    private List<User> users;

    public BeanModel<User> getModel() {
        final BeanModel<User> model = beanModelSource.createDisplayModel(User.class, messages);

        model.include("username", "email", "enabled");
        model.add("fullname");
        // Set labels
        model.get("username").label(messages.get("user.username"));
        model.get("email").label(messages.get("user.email"));
        model.get("enabled").label(messages.get("user.enabled"));
        model.get("fullname").label(messages.get("activeUsers.fullName"));

        return model;
    }

    void setupRender() {
        try {
            users = userManager.search(q);
        } catch (SearchException se) {
            errorMessage = se.getMessage();
            users = userManager.getUsers();
        }
    }

    Object onAdd() {
        return userEdit.initialize(null, "list", messages.get("userProfile.admin.message"));
    }

    Object onDone() {
        return Home.class;
    }

    Object onActionFromEdit(User user) {
        logger.debug("fetching user with id: " + user.getId());
        user.setConfirmPassword(user.getPassword());
        return userEdit.initialize(user, "list", messages.get("userProfile.admin.message"));
    }

    Object onSubmit() {
        return this;
    }
}
