package org.appfuse.webapp.client.ui;

import org.appfuse.webapp.client.application.Application;
import org.appfuse.webapp.client.ui.login.events.LoginEvent;
import org.appfuse.webapp.client.ui.login.events.LogoutEvent;

import com.github.gwtbootstrap.client.ui.base.AlertBase;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.NotificationMole;
import com.google.gwt.user.client.ui.Widget;

/**
 * The outermost UI of the application.
 */
public class DesktopShell extends Shell implements LoginEvent.Handler, LogoutEvent.Handler {
	
	interface Binder extends UiBinder<Widget, DesktopShell> {	}
	private static final Binder uiBinder = GWT.create(Binder.class);

	@UiField DesktopNavigationBar navigationBar;

	@UiField FlowPanel messages;
	@UiField NotificationMole mole;

	public DesktopShell() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@Override
	public void setApplication(Application application) {
		super.setApplication(application);
		navigationBar.setApplication(application);
	}

	/**
	 * @return the notification mole for loading feedback
	 */
	public NotificationMole getMole() {
		return mole;
	}


	/**
	 * @param string
	 */
	public void addMessage(AlertBase alert) {
		messages.add(alert);
	}
	
	@Override
	protected void onAttach() {
		super.onAttach();
		navigationBar.load();
	}

	@Override
	public void onLoginEvent(LoginEvent loginEvent) {
		navigationBar.load();
	}


	@Override
	public void onLogoutEvent(LogoutEvent logoutEvent) {
		navigationBar.load();
	}
}