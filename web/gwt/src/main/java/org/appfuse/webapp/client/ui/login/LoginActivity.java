/**
 * 
 */
package org.appfuse.webapp.client.ui.login;

import java.util.Set;

import javax.validation.ConstraintViolation;

import org.appfuse.webapp.client.application.Application;
import org.appfuse.webapp.client.application.base.activity.AbstractBaseActivity;
import org.appfuse.webapp.client.application.base.security.LoginEvent;
import org.appfuse.webapp.client.ui.login.LoginView.LoginDetails;

import com.github.gwtbootstrap.client.ui.Alert;
import com.github.gwtbootstrap.client.ui.constants.AlertType;
import com.google.gwt.editor.client.EditorDriver;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.requestfactory.shared.Receiver;

/**
 * @author ivangsa
 *
 */
public class LoginActivity extends AbstractBaseActivity implements LoginView.Delegate {

	private LoginView view;

	public LoginActivity(Application application) {
		super(application);
		setTitle(i18n.login_title());
		setBodyId("login");
		setBodyClassname("login");
	}

	/**
	 * @see com.google.gwt.activity.shared.Activity#start(com.google.gwt.user.client.ui.AcceptsOneWidget, com.google.gwt.event.shared.EventBus)
	 */
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		view = viewFactory.getView(LoginView.class);
		view.setDelegate(this);
		view.setRememberMeEnabled(application.isRememberMeEnabled());
		view.setWaiting(false);
		view.setMessage(null);
		panel.setWidget(view);
	}


	@Override
	public void onLoginClick() {
		view.setMessage(null);
		EditorDriver<LoginDetails> editorDriver = view.getEditorDriver();
		LoginDetails login = editorDriver.flush();
		Set<ConstraintViolation<LoginDetails>> violations = getValidator().validate(login);
		if(!violations.isEmpty()) {
			editorDriver.setConstraintViolations((Set) violations);
			return;
		}
		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, "j_security_check");
		requestBuilder.setHeader("Content-Type","application/x-www-form-urlencoded");
		requestBuilder.setHeader("X-Requested-With","XMLHttpRequest");
		try {
			view.setWaiting(true);
			requestBuilder.sendRequest(createLoginPostData(login), new RequestCallback() {
				
				@Override
				public void onResponseReceived(Request request, Response response) {
					int statusCode = response.getStatusCode();
					if(statusCode == Response.SC_OK) {
						eventBus.fireEvent(new LoginEvent());
					} 
					else if(statusCode == Response.SC_FORBIDDEN || statusCode == Response.SC_UNAUTHORIZED) {
						view.setWaiting(false);	
						view.setMessage(new Alert(i18n.errors_password_mismatch(), AlertType.ERROR, false));
					}
					else {
						throw new RuntimeException("Login could not understand response code: " + statusCode);
					}
				}
				
				@Override
				public void onError(Request request, Throwable exception) {
					view.setWaiting(false);					
					Window.alert("Response error " + exception.getMessage());
				}
			});
		} catch (RequestException e) {
			throw new RuntimeException(e);
		}
	}


	private String createLoginPostData(LoginView.LoginDetails login) {
		return "j_username=" + URL.encodeQueryString(login.getUsername()) + 
				"&j_password=" + URL.encodeQueryString(login.getPassword()) +
				(login.isRememberMe()? "&_spring_security_remember_me=on" : "");
	}

	@Override
	public void onPasswordHintClick() {
		LoginDetails login = view.getEditorDriver().flush();
		final String username = login.getUsername(); 
		if(username == null || "".equals(username.trim())) {
			Window.alert(i18n.errors_required(i18n.user_username()));
			return;
		}
		requests.userRequest().sendPasswordHint(login.getUsername()).fire(new Receiver<String>() {
			@Override
			public void onSuccess(String userEmail) {
				Alert message = null;
				if(userEmail != null) {
					message = new Alert(i18n.login_passwordHint_sent(username, userEmail), AlertType.SUCCESS);
				} else {
					message = new Alert(i18n.login_passwordHint_error(username), AlertType.ERROR);
				}
				if(message != null) {
					shell.addMessage(message);
				}
			}
		});
	}

	@Override
	public void onCancelClick() {
		
	}
}
