/**
 * 
 */
package org.appfuse.webapp.client.ui.users.edit.views;


/**
 * @author ivangsa
 *
 */
public class EditProfileViewImpl extends EditUserViewImpl {

	/**
	 * 
	 */
	public EditProfileViewImpl() {
		super();
		subheading.setText(i18n.userProfile_message());
		accountSettings.removeFromParent();
		deleteButton.removeFromParent();
		roles.setReadonly(true);
	}

}
