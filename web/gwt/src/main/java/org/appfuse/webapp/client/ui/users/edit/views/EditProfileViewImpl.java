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
		accountSettings.removeFromParent();
		deleteButton.removeFromParent();
		roles.setReadonly(true);
	}

}
