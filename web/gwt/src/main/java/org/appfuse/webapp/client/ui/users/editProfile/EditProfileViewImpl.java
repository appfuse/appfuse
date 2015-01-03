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
        subheading.setInnerText(i18n.userProfile_message());
        passwordControlGroup.removeFromParent();
        updatePasswordControl.setVisible(true);
        accountSettings.removeFromParent();
        deleteButton.removeFromParent();
        roles.setReadonly(true);
    }

}
