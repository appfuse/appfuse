package org.appfuse.webapp.pages;

import org.apache.wicket.Page;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.appfuse.model.User;
import org.appfuse.service.UserExistsException;

/**
 * Page for editing current user from list and administrative addition of a new user.
 *
 * @author Marcin Zajączkowski, 2011-03-12
 */
@AuthorizeInstantiation({"ROLE_ADMIN"})
public class FromListUserEdit extends AbstractUserEdit {

    /**
     * Constructor for adding a new user.
     *
     * @param responsePage page to come back
     */
    @Deprecated //rather user and proper model should be created in the place which calls that action
    public FromListUserEdit(Page responsePage) {
        super(responsePage, new Model<User>());
        //TODO: MZA: Here or in onInitialize?
        setUser(new User());
    }

    /**
     * Constructor for editing an existing user.
     *
     * @param responsePage page to come back
     * @param userModel model for editing user
     */
    public FromListUserEdit(Page responsePage, IModel<User> userModel) {
        super(responsePage, userModel);
    }

    protected void onSaveButtonSubmit() {
        User user = getUser();

        log.info("onSubmit: {}", user);
        log.info("onSubmit (address): {}", user.getAddress());

        try {
            getUserManager().saveUser(user);
            getSession().info(new StringResourceModel(
                    "user.added", this, null, new Object[] {user.getUsername()}).getString());
            resolveAndSetResponsePage();
        } catch (UserExistsException e) {
            log.warn("User already exists", e);
            error(new StringResourceModel("errors.existing.user", this, null, new Object[] {
                    user.getUsername(), user.getEmail()}).getString());
        }
    }

    @Override
    protected void onDeleteButtonSubmit() {
        User editedUser = getUser();
        String fullName = editedUser.getFullName();
        log.debug("deleting user: {}, {}", editedUser.getId(), fullName);
        getUserManager().removeUser(editedUser.getId().toString());
        getSession().info(new StringResourceModel(
                "user.deleted", this, null, new Object[] {fullName}).getString());
        resolveAndSetResponsePage();
    }

    @Override
    protected void onCancelButtonSubmit() {
        resolveAndSetResponsePage();
    }

    @Override
    protected boolean getDisplayRolesGroupVisibility() {
        return false;
    }

    @Override
    protected boolean getAccountSettingsGroupVisibility() {
        return true;
    }

    @Override
    protected boolean getButtonsGroupVisibility() {
        return true;
    }

    @Override
    protected boolean getDeleteButtonVisibility() {
        return isExistingUserEdited();
    }

    private boolean isExistingUserEdited() {
        //Quite not elegant, but it seems there is not better way to determine if User is a new user (after add button)
        return getUser().getFirstName() != null;
    }
}
