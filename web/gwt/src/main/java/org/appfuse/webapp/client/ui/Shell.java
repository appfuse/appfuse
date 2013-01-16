/**
 * 
 */
package org.appfuse.webapp.client.ui;

import org.appfuse.webapp.client.application.Application;
import org.appfuse.webapp.client.ui.login.events.LoginEvent;
import org.appfuse.webapp.client.ui.login.events.LogoutEvent;

import com.github.gwtbootstrap.client.ui.base.AlertBase;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.NotificationMole;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * @author ivangsa
 *
 */
public abstract class Shell extends Composite implements LoginEvent.Handler, LogoutEvent.Handler {

	protected Application application;

	@UiField SimplePanel contentsPanel;

	/**
	 * Contents Panel
	 * @return
	 */
	public SimplePanel getContentsPanel() {
		return contentsPanel;
	}
	
	public void addMessage(AlertBase alert) {
		//TODO
	}

	public NotificationMole getMole() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setApplication(Application application) {
		this.application = application;
	}
}
