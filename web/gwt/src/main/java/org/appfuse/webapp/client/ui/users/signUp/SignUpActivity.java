/**
 * 
 */
package org.appfuse.webapp.client.ui.users.signUp;

import org.appfuse.webapp.client.application.Application;
import org.appfuse.webapp.client.application.base.activity.AbstractProxyEditActivity;
import org.appfuse.webapp.client.application.base.security.LoginEvent;
import org.appfuse.webapp.client.application.base.view.ProxyEditView;
import org.appfuse.webapp.client.ui.login.LoginPlace;
import org.appfuse.webapp.client.ui.mainMenu.MainMenuPlace;
import org.appfuse.webapp.client.ui.users.editUser.EditUserView;
import org.appfuse.webapp.proxies.UserProxy;
import org.appfuse.webapp.requests.UserRequest;

import com.google.gwt.place.shared.Place;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;

/**
 * @author ivangsa
 *
 */
public class SignUpActivity extends AbstractProxyEditActivity<UserProxy> implements EditUserView.Delegate {

	
	public SignUpActivity(Application application) {
		super(application);
		setTitle(i18n.signup_title());
		setBodyClassname("signup");
		setDeleteConfirmation(i18n.delete_confirm(i18n.userList_user()));
	}
	

	@Override
	public String getSavedMessage() {
		return application.getI18n().user_registered();
	}
	
	@Override
	public String getDeletedMessage() {
		return application.getI18n().user_deleted(entityProxy.getUsername());
	}
	
	
	@Override
	protected ProxyEditView<UserProxy, ?> createView(Place place) {
		EditUserView editUserView = viewFactory.getView(SignUpViewImpl.class);
		
		if(editUserView != null) {
			editUserView.setAvailableRoles(application.getLookupConstants().getAvailableRoles());
			editUserView.setCountries(application.getLookupConstants().getCountries());
		}
		return editUserView;
	}

	@Override
	protected String getEntityId() {
		//return a not null entityId so super does not try to create a new profile
		return "x";
	}

	@Override
	protected RequestContext createProxyRequest() {
		return requests.userRequest();
	}

	
	@Override
	protected Request<UserProxy> loadProxyRequest(RequestContext requestContext, String proxyId) {
		return ((UserRequest)requestContext).signUp();
	}
	

	@Override
	protected RequestContext saveOrUpdateRequest(RequestContext requestContext, UserProxy proxy) {
		((UserRequest) requestContext).signUp(proxy);		
		return requestContext;
	}	
	
	@Override
	protected RequestContext deleteRequest(RequestContext requestContext, UserProxy proxy) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected Place previousPlace() {
		return new LoginPlace();
	}
	
	@Override
	protected Place nextPlace(boolean saved) {
		eventBus.fireEvent(new LoginEvent());
		return new MainMenuPlace();
	}

}
