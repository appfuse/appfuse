package org.appfuse.webapp.client.ui.navigation;

import org.appfuse.webapp.client.application.utils.menu.MenuItem;

import com.github.gwtbootstrap.client.ui.constants.Constants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

public class SideNavigationBar extends BaseNavigationBar {

    private static SideNavigationBarUiBinder uiBinder = GWT.create(SideNavigationBarUiBinder.class);

    interface SideNavigationBarUiBinder extends UiBinder<Widget, SideNavigationBar> {
    }

    @UiField
    UListElement sideBar;

    public SideNavigationBar() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public void onPlaceChange(final PlaceChangeEvent event) {
        sideBar.setInnerHTML("");
        final MenuItem currentMenuItem = findCurrentMenuItem(event.getNewPlace());
        if (currentMenuItem != null && currentMenuItem.getParent() != null && currentMenuItem.getParent().getTitle() != null) {
            drawMenu(currentMenuItem);
            sideBar.removeClassName("hide");
        } else {
            // do not draw root menu on the side
            sideBar.addClassName("hide");
        }
    }

    private void drawMenu(final MenuItem currentMenuItem) {
        final LIElement navHeader = Document.get().createLIElement();
        navHeader.setInnerText(currentMenuItem.getParent().getTitle());
        navHeader.addClassName("nav-header");
        sideBar.appendChild(navHeader);

        for (final MenuItem menuItem : currentMenuItem.getParent()) {
            final LIElement li = Document.get().createLIElement();
            final AnchorElement anchor = Document.get().createAnchorElement();
            anchor.setTitle(menuItem.getTitle());
            anchor.setInnerText(menuItem.getTitle());
            anchor.setHref("#" + application.getPlaceHistoryMapper().getToken(menuItem.getPlace()));
            li.appendChild(anchor);
            menuItem.setElement(li);
            sideBar.appendChild(li);

            if (menuItem == currentMenuItem) {
                li.addClassName(Constants.ACTIVE);
            }
        }
    }

}
