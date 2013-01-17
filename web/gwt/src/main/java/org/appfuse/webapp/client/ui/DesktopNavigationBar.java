package org.appfuse.webapp.client.ui;

import org.appfuse.webapp.client.application.Application;
import org.appfuse.webapp.client.application.base.place.EntityListPlace;
import org.appfuse.webapp.client.ui.login.LoginPlace;
import org.appfuse.webapp.client.ui.logout.LogoutPlace;
import org.appfuse.webapp.client.ui.mainMenu.MainMenuPlace;
import org.appfuse.webapp.client.ui.users.edit.places.EditProfilePlace;
import org.appfuse.webapp.proxies.RoleProxy;
import org.appfuse.webapp.proxies.UserProxy;

import com.github.gwtbootstrap.client.ui.Brand;
import com.github.gwtbootstrap.client.ui.Dropdown;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.place.shared.Place;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

class DesktopNavigationBar extends Composite {
	interface Binder extends UiBinder<Widget, DesktopNavigationBar> {	}
	private static final Binder BINDER = GWT.create(Binder.class);

	private Application application;
	
	@UiField Brand brand;
	@UiField NavLink login;
	@UiField NavLink mainMenu;
	@UiField NavLink editProfile;
	
	@UiField Dropdown adminMenu;
	@UiField NavLink users;
	@UiField NavLink activeUsers;
	@UiField NavLink reload;
	@UiField NavLink upload;
	
	@UiField NavLink logout;
	
	/**
	 * 
	 */
	public DesktopNavigationBar() {
		super();
		initWidget(BINDER.createAndBindUi(this));
	}

	public void setApplication(Application application) {
		this.application = application;
		registerTargetPlaces();
	}
	
	public void load() {
		updateVisibility();
	}

	public void updateVisibility() {
		boolean isAuthenticated = application.getCurrentUser() != null; 
		boolean isUser = application.isUserInRole(RoleProxy.ROLE_USER);
		boolean isAdmin = application.isUserInRole(RoleProxy.ROLE_ADMIN);
		
		login.setVisible(!isAuthenticated);
		mainMenu.setVisible(isAuthenticated);
		editProfile.setVisible(isAuthenticated);
		adminMenu.setVisible(isAdmin);
		users.setVisible(isAdmin);
		activeUsers.setVisible(isAdmin);
		reload.setVisible(isAdmin);
		upload.setVisible(isAdmin);
		logout.setVisible(isAuthenticated);
	}
	
	private void registerTargetPlaces() {
		registerTargetPlace(login, new LoginPlace());
		registerTargetPlace(mainMenu, new MainMenuPlace());
		registerTargetPlace(editProfile, new EditProfilePlace());
		registerTargetPlace(users, new EntityListPlace(UserProxy.class));
		registerTargetPlace(activeUsers, new EntityListPlace(UserProxy.class));
		registerTargetPlace(logout, new LogoutPlace());
	}

	private void registerTargetPlace(HasClickHandlers widget, final Place place) {
		widget.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				application.getPlaceController().goTo(place);
			}
		});
	}
}