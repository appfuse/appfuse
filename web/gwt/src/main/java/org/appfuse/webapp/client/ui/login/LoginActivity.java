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
		view = viewFactory.getView(LoginWidget.class);
		view.setDelegate(this);

		dialog = new DialogBox();
		dialog.add(view);
		dialog.center();
	}

	@Override
	public void onLoginClick() {
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
					if(response.getStatusCode() == Response.SC_OK) {
						if(dialog != null) {
							dialog.hide();
							Window.Location.reload();
						} else {
							placeController.goTo(new MainMenuPlace(true));
						}
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
