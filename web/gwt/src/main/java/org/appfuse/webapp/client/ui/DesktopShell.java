package org.appfuse.webapp.client.ui;

import org.appfuse.webapp.client.application.Application;
import org.appfuse.webapp.client.application.base.security.LoginEvent;
import org.appfuse.webapp.client.application.base.security.LogoutEvent;
import org.appfuse.webapp.client.ui.navigation.NavigationBar;
import org.appfuse.webapp.client.ui.navigation.SideNavigationBar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
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