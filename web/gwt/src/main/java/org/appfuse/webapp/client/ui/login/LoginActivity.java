/**
 * 
 */
package org.appfuse.webapp.client.ui.login;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;

import org.appfuse.webapp.client.application.Application;
import org.appfuse.webapp.client.application.base.activity.AbstractBaseActivity;
import org.appfuse.webapp.client.ui.login.LoginView.LoginDetails;
import org.appfuse.webapp.client.ui.login.events.AuthRequiredEvent;
import org.appfuse.webapp.client.ui.login.events.LoginEvent;
import org.appfuse.webapp.client.ui.mainMenu.MainMenuPlace;

import com.github.gwtbootstrap.client.ui.Alert;
import com.google.gwt.editor.client.EditorDriver;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.DialogBox;

/**
 * @author ivangsa
 *
 */
public class LoginActivity extends AbstractBaseActivity implements LoginView.Delegate, AuthRequiredEvent.Handler {

	private LoginView view;
	private DialogBox dialog;

	public LoginActivity(Application application) {
		super(application);
	}

	/**
	 * @see com.google.gwt.activity.shared.Activity#start(com.google.gwt.user.client.ui.AcceptsOneWidget, com.google.gwt.event.shared.EventBus)
	 */
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		if(application.getCurrentUser() != null) {//we are already loged in
			//TODO inform the user
			placeController.goTo(new MainMenuPlace());
		}else {
			view = viewFactory.getView(LoginView.class);
			view.setDelegate(this);
			panel.setWidget(view);
		}
	}

	@Override
	public void onAuthRequiredEvent(AuthRequiredEvent authRequiredEvent) {
		view = viewFactory.getView(LoginForm.class);
		view.setDelegate(this);

		dialog = new DialogBox();
		dialog.setGlassEnabled(true);
		dialog.setAutoHideEnabled(true);
		dialog.add(view);
		dialog.center();
	}

	@Override
	public void onLoginClick() {
		view.setMessage(null);
		EditorDriver<LoginDetails> editorDriver = view.getEditorDriver();
		LoginDetails login = editorDriver.flush();
		 Set<ConstraintViolation<LoginDetails>> violations = Validation.buildDefaultValidatorFactory().getValidator().validate(login);
		if(!violations.isEmpty()) {
			for (ConstraintViolation<LoginDetails> violation : violations) {
				shell.addMessage(new Alert("Violation on " + violation.getPropertyPath()));
			}
			return;
		}
		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, "j_security_check");
		requestBuilder.setHeader("Content-Type","application/x-www-form-urlencoded");
		requestBuilder.setHeader("X-Requested-With","XMLHttpRequest");
		try {
			requestBuilder.sendRequest(createLoginPostData(login), new RequestCallback() {
				
				@Override
				public void onResponseReceived(Request request, Response response) {
					int statusCode = response.getStatusCode();
					if(statusCode == Response.SC_OK) {
						if(dialog != null) {
							dialog.hide();
							shell.addMessage(new Alert("you are back in XXX"));
						}
						eventBus.fireEvent(new LoginEvent());
					} 
					else if(statusCode == Response.SC_FORBIDDEN || statusCode == Response.SC_UNAUTHORIZED) {
						view.setMessage(new Alert("login failedXX"));
					}
					else {
						throw new RuntimeException("Login could not understand response code: " + statusCode);
					}
				}
				
				@Override
				public void onError(Request request, Throwable exception) {
					Window.alert("Response error " + exception.getMessage());
				}
			});
		} catch (RequestException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onCancelClick() {
		if(dialog != null) {
			dialog.hide();
		}
	}

	private String createLoginPostData(LoginView.LoginDetails login) {
		return "j_username=" + URL.encodeQueryString(login.getUsername()) + 
				"&j_password=" + URL.encodeQueryString(login.getPassword()) +
				(login.isSpring_security_remember_me()? "&_spring_security_remember_me=true" : "");
	}
}
