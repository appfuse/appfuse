/**
 * 
 */
package org.appfuse.webapp.client.ui.users.editProfile;

import org.appfuse.webapp.client.application.Application;
import org.appfuse.webapp.client.application.base.activity.AbstractProxyEditActivity;
import org.appfuse.webapp.client.application.base.view.ProxyEditView;
import org.appfuse.webapp.client.ui.mainMenu.MainMenuPlace;
import org.appfuse.webapp.client.ui.users.editUser.EditUserView;
import org.appfuse.webapp.proxies.RoleProxy;
import org.appfuse.webapp.proxies.UserProxy;
import org.appfuse.webapp.requests.UserRequest;

import com.github.gwtbootstrap.client.ui.constants.AlertType;
import com.google.gwt.place.shared.Place;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;

/**
 * @author ivangsa
 *
 */
public class EditProfileActivity extends AbstractProxyEditActivity<UserProxy> implements EditUserView.Delegate {

	
	public EditProfileActivity(Application application) {
		super(application);
		setTitle(i18n.userProfile_title());
		setDeleteConfirmation(i18n.delete_confirm(i18n.userList_user()));
	}
	

	@Override
	public String getSavedMessage() {
		return application.getI18n().user_saved();
	}
	
	@Override
	public String getDeletedMessage() {
		return application.getI18n().user_deleted(entityProxy.getUsername());
	}
	
	
	@Override
	protected ProxyEditView<UserProxy, ?> createView(Place place) {
		EditUserView editUserView = viewFactory.getView(EditProfileViewImpl.class);
		if(!application.isUserInRole(RoleProxy.FULLY_AUTHENTICATED)) {
			shell.addMessage(i18n.userProfile_cookieLogin(), AlertType.WARNING);
			//TODO disable passwords in view
		}
		
		if(editUserView != null) {
			editUserView.setAvailableRoles(application.getLookupConstants().getAvailableRoles());
			editUserView.setCountries(application.getLookupConstants().getCountries());
		}
		return editUserView;
	}

	@Override
	protected EntityProxyId<UserProxy> getProxyId() {
		//return a bogus proxyId so it will be resolved on the server
		return  new EntityProxyId<UserProxy>() {
			@Override
			public Class<UserProxy> getProxyClass() {
				return UserProxy.class;
			}
		};
	}

	@Override
	protected RequestContext createProxyRequest() {
		return requests.userRequest();
	}
	
	@Override
	protected Request<UserProxy> findProxyRequest(RequestContext requestContext, EntityProxyId<UserProxy> proxyId) {
		return ((UserRequest) requestContext).editProfile();
	}
	

	@Override
	protected RequestContext saveOrUpdateRequest(RequestContext requestContext, UserProxy proxy) {
		((UserRequest) requestContext).editProfile(proxy);
		return requestContext;
	}	
	
	@Override
	protected RequestContext deleteRequest(RequestContext requestContext, UserProxy proxy) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected Place previousPlace() {
		return new MainMenuPlace();
	}
	
	@Override
	protected Place nextPlace(boolean saved) {
		return new EditProfilePlace();
	}

	private String getFullName(UserProxy userProxy) {//XXX this is already duplicated
		return userProxy.getFirstName()  + " " + userProxy.getLastName();
	}
}
