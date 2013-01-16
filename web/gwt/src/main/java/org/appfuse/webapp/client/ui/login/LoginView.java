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
	}

	public static class LoginDetails {
		@NotNull
		String username;
		@NotNull
		String password;
		boolean spring_security_remember_me;
		
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public boolean isSpring_security_remember_me() {
			return spring_security_remember_me;
		}
		
		public void setSpring_security_remember_me(boolean spring_security_remember_me) {
			this.spring_security_remember_me = spring_security_remember_me;
		}
		
		@Override
		public String toString() {
			return "LoginDetails [username=" + username + ", password=" + password + ", spring_security_remember_me=" + spring_security_remember_me + "]";
		}
		
		
	}

	void setDelegate(Delegate delegate);
	
	void setMessage(AlertBase alert);

	EditorDriver<LoginDetails> getEditorDriver();
}
