/**
 * 
 */
package org.appfuse.webapp.client.ui.logout;

import org.appfuse.webapp.client.application.Application;
import org.appfuse.webapp.client.application.base.activity.AbstractBaseActivity;
import org.appfuse.webapp.client.ui.login.LoginPlace;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

/**
 * @author ivangsa
 *
 */
public class LogoutActivity extends AbstractBaseActivity {

	public LogoutActivity(Application application) {
		super(application);
	}

	/**
	 * @see com.google.gwt.activity.shared.Activity#start(com.google.gwt.user.client.ui.AcceptsOneWidget, com.google.gwt.event.shared.EventBus)
	 */
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		application.setCurrentUser(null);
		requests.userRequest().logout().fire(new Receiver<Boolean>() {

			@Override
			public void onSuccess(Boolean response) {
				placeController.goTo(new LoginPlace());
			}
			
			@Override
			public void onFailure(ServerFailure error) {
				Cookies.removeCookie("JSESSIONID");
				Cookies.removeCookie("SPRING_SECURITY_REMEMBER_ME_COOKIE");
				placeController.goTo(new LoginPlace());
			}
		});
	}

}
