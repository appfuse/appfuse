package org.appfuse.webapp.client.ui.users.active;

import java.util.HashSet;
import java.util.Set;

import org.appfuse.webapp.client.application.ApplicationResources;
import org.appfuse.webapp.client.application.utils.tables.CustomColumn;
import org.appfuse.webapp.client.proxies.UserProxy;

import com.github.gwtbootstrap.client.ui.Button;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class ActiveUsersViewImpl extends Composite implements ActiveUsersView {

    interface Binder extends UiBinder<Widget, ActiveUsersViewImpl> {
    }

    private static final Binder uiBinder = GWT.create(Binder.class);

    private Delegate delegate;

    @UiField(provided = true)
    ApplicationResources i18n = GWT.create(ApplicationResources.class);

    @UiField
    Button doneButton;

    @UiField
    CellTable<UserProxy> table;
    Set<String> paths = new HashSet<String>();

    public ActiveUsersViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
        table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
        createTableColumns();
    }

    @Override
    public void setDelegate(final Delegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public CellTable<UserProxy> getCellTable() {
        return table;
    }

    @Override
    public String[] getPaths() {
        return paths.toArray(new String[paths.size()]);
    }

    @UiHandler("doneButton")
    public void doneClicked(final ClickEvent event) {
        delegate.cancelClicked();
    }

    public void createTableColumns() {

        int columnNumber = 0;
        paths.add("username");
        table.addColumn(new CustomColumn<UserProxy, String>("username", true) {
            @Override
            public String getValue(final UserProxy user) {
                return user.getUsername();
            }
        }, i18n.user_username());
        table.setColumnWidth(columnNumber++, "30%");

        paths.add("email");
        paths.add("firstName");
        paths.add("lastName");
        table.addColumn(new CustomColumn<UserProxy, String>("firstName", true) {

            @Override
            public String getValue(final UserProxy user) {
                return user.getFirstName() + " " + user.getLastName();
            }

            @Override
            public void render(final Context context, final UserProxy object, final SafeHtmlBuilder sb) {
                final String template =
                        SafeHtmlUtils.htmlEscape(getValue(object)) + " " +
                                "<a href=\"mailto:" + SafeHtmlUtils.htmlEscape(object.getEmail()) + "\">\n" +
                                "	<img class=\"icon\" alt=\"E-Mail\" src=\"images/iconEmail.gif\">\n" +
                                "</a>";
                sb.append(SafeHtmlUtils.fromTrustedString(template));
            };
        }, i18n.activeUsers_fullName());

    }

}
