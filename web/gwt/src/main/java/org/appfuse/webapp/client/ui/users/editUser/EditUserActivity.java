/**
 * 
 */
package org.appfuse.webapp.client.ui.users.editUser;

import org.appfuse.webapp.client.application.Application;
import org.appfuse.webapp.client.application.base.activity.AbstractProxyEditActivity;
import org.appfuse.webapp.client.application.base.place.EntitySearchPlace;
import org.appfuse.webapp.client.proxies.AddressProxy;
import org.appfuse.webapp.client.proxies.UserProxy;
import org.appfuse.webapp.client.requests.UserRequest;
import org.appfuse.webapp.client.ui.mainMenu.MainMenuPlace;

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
public class EditUserActivity extends AbstractProxyEditActivity<UserProxy> implements EditUserView.Delegate {

    private final EditUserView editUserView;

    @Inject
    public EditUserActivity(final Application application, final EditUserView editUserView) {
	super(application, editUserView);
	this.editUserView = editUserView;
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
    public void start(final AcceptsOneWidget display, final EventBus eventBus) {
	if (editUserView != null) {
	    editUserView.setAvailableRoles(application.getLookupConstants().getAvailableRoles());
	    editUserView.setCountries(application.getLookupConstants().getCountries());
	}
	super.start(display, eventBus);
    }

    @Override
    protected RequestContext createProxyRequest() {
	return requests.userRequest();
    }

    @Override
    protected UserProxy createProxy(final RequestContext requestContext) {
	final UserProxy user = requestContext.create(UserProxy.class);
	final AddressProxy address = requestContext.create(AddressProxy.class);
	user.setAddress(address);
	return user;
    }

    @Override
    protected Request<UserProxy> loadProxyRequest(final RequestContext requestContext, final String entityId) {
	return ((UserRequest) requestContext).getUser(Long.parseLong(entityId));
    }


    @Override
    protected RequestContext saveOrUpdateRequest(final RequestContext requestContext, final UserProxy proxy) {
	((UserRequest) requestContext).saveUser(proxy);
	return requestContext;
    }

    @Override
    protected RequestContext deleteRequest(final RequestContext requestContext, final UserProxy proxy) {
	((UserRequest) requestContext).removeUser(proxy.getId());
	return requestContext;
    }

    @Override
    protected Place previousPlace() {
	return new EntitySearchPlace(UserProxy.class);
    }

    @Override
    protected Place nextPlace(final boolean saved) {
	if(saved) {
	    return new EntitySearchPlace(UserProxy.class);
	} else { // deleted
	    return new MainMenuPlace();
	}
    }

    private String getFullName(final UserProxy userProxy) {//XXX this is already duplicated
	return userProxy.getFirstName()  + " " + userProxy.getLastName();
    }
}
