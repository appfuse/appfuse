/**
 * 
 */
package org.appfuse.webapp.client.ui.login;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.CheckBox;
import com.github.gwtbootstrap.client.ui.PasswordTextBox;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.base.AlertBase;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorDriver;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author ivangsa
 *
 */
public class LoginForm extends Composite implements LoginView, Editor<LoginView.LoginDetails> {

	interface Binder extends UiBinder<Widget, LoginForm> {}
	private static Binder uiBinder = GWT.create(Binder.class);
	
	interface Driver extends SimpleBeanEditorDriver<LoginView.LoginDetails, LoginForm> { }	
	private Driver driver = GWT.create(Driver.class);

	private Delegate delegate;
	
	@UiField SimplePanel messages;
	
	@UiField TextBox username;
	@UiField PasswordTextBox password;
	
	@UiField Widget rememberMeControl;
	@UiField CheckBox rememberMe;
	
	@UiField Button loginButton;
	
	/**
	 * 
	 */
	public LoginForm() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
		username.getElement().setAttribute("required", "required");
		username.getElement().setAttribute("autofocus", "autofocus");
		password.getElement().setAttribute("required", "required");
		driver.initialize(this);
	}
	
	@Override
	public void setDelegate(Delegate delegate) {
		driver.edit(new LoginDetails());
		this.delegate = delegate;
	}
	
	public void setMessage(AlertBase alert) {
		messages.clear();
		messages.add(alert);
	}
	
	@UiHandler("loginButton")
	void onLoginClick(ClickEvent event) {
		delegate.onLoginClick();
	}

	@UiHandler({"username", "password"})
	void defaultAction(KeyDownEvent event) {
		if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
			delegate.onLoginClick();
		}
	}
	
	@Override
	public EditorDriver<LoginView.LoginDetails> getEditorDriver() {
		return driver;
	}
	
	@Override
	public void setRememberMeEnabled(boolean rememberMeEnabled) {
		rememberMeControl.setVisible(rememberMeEnabled);
	}
	
	@Override
	public void setWaiting(boolean wait) {
		loginButton.setEnabled(!wait);
	}
}

