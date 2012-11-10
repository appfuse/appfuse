package org.appfuse.webapp.pages.admin;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.html.basic.Label;
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
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.appfuse.webapp.AbstractWebPage;
import org.appfuse.webapp.pages.FromListUserEdit;
import org.appfuse.webapp.pages.MainMenu;
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

    @Override
    protected void onInitialize() {
        super.onInitialize();

        //TODO: MZA: Externalize to separate class
        FeedbackPanel feedbackPanel = new FeedbackPanel("feedback") {
            @Override
            protected String getCSSClass(final FeedbackMessage message) {
                return message.getLevelAsString().toLowerCase();
            }
        };
        add(feedbackPanel);

        add(createAddButton("addButtonTop"));
        add(createDoneButton("doneButtonTop"));
        
        add(createAddButton("addButtonBottom"));
        add(createDoneButton("doneButtonBottom"));

        add(createUserListTable());
    }

    private Link createDoneButton(String buttonId) {
        return new Link(buttonId) {
            @Override
            public void onClick() {
                setResponsePage(MainMenu.class);
            }
        };
    }

    private Link createAddButton(String buttonId) {
        return new Link(buttonId) {
            @Override
            public void onClick() {
                log.info("addButton submitted");
                User user = new User();
                user.addRole(new Role(Constants.USER_ROLE));
                //TODO: MZA: Is it the best way to create that Model here?
                setResponsePage(new FromListUserEdit(getPage(), new Model<User>(user)));
            }
        };
    }

    private AjaxFallbackDefaultDataTable<User> createUserListTable() {
        return new AjaxFallbackDefaultDataTable<User>(
                "userListTable", createColumns(), new UserDataProvider(userManager), ROWS_PER_PAGE);
    }

    private List<IColumn<User>> createColumns() {
        List<IColumn<User>> userListColumns = new ArrayList<IColumn<User>>();
        userListColumns.add(createLinkableColumn("user.username", "username", "username"));
        userListColumns.add(createColumn("user.lastName", "lastName", "lastName"));
        userListColumns.add(createColumn("user.email", "email", "email"));
        //TODO: MZA: Is there a "boolean" column? Check version from PhoneBook
        userListColumns.add(createColumn("user.enabled", "enabled", "enabled"));
        return userListColumns;
    }

    private IColumn<User> createLinkableColumn(String key, String sortProperty, String propertyExpression) {
        return new PropertyColumn<User>(new ResourceModel(key), sortProperty, propertyExpression) {
            @Override
            public void populateItem(Item<ICellPopulator<User>> iCellPopulatorItem, String componentId,
                                     IModel<User> rowModel) {
                iCellPopulatorItem.add(new UsernamePanel(componentId, rowModel));
            }
        };
    }

    private PropertyColumn<User> createColumn(String key, String sortProperty, String propertyExpression) {
        return new PropertyColumn<User>(new ResourceModel(key), sortProperty, propertyExpression);
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
