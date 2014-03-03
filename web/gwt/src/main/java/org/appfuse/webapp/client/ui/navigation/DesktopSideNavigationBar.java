/**
 * 
 */
package org.appfuse.webapp.client.ui.navigation;

import org.appfuse.webapp.client.application.utils.menu.MenuItem;

import com.github.gwtbootstrap.client.ui.NavHeader;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.NavList;
import com.github.gwtbootstrap.client.ui.constants.Constants;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.place.shared.PlaceChangeEvent;

/**
 * @author ivangsa
 *
 */
public class DesktopSideNavigationBar extends BaseNavigationBar {

	private NavList navList = new NavList();
	
	/**
	 * @param menu
	 */
	public DesktopSideNavigationBar() {
		super();
		navList.setVisible(false);
		initWidget(navList);
	}

	@Override
	public void onPlaceChange(PlaceChangeEvent event) {
		navList.clear();
		MenuItem currentMenuItem = findCurrentMenuItem(event.getNewPlace());
		if(currentMenuItem != null && currentMenuItem.getParent() != null 
				&& currentMenuItem.getParent().getTitle() != null) {// do not draw root menu on the side
			drawMenu(currentMenuItem);
			navList.setVisible(true);
		} else {
			navList.setVisible(false);
		}
	}
	
	private void drawMenu(MenuItem currentMenuItem) {
		NavHeader navHeader = new NavHeader(currentMenuItem.getParent().getTitle());
		navList.add(navHeader);
		for(final MenuItem menuItem : currentMenuItem.getParent()) {
			NavLink navLink = new NavLink(menuItem.getTitle());
			navList.add(navLink);
			if(menuItem.getPlace() != null) {
				navLink.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						application.getPlaceController().goTo(menuItem.getPlace());
					}
				});
			}
			if(menuItem == currentMenuItem) {
				navLink.addStyleName(Constants.ACTIVE);
			}
		}
	}
}
