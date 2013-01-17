package org.appfuse.webapp.client.ui.users.edit.views;

import java.util.List;

import org.appfuse.webapp.client.application.base.view.ProxyEditView;
import org.appfuse.webapp.proxies.LabelValueProxy;
import org.appfuse.webapp.proxies.RoleProxy;
import org.appfuse.webapp.proxies.UserProxy;

public interface EditUserView extends ProxyEditView<UserProxy, EditUserView> {

	void setCountries(List<LabelValueProxy> countries);
	void setAvailableRoles(List<RoleProxy> roles);
}