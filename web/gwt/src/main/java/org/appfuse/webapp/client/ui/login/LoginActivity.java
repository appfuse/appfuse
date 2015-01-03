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
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;

/**
 * @author ivangsa
 *
 */
public class LoginActivity extends AbstractBaseActivity implements LoginView.Delegate {

    private final LoginView view;

    @Inject
    public LoginActivity(final Application application, final LoginView view) {
        super(application);
        this.view = view;
        setTitle(i18n.login_title());
        setBodyId("login");
        setBodyClassname("login");
    }

    /**
     * @see com.google.gwt.activity.shared.Activity#start(com.google.gwt.user.client.ui.AcceptsOneWidget,
     *      com.google.gwt.event.shared.EventBus)
     */
    @Override
    public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
        view.setDelegate(this);
        view.setRememberMeEnabled(application.isRememberMeEnabled());
        view.setWaiting(false);
        view.setMessage(null);
        panel.setWidget(view);
        setDocumentTitleAndBodyAttributtes();
    }

    @Override
    public void onLoginClick() {
        view.setMessage(null);
        final EditorDriver<LoginDetails> editorDriver = view.getEditorDriver();
        final LoginDetails login = editorDriver.flush();
        final Set<ConstraintViolation<LoginDetails>> violations = getValidator().validate(login);
        if (!violations.isEmpty()) {
            editorDriver.setConstraintViolations((Set) violations);
            return;
        }
        final RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, "j_security_check");
        requestBuilder.setHeader("Content-Type", "application/x-www-form-urlencoded");
        requestBuilder.setHeader("X-Requested-With", "XMLHttpRequest");
        try {
            view.setWaiting(true);
            requestBuilder.sendRequest(createLoginPostData(login), new RequestCallback() {

                @Override
                public void onResponseReceived(final Request request, final Response response) {
                    final int statusCode = response.getStatusCode();
                    if (statusCode == Response.SC_OK) {
                        eventBus.fireEvent(new LoginEvent());
                    }
                    else if (statusCode == Response.SC_FORBIDDEN || statusCode == Response.SC_UNAUTHORIZED) {
                        view.setWaiting(false);
                        view.setMessage(new Alert(i18n.errors_password_mismatch(), AlertType.ERROR, false));
                    }
                    else {
                        throw new RuntimeException("Login could not understand response code: " + statusCode);
                    }
                }

                @Override
                public void onError(final Request request, final Throwable exception) {
                    view.setWaiting(false);
                    Window.alert("Response error " + exception.getMessage());
                }
            });
        } catch (final RequestException e) {
            throw new RuntimeException(e);
        }
    }

    private String createLoginPostData(final LoginView.LoginDetails login) {
        return "j_username=" + URL.encodeQueryString(login.getUsername()) +
                "&j_password=" + URL.encodeQueryString(login.getPassword()) +
                (login.isRememberMe() ? "&_spring_security_remember_me=on" : "");
    }

    @Override
    public void onPasswordHintClick() {
        final LoginDetails login = view.getEditorDriver().flush();
        final String username = login.getUsername();
        if (username == null || "".equals(username.trim())) {
            Window.alert(i18n.errors_required(i18n.user_username()));
            return;
        }
        requests.userRequest().sendPasswordHint(login.getUsername()).fire(new Receiver<String>() {
            @Override
            public void onSuccess(final String userEmail) {
                Alert message = null;
                if (userEmail != null) {
                    message = new Alert(i18n.login_passwordHint_sent(username, userEmail), AlertType.SUCCESS);
                } else {
                    message = new Alert(i18n.login_passwordHint_error(username), AlertType.ERROR);
                }
                if (message != null) {
                    shell.addMessage(message);
                }
            }
        });
    }

    @Override
    public void onRequestPasswordRecoveryClick() {
        final LoginDetails login = view.getEditorDriver().flush();
        final String username = login.getUsername();
        if (username == null || "".equals(username.trim())) {
            Window.alert(i18n.errors_required(i18n.user_username()));
            return;
        }
        requests.userRequest().requestRecoveryToken(login.getUsername()).fire(new Receiver<String>() {
            @Override
            public void onSuccess(final String message) {
                if (message != null) {
                    shell.addMessage(message, AlertType.SUCCESS);
                }
            }
        });
    }

    @Override
    public void onCancelClick() {

    }
}
