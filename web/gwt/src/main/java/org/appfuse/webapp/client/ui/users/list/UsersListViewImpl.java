package org.appfuse.webapp.client.ui.users.list;

import java.util.HashSet;
import java.util.Set;

import org.appfuse.webapp.client.application.base.view.AbstractProxyListView;
import org.appfuse.webapp.proxies.UserProxy;

import com.github.gwtbootstrap.client.ui.Button;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.HTMLPanel;

public class UsersListViewImpl extends AbstractProxyListView<UserProxy> implements UsersListView {

    interface Binder extends UiBinder<HTMLPanel, UsersListViewImpl> { }
    private static final Binder uiBinder = GWT.create(Binder.class);

    @UiField
    CellTable<UserProxy> table;
    Set<String> paths = new HashSet<String>();

    @UiField
    Button addButton;

    public UsersListViewImpl() {
        init(uiBinder.createAndBindUi(this), table);
        table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);        
        createTableColumns();
    }

    public String[] getPaths() {
    	return paths.toArray(new String[paths.size()]);
    }
    
    @UiHandler("addButton")
    public void addButtonClicked(ClickEvent event) {
    	delegate.addClicked();
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
