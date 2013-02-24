/**
 * 
 */
package org.appfuse.webapp.client.ui.users.edit;

import org.appfuse.webapp.client.application.Application;
import org.appfuse.webapp.client.application.base.activity.AbstractProxyEditActivity;
import org.appfuse.webapp.client.application.base.place.EntitySearchPlace;
import org.appfuse.webapp.client.application.base.view.ProxyEditView;
import org.appfuse.webapp.client.ui.login.events.LoginEvent;
import org.appfuse.webapp.client.ui.mainMenu.MainMenuPlace;
import org.appfuse.webapp.client.ui.users.edit.places.EditProfilePlace;
import org.appfuse.webapp.client.ui.users.edit.places.SignUpPlace;
import org.appfuse.webapp.client.ui.users.edit.views.EditProfileViewImpl;
import org.appfuse.webapp.client.ui.users.edit.views.EditUserView;
import org.appfuse.webapp.client.ui.users.edit.views.SignUpViewImpl;
import org.appfuse.webapp.proxies.AddressProxy;
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
public class EditUserActivity extends AbstractProxyEditActivity<UserProxy> implements EditUserView.Delegate {

	
	public EditUserActivity(Application application) {
		super(application);
		Place place = placeController.getWhere();
		if(place instanceof SignUpPlace) {
			setTitle(i18n.signup_title());
			setBodyClassname("signup");
		} else if(place instanceof EditProfilePlace) {
			setTitle(i18n.userProfile_title());
		} else {
			setTitle(i18n.userProfile_title());
		}
		setDeleteConfirmation(i18n.delete_confirm(i18n.userList_user()));
	}
	

	@Override
	public String getSavedMessage() {
		Place place = placeController.getWhere();
		if(place instanceof SignUpPlace) {
			return application.getI18n().user_registered();
		} else if(place instanceof EditProfilePlace) {
			return application.getI18n().user_saved();
		} else {
			if(entityProxy.getVersion() == null) {
				return application.getI18n().user_added(getFullName(entityProxy));
			} else {
				return application.getI18n().user_updated_byAdmin(getFullName(entityProxy));
			}
		}		
	}
	
	@Override
	public String getDeletedMessage() {
		return application.getI18n().user_deleted(entityProxy.getUsername());
	}
	
	
	@Override
	protected ProxyEditView<UserProxy, ?> createView(Place place) {
		EditUserView editUserView = null;
		if(place instanceof SignUpPlace) {
			editUserView = viewFactory.getView(SignUpViewImpl.class);
		} else if(place instanceof EditProfilePlace) {
			if(!application.isUserInRole(RoleProxy.FULLY_AUTHENTICATED)) {
				shell.addMessage(i18n.userProfile_cookieLogin(), AlertType.WARNING);
				//TODO disable passwords in view
			}
			editUserView = viewFactory.getView(EditProfileViewImpl.class);
		} else {
			editUserView = viewFactory.getView(EditUserView.class);
		}
		
		if(editUserView != null) {
			editUserView.setAvailableRoles(application.getLookupConstants().getAvailableRoles());
			editUserView.setCountries(application.getLookupConstants().getCountries());
		}
		return editUserView;
	}

	@Override
	protected EntityProxyId<UserProxy> getProxyId() {
		if(currentPlace instanceof SignUpPlace || currentPlace instanceof EditProfilePlace) {
			//return a empty entityId as it won't be used in findProxyRequest and it will be resolved on the server
			return  new EntityProxyId<UserProxy>() {
				@Override
				public Class<UserProxy> getProxyClass() {
					return UserProxy.class;
				}
			};
		}
		else {
			return super.getProxyId();
		}
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
	protected Request<UserProxy> findProxyRequest(RequestContext requestContext, EntityProxyId<UserProxy> proxyId) {
		if(currentPlace instanceof SignUpPlace) {
			return ((UserRequest) requestContext).signUp();
		}
		else if(currentPlace instanceof EditProfilePlace) {
			return ((UserRequest) requestContext).editProfile();
		} 
		else {
			return super.findProxyRequest(requestContext, proxyId);
		}
		
	}
	

	@Override
	protected RequestContext saveOrUpdateRequest(RequestContext requestContext, UserProxy proxy) {
		if(currentPlace instanceof SignUpPlace) {
			((UserRequest) requestContext).signUp(proxy);		
		} else if(currentPlace instanceof EditProfilePlace) {
			((UserRequest) requestContext).editProfile(proxy);
		} else {
			((UserRequest) requestContext).saveUser(proxy);
		}		
		return requestContext;
	}	
	
	@Override
	protected RequestContext deleteRequest(RequestContext requestContext, UserProxy proxy) {
		((UserRequest) requestContext).removeUser(proxy.getId());
		return requestContext;
	}

	@Override
	protected Place previousPlace() {
		if(currentPlace instanceof SignUpPlace || currentPlace instanceof EditProfilePlace ) {
			return new EntitySearchPlace(UserProxy.class);
		} else {
			return new EntitySearchPlace(UserProxy.class);
		}
	}
	
	@Override
	protected Place nextPlace(boolean saved) {
		if(saved) {
			if(currentPlace instanceof SignUpPlace) {
				eventBus.fireEvent(new LoginEvent());
				return new MainMenuPlace();
			} else if (currentPlace instanceof EditProfilePlace) {
				return new EditProfilePlace();
			} else {
				return new EntitySearchPlace(UserProxy.class);
			}
		} else {
			return new MainMenuPlace();
		}
	}

	private String getFullName(UserProxy userProxy) {//XXX this is already duplicated
		return userProxy.getFirstName()  + " " + userProxy.getLastName();
	}
}
