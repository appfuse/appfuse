/**
 * 
 */
package org.appfuse.webapp.client.ui;

import com.github.gwtbootstrap.client.ui.base.AlertBase;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.NotificationMole;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * @author ivangsa
 *
 */
public class Shell extends Composite {

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

}
