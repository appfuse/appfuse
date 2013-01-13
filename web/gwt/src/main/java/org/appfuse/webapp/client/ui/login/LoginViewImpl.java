/**
 * 
 */
package org.appfuse.webapp.client.ui.login;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorDriver;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author ivangsa
 *
 */
public class LoginViewImpl extends Composite implements LoginView, Editor<LoginView.LoginDetails> {

	interface Binder extends UiBinder<Widget, LoginViewImpl> {}
	private static Binder uiBinder = GWT.create(Binder.class);
	
	private Delegate delegate;
	
	@UiField LoginWidget loginWidget;
	
	/**
	 * 
	 */
	public LoginViewImpl() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
		loginWidget.setDelegate(delegate);
	}
	

	@Override
	public EditorDriver<LoginView.LoginDetails> getEditorDriver() {
		return loginWidget.getEditorDriver();
	}
	
	
}

