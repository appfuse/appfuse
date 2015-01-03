/**
 *
 */
package org.appfuse.webapp.client.ui.users.updatePassword;

import javax.validation.constraints.NotNull;

import com.google.gwt.editor.client.EditorDriver;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * @author ivangsa
 *
 */
public interface UpdatePasswordView extends IsWidget {

    public interface Delegate {
        void onUpdatePasswordClick();

        void onCancelClick();
    }

    public static class UserCredentials {
        @NotNull
        String username;
        @NotNull
        String password;
        String token;
        String currentPassword;

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public String getToken() {
            return token;
        }

        public String getCurrentPassword() {
            return currentPassword;
        }

        public void setUsername(final String username) {
            this.username = username;
        }

        public void setPassword(final String password) {
            this.password = password;
        }

        public void setToken(final String token) {
            this.token = token;
        }

        public void setCurrentPassword(final String currentPassword) {
            this.currentPassword = currentPassword;
        }

    }

    void setDelegate(Delegate delegate);

    void setUserCredentials(final UserCredentials userCredentials);

    void setWaiting(boolean wait);

    EditorDriver<UserCredentials> getEditorDriver();
}
