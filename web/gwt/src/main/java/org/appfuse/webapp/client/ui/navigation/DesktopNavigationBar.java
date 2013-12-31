/**
 * 
 */
package org.appfuse.webapp.client.ui.navigation;

import org.appfuse.webapp.client.application.utils.menu.MenuItem;

import com.github.gwtbootstrap.client.ui.Brand;
import com.github.gwtbootstrap.client.ui.Dropdown;
import com.github.gwtbootstrap.client.ui.Nav;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.ResponsiveNavbar;
import com.github.gwtbootstrap.client.ui.constants.Constants;
import com.github.gwtbootstrap.client.ui.constants.NavbarPosition;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author ivangsa
 *
 */
public class DesktopNavigationBar extends BaseNavigationBar implements PlaceChangeEvent.Handler {

	private final ResponsiveNavbar responsiveNavbar;
	private final Brand brand;
	private final Nav nav;

	
	/**
	 * @param menu
	 */
	public DesktopNavigationBar() {
		super();
		responsiveNavbar = new ResponsiveNavbar();
		responsiveNavbar.setPosition(NavbarPosition.TOP);
		
		responsiveNavbar.add(brand = new Brand(i18n.webapp_name()));
		responsiveNavbar.add(nav = new Nav());
		
		initWidget(responsiveNavbar);
	}
	
	public void load() {
		nav.clear();
		brand.setHref(application.getContextPath());
		for (MenuItem menuItem : menu.getRootMenu()) {
			drawMenu(nav, menuItem);
		}
		activateCurrentMenuItem(application.getPlaceController().getWhere());
	}
	
	private void drawMenu(Panel panel, final MenuItem menuItem) {
		if(!isMenuItemAllowed(menuItem)) {
			return;
		}
		if(menuItem.isLeafMenuItem()) {
			NavLink navLink = new NavLink(menuItem.getTitle());
			menuItem.setWidget(navLink);
			navLink.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					application.getPlaceController().goTo(menuItem.getPlace());
				}
			});
			panel.add(navLink);
		} else {
			Dropdown dropdown = new Dropdown(menuItem.getTitle());
			menuItem.setWidget(dropdown);
			panel.add(dropdown);
			for (MenuItem childItem : menuItem) {
				drawMenu(dropdown, childItem);
			}
		}
	}

	@Override
	public void onPlaceChange(PlaceChangeEvent event) {
		activateCurrentMenuItem(event.getNewPlace());
	}
	
	private void activateCurrentMenuItem(Place currentPlace) {
		for (MenuItem menuItem : menu.asList()) {
			Widget menuWidget = menuItem.getWidget();
			if(menuWidget != null) {
				menuWidget.removeStyleName(Constants.ACTIVE);
			}
		}
		MenuItem currentMenuItem = findCurrentMenuItem(currentPlace);
		if(currentMenuItem != null) {
			setActive(currentMenuItem);
		}
	}
	
	/**
	 * Marks as 'active' menu item for current place and its parent menu places.
	 * @param menuItem
	 * @return
	 */
	private void setActive(MenuItem menuItem) {
		if(menuItem != null) {
			Widget menuWidget = menuItem.getWidget();
			if(menuWidget != null) {
				menuWidget.addStyleName(Constants.ACTIVE);			
			}
			setActive(menuItem.getParent());
		}
	}
}
