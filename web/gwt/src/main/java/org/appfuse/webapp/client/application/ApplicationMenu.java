/**
 * 
 */
package org.appfuse.webapp.client.application;

import static org.appfuse.webapp.proxies.RoleProxy.ANONYMOUS;
import static org.appfuse.webapp.proxies.RoleProxy.AUTHENTICATED;
import static org.appfuse.webapp.proxies.RoleProxy.ROLE_ADMIN;
import static org.appfuse.webapp.proxies.RoleProxy.ROLE_USER;

import java.util.ArrayList;
import java.util.List;

import org.appfuse.webapp.client.application.base.place.EntitySearchPlace;
import org.appfuse.webapp.client.application.utils.menu.MenuItem;
import org.appfuse.webapp.client.ui.login.LoginPlace;
import org.appfuse.webapp.client.ui.logout.LogoutPlace;
import org.appfuse.webapp.client.ui.mainMenu.MainMenuPlace;
import org.appfuse.webapp.client.ui.reloadOptions.ReloadOptionsPlace;
import org.appfuse.webapp.client.ui.upload.FileUploadPlace;
import org.appfuse.webapp.client.ui.users.active.ActiveUsersPlace;
import org.appfuse.webapp.client.ui.users.editProfile.EditProfilePlace;
import org.appfuse.webapp.proxies.UserProxy;

import com.google.gwt.core.client.GWT;

/**
 * @author ivangsa
 *
 */
public class ApplicationMenu {

	private static final ApplicationResources i18n = GWT.create(ApplicationResources.class);
	
	private MenuItem rootMenu = new MenuItem(null);
	private MenuItem adminMenu = new MenuItem(i18n.menu_admin(), ROLE_ADMIN);
	
	private List<MenuItem> allMenuItems;

	{
		rootMenu.add(new MenuItem(i18n.mainMenu_title(), new MainMenuPlace(), ROLE_USER, ROLE_ADMIN));
		rootMenu.add(new MenuItem(i18n.menu_user(), new EditProfilePlace(), ROLE_USER, ROLE_ADMIN));
		
		rootMenu.add(adminMenu);
		adminMenu.add(new MenuItem(i18n.menu_admin_users(), new EntitySearchPlace(UserProxy.class), ROLE_ADMIN));
		adminMenu.add(new MenuItem(i18n.mainMenu_activeUsers(), new ActiveUsersPlace(), ROLE_ADMIN));
		adminMenu.add(new MenuItem(i18n.menu_admin_reload(), new ReloadOptionsPlace(), ROLE_ADMIN));
		adminMenu.add(new MenuItem(i18n.menu_selectFile(), new FileUploadPlace(), ROLE_ADMIN));
		
		rootMenu.add(new MenuItem(i18n.login_title(), new LoginPlace(), ANONYMOUS));
		rootMenu.add( new MenuItem(i18n.user_logout(), new LogoutPlace(), AUTHENTICATED));
	}
	
	public MenuItem getRootMenu() {
		return rootMenu;
	}
	
	public List<MenuItem> asList() {
		if(allMenuItems == null) {
			allMenuItems = new ArrayList<MenuItem>();
			appendChildrenToList(allMenuItems, rootMenu);
		}
		return allMenuItems;
	}
	
	protected void appendChildrenToList(List<MenuItem> menuItems, MenuItem menuItem){
		for (MenuItem childItem : menuItem) {
			menuItems.add(childItem);
			appendChildrenToList(menuItems, childItem);
		}
	}
}
