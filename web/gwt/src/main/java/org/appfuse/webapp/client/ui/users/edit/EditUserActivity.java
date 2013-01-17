/**
 * 
 */
package org.appfuse.webapp.client.ui.users.edit;

import org.appfuse.webapp.client.application.Application;
import org.appfuse.webapp.client.application.base.activity.AbstractProxyEditActivity;
import org.appfuse.webapp.client.application.base.place.EntityListPlace;
import org.appfuse.webapp.client.application.base.view.ProxyEditView;
import org.appfuse.webapp.client.ui.mainMenu.MainMenuPlace;
import org.appfuse.webapp.client.ui.users.edit.places.EditProfilePlace;
import org.appfuse.webapp.client.ui.users.edit.places.SignUpPlace;
import org.appfuse.webapp.client.ui.users.edit.views.EditProfileViewImpl;
import org.appfuse.webapp.client.ui.users.edit.views.EditUserView;
import org.appfuse.webapp.client.ui.users.edit.views.SignUpViewImpl;
import org.appfuse.webapp.proxies.UserProxy;
import org.appfuse.webapp.requests.UserRequest;

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
	}
	
	@Override
	protected ProxyEditView<UserProxy, ?> createView(Place place) {
		EditUserView editUserView = null;
		if(place instanceof SignUpPlace) {
			editUserView = viewFactory.getView(SignUpViewImpl.class);
		} else if(place instanceof EditProfilePlace) {
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
	protected RequestContext createProxyRequest() {
		return requests.userRequest();
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
		((UserRequest) requestContext).saveUser(proxy);
		return requestContext;
	}	
	
	@Override
	protected RequestContext deleteRequest(RequestContext requestContext, UserProxy proxy) {
		((UserRequest) requestContext).removeUser(proxy);
		return requestContext;
	}

	@Override
	protected Place previousPlace() {
		if(currentPlace instanceof SignUpPlace || currentPlace instanceof EditProfilePlace ) {
			return new EntityListPlace(UserProxy.class);//XXX
		} else {
			return new EntityListPlace(UserProxy.class);
		}
	}
	
	@Override
	protected Place nextPlace(boolean saved) {
		if(saved) {
			if(currentPlace instanceof SignUpPlace) {
				return new MainMenuPlace();
			} else if (currentPlace instanceof EditProfilePlace) {
				return new EditProfilePlace();
			} else {
				return new EntityListPlace(UserProxy.class);
			}
		} else {
			return new MainMenuPlace();
		}
	}

}
