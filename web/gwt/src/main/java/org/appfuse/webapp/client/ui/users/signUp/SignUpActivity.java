/**
 * 
 */
package org.appfuse.webapp.client.ui.users.signUp;

import org.appfuse.webapp.client.application.Application;
import org.appfuse.webapp.client.application.base.activity.AbstractProxyEditActivity;
import org.appfuse.webapp.client.application.base.security.LoginEvent;
import org.appfuse.webapp.client.proxies.UserProxy;
import org.appfuse.webapp.client.requests.UserRequest;
import org.appfuse.webapp.client.ui.login.LoginPlace;
import org.appfuse.webapp.client.ui.mainMenu.MainMenuPlace;
import org.appfuse.webapp.client.ui.users.editUser.EditUserView;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;

/**
 * @author ivangsa
 *
 */
public class SignUpActivity extends AbstractProxyEditActivity<UserProxy> implements EditUserView.Delegate {

    private final SignUpView signUpView;

    @Inject
    public SignUpActivity(final Application application, final SignUpView editUserView) {
	super(application, editUserView);
	this.signUpView = editUserView;
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
    public void start(final AcceptsOneWidget display, final EventBus eventBus) {
	if(signUpView != null) {
	    signUpView.setAvailableRoles(application.getLookupConstants().getAvailableRoles());
	    signUpView.setCountries(application.getLookupConstants().getCountries());
	}
	super.start(display, eventBus);
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
    protected Request<UserProxy> loadProxyRequest(final RequestContext requestContext, final String proxyId) {
	return ((UserRequest)requestContext).signUp();
    }


    @Override
    protected RequestContext saveOrUpdateRequest(final RequestContext requestContext, final UserProxy proxy) {
	((UserRequest) requestContext).signUp(proxy);
	return requestContext;
    }

    @Override
    protected RequestContext deleteRequest(final RequestContext requestContext, final UserProxy proxy) {
	throw new UnsupportedOperationException();
    }

    @Override
    protected Place previousPlace() {
	return new LoginPlace();
    }

    @Override
    protected Place nextPlace(final boolean saved) {
	eventBus.fireEvent(new LoginEvent());
	return new MainMenuPlace();
    }

}
