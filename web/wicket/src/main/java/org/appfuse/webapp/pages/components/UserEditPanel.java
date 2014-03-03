package org.appfuse.webapp.pages.components;

import com.google.common.collect.Lists;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.GlyphIconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.tabs.Collapsible;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.*;
import org.appfuse.model.Address;
import org.appfuse.model.Role;
import org.appfuse.model.User;
import org.appfuse.webapp.pages.components.country.SimpleCountryDropDownChoice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

import static java.lang.String.format;

/**
 * Reusable form for editing users.
 *
 * Available abstract methods can be used to define specific behavior for different pages.
 *
 * TODO: MZA: Where to put (possible) different validators configuration?
 *
 * @author Marcin Zajączkowski, 2011-03-14
 */
public abstract class UserEditPanel extends Panel {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final List<Role> allAvailableRoles;

    //TODO: wrap allAvailableRoles into detachable model
    public UserEditPanel(String id, IModel<User> userModel, List<Role> allAvailableRoles) {
        super(id, userModel);
        this.allAvailableRoles = allAvailableRoles;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        add(new Label("usernameLabel", new ResourceModel("user.username")));
        add(new RequiredTextField<String>("username").add(new AutofocusBehavior()).add(new RequiredBehavior()));

        add(createPasswordGroup());

        add(new Label("passwordHintLabel", getString("user.passwordHint")));
        add(new RequiredTextField("passwordHint").add(new RequiredBehavior()));

        add(new Label("firstNameLabel", getString("user.firstName")));
        add(new RequiredTextField("firstName").add(new RequiredBehavior()));

        add(new Label("lastNameLabel", getString("user.lastName")));
        add(new RequiredTextField("lastName").add(new RequiredBehavior()));

        add(new Label("emailLabel", getString("user.email")));
        add(new RequiredTextField("email").add(new RequiredBehavior()));

        add(new Label("phoneNumberLabel", getString("user.phoneNumber")));
        add(new TextField("phoneNumber"));

        add(new Label("websiteLabel", getString("user.website")));
        add(new RequiredTextField("website").add(new RequiredBehavior()));

        add(createCollapsibleAddress());

        PropertyModel<Set<Role>> rolesModel = new PropertyModel<Set<Role>>(getDefaultModel(), "roles");
        add(createAccountSettingsGroup(rolesModel));
        add(createDisplayRolesGroup(rolesModel));
        add(createGroupWithTopButtons());
    }

    private WebMarkupContainer createPasswordGroup() {
        final WebMarkupContainer passwordGroup = new WebMarkupContainer("passwordGroup");
        passwordGroup.add(new Label("passwordLabel", getString("user.password")));
        //TODO: setResetPassword() disabled temporarily to allow user edition without entering password each time
        // See APF-1370
        passwordGroup.add(new PasswordTextField("password").setResetPassword(false).add(new RequiredBehavior()));
        passwordGroup.add(new Label("confirmPasswordLabel", getString("user.confirmPassword")));
        passwordGroup.add(new PasswordTextField("confirmPassword").setResetPassword(false).add(new RequiredBehavior()));
        return passwordGroup;
    }

    private Collapsible createCollapsibleAddress() {
        final PropertyModel<Address> addressModel = new PropertyModel<Address>(getDefaultModel(), "address");
        AbstractTab addressTab = new AbstractTab(new ResourceModel("user.address.address")) {
            @Override
            public WebMarkupContainer getPanel(String panelId) {
                return new AddressFragment(panelId, "address", new CompoundPropertyModel<Address>(addressModel));
            }
        };
        return new Collapsible("collapsibleAddress", Lists.<ITab>newArrayList(addressTab), shouldAddressTabBeCollapsed());
    }

    private Model<Integer> shouldAddressTabBeCollapsed() {
        return isCollapsedAddressTab() ? Model.of(-1) : Model.of(0);
    }

