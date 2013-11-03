/**
 * 
 */
package org.appfuse.webapp.client.ui.users.editProfile;

import org.appfuse.webapp.client.ui.users.editUser.EditUserViewImpl;


/**
 * @author ivangsa
 *
 */
public class EditProfileViewImpl extends EditUserViewImpl implements EditProfileView {

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
