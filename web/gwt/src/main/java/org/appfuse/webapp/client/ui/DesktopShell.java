package org.appfuse.webapp.client.ui;

import java.util.ArrayList;
import java.util.List;

import org.appfuse.webapp.client.application.Application;
import org.appfuse.webapp.client.ui.login.events.LoginEvent;
import org.appfuse.webapp.client.ui.login.events.LogoutEvent;
import org.appfuse.webapp.client.ui.navigation.DesktopNavigationBar;
import org.appfuse.webapp.client.ui.navigation.DesktopSideNavigationBar;

import com.github.gwtbootstrap.client.ui.Alert;
import com.github.gwtbootstrap.client.ui.base.AlertBase;
import com.github.gwtbootstrap.client.ui.constants.AlertType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.NotificationMole;
import com.google.gwt.user.client.ui.Widget;

/**
 * The outermost UI of the application.
 */
public class DesktopShell extends Shell implements LoginEvent.Handler, LogoutEvent.Handler, PlaceChangeEvent.Handler {
	
	interface Binder extends UiBinder<Widget, DesktopShell> {	}
	private static final Binder uiBinder = GWT.create(Binder.class);

	@UiField DesktopNavigationBar navigationBar;
	@UiField DesktopSideNavigationBar sideNavigationBar;

	@UiField FlowPanel messages;
	@UiField NotificationMole mole;
	
	@UiField Element currentUserInfo;

	public DesktopShell() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@Override
	public void setApplication(Application application) {
		super.setApplication(application);
		navigationBar.setApplication(application);
		sideNavigationBar.setApplication(application);
	}

	/**
	 * @return the notification mole for loading feedback
	 */
	public NotificationMole getMole() {
		return mole;
	}

	@Override
	public void clearMessages() {
		messages.clear();
	}

	/**
	 * Add an user message to the shell.
	 * 
	 * Messages live on screen until next {@link PlaceChangeEvent}.
	 *  
	 * @param alert
	 */
	@Override
	public void addMessage(AlertBase alert) {
		messages.add(alert);
		Window.scrollTo(0 ,0);
	}
	
	/**
	 * 
	 * @param html
	 * @param alertType
	 */
	@Override
	public void addMessage(String html, AlertType alertType) {
		Alert alert = new Alert(html);
		alert.setType(alertType);
		addMessage(alert);
	}
	
	@Override
	protected void onAttach() {
		super.onAttach();
		navigationBar.load();
	}

	@Override
	public void onLoginEvent(LoginEvent loginEvent) {
		navigationBar.load();
		SafeHtmlBuilder sb = new SafeHtmlBuilder();
		sb.appendEscaped(" | ");
		sb.appendEscaped(application.getI18n().user_status());
		sb.append(' ');
		sb.appendEscaped(application.getCurrentUsername());
		currentUserInfo.setInnerSafeHtml(sb.toSafeHtml());
	}


	@Override
	public void onLogoutEvent(LogoutEvent logoutEvent) {
		navigationBar.load();
		currentUserInfo.setInnerHTML("");
	}

	@Override
	public void onPlaceChange(PlaceChangeEvent event) {
		Window.scrollTo(0 ,0);
		for (Widget widget : messages) {
			try {
				widget.removeFromParent();
			} catch (Throwable e) {
				//already removed
				//XXX null in native method Node.removeChild:L291
			}
		}
	}
}