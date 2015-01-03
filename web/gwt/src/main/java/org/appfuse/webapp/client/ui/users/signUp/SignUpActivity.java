/**
 *
 */
package org.appfuse.webapp.client.ui.users.signUp;

import org.appfuse.webapp.client.application.Application;
import org.appfuse.webapp.client.application.base.activity.AbstractProxyEditActivity;
import org.appfuse.webapp.client.application.base.security.LoginEvent;
import org.appfuse.webapp.client.proxies.UserProxy;
import org.appfuse.webapp.client.requests.UserRequest;
import org.appfuse.webapp.client.ui.home.HomePlace;
import org.appfuse.webapp.client.ui.login.LoginPlace;
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
        this.setTitle(this.i18n.signup_title());
        this.setBodyClassname("signup");
        this.setDeleteConfirmation(this.i18n.delete_confirm(this.i18n.userList_user()));
    }

    @Override
    public String getSavedMessage() {
        return this.application.getI18n().user_registered();
    }

    @Override
    public String getDeletedMessage() {
        return this.application.getI18n().user_deleted(this.entityProxy.getUsername());
    }

    @Override
    public void start(final AcceptsOneWidget display, final EventBus eventBus) {
        if (this.signUpView != null) {
            this.signUpView.setAvailableRoles(this.application.getLookupConstants().getAvailableRoles());
            this.signUpView.setCountries(this.application.getLookupConstants().getCountries());
        }
        super.start(display, eventBus);
    }

    @Override
    public void updatePasswordClicked() {
    }

    @Override
    protected String getEntityId() {
        // return a not null entityId so super does not try to create a new
        // profile
        return "x";
    }

    @Override
    protected RequestContext createProxyRequest() {
        return this.requests.userRequest();
    }

    @Override
    protected Request<UserProxy> loadProxyRequest(final RequestContext requestContext, final String proxyId) {
        return ((UserRequest) requestContext).signUp();
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
        this.eventBus.fireEvent(new LoginEvent());
        return new HomePlace();
    }

}
