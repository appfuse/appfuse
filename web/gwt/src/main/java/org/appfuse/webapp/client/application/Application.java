/**
 * 
 */
package org.appfuse.webapp.client.application;

import javax.validation.Validation;

import org.appfuse.webapp.client.ui.Shell;
import org.appfuse.webapp.proxies.LookupConstantsProxy;
import org.appfuse.webapp.proxies.RoleProxy;
import org.appfuse.webapp.proxies.UserProxy;
import org.appfuse.webapp.requests.ApplicationRequestFactory;

import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;


/**
 * @author ivangsa
 *
 */
public abstract class Application {

	protected final Shell shell;
	protected final EventBus eventBus;
	protected final PlaceController placeController;
	protected final ApplicationRequestFactory requestFactory;
	protected final ApplicationViewFactory viewFactory;
	protected final ApplicationValidatorFactory validatorFactory;

	private UserProxy currentUser;
	private LookupConstantsProxy lookupConstants;
	
	@Inject
	public Application(
			Shell shell,
			ApplicationRequestFactory requestFactory, 
			EventBus eventBus,
			PlaceController placeController,
			ApplicationViewFactory viewFactory,
			ApplicationValidatorFactory validatorFactory) {
		super();
		this.shell = shell;
		this.requestFactory = requestFactory;
		this.eventBus = eventBus;
		this.placeController = placeController;
		this.viewFactory = viewFactory;
		this.validatorFactory = validatorFactory;
	}

	public abstract void run();

	public Shell getShell() {
		return shell;
	}
	
	public EventBus getEventBus() {
		return eventBus;
	}

	public PlaceController getPlaceController() {
		return placeController;
	}

	public ApplicationRequestFactory getRequestFactory() {
		return requestFactory;
	}
	
	public ApplicationViewFactory getViewFactory() {
		return viewFactory;
	}
	
	public ApplicationValidatorFactory getValidatorFactory() {
		return validatorFactory;
	}
	
	public UserProxy getCurrentUser() {
		return currentUser;
	}
	
	public void setCurrentUser(UserProxy currentUser) {
		this.currentUser = currentUser;
	}
	
	public LookupConstantsProxy getLookupConstants() {
		return lookupConstants;
	}

	public void setLookupConstants(LookupConstantsProxy lookupConstants) {
		this.lookupConstants = lookupConstants;
	}

	public boolean isUserInRole(String role) {
		if(currentUser != null && role != null) {
			for (RoleProxy roleProxy : currentUser.getRoles()) {
				if(role.equalsIgnoreCase(roleProxy.getName())) {
					return true;
				}
			}
		}
		return false;
	}
	
}
