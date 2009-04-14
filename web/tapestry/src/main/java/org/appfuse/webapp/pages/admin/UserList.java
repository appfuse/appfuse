package org.appfuse.webapp.pages.admin;

import java.util.List;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.EventLink;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.appfuse.Constants;
import org.appfuse.model.Role;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.appfuse.webapp.pages.BasePage;
import org.appfuse.webapp.pages.MainMenu;
import org.appfuse.webapp.pages.UserEdit;
import org.slf4j.Logger;

/**
 * @author Serge Eby
 * @version $Id: UserList.java 5 2008-08-30 09:59:21Z serge.eby $
 */
public class UserList extends BasePage {

    private final static String[] COLUMNS = {"username", "email", "enabled"};

    @Inject
    private Logger log;

    @Inject
    private Messages messages;

    @Inject
    private UserManager userManager;


    @Property
    private BeanModel<User> model;

    @Inject
    private BeanModelSource beanModelSource;

    @Inject
    private ComponentResources resources;

    @InjectPage
    private UserEdit userEdit;

    @Property
    private User user;

    @Component(parameters = {"event=add"})
    private EventLink addTop, addBottom;

    @Component(parameters = {"event=done"})
    private EventLink doneTop, doneBottom;

    {
        model = beanModelSource.create(User.class, true, resources.getMessages());
        model.include(COLUMNS);
        model.add("fullname");
        // Set labels
        model.get("username").label(messages.get("user.username"));
        model.get("email").label(messages.get("user.email"));
        model.get("enabled").label(messages.get("user.enabled"));
        model.get("fullname").label(messages.get("activeUsers.fullName"));
    }

    public UserManager getUserManager() {
        return userManager;
    }

    @SuppressWarnings("unchecked")
    public List<User> getUsers() {
        return getUserManager().getUsers();
    }

    Object onAdd() {
        user = new User();
        user.addRole(new Role(Constants.USER_ROLE));
        userEdit.setUser(user);
        userEdit.setFrom("list");
        return userEdit;
    }

    Object onDone() {
        return MainMenu.class;
    }

    Object onActionFromEdit(Long id) {
        log.debug("fetching user with id: " + id);
        User user = getUserManager().getUser("" + id);
        user.setConfirmPassword(user.getPassword());
        userEdit.setUser(user);
        userEdit.setFrom("list");
        return userEdit;
    }


}
