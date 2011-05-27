package org.appfuse.webapp.pages.components;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.appfuse.model.Address;
import org.appfuse.model.Role;
import org.appfuse.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * Resusable form for editing users.
 *
 * Available abstract methods can be used to define specific behavior for different pages.
 *
 * TODO: MZA: Where to put (possible) different validators configuration?
 *
 * @author Marcin Zajączkowski, 2011-03-14
 */
public abstract class UserEditPanel extends Panel {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    //TODO: MZA: User @Cacheable to cache countries
    private static final List<String> countryList = Arrays.asList("USA", "Poland", "Japan");

    private final List<Role> allAvailableRoles;

    public UserEditPanel(String id, IModel<User> userIModel, List<Role> allAvailableRoles) {
        super(id, userIModel);
        this.allAvailableRoles = allAvailableRoles;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        
        add(new RequiredLabel("usernameLabel", new ResourceModel("user.username")));
        add(new RequiredTextField<String>("username"));

        add(createPasswordGroup());

        add(new RequiredLabel("passwordHintLabel", getString("user.passwordHint")));
        add(new RequiredTextField("passwordHint"));

        add(new RequiredLabel("firstNameLabel", getString("user.firstName")));
        add(new RequiredTextField("firstName"));

        add(new RequiredLabel("lastNameLabel", getString("user.lastName")));
        add(new RequiredTextField("lastName"));

        add(new RequiredLabel("emailLabel", getString("user.email")));
        add(new RequiredTextField("email"));

        add(new Label("phoneNumberLabel", getString("user.phoneNumber")));
        add(new TextField("phoneNumber"));

        add(new RequiredLabel("websiteLabel", getString("user.website")));
        add(new RequiredTextField("website"));

        User user = (User)getDefaultModelObject();

        add(new AddressFragment("mainAddress", "address",
               new CompoundPropertyModel<Address>(user.getAddress())));

        add(createAccountSettingsGroup(user));
        add(createDisplayRolesGroup(user));
        add(createGroupWithTopButtons());
        add(createGroupWithBottomButtons());
        
    }

    private WebMarkupContainer createPasswordGroup() {
        final WebMarkupContainer passwordGroup = new WebMarkupContainer("passwordGroup");
        passwordGroup.add(new RequiredLabel("passwordLabel", getString("user.password")));
        passwordGroup.add(new PasswordTextField("password"));
        passwordGroup.add(new RequiredLabel("confirmPasswordLabel", getString("user.confirmPassword")));
        passwordGroup.add(new PasswordTextField("confirmPassword"));
        return passwordGroup;
    }

    private WebMarkupContainer createAccountSettingsGroup(User user) {
        final WebMarkupContainer accountSettingsGroup = new WebMarkupContainer("accountSettingsGroup");
        accountSettingsGroup.setVisible(getAccountSettingsGroupVisibility());

        accountSettingsGroup.add(new CheckBox("enabled"));
        accountSettingsGroup.add(new CheckBox("accountExpired"));
        accountSettingsGroup.add(new CheckBox("accountLocked"));
        accountSettingsGroup.add(new CheckBox("credentialsExpired"));
        accountSettingsGroup.add(createRolesCheckGroup(user));
        return accountSettingsGroup;
    }

    private WebMarkupContainer createDisplayRolesGroup(User user) {
        WebMarkupContainer displayRolesGroup = new WebMarkupContainer("displayRolesGroup");
        displayRolesGroup.setVisible(getDisplayRolesGroupVisibility());
        displayRolesGroup.add(createRolesRepeater(user));
        return displayRolesGroup;
    }

    private CheckGroup<Role> createRolesCheckGroup(User user) {
        CheckGroup<Role> rolesCheckGroup = new CheckGroup<Role>("rolesGroup", user.getRoles());

        ListView<Role> roles = new ListView<Role>("roles", allAvailableRoles) {
            @Override
            protected void populateItem(ListItem<Role> roleListItem) {
                roleListItem.add(new Check<Role>("value", roleListItem.getModel()));
                roleListItem.add(new Label("label", roleListItem.getModel()));
            }
        }.setReuseItems(true);
        rolesCheckGroup.add(roles);
        return rolesCheckGroup;
    }

