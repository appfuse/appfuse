/**
 * 
 */
package org.appfuse.webapp.client.application;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.appfuse.webapp.client.ui.AbstractShell;
import org.appfuse.webapp.proxies.LookupConstantsProxy;
import org.appfuse.webapp.proxies.RoleProxy;
import org.appfuse.webapp.proxies.UserProxy;
import org.appfuse.webapp.requests.ApplicationRequestFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.MetaElement;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;


/**
 * @author ivangsa
 *
 */
public abstract class Application {

	protected final AbstractShell shell;
	protected final EventBus eventBus;
	protected final ApplicationMenu menu;
	protected final PlaceController placeController;
	protected final ApplicationRequestFactory requestFactory;
	protected final ApplicationViewFactory viewFactory;
	protected final ValidatorFactory validatorFactory;
	protected final ApplicationProxyFactory proxyFactory;
	protected final ApplicationResources i18n = GWT.create(ApplicationResources.class);

	private String contextPath = "/";
	private boolean rememberMeEnabled = false;
	private UserProxy currentUser;
	private LookupConstantsProxy lookupConstants;
	
	@Inject
	public Application(
			AbstractShell shell,
			ApplicationMenu menu,
			ApplicationRequestFactory requestFactory, 
			EventBus eventBus,
			PlaceController placeController,
			ApplicationViewFactory viewFactory,
			ApplicationProxyFactory proxyFactory, 
			ApplicationValidatorFactory validatorFactory) {
		super();
		this.shell = shell;
		this.menu = menu;
		this.requestFactory = requestFactory;
		this.eventBus = eventBus;
		this.placeController = placeController;
		this.viewFactory = viewFactory;
		this.proxyFactory = proxyFactory;
		this.validatorFactory = Validation.buildDefaultValidatorFactory();
		shell.setApplication(this);
		
		NodeList<Element> metas = Document.get().getElementsByTagName("meta");
		for (int i = 0; i < metas.getLength(); i++) {
			MetaElement meta = (MetaElement) metas.getItem(i);
			if("rememberMeEnabled".equals(meta.getName())) {
				rememberMeEnabled = "true".equals(meta.getContent());
			}
		}
		
		contextPath = GWT.getModuleBaseURL()
				.replace(GWT.getModuleName() + "/" , "");
		
	}

	public abstract void run();

	public AbstractShell getShell() {
		return shell;
	}
	
	public ApplicationMenu getMenu() {
		return menu;
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
	
	public ApplicationProxyFactory getProxyFactory() {
		return proxyFactory;
	}
	
	public ValidatorFactory getValidatorFactory() {
		return validatorFactory;
	}
	
	public ApplicationResources getI18n() {
		return i18n;
	}
	
	public String getContextPath() {
		return contextPath;
	}
	
	public boolean isRememberMeEnabled() {
		return rememberMeEnabled;
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

	public String getCurrentUsername() {
		if(getCurrentUser() == null) {
			return null;
		}
		return getCurrentUser().getUsername();
	}
	
	public boolean isUserInRole(String role) {
		if(RoleProxy.ANONYMOUS.equals(role)) {
			return currentUser == null;
		} else if(RoleProxy.AUTHENTICATED.equals(role)) {
			return currentUser != null;
		}
		else if(currentUser != null && currentUser.getRoles() != null && role != null) {
			for (RoleProxy roleProxy : currentUser.getRoles()) {
				if(role.equalsIgnoreCase(roleProxy.getName())) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean hasAnyRole(String[] roles) {
		if(roles != null) {
			for (String role : roles) {
				if(isUserInRole(role)) {
					return true;
				}
			}
		}
		return false;
	}
	
}
