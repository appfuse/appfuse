/**
 * 
 */
package org.appfuse.webapp.client.ui.login;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.CheckBox;
import com.github.gwtbootstrap.client.ui.PasswordTextBox;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorDriver;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author ivangsa
 *
 */
public class LoginWidget extends Composite implements LoginView, Editor<LoginView.LoginDetails> {

	interface Binder extends UiBinder<Widget, LoginWidget> {}
	private static Binder uiBinder = GWT.create(Binder.class);
	
	interface Driver extends SimpleBeanEditorDriver<LoginView.LoginDetails, LoginWidget> { }	
	private Driver driver = GWT.create(Driver.class);

	private Delegate delegate;
	
	@UiField TextBox username;
	@UiField PasswordTextBox password;
	@UiField CheckBox spring_security_remember_me;
	
	@UiField Button loginButton;
	
	/**
	 * 
	 */
	public LoginWidget() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
		driver.initialize(this);
	}
	
	@Override
	public void setDelegate(Delegate delegate) {
		driver.edit(new LoginDetails());
		this.delegate = delegate;
	}
	
	@UiHandler("loginButton")
	void onLoginClick(ClickEvent event) {
		delegate.onLoginClick();
	}

	@Override
	public EditorDriver<LoginView.LoginDetails> getEditorDriver() {
		return driver;
	}
	
	
}

