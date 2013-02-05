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
		accountSettings.removeFromParent();
		roles.removeFromParent();
		deleteButton.removeFromParent();
	}


}
