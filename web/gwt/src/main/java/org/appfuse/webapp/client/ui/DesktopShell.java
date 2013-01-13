package org.appfuse.webapp.client.ui;

import com.github.gwtbootstrap.client.ui.base.AlertBase;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.NotificationMole;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The outermost UI of the application.
 */
public class DesktopShell extends Shell {
	
	interface Binder extends UiBinder<Widget, DesktopShell> {	}
	private static final Binder uiBinder = GWT.create(Binder.class);

	@UiField FlowPanel messages;
	@UiField NotificationMole mole;

	public DesktopShell() {
		initWidget(uiBinder.createAndBindUi(this));
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
}