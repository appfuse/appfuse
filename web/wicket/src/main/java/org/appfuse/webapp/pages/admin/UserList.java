package org.appfuse.webapp.pages.admin;

import de.agilecoders.wicket.markup.html.bootstrap.button.*;
import de.agilecoders.wicket.markup.html.bootstrap.image.IconType;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.*;
import org.appfuse.Constants;
import org.appfuse.model.Role;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.appfuse.webapp.AbstractWebPage;
import org.appfuse.webapp.pages.FromListUserEdit;
import org.appfuse.webapp.pages.MainMenu;
import org.appfuse.webapp.pages.components.PlaceholderBehavior;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.*;

/**
 * Page for displaying user list.
 *
 * @author Marcin Zajączkowski, 2010-09-11
 */
@MountPath("admin/userList")
@AuthorizeInstantiation("ROLE_ADMIN")
public class UserList extends AbstractWebPage {

    private static final int ROWS_PER_PAGE = 3; //10 - 3 is for paging test

    @SpringBean(name = "userManager")
    private UserManager userManager;
    private TextField<String> searchQuery;
    private UserDataProvider userDataProvider;

    @Override
    protected void onInitialize() {
        super.onInitialize();

        add(createPageTitleTag("userList.title"));
        add(createFeedbackPanel());
        add(createPageHeading("userList.heading"));
        add(createSearchForm());
        add(createAddButton());
        add(createDoneButton());
        
        add(createUserListTable());
    }

    private Form createSearchForm() {
        Form<Void> searchForm = new Form<Void>("searchForm") {
            @Override
            protected void onSubmit() {
                //TODO: MZA: Could be done without refreshing the whole page?
                userDataProvider.setSearchFilter(searchQuery.getModelObject());
            }
        };
        searchQuery = new TextField<String>("searchQuery", Model.of(""));
        searchQuery.add(new PlaceholderBehavior(getString("search.enterTerms")));
        searchForm.add(searchQuery);
        return searchForm;
    }

    private TypedLink<String> createDoneButton() {
        return new TypedLink<String>("doneButton", new ResourceModel("button.done"), ButtonType.Default) {
            @Override
            public void onClick() {
                setResponsePage(MainMenu.class);
            }
        }.setIconType(IconType.ok).setInverted(false);
    }

    private TypedLink<String> createAddButton() {
        return new TypedLink<String>("addButton", new ResourceModel("button.add"), ButtonType.Primary) {

            @Override
            public void onClick() {
                log.info("addButton submitted");
                User user = new User();
                user.addRole(new Role(Constants.USER_ROLE));
                //TODO: MZA: Is it the best way to create that Model here?
                setResponsePage(new FromListUserEdit(getPage(), new Model<User>(user)));
            }
        }.setIconType(IconType.plus);
    }

    private AjaxFallbackDefaultDataTable<User, String> createUserListTable() {
        userDataProvider = new UserDataProvider(userManager);
        return new AjaxFallbackDefaultDataTable<User, String>(
                "userListTable", createColumns(), userDataProvider, ROWS_PER_PAGE);
    }

    private List<IColumn<User, String>> createColumns() {
        List<IColumn<User, String>> userListColumns = new ArrayList<IColumn<User, String>>();
        userListColumns.add(createLinkableColumn("user.username", "username", "username"));
        userListColumns.add(createColumn("activeUsers.fullName", "fullName", "fullName"));
        userListColumns.add(createColumn("user.email", "email", "email"));
        //TODO: MZA: Is there a "boolean" column? Check version from PhoneBook
        userListColumns.add(createColumn("user.enabled", "enabled", "enabled"));
        return userListColumns;
    }

    private IColumn<User, String> createLinkableColumn(String key, String sortProperty, String propertyExpression) {
        return new PropertyColumn<User, String>(new ResourceModel(key), sortProperty, propertyExpression) {
            @Override
            public void populateItem(Item<ICellPopulator<User>> iCellPopulatorItem, String componentId,
                                     IModel<User> rowModel) {
                iCellPopulatorItem.add(new UsernamePanel(componentId, rowModel));
            }
        };
    }

    private PropertyColumn<User, String> createColumn(String key, String sortProperty, String propertyExpression) {
        return new PropertyColumn<User, String>(new ResourceModel(key), sortProperty, propertyExpression);
    }

    //TODO: MZA: Change to LinkPanel make more generic
    private class UsernamePanel extends Panel {

        private UsernamePanel(String id, IModel<User> userModel) {
            super(id, userModel);

            //TODO: MZA: The whole row should be clickable
            //TODO: MZA: A cursor isn't changed when on link
            final Link<User> editLink = new Link<User>("editLink", userModel) {
                @Override
                public void onClick() {
                    User clickedUser = getModelObject();
                    log.info("clicked {}", clickedUser.getUsername());

                    setResponsePage(new FromListUserEdit(getPage(), getModel()));
                }
            };
            add(editLink);

            editLink.add(new Label("editLabel", new Model<String>(userModel.getObject().getUsername())));
        }
    }
}
