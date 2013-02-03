package org.appfuse.webapp.client.ui.users.search;

import java.util.HashSet;
import java.util.Set;

import org.appfuse.webapp.client.application.base.view.AbstractProxySearchView;
import org.appfuse.webapp.client.ui.login.LoginView;
import org.appfuse.webapp.proxies.UserProxy;
import org.appfuse.webapp.proxies.UsersSearchCriteriaProxy;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorDriver;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Widget;

public class UsersSearchViewImpl extends AbstractProxySearchView<UserProxy, UsersSearchCriteriaProxy> implements UsersSearchView, Editor<UsersSearchCriteriaProxy> {

    interface Binder extends UiBinder<Widget, UsersSearchViewImpl> { }
    private static final Binder uiBinder = GWT.create(Binder.class);

	interface Driver extends SimpleBeanEditorDriver<UsersSearchCriteriaProxy, UsersSearchViewImpl> { }	
	private Driver driver = GWT.create(Driver.class);
    
  
    @UiField TextBox searchTerm;
    
    @UiField Button addButton;
    @UiField Button doneButton;
    @UiField Button searchButton;

    @UiField CellTable<UserProxy> table;
    Set<String> paths = new HashSet<String>();

    public UsersSearchViewImpl() {
        init(uiBinder.createAndBindUi(this), table);
        driver.initialize(this);
        table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);        
        createTableColumns();
    }

    @Override
    public String[] getPaths() {
    	return paths.toArray(new String[paths.size()]);
    }

    @Override
    public void setSearchCriteria(UsersSearchCriteriaProxy searchCriteria) {
    	driver.edit(searchCriteria);
    }
    
    @Override
    public UsersSearchCriteriaProxy getSearchCriteria() {
    	return driver.flush();
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
        paths.add("id");
        table.addColumn(new TextColumn<UserProxy>() {

            Renderer<java.lang.Long> renderer = new AbstractRenderer<java.lang.Long>() {

                public String render(java.lang.Long obj) {
                    return obj == null ? "" : String.valueOf(obj);
                }
            };

            @Override
            public String getValue(UserProxy object) {
                return renderer.render(object.getId());
            }
        }, "Id");
        paths.add("version");
        table.addColumn(new TextColumn<UserProxy>() {

            Renderer<java.lang.Integer> renderer = new AbstractRenderer<java.lang.Integer>() {

                public String render(java.lang.Integer obj) {
                    return obj == null ? "" : String.valueOf(obj);
                }
            };

            @Override
            public String getValue(UserProxy object) {
                return renderer.render(object.getVersion());
            }
        }, "Version");
        paths.add("firstName");
        table.addColumn(new TextColumn<UserProxy>() {

            Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

                public String render(java.lang.String obj) {
                    return obj == null ? "" : String.valueOf(obj);
                }
            };

            @Override
            public String getValue(UserProxy object) {
                return renderer.render(object.getFirstName());
            }
        }, "First Name");
        paths.add("lastName");
        table.addColumn(new TextColumn<UserProxy>() {

            Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

                public String render(java.lang.String obj) {
                    return obj == null ? "" : String.valueOf(obj);
                }
            };

            @Override
            public String getValue(UserProxy object) {
                return renderer.render(object.getLastName());
            }
        }, "Last Name");
        paths.add("address");
        table.addColumn(new TextColumn<UserProxy>() {

            Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

                public String render(java.lang.String obj) {
                    return obj == null ? "" : String.valueOf(obj);
                }
            };

            @Override
            public String getValue(UserProxy object) {
            	if(object.getAddress() != null) {
            		return renderer.render(object.getAddress().getAddress());
            	}
            	return null;
            }
        }, "Address");

        paths.add("email");
        table.addColumn(new TextColumn<UserProxy>() {
        	
        	Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {
        		
        		public String render(java.lang.String obj) {
        			return obj == null ? "" : String.valueOf(obj);
        		}
        	};
        	
        	@Override
        	public String getValue(UserProxy object) {
        		return renderer.render(object.getEmail());
        	}
        }, "Email");

        paths.add("website");
        table.addColumn(new TextColumn<UserProxy>() {

            Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

                public String render(java.lang.String obj) {
                    return obj == null ? "" : String.valueOf(obj);
                }
            };

            @Override
            public String getValue(UserProxy object) {
                return renderer.render(object.getWebsite());
            }
        }, "Website");

    }

}
