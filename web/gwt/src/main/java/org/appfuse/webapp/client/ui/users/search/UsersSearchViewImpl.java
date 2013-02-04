package org.appfuse.webapp.client.ui.users.search;

import java.util.HashSet;
import java.util.Set;

import org.appfuse.webapp.client.application.ApplicationResources;
import org.appfuse.webapp.client.application.base.view.AbstractProxySearchView;
import org.appfuse.webapp.proxies.UserProxy;
import org.appfuse.webapp.proxies.UsersSearchCriteriaProxy;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorDriver;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;

public class UsersSearchViewImpl extends AbstractProxySearchView<UserProxy, UsersSearchCriteriaProxy> implements UsersSearchView, Editor<UsersSearchCriteriaProxy> {

    interface Binder extends UiBinder<Widget, UsersSearchViewImpl> { }
    private static final Binder uiBinder = GWT.create(Binder.class);

	interface Driver extends SimpleBeanEditorDriver<UsersSearchCriteriaProxy, UsersSearchViewImpl> { }	
	private Driver editorDriver = GWT.create(Driver.class);
	
	@UiField(provided=true) ApplicationResources i18n = GWT.create(ApplicationResources.class);
  
    @UiField TextBox searchTerm;
    
    @UiField Button addButton;
    @UiField Button doneButton;
    @UiField Button searchButton;

    @UiField CellTable<UserProxy> table;
    Set<String> paths = new HashSet<String>();

    public UsersSearchViewImpl() {
        init(uiBinder.createAndBindUi(this), table);
        editorDriver.initialize(this);
        table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
        createTableColumns();
        
		ProvidesKey<UserProxy> keyProvider = table.getKeyProvider();
		final SingleSelectionModel<UserProxy> selectionModel = new SingleSelectionModel<UserProxy>(keyProvider);
		table.setSelectionModel(selectionModel);

		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				UserProxy selectedObject = selectionModel.getSelectedObject();
				if (selectedObject != null) {
					delegate.showDetails(selectedObject);
				}
			}
		});		        
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
    
    public void createTableColumns() {
        paths.add("username");
        table.addColumn(new Column<UserProxy, Anchor>(new AnchorCell()){

			@Override
			public Anchor getValue(final UserProxy object) {
				Anchor anchor = new Anchor(SafeHtmlUtils.fromString(object.getUsername()));
				anchor.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						delegate.showDetails(object);
					}
				});
				return anchor;
			}
        }, i18n.user_username());
        
        paths.add("firstName");
        paths.add("lastName");
        table.addColumn(new TextColumn<UserProxy>() {
            @Override
            public String getValue(UserProxy object) {
                return object.getFirstName() + " " + object.getLastName();
            }
        }, i18n.activeUsers_fullName());

        paths.add("email");
        table.addColumn(new Column<UserProxy, Anchor>(new AnchorCell()){

			@Override
			public Anchor getValue(UserProxy object) {
				return new Anchor(SafeHtmlUtils.fromString(object.getEmail()), "mailto:" + object.getEmail());
			}
        	
        }, i18n.user_email());

        paths.add("enabled");
        table.addColumn(new Column<UserProxy, Boolean>(new CheckboxCell(false, false)) {
              @Override
              public Boolean getValue(UserProxy object) {
                return object.isEnabled();
              }
        }, i18n.user_enabled());

    }

    private class AnchorCell extends AbstractCell<Anchor> {

		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context, Anchor value, SafeHtmlBuilder sb) {
			sb.append(SafeHtmlUtils.fromTrustedString(value.toString()));
		}
	};
}
