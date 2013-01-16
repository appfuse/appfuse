/**
 * 
 */
package org.appfuse.webapp.client.application;

import org.appfuse.webapp.client.application.base.place.EntityListPlace;
import org.appfuse.webapp.client.application.base.place.EntityProxyPlace;
import org.appfuse.webapp.client.ui.login.LoginActivity;
import org.appfuse.webapp.client.ui.login.LoginPlace;
import org.appfuse.webapp.client.ui.logout.LogoutActivity;
import org.appfuse.webapp.client.ui.logout.LogoutPlace;
import org.appfuse.webapp.client.ui.mainMenu.MainMenuActivity;
import org.appfuse.webapp.client.ui.mainMenu.MainMenuPlace;
import org.appfuse.webapp.client.ui.users.edit.EditUserActivity;
import org.appfuse.webapp.client.ui.users.edit.places.EditProfilePlace;
import org.appfuse.webapp.client.ui.users.edit.places.SignUpPlace;
import org.appfuse.webapp.client.ui.users.list.UsersListActivity;
import org.appfuse.webapp.proxies.UserProxy;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;

/**
 * @author ivangsa
 *
 */
public class ApplicationActivityMapper implements ActivityMapper {

    protected final Application application;


    /**
     * @param application
     */
	@Inject
	public ApplicationActivityMapper(Application application) {
		super();
		this.application = application;
	}



	@Override
	public Activity getActivity(Place place) {
		Activity activity = null;
		
		if(place instanceof LoginPlace) {
			activity = new LoginActivity(application);
		}
		else if(place instanceof LogoutPlace) {
			activity = new LogoutActivity(application);
		}
		else if(place instanceof SignUpPlace) {
			activity = new EditUserActivity(application);
		}
		else if(place instanceof MainMenuPlace) {
			activity = new MainMenuActivity(application);
		}
		else if(place instanceof EditProfilePlace) {
			activity = new EditUserActivity(application);
		}
		else if(place instanceof EntityProxyPlace) {
			EntityProxyPlace proxyPlace = (EntityProxyPlace) place;
			if(UserProxy.class.equals(proxyPlace.getProxyClass())) {
				activity = new EditUserActivity(application);
			}
		}
		else if(place instanceof EntityListPlace) {
			EntityListPlace listPlace = (EntityListPlace) place;
			if(UserProxy.class.equals(listPlace.getProxyClass())) {
				activity = new UsersListActivity(listPlace, application);
			}
		}
		
		return activity;
	}

}
