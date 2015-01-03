/**
 * 
 */
package org.appfuse.webapp.client.ui.login;

import javax.validation.constraints.NotNull;

import com.github.gwtbootstrap.client.ui.base.AlertBase;
import com.google.gwt.editor.client.EditorDriver;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * @author ivangsa
 *
 */
public interface LoginView extends IsWidget {

    public interface Delegate {
        void onLoginClick();

        void onCancelClick();

        void onPasswordHintClick();

        void onRequestPasswordRecoveryClick();
    }

    public static class LoginDetails {
        @NotNull
        String username;
        @NotNull
        String password;
        boolean rememberMe;

        public String getUsername() {
            return username;
        }

        public void setUsername(final String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(final String password) {
            this.password = password;
        }

        public boolean isRememberMe() {
            return rememberMe;
        }

        public void setRememberMe(final boolean spring_security_remember_me) {
            this.rememberMe = spring_security_remember_me;
        }

        @Override
        public String toString() {
            return "LoginDetails [username=" + username + ", password=" + password + ", spring_security_remember_me=" + rememberMe + "]";
        }

    }

    void setRememberMeEnabled(boolean rememberMeEnabled);

    void setDelegate(Delegate delegate);

    void setMessage(AlertBase alert);

    void setWaiting(boolean wait);

    EditorDriver<LoginDetails> getEditorDriver();
}
