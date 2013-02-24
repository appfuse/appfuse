package org.appfuse.webapp.client.ui.users.search;

import java.util.HashSet;
import java.util.Set;

import org.appfuse.webapp.client.application.ApplicationResources;
import org.appfuse.webapp.client.application.base.view.AbstractProxySearchView;
import org.appfuse.webapp.client.application.utils.tables.CustomColumn;
import org.appfuse.webapp.proxies.UserProxy;
import org.appfuse.webapp.proxies.UsersSearchCriteriaProxy;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorDriver;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.ColumnSortList;
import com.google.gwt.user.cellview.client.ColumnSortEvent.Handler;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasData;

public class UsersSearchViewImpl extends AbstractProxySearchView<UserProxy, UsersSearchCriteriaProxy> implements UsersSearchView, Editor<UsersSearchCriteriaProxy> {

    interface Binder extends UiBinder<Widget, UsersSearchViewImpl> { }
    private static final Binder uiBinder = GWT.create(Binder.class);

	interface Driver extends SimpleBeanEditorDriver<UsersSearchCriteriaProxy, UsersSearchViewImpl> { }	
	private Driver editorDriver = GWT.create(Driver.class);
	
	@UiField(provided=true) ApplicationResources i18n = GWT.create(ApplicationResources.class);
  
    @UiField TextBox searchTerm;
    
    @UiField Button addButton;
    @UiField Button doneButton;
    @UiField com.google.gwt.user.client.ui.Button searchButton;

    @UiField CellTable<UserProxy> table;
    Set<String> paths = new HashSet<String>();

    public UsersSearchViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
        editorDriver.initialize(this);
        table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
        createTableColumns();
    }

    @Override
    public HasData<UserProxy> asHasData() {
    	return table;
    }
    
    @Override
    public ColumnSortList getColumnSortList() {
    	return table.getColumnSortList();
    }
    
    @Override
    public void addColumnSortHandler(Handler clientSideSortHandler) {
    	table.addColumnSortHandler(clientSideSortHandler);
    }
    
    @Override
    public String[] getPaths() {
    	return paths.toArray(new String[paths.size()]);
    }

    @Override
    public EditorDriver<UsersSearchCriteriaProxy> getEditorDriver() {
    	return editorDriver;
    }
    
    @Override
    public void setSearchCriteria(UsersSearchCriteriaProxy searchCriteria) {
    	editorDriver.edit(searchCriteria);
    }
    
    @UiHandler("addButton")
    public void addButtonClicked(ClickEvent event) {
    	delegate.addClicked();
    }

    @UiHandler("doneButton")
    public void doneClicked(ClickEvent event) {
    	delegate.cancelClicked();
    }
    
    @UiHandler("searchButton")
    public void searchButtonClicked(ClickEvent event) {
    	delegate.searchClicked();
    }
    
	@UiHandler("searchTerm")
	void defaultAction(KeyDownEvent event) {
		if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
			delegate.searchClicked();
		}
	}
    
    public void createTableColumns() {
    	FieldUpdater<UserProxy, String> showDetails = new FieldUpdater<UserProxy, String>() {
			@Override
			public void update(int index, UserProxy object, String value) {
				delegate.showDetails(object);
			}
		};
    	
		int columnNumber = 0;
        paths.add("username");
        table.addColumn(new CustomColumn<UserProxy, String>(showDetails, true) {
			@Override
			public String getValue(UserProxy user) {
				return user.getUsername();
			}
			@Override
			public void render(Context context, UserProxy object, SafeHtmlBuilder sb) {
				Anchor anchor = new Anchor(SafeHtmlUtils.htmlEscape(getValue(object)));
				sb.append(SafeHtmlUtils.fromTrustedString(anchor.toString()));
			};
		}, i18n.user_username());
        table.setColumnWidth(columnNumber++, "25%");
        
        paths.add("firstName");
        paths.add("lastName");
        table.addColumn(new CustomColumn<UserProxy, String>(true) {

			@Override
			public String getValue(UserProxy user) {
				return user.getFirstName() + " " + user.getLastName();
			}
		}, i18n.activeUsers_fullName());
        table.setColumnWidth(columnNumber++, "34%");
        
        paths.add("email");
        table.addColumn(new CustomColumn<UserProxy, String>(true) {

			@Override
			public String getValue(UserProxy user) {
				return user.getEmail();
			}
			@Override
			public void render(Context context, UserProxy object, SafeHtmlBuilder sb) {
				String email = object.getEmail();
				Anchor anchor = new Anchor(SafeHtmlUtils.htmlEscape(email), "mailto:" + email);
				sb.append(SafeHtmlUtils.fromTrustedString(anchor.toString()));
			};			
		}, i18n.user_email());           
        table.setColumnWidth(columnNumber++, "25%");
        
        paths.add("enabled");
        table.addColumn(new CustomColumn<UserProxy, Boolean>(true) {
			@Override
			public Boolean getValue(UserProxy user) {
				return user.isEnabled();
			}
			@Override
			public void render(Context context, UserProxy object, SafeHtmlBuilder sb) {
				boolean isEnabled = object.isEnabled();
				CheckBox checkBox = new CheckBox();
				checkBox.setValue(isEnabled);
				checkBox.setEnabled(false);
				sb.append(SafeHtmlUtils.fromTrustedString(checkBox.toString()));
			};				
		}, i18n.user_enabled());          
        table.setColumnWidth(columnNumber++, "16%");
        
    }

}
