package org.appfuse.webapp.client.ui;

import org.appfuse.webapp.client.application.Application;
import org.appfuse.webapp.client.application.base.security.LoginEvent;
import org.appfuse.webapp.client.application.base.security.LogoutEvent;
import org.appfuse.webapp.client.ui.navigation.NavigationBar;
import org.appfuse.webapp.client.ui.navigation.SideNavigationBar;

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

    interface Binder extends UiBinder<Widget, DesktopShell> {
    }

    private static final Binder uiBinder = GWT.create(Binder.class);

    @UiField
    NavigationBar navigationBar;
    @UiField
    SideNavigationBar sideNavigationBar;

    @UiField
    FlowPanel messages;
    @UiField
    NotificationMole mole;

    @UiField
    Element currentUserInfo;

    public DesktopShell() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public void setApplication(final Application application) {
        super.setApplication(application);
        navigationBar.setApplication(application);
        sideNavigationBar.setApplication(application);
    }

    /**
     * @return the notification mole for loading feedback
     */
    @Override
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
    public void addMessage(final AlertBase alert) {
        messages.add(alert);
        Window.scrollTo(0, 0);
    }

    /**
     * 
     * @param html
     * @param alertType
     */
    @Override
    public void addMessage(final String html, final AlertType alertType) {
        final Alert alert = new Alert(html);
        alert.setType(alertType);
        addMessage(alert);
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        navigationBar.load();
    }

    @Override
    public void onLoginEvent(final LoginEvent loginEvent) {
        navigationBar.load();
        final SafeHtmlBuilder sb = new SafeHtmlBuilder();
        sb.appendEscaped(" | ");
        sb.appendEscaped(application.getI18n().user_status());
        sb.append(' ');
        sb.appendEscaped(application.getCurrentUsername());
        currentUserInfo.setInnerSafeHtml(sb.toSafeHtml());
    }

    @Override
    public void onLogoutEvent(final LogoutEvent logoutEvent) {
        navigationBar.load();
        currentUserInfo.setInnerHTML("");
    }

    @Override
    public void onPlaceChange(final PlaceChangeEvent event) {
        Window.scrollTo(0, 0);
        for (final Widget widget : messages) {
            try {
                widget.removeFromParent();
            } catch (final Throwable e) {
                // already removed
                // XXX null in native method Node.removeChild:L291
            }
        }
    }
}