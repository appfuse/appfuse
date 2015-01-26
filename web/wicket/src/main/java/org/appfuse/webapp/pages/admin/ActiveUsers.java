package org.appfuse.webapp.pages.admin;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapBookmarkablePageLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.GlyphIconType;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.model.ResourceModel;
import org.appfuse.model.User;
import org.appfuse.webapp.pages.AbstractWebPage;
import org.appfuse.webapp.SSAuthenticatedWebSession;
import org.appfuse.webapp.pages.Home;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.ArrayList;
import java.util.List;

/**
 * Page displaying a list of users that have logged in and their sessions have not expired.
 *
 * TODO: MZA: Partial duplication with UserList. Can be removed?
 */
@MountPath("admin/activeUsers")
public class ActiveUsers extends AbstractWebPage {

    public static final int ACTIVE_USERS_PER_PAGE = 20;

    @Override
    protected void onInitialize() {
        super.onInitialize();

        add(createPageTitleTag("activeUsers.title"));
        add(createFeedbackPanel());
        add(createPageHeading("activeUsers.heading"));

        add(createDoneButton());
        add(createActiveUsersTable());
    }

    private BootstrapBookmarkablePageLink<String> createDoneButton() {
        return new BootstrapBookmarkablePageLink<String>("doneButton", Home.class, Buttons.Type.Primary)
                .setLabel(new ResourceModel("button.done"))
                .setIconType(GlyphIconType.ok);
    }

    private AjaxFallbackDefaultDataTable<User, String> createActiveUsersTable() {
        List<User> userNames = ((SSAuthenticatedWebSession)getSession()).getActiveUsers();
        StaticUserDataProvider userProvider = new StaticUserDataProvider(userNames);
        return new AjaxFallbackDefaultDataTable<User, String>(
                "activeUsersTable", createColumns(), userProvider, ACTIVE_USERS_PER_PAGE);
    }

    private List<IColumn<User, String>> createColumns() {
        List<IColumn<User, String>> userListColumns = new ArrayList<IColumn<User, String>>();
        userListColumns.add(createColumn("user.username", "username", "username"));
        userListColumns.add(createColumn("activeUsers.fullName", "fullName", "fullName"));
        return userListColumns;
    }

    private PropertyColumn<User, String> createColumn(String key, String sortProperty, String propertyExpression) {
        return new PropertyColumn<User, String>(new ResourceModel(key), sortProperty, propertyExpression);
    }
}
