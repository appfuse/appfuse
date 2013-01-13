/**
 * 
 */
package org.appfuse.webapp.client.ui.users.edit;

import org.appfuse.webapp.client.application.Application;
import org.appfuse.webapp.client.application.base.activity.AbstractProxyEditActivity;
import org.appfuse.webapp.client.application.base.place.EntityListPlace;
import org.appfuse.webapp.client.application.base.place.EntityProxyPlace;
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
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.RequestContext;

/**
 * @author ivangsa
 *
 */
public class EditUserActivity extends AbstractProxyEditActivity<UserProxy> implements EditUserView.Delegate {

	public EditUserActivity(EntityProxyPlace place, Application application) {
		super(place, application);
	}
	
	@Override
	protected ProxyEditView<UserProxy, ?> createView(Place place) {
		if(place instanceof SignUpPlace) {
			return viewFactory.getView(SignUpViewImpl.class);
		} else if(place instanceof EditProfilePlace) {
			return viewFactory.getView(EditProfileViewImpl.class);
		} else {
			return viewFactory.getView(EditUserView.class);
		}
	}
	
	/**
	 * 
	 */
	protected void loadEntityProxy() {
		if(currentPlace instanceof SignUpPlace) {
			requests.userRequest().signUp().fire(new Receiver<UserProxy>() {
				@Override
				public void onSuccess(UserProxy response) {
					entityProxy = response;
					editorDriver.edit(entityProxy, requests.userRequest());
				}
				
			});
		}
		else if(currentPlace instanceof EditProfilePlace) {
			requests.userRequest().editProfile().fire(new Receiver<UserProxy>() {
				@Override
				public void onSuccess(UserProxy response) {
					entityProxy = response;
					UserRequest userRequest = requests.userRequest();
					try {
						userRequest.editProfile(entityProxy);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
					editorDriver.edit(entityProxy, userRequest);
				}
			});
		} 
		else {
			super.loadEntityProxy();
		}
	}


	@Override
	protected RequestContext createSaveRequest(UserProxy proxy) {
		UserRequest userRequest = requests.userRequest();
		userRequest.saveUser(proxy);
		return userRequest;
	}
	
	@Override
	protected RequestContext createDeleteRequest(UserProxy proxy) {
		UserRequest userRequest = requests.userRequest();
		userRequest.removeUser(proxy);
		return userRequest;
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
