/**
 * 
 */
package org.appfuse.webapp.client.ui.navigation;

import org.appfuse.webapp.client.application.utils.menu.MenuItem;

import com.github.gwtbootstrap.client.ui.constants.Constants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author ivangsa
 *
 */
public class NavigationBar extends BaseNavigationBar implements PlaceChangeEvent.Handler {
    private static NavigationBarUiBinder uiBinder = GWT.create(NavigationBarUiBinder.class);

    interface NavigationBarUiBinder extends UiBinder<Widget, NavigationBar> {
    }

    @UiField
    AnchorElement brand;
    @UiField
    UListElement navBar;

    public NavigationBar() {
        initWidget(uiBinder.createAndBindUi(this));
        this.setStyleName(DEBUG_ID_PREFIX);
    }

    public void load() {
        brand.setHref(application.getContextPath());
        navBar.setInnerHTML("");
        for (final MenuItem menuItem : menu.getRootMenu()) {
            drawMenu(navBar, menuItem);
        }
        activateCurrentMenuItem(application.getPlaceController().getWhere());
    }

    private void drawMenu(final Element panel, final MenuItem menuItem) {
        if (!isMenuItemAllowed(menuItem)) {
            return;
        }
        if (menuItem.isLeafMenuItem()) {
            final LIElement li = Document.get().createLIElement();
            final AnchorElement a = Document.get().createAnchorElement();
            a.setTitle(menuItem.getTitle());
            a.setInnerText(menuItem.getTitle());
            a.setHref("#" + application.getPlaceHistoryMapper().getToken(menuItem.getPlace()));
            li.appendChild(a);
            panel.appendChild(li);
            menuItem.setElement(li);
        } else {
            final LIElement dropdown = Document.get().createLIElement();
            dropdown.addClassName("dropdown");
            final AnchorElement toogle = Document.get().createAnchorElement();
            toogle.setTitle(menuItem.getTitle());
            toogle.setInnerText(menuItem.getTitle());
            toogle.addClassName("dropdown-toggle");
            toogle.setAttribute("data-toggle", "dropdown");// XXX
            menuItem.setElement(dropdown);
            final UListElement dropdownMenu = Document.get().createULElement();
            dropdownMenu.addClassName("dropdown-menu");
            dropdown.appendChild(toogle);
            dropdown.appendChild(dropdownMenu);
            panel.appendChild(dropdown);
            for (final MenuItem childItem : menuItem) {
                drawMenu(dropdownMenu, childItem);
            }
        }
    }

    @Override
    public void onPlaceChange(final PlaceChangeEvent event) {
        activateCurrentMenuItem(event.getNewPlace());
    }

    private void activateCurrentMenuItem(final Place currentPlace) {
        for (final MenuItem menuItem : menu.asList()) {
            final Element element = menuItem.getElement();
            if (element != null) {
                element.removeClassName(Constants.ACTIVE);
            }
        }
        final MenuItem currentMenuItem = findCurrentMenuItem(currentPlace);
        if (currentMenuItem != null) {
            setActive(currentMenuItem);
        }
    }

    /**
     * Marks as 'active' menu item for current place and its parent menu places.
     * 
     * @param menuItem
     * @return
     */
    private void setActive(final MenuItem menuItem) {
        if (menuItem != null) {
            final Element element = menuItem.getElement();
            if (element != null) {
                element.addClassName(Constants.ACTIVE);
            }
            setActive(menuItem.getParent());
        }
    }
}
