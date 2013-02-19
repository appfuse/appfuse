/**
 * 
 */
package org.appfuse.webapp.client.ui.navigation;

import org.appfuse.webapp.client.application.Application;
import org.appfuse.webapp.client.application.ApplicationMenu;
import org.appfuse.webapp.client.application.ApplicationResources;
import org.appfuse.webapp.client.application.base.place.EntityProxyPlace;
import org.appfuse.webapp.client.application.base.place.EntitySearchPlace;
import org.appfuse.webapp.client.application.utils.menu.MenuItem;

import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusWidget;

/**
 * @author ivangsa
 *
 */
public abstract class BaseNavigationBar extends Composite implements PlaceChangeEvent.Handler {

	protected static final ApplicationResources i18n = GWT.create(ApplicationResources.class);
	
	protected Application application;
	protected ApplicationMenu menu;

	/**
	 * 
	 */
	public BaseNavigationBar() {
		super();
	}

	public void setApplication(Application application) {
		this.application = application;
		this.menu = application.getMenu();
		this.application.getEventBus().addHandler(PlaceChangeEvent.TYPE, this);
	}

	protected boolean isSamePlace(Place newPlace,Place menuPlace) {
		if(newPlace == null || menuPlace == null) {
			return false;
		}
		
		if(newPlace instanceof EntityProxyPlace && menuPlace instanceof EntityProxyPlace) {
			EntityProxyPlace newEntityPlace = (EntityProxyPlace) newPlace;
			EntityProxyPlace menuEntityPlace = (EntityProxyPlace) menuPlace;
			return newEntityPlace.getProxyClass().equals(menuEntityPlace.getProxyClass());
		}
		else if(newPlace instanceof EntitySearchPlace && menuPlace instanceof EntitySearchPlace) {
			EntitySearchPlace newEntityPlace = (EntitySearchPlace) newPlace;
			EntitySearchPlace menuEntityPlace = (EntitySearchPlace) menuPlace;
			return newEntityPlace.getProxyClass().equals(menuEntityPlace.getProxyClass());
		}
		return newPlace.getClass().equals(menuPlace.getClass());
	}
	
	protected boolean isMenuItemAllowed(MenuItem menuItem) {
		return application.hasAnyRole(menuItem.getRoles().toArray(new String[menuItem.getRoles().size()]));
	}

	protected MenuItem findCurrentMenuItem(Place currentPlace) {
		for (MenuItem menuItem : menu.asList()) {
			if(isSamePlace(currentPlace, menuItem.getPlace())) {
				if(menuItem.getWidget() instanceof FocusWidget) {
					((FocusWidget) menuItem.getWidget()).setFocus(false);
				}
				return menuItem;
			}
		}
		return null;
	}
}
