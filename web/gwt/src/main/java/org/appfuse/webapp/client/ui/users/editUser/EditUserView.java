package org.appfuse.webapp.client.ui.users.editUser;

import java.util.List;

import org.appfuse.webapp.client.application.base.view.ProxyEditView;
import org.appfuse.webapp.client.proxies.LabelValueProxy;
import org.appfuse.webapp.client.proxies.RoleProxy;
import org.appfuse.webapp.client.proxies.UserProxy;

public interface EditUserView extends ProxyEditView<UserProxy, EditUserView> {

	void hidePasswordFields(boolean hide);
	void setCountries(List<LabelValueProxy> countries);
	void setAvailableRoles(List<RoleProxy> roles);
}