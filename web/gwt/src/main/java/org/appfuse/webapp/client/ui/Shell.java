/**
 * 
 */
package org.appfuse.webapp.client.ui;

import org.appfuse.webapp.client.application.Application;
import org.appfuse.webapp.client.application.base.security.LoginEvent;
import org.appfuse.webapp.client.application.base.security.LogoutEvent;

import com.github.gwtbootstrap.client.ui.Alert;
import com.github.gwtbootstrap.client.ui.base.AlertBase;
import com.github.gwtbootstrap.client.ui.constants.AlertType;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.NotificationMole;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * @author ivangsa
 *
 */
public abstract class Shell extends Composite implements LoginEvent.Handler, LogoutEvent.Handler {

    protected Application application;

    @UiField
    SimplePanel contentsPanel;

    @UiField
    NotificationMole mole;

    @UiField
    FlowPanel messages;

    /**
     * Contents Panel
     * 
     * @return
     */
    public SimplePanel getContentsPanel() {
        return contentsPanel;
    }

    /**
     * @return the notification mole for loading feedback
     */
    public NotificationMole getMole() {
        return mole;
    }

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
    public void addMessage(final AlertBase alert) {
        messages.add(alert);
        Window.scrollTo(0, 0);
    }

    /**
     * 
     * @param html
     * @param alertType
     */
    public void addMessage(final String html, final AlertType alertType) {
        final Alert alert = new Alert(html);
        alert.setType(alertType);
        addMessage(alert);
    }

    public void setApplication(Application application) {
        this.application = application;
    }

}
