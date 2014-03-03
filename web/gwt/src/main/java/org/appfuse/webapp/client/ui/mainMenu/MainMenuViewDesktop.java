/**
 * 
 */
package org.appfuse.webapp.client.ui.mainMenu;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author ivangsa
 *
 */
public class MainMenuViewDesktop extends Composite implements MainMenuView {

	interface Binder extends UiBinder<Widget, MainMenuViewDesktop> {}
	private static final Binder BINDER = GWT.create(Binder.class);
	
	/**
	 * 
	 */
	public MainMenuViewDesktop() {
		super();
		initWidget(BINDER.createAndBindUi(this));
	}
	
	
}
