/**
 * 
 */
package org.appfuse.webapp.client.ui.login;

import org.appfuse.webapp.client.application.ApplicationResources;
import org.appfuse.webapp.client.ui.users.signUp.SignUpPlace;

import com.github.gwtbootstrap.client.ui.base.AlertBase;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorDriver;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author ivangsa
 *
 */
public class LoginViewImpl extends Composite implements LoginView, Editor<LoginView.LoginDetails> {

	interface Binder extends UiBinder<Widget, LoginViewImpl> {}
	private static Binder uiBinder = GWT.create(Binder.class);
	
	ApplicationResources i18n = GWT.create(ApplicationResources.class);
	
	
	private Delegate delegate;
	
	@UiField LoginForm loginForm;
	@UiField Element signupParaElement;
	@UiField Element passwordHintParaElement;
	
	/**
	 * 
	 */
	public LoginViewImpl() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
		
		//
		signupParaElement.setInnerHTML(i18n.login_signup("#" + SignUpPlace.PREFIX + ":"));

		//
		HTMLPanel passwordHintHtml = new HTMLPanel(i18n.login_passwordHint());
		NodeList<Element> anchors = passwordHintHtml.getElement().getElementsByTagName("a");
	    Element a = anchors.getItem(0);
	    Anchor link = new Anchor(a.getInnerHTML());
	    link.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				delegate.onPasswordHintClick();
			}
		});
	    passwordHintHtml.addAndReplaceElement(link, a);
	    try {//fails on dev mode..
			HTMLPanel.wrap(passwordHintParaElement).add(passwordHintHtml);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
		loginForm.setDelegate(delegate);
	}
	
	public void setMessage(AlertBase alert) {
		loginForm.setMessage(alert);
	}

	@Override
	public EditorDriver<LoginView.LoginDetails> getEditorDriver() {
		return loginForm.getEditorDriver();
	}
	
	@Override
	public void setRememberMeEnabled(boolean rememberMeEnabled) {
		loginForm.setRememberMeEnabled(rememberMeEnabled);
	}
	
	@Override
	public void setWaiting(boolean wait) {
		loginForm.setWaiting(wait);
	}
}

