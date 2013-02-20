/**
 * 
 */
package org.appfuse.webapp.client.ui.users.edit.views;

/**
 * @author ivangsa
 *
 */
public class SignUpViewImpl extends EditUserViewImpl {

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
