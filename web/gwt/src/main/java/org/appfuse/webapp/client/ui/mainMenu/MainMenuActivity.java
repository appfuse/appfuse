/**
 * 
 */
package org.appfuse.webapp.client.ui.mainMenu;

import org.appfuse.webapp.client.application.Application;
import org.appfuse.webapp.client.application.base.activity.AbstractBaseActivity;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

/**
 * @author ivangsa
 *
 */
public class MainMenuActivity extends AbstractBaseActivity {


    private final MainMenuView view;

    @Inject
    public MainMenuActivity(final Application application, final MainMenuView view) {
        super(application);
        this.view = view;
        setTitle(i18n.mainMenu_title());
        setBodyId("home");
        setBodyClassname("home");
    }

    /* (non-Javadoc)
     * @see com.google.gwt.activity.shared.Activity#start(com.google.gwt.user.client.ui.AcceptsOneWidget, com.google.gwt.event.shared.EventBus)
     */
    @Override
    public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
        panel.setWidget(view);
        setDocumentTitleAndBodyAttributtes();
    }

}
