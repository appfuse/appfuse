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
        subheading.setInnerText(i18n.signup_message());
        passwordControlGroup.setVisible(true);
        updatePasswordControl.removeFromParent();
        accountSettings.removeFromParent();
        userRoles.removeFromParent();
        deleteButton.removeFromParent();
    }

}
