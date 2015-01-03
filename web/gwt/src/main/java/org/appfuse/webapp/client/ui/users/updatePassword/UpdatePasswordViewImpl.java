/**
 *
 */
package org.appfuse.webapp.client.ui.users.updatePassword;

import org.appfuse.webapp.client.application.ApplicationResources;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Paragraph;
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
public class UpdatePasswordViewImpl extends Composite implements UpdatePasswordView, Editor<UpdatePasswordView.UserCredentials> {

    interface Binder extends UiBinder<Widget, UpdatePasswordViewImpl> {
    }

    private static Binder uiBinder = GWT.create(Binder.class);

    interface Driver extends SimpleBeanEditorDriver<UpdatePasswordView.UserCredentials, UpdatePasswordViewImpl> {
    }

    private final Driver driver = GWT.create(Driver.class);

    ApplicationResources i18n = GWT.create(ApplicationResources.class);

    private Delegate delegate;

    @UiField
    Paragraph subheading;

    @UiField
    TextBox username;
    @UiField
    TextBox token;
    @UiField
    PasswordTextBox currentPassword;
    @UiField
    PasswordTextBox password;

    @UiField
    Widget currentPasswordControlGroup;

    @UiField
    Button updatePasswordButton;
    @UiField
    Button cancelButton;

    /**
     *
     */
    public UpdatePasswordViewImpl() {
        super();
        initWidget(uiBinder.createAndBindUi(this));
        driver.initialize(this);

        username.getElement().setAttribute("required", "required");
        username.getElement().setAttribute("autofocus", "autofocus");
        password.getElement().setAttribute("required", "required");

    }

    @Override
    public void setDelegate(final Delegate delegate) {
        driver.edit(new UserCredentials());
        this.delegate = delegate;
    }

    @Override
    public void setUserCredentials(final UserCredentials userCredentials) {
        driver.edit(userCredentials);
        final String token = userCredentials.getToken();
        if (token == null || "".equals(token.trim())) {
            currentPasswordControlGroup.setVisible(true);
            subheading.setText(i18n.updatePassword_changePassword_message());
        } else {
            currentPasswordControlGroup.setVisible(false);
            subheading.setText(i18n.updatePassword_passwordReset_message());
        }
    }

    @UiHandler("updatePasswordButton")
    void onUpdatePasswordClick(final ClickEvent event) {
        delegate.onUpdatePasswordClick();
    }

    @UiHandler("cancelButton")
    void onCancelClick(final ClickEvent event) {
        delegate.onCancelClick();
    }

    @Override
    public EditorDriver<UpdatePasswordView.UserCredentials> getEditorDriver() {
        return driver;
    }

    @Override
    public void setWaiting(final boolean wait) {
        updatePasswordButton.setEnabled(!wait);
    }
}
