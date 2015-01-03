/**
 * 
 */
package org.appfuse.webapp.client.ui.home;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author ivangsa
 *
 */
public class HomeViewDesktop extends Composite implements HomeView {

    interface Binder extends UiBinder<Widget, HomeViewDesktop> {
    }

    private static final Binder BINDER = GWT.create(Binder.class);

    /**
	 * 
	 */
    public HomeViewDesktop() {
        super();
        initWidget(BINDER.createAndBindUi(this));
    }

}
