/**
 * 
 */
package org.appfuse.webapp.client.ui.logout;

import org.appfuse.webapp.client.application.Application;
import org.appfuse.webapp.client.application.base.activity.AbstractBaseActivity;
import org.appfuse.webapp.client.application.base.security.LogoutEvent;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

/**
 * @author ivangsa
 *
 */
public class LogoutActivity extends AbstractBaseActivity {

    @Inject
    public LogoutActivity(final Application application) {
        super(application);
    }

    /**
     * @see com.google.gwt.activity.shared.Activity#start(com.google.gwt.user.client.ui.AcceptsOneWidget,
     *      com.google.gwt.event.shared.EventBus)
     */
    @Override
    public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
        application.setCurrentUser(null);
        requests.userRequest().logout().fire(new Receiver<Boolean>() {

            @Override
            public void onSuccess(final Boolean response) {
                eventBus.fireEvent(new LogoutEvent());
            }

            @Override
            public void onFailure(final ServerFailure error) {
                Cookies.removeCookie("JSESSIONID");
                Cookies.removeCookie("SPRING_SECURITY_REMEMBER_ME_COOKIE");
                eventBus.fireEvent(new LogoutEvent());
            }
        });
    }

}
