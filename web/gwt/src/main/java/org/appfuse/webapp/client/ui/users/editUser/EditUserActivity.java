/**
 * 
 */
package org.appfuse.webapp.client.ui.users.editUser;

import org.appfuse.webapp.client.application.Application;
import org.appfuse.webapp.client.application.base.activity.AbstractProxyEditActivity;
import org.appfuse.webapp.client.application.base.place.EntitySearchPlace;
import org.appfuse.webapp.client.application.base.view.ProxyEditView;
import org.appfuse.webapp.client.proxies.AddressProxy;
import org.appfuse.webapp.client.proxies.UserProxy;
import org.appfuse.webapp.client.requests.UserRequest;
import org.appfuse.webapp.client.ui.mainMenu.MainMenuPlace;

import com.google.gwt.place.shared.Place;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;

/**
 * @author ivangsa
 *
 */
public class EditUserActivity extends AbstractProxyEditActivity<UserProxy> implements EditUserView.Delegate {

	
	public EditUserActivity(Application application) {
		super(application);
		setTitle(i18n.userProfile_title());
		setDeleteConfirmation(i18n.delete_confirm(i18n.userList_user()));
	}
	

	@Override
	public String getSavedMessage() {
		if(entityProxy.getVersion() == null) {
			return application.getI18n().user_added(getFullName(entityProxy));
		} else {
			return application.getI18n().user_updated_byAdmin(getFullName(entityProxy));
		}
	}
	
	@Override
	public String getDeletedMessage() {
		return application.getI18n().user_deleted(entityProxy.getUsername());
	}
	
	
	@Override
	protected ProxyEditView<UserProxy, ?> createView(Place place) {
		EditUserView editUserView = viewFactory.getView(EditUserView.class);
		if(editUserView != null) {
			editUserView.setAvailableRoles(application.getLookupConstants().getAvailableRoles());
			editUserView.setCountries(application.getLookupConstants().getCountries());
		}
		return editUserView;
	}

	@Override
	protected RequestContext createProxyRequest() {
		return requests.userRequest();
	}
	
	@Override
	protected UserProxy createProxy(RequestContext requestContext) {
		UserProxy user = requestContext.create(UserProxy.class);
		AddressProxy address = requestContext.create(AddressProxy.class);
		user.setAddress(address);
		return user;
	}
	
	@Override
	protected Request<UserProxy> loadProxyRequest(RequestContext requestContext, String entityId) {
		return ((UserRequest) requestContext).getUser(Long.parseLong(entityId));
	}
	

	@Override
	protected RequestContext saveOrUpdateRequest(RequestContext requestContext, UserProxy proxy) {
		((UserRequest) requestContext).saveUser(proxy);
		return requestContext;
	}	
	
	@Override
	protected RequestContext deleteRequest(RequestContext requestContext, UserProxy proxy) {
		((UserRequest) requestContext).removeUser(proxy.getId());
		return requestContext;
	}

	@Override
	protected Place previousPlace() {
		return new EntitySearchPlace(UserProxy.class);
	}
	
	@Override
	protected Place nextPlace(boolean saved) {
		if(saved) {
			return new EntitySearchPlace(UserProxy.class);
		} else { // deleted
			return new MainMenuPlace();
		}
	}

	private String getFullName(UserProxy userProxy) {//XXX this is already duplicated
		return userProxy.getFirstName()  + " " + userProxy.getLastName();
	}
}
