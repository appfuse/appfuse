/**
 * 
 */
package org.appfuse.webapp.client.ui.users.edit.views;

import java.util.List;

import org.appfuse.webapp.client.application.ApplicationResources;
import org.appfuse.webapp.proxies.LabelValueProxy;
import org.appfuse.webapp.proxies.RoleProxy;
import org.appfuse.webapp.proxies.UserProxy;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.CheckBox;
import com.github.gwtbootstrap.client.ui.IntegerBox;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.client.ui.LongBox;
import com.github.gwtbootstrap.client.ui.Paragraph;
import com.github.gwtbootstrap.client.ui.PasswordTextBox;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;

/**
 * @author ivangsa
 *
 */
public class EditUserViewImpl extends Composite implements EditUserView {

	interface Binder extends UiBinder<Widget, EditUserViewImpl> {}
	private static final Binder BINDER = GWT.create(Binder.class);

	interface Driver extends RequestFactoryEditorDriver<UserProxy, EditUserViewImpl> { }
	
	@UiField(provided=true) ApplicationResources i18n = GWT.create(ApplicationResources.class);
	
	private EditUserView.Delegate delegate;
	
	@UiField Paragraph subheading;
	
    @UiField LongBox id;
    @UiField IntegerBox version;
    @UiField TextBox username;
    @UiField PasswordTextBox password;
    @UiField PasswordTextBox confirmPassword;
    @UiField TextBox passwordHint;
    @UiField TextBox firstName;
    @UiField TextBox lastName;
    @UiField TextBox email;
    @UiField TextBox phoneNumber;
    @UiField TextBox website;

    @UiField UIObject addressFieldset;
    @UiField @Path("address.address")
    TextBox address;
    @UiField @Path("address.city")
    TextBox city;
    @UiField @Path("address.province")
    TextBox province;
    @UiField @Path("address.country")
    ListBox country;
    @UiField  @Path("address.postalCode")
    TextBox postalCode;    
    
    @UiField(provided=true) 
    RolesListBox roles = new RolesListBox();
    
    @UiField Widget userRoles;//control group for
    @UiField Widget accountSettings;//control group for

    @UiField CheckBox enabled;
    @UiField CheckBox accountExpired;
    @UiField CheckBox accountLocked;
    @UiField CheckBox credentialsExpired;
    
    @UiField Button saveButton;
    @UiField Button deleteButton;
    @UiField Button cancelButton;
	
	/**
	 * 
	 */
	public EditUserViewImpl() {
		super();
		initWidget(BINDER.createAndBindUi(this));
	}
	
	public void setDelegate(EditUserView.Delegate delegate) {
		this.delegate = delegate;
	}
	
	@Override
	public RequestFactoryEditorDriver<UserProxy, ? extends EditUserView> createEditorDriver() {
		RequestFactoryEditorDriver<UserProxy, EditUserViewImpl> editorDriver = GWT.create(Driver.class);
		editorDriver.initialize(this);		
		return editorDriver;
	}

	@Override
	public void setCountries(List<LabelValueProxy> countries) {
		for (LabelValueProxy labelValue : countries) {
			country.addItem(labelValue.getLabel(), labelValue.getValue());
		}
	}
	
	@Override
	public void setAvailableRoles(List<RoleProxy> roles) {
		this.roles.setRowData(roles);
	}

	@Override
	public void showErrors(List<EditorError> errors) {
		if(errors != null && !errors.isEmpty()) {
	        SafeHtmlBuilder b = new SafeHtmlBuilder();
	        for (EditorError error : errors) {
	        	if(error.getPath() != null && !"".equals(error.getPath())) {
	        		Object userData = error.getUserData();
	        		b.appendEscaped(error.getPath()).appendEscaped(": ");
	        	}
        		b.appendEscaped(error.getMessage()).appendEscaped("\n");
	        }
			Window.alert(b.toSafeHtml().asString());
		}
	}
	
	@UiHandler("saveButton")
	public void onSaveButtonClick(ClickEvent event) {
		delegate.saveClicked();
	}

	@UiHandler("deleteButton")
	public void onDeleteButtonClick(ClickEvent event) {
		delegate.deleteClicked();
	}

	@UiHandler("cancelButton")
	public void onCancelButtonClick(ClickEvent event) {
		delegate.cancelClicked();
	}


	@Override
	public void setEnabled(boolean b) {
		//TODO
	}

}