    private RepeatingView createRolesRepeater(User user) {
        RepeatingView rolesRepeater = new RepeatingView("rolesRepeater");
        for (Role role : user.getRoles()) {
            WebMarkupContainer roleItem = new WebMarkupContainer(rolesRepeater.newChildId());
            rolesRepeater.add(roleItem);
            roleItem.add(new Label("roleName", role.toString()));
//            //MZA: WebMarkupContainer could be removed when ugly hack with " " was used
//            rolesRepeater.add(new Label(rolesRepeater.newChildId(), role + " "));
        }
        return rolesRepeater;
    }

    private WebMarkupContainer createGroupWithTopButtons() {
        WebMarkupContainer buttonsGroup = createInvisibleAtSignupGroup("buttonsGroup");

        buttonsGroup.add(new SaveButton("saveButton"));
        //TODO: MZA: Find a better way to control visibility on the page
        //TODO: MZA: DeleteButton visible only when from list and not new user
        buttonsGroup.add(new DeleteButton("deleteButton"));
        buttonsGroup.add(createCancelButton("cancelButton"));
        return buttonsGroup;
    }

    private Button createCancelButton(String buttonId) {
        Button cancelButton = new Button(buttonId) {
            @Override
            public void onSubmit() {
                onCancelButtonSubmit();
            }
        };
        cancelButton.setDefaultFormProcessing(false);
        return cancelButton;
    }

    //TODO: MZA: Rename to createButtonsGroup
    private WebMarkupContainer createInvisibleAtSignupGroup(String groupId) {
        WebMarkupContainer buttonsGroup = new WebMarkupContainer(groupId);
        buttonsGroup.setVisible(getButtonsGroupVisibility());
        return buttonsGroup;
    }

    private WebMarkupContainer createGroupWithBottomButtons() {
        WebMarkupContainer buttonsBottomGroup = createInvisibleAtSignupGroup("buttonsBottomGroup");

        buttonsBottomGroup.add(new SaveButton("saveButtonBottom"));
        buttonsBottomGroup.add(new DeleteButton("deleteButtonBottom"));
        buttonsBottomGroup.add(createCancelButton("cancelButtonBottom"));
        return buttonsBottomGroup;
    }

    public class AddressFragment extends Fragment {
        public AddressFragment(String id, String markupId, IModel<Address> model) {
            super(id, markupId, UserEditPanel.this, model);
        }

        @Override
        protected void onInitialize() {
            super.onInitialize();
            //moved to onInitilize to prevent:
            // "Make sure you are not calling Component#getString() inside your Component's constructor."
            add(new Label("addressLabel", getString("user.address.address")));
            add(new TextField("address"));
            add(new RequiredTextField("city"));
            add(new RequiredTextField("province"));
            add(new RequiredTextField("postalCode"));
            //TODO: MZA: How to play with IDs? Is it needed? - IChoiceRenderer
            add(new DropDownChoice<String>("country", countryList));
        }
    }

    private final /*static*/ class SaveButton extends Button {

        private SaveButton(String buttonId) {
            super(buttonId, new ResourceModel("button.save"));
        }

        @Override
        public void onSubmit() {
            onSaveButtonSubmit();
        }
    }

    private class DeleteButton extends Button {
        public DeleteButton(String buttonId) {
            super(buttonId);
            setDefaultFormProcessing(false);
            setVisible(getDeleteButtonVisibility());
        }

        @Override
        public void onSubmit() {
            onDeleteButtonSubmit();
        }
    }

    protected abstract void onSaveButtonSubmit();

    protected abstract void onDeleteButtonSubmit();

    protected abstract void onCancelButtonSubmit();

    protected abstract boolean getAccountSettingsGroupVisibility();

    protected abstract boolean getDisplayRolesGroupVisibility();

    protected abstract boolean getDeleteButtonVisibility();

    protected abstract boolean getButtonsGroupVisibility();
}
