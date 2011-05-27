package org.appfuse.webapp.pages;

import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.model.Model;
import org.appfuse.model.User;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * Page for editing current user.
 *
 * @author Marcin Zajączkowski, 2011-03-13
 */
@MountPath(path = "userEdit")
@AuthorizeInstantiation({"ROLE_ADMIN", "ROLE_USER"})
public class CurrentUserEdit extends AbstractUserEdit {

    public CurrentUserEdit() {
        super(null, new Model<User>());
    }

    @Override
    protected void onInitialize() {

        //TODO: MZA: Here or in constructor?
        //TODO: Quite odd - before super
        User user = getUserManager().getUserByUsername(getCurrentUserUsername());
        setUser(user);

        super.onInitialize();

    }

    private String getCurrentUserUsername() {
        String username = getWebRequestCycle().getWebRequest().getHttpServletRequest().getRemoteUser();
        if (username == null) {
            throw new IllegalStateException("Unable to get login of current user");
        }
        return username;
    }

    @Override
    protected void onSaveButtonSubmit() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    protected void onDeleteButtonSubmit() {
        throw new IllegalStateException("Delete button should not be able to use on edit current user page");
    }

    @Override
    protected void onCancelButtonSubmit() {
        resolveAndSetResponsePage();
    }

    @Override
    protected boolean getDisplayRolesGroupVisibility() {
        return true;
    }

    @Override
    protected boolean getAccountSettingsGroupVisibility() {
        return false;
    }

    @Override
    protected boolean getButtonsGroupVisibility() {
        return true;
    }

    @Override
    protected boolean getDeleteButtonVisibility() {
        return false;
    }
}
