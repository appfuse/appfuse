/**
 * 
 */
package org.appfuse.webapp.client.ui.mainMenu;

import org.appfuse.webapp.client.application.Application;
import org.appfuse.webapp.client.application.base.activity.AbstractBaseActivity;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

/**
 * @author ivangsa
 *
 */
public class MainMenuActivity extends AbstractBaseActivity {


	public MainMenuActivity(MainMenuPlace currentPlace, Application application) {
		super(application);
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.activity.shared.Activity#start(com.google.gwt.user.client.ui.AcceptsOneWidget, com.google.gwt.event.shared.EventBus)
	 */
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		if(((MainMenuPlace)currentPlace).isReload()) {
			Window.Location.reload();
		}else {
			MainMenuView view = viewFactory.getView(MainMenuView.class);
			panel.setWidget(view);
		}
	}

}
