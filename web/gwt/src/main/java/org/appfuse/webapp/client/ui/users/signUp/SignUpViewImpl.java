/**
 * 
 */
package org.appfuse.webapp.client.ui.users.signUp;

import org.appfuse.webapp.client.ui.users.editUser.EditUserViewImpl;

/**
 * @author ivangsa
 *
 */
public class SignUpViewImpl extends EditUserViewImpl implements SignUpView {

	/**
	 * 
	 */
	public SignUpViewImpl() {
		super();
		subheading.setText(i18n.signup_message());
		accountSettings.removeFromParent();
		userRoles.removeFromParent();
		deleteButton.removeFromParent();
	}


}
