package org.appfuse.webapp.pages;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.appfuse.model.User;
import org.appfuse.service.RoleManager;
import org.appfuse.service.UserManager;
import org.appfuse.webapp.AbstractWebPage;
import org.appfuse.webapp.pages.components.UserEditPanel;

/**
 * Abstract page for editing users.
 *
 * Reusable UserEditPanel allows to simple adjust for user signup and editing: current user, existing user, new user.
 *
 * @author Marcin Zajączkowski, 2011-03-19
 */
public abstract class AbstractUserEdit extends AbstractWebPage {

    protected static Page NO_RESPONSE_PAGE = null;

    @SpringBean(name = "userManager")
    private UserManager userManager;

    @SpringBean(name = "roleManager")
    private RoleManager roleManager;

    private final Page responsePage;
    private final String propertyPrefix;
    private final IModel<User> userModel;

    private Form<User> userEditForm;

    protected AbstractUserEdit(Page responsePage, String propertyPrefix, IModel<User> userModel) {
        super();
        this.responsePage = responsePage;
        this.propertyPrefix = propertyPrefix;
        this.userModel = userModel;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        add(createPageTitleTag(propertyPrefix + ".title"));
        add(createPageHeading(propertyPrefix + ".heading"));
        add(createPageMessage(propertyPrefix + ".message"));

        add(createFeedbackPanel());

        CompoundPropertyModel<User> userCompoundPropertyModel = new CompoundPropertyModel<User>(userModel);

        userEditForm = new Form<User>("userEditForm", userCompoundPropertyModel);
        add(userEditForm);

        //TODO: MZA: Extract concrete class and pass interface to avoid Foo.this.on... duplication
        UserEditPanel userEditPanel = new UserEditPanel("userEditPanel", userCompoundPropertyModel,
                roleManager.getAll()) {

            @Override
            protected void onSaveButtonSubmit() {
                log.info("onSaveButtonSubmit");
                AbstractUserEdit.this.onSaveButtonSubmit();
            }

            @Override
            protected void onDeleteButtonSubmit() {
                log.info("onDeleteButtonSubmit");
                AbstractUserEdit.this.onDeleteButtonSubmit();
            }

            @Override
            protected void onCancelButtonSubmit() {
                log.info("onCancelButtonSubmit");
                AbstractUserEdit.this.onCancelButtonSubmit();
            }

            @Override
            protected boolean getAccountSettingsGroupVisibility() {
                return AbstractUserEdit.this.getAccountSettingsGroupVisibility();
            }

            @Override
            protected boolean getDisplayRolesGroupVisibility() {
                return AbstractUserEdit.this.getDisplayRolesGroupVisibility();
            }

            @Override
            protected boolean getDeleteButtonVisibility() {
                return AbstractUserEdit.this.getDeleteButtonVisibility();
            }
        };

        userEditForm.add(userEditPanel);
    }

    protected void resolveAndSetResponsePage() {
        if (responsePage == null) {
            setResponsePage(getApplication().getHomePage());
        } else {
            setResponsePage(responsePage);
        }
    }

    protected UserManager getUserManager() {
        return userManager;
    }

    protected RoleManager getRoleManager() {
        return roleManager;
    }

    protected Form<User> getUserEditForm() {
        return userEditForm;
    }

    protected User getUser() {
        return userModel.getObject();
    }

    //required by CurrentUserEdit which cannot lookup current user in a constructor
    protected void setUser(User user) {
        userModel.setObject(user);
    }

    protected abstract void onSaveButtonSubmit();

    protected abstract void onDeleteButtonSubmit();

    /**
     * A handler for a click on a cancel button.
     *
     * Return to a requested response page is usually a good action. Can be overridden if needed.
     */
    protected void onCancelButtonSubmit() {
        resolveAndSetResponsePage();
    }

    protected abstract boolean getDisplayRolesGroupVisibility();

    protected abstract boolean getAccountSettingsGroupVisibility();

    protected abstract boolean getDeleteButtonVisibility();
}