    private WebMarkupContainer createAccountSettingsGroup(IModel<Set<Role>> rolesModel) {
        final WebMarkupContainer accountSettingsGroup = new WebMarkupContainer("accountSettingsGroup");
        accountSettingsGroup.setVisible(getAccountSettingsGroupVisibility());

        accountSettingsGroup.add(new CheckBox("enabled"));
        accountSettingsGroup.add(new CheckBox("accountExpired"));
        accountSettingsGroup.add(new CheckBox("accountLocked"));
        accountSettingsGroup.add(new CheckBox("credentialsExpired"));
        accountSettingsGroup.add(createRolesCheckGroup(rolesModel));
        return accountSettingsGroup;
    }

    private WebMarkupContainer createDisplayRolesGroup(IModel<Set<Role>> rolesModel) {
        WebMarkupContainer displayRolesGroup = new WebMarkupContainer("displayRolesGroup");
        displayRolesGroup.setVisible(getDisplayRolesGroupVisibility());
        displayRolesGroup.add(createRolesRepeater(rolesModel));
        return displayRolesGroup;
    }

    private CheckGroup<Role> createRolesCheckGroup(IModel<Set<Role>> rolesModel) {
        CheckGroup<Role> rolesCheckGroup = new CheckGroup<Role>("rolesGroup", rolesModel);

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

    private RepeatingView createRolesRepeater(IModel<Set<Role>> rolesModel) {
        RepeatingView rolesRepeater = new RepeatingView("rolesRepeater");
        Set<Role> roles = rolesModel.getObject();
        for (Role role : roles) {
            WebMarkupContainer roleItem = new WebMarkupContainer(rolesRepeater.newChildId());
            rolesRepeater.add(roleItem);
            roleItem.add(new Label("roleName", "[" + role.toString() + "]"));
//            //MZA: WebMarkupContainer could be removed when ugly hack with " " was used
//            rolesRepeater.add(new Label(rolesRepeater.newChildId(), role + " "));
        }
        return rolesRepeater;
    }

    private WebMarkupContainer createGroupWithTopButtons() {
        WebMarkupContainer buttonsGroup = createButtonsGroup("buttonsGroup");

        buttonsGroup.add(new SaveButton("saveButton"));
        //TODO: MZA: Find a better way to control visibility on the page
        //TODO: MZA: DeleteButton visible only when from list and not new user
        buttonsGroup.add(new DeleteButton("deleteButton", generateDeleteConfirmMessage()));
        buttonsGroup.add(createCancelButton("cancelButton"));
        return buttonsGroup;
    }

    private WebMarkupContainer createButtonsGroup(String groupId) {
        return new WebMarkupContainer(groupId);
    }

    private String generateDeleteConfirmMessage() {
        return new StringResourceModel(
                    "delete.confirm", this, null, new Object[]{getString("userList.user")}).getString();
    }

    private BootstrapLink<Panel> createCancelButton(String buttonId) {
        return new BootstrapLink<Panel>(buttonId, Model.<Panel>of(this), Buttons.Type.Default) {
            @Override
            public void onClick() {
                onCancelButtonSubmit();
            }
        }.setIconType(GlyphIconType.remove).setLabel(new ResourceModel("button.cancel"));
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
            add(new TextField("address"));
            add(new RequiredTextField("city").add(new RequiredBehavior()));
            add(new RequiredTextField("province").add(new RequiredBehavior()));
            add(new RequiredTextField("postalCode").add(new RequiredBehavior()));
            SimpleCountryDropDownChoice countries = new SimpleCountryDropDownChoice("country",
                    new PropertyModel<String>(getDefaultModel(), "country"));
            add(countries.setRequired(true).add(new RequiredBehavior()));
        }
    }

    private final /*static*/ class SaveButton extends BootstrapButton {

        private SaveButton(String buttonId) {
            super(buttonId, new ResourceModel("button.save"), Buttons.Type.Primary);
            setIconType(GlyphIconType.ok);
        }

        @Override
        public void onSubmit() {
            onSaveButtonSubmit();
        }
    }

    private class DeleteButton extends BootstrapButton {
        public DeleteButton(String buttonId, String confirmMessage) {
            super(buttonId, new ResourceModel("button.delete"), Buttons.Type.Danger);
            setIconType(GlyphIconType.trash);
            setDefaultFormProcessing(false);
            setVisible(getDeleteButtonVisibility());

            add(new AttributeAppender("onclick", format("return confirmMessage('%s')", confirmMessage)));
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

    protected abstract boolean isCollapsedAddressTab();
}
