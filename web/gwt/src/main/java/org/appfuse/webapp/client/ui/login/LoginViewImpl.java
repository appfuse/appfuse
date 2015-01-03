/**
 * 
 */
package org.appfuse.webapp.client.ui.login;

import org.appfuse.webapp.client.application.ApplicationResources;
import org.appfuse.webapp.client.ui.users.signUp.SignUpPlace;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.CheckBox;
import com.github.gwtbootstrap.client.ui.PasswordTextBox;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.base.AlertBase;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorDriver;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author ivangsa
 *
 */
public class LoginViewImpl extends Composite implements LoginView, Editor<LoginView.LoginDetails> {

    interface Binder extends UiBinder<Widget, LoginViewImpl> {
    }

    private static Binder uiBinder = GWT.create(Binder.class);

    interface Driver extends SimpleBeanEditorDriver<LoginView.LoginDetails, LoginViewImpl> {
    }

    private final Driver driver = GWT.create(Driver.class);

    ApplicationResources i18n = GWT.create(ApplicationResources.class);

    private Delegate delegate;

    @UiField
    SimplePanel messages;

    @UiField
    TextBox username;
    @UiField
    PasswordTextBox password;

    @UiField
    Widget rememberMeControl;
    @UiField
    CheckBox rememberMe;

    @UiField
    Button loginButton;

    @UiField
    Element signupParaElement;
    @UiField
    Element passwordHintParaElement;
    @UiField
    Element requestRecoveryTokenElement;

    /**
     * 
     */
    public LoginViewImpl() {
        super();
        initWidget(uiBinder.createAndBindUi(this));
        driver.initialize(this);

        username.getElement().setAttribute("required", "required");
        username.getElement().setAttribute("autofocus", "autofocus");
        password.getElement().setAttribute("required", "required");

        //
        signupParaElement.setInnerHTML(i18n.login_signup("#" + SignUpPlace.PREFIX + ":"));

        final HTMLPanel passwordHintHtml = new HTMLPanel(i18n.login_passwordHint());
        final NodeList<Element> anchors = passwordHintHtml.getElement().getElementsByTagName("a");
        final Element a = anchors.getItem(0);
        final Anchor link = new Anchor(a.getInnerHTML());
        link.getElement().setId("signUpLink");
        link.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                delegate.onPasswordHintClick();
            }
        });
        passwordHintHtml.addAndReplaceElement(link, a);
        try {// fails on dev mode..
            HTMLPanel.wrap(passwordHintParaElement).add(passwordHintHtml);
        } catch (final Throwable e) {
            e.printStackTrace();
        }

        final HTMLPanel passwordRecoveryParaHtml = new HTMLPanel(i18n.updatePassword_requestRecoveryTokenLink());
        final Element passwordRecoveryA = passwordRecoveryParaHtml.getElement().getElementsByTagName("a").getItem(0);
        final Anchor passwordRecoveryLink = new Anchor(passwordRecoveryA.getInnerHTML());
        passwordRecoveryLink.getElement().setId("requestRecoveryTokenLink");
        passwordRecoveryLink.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                delegate.onRequestPasswordRecoveryClick();
            }
        });
        passwordRecoveryParaHtml.addAndReplaceElement(passwordRecoveryLink, passwordRecoveryA);

        try {// fails on dev mode..
            HTMLPanel.wrap(requestRecoveryTokenElement).add(passwordRecoveryParaHtml);
        } catch (final Throwable e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setDelegate(final Delegate delegate) {
        driver.edit(new LoginDetails());
        this.delegate = delegate;
    }

    @Override
    public void setMessage(final AlertBase alert) {
        messages.clear();
        messages.add(alert);
    }

    @UiHandler("loginButton")
    void onLoginClick(final ClickEvent event) {
        delegate.onLoginClick();
    }

    @UiHandler({ "username", "password" })
    void defaultAction(final KeyDownEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
            delegate.onLoginClick();
        }
    }

    @Override
    public EditorDriver<LoginView.LoginDetails> getEditorDriver() {
        return driver;
    }

    @Override
    public void setRememberMeEnabled(final boolean rememberMeEnabled) {
        rememberMeControl.setVisible(rememberMeEnabled);
    }

    @Override
    public void setWaiting(final boolean wait) {
        loginButton.setEnabled(!wait);
    }
}
