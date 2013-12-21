package org.appfuse.webapp.client.application.utils.menu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.appfuse.webapp.client.proxies.RoleProxy;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.Widget;

public class MenuItem implements Iterable<MenuItem>{

	private final String title;
	private final Place place;
	private final List<String> roles = new ArrayList<String>();
	private MenuItem parent;
	private final List<MenuItem> children = new ArrayList<MenuItem>();
	private Widget widget;
	
	/**
	 * @param title
	 * @param children
	 */
	public MenuItem(String title, String... roles) {
		this(title, null, roles);
	}
	
	public MenuItem(String title, Place place, String... roles) {
		super();
		this.title = title;
		this.place = place;
		this.roles.addAll(Arrays.asList(roles));
		if(this.roles.isEmpty()) {
			this.roles.add(RoleProxy.ANONYMOUS);
		}
	}
	
	public String getTitle() {
		return title;
	}
	
	public Place getPlace() {
		return place;
	}
	
	public List<String> getRoles() {
		return roles;
	}

	public MenuItem getParent() {
		return parent;
	}
	
	public List<MenuItem> getChildren() {
		return children;
	}
	
	public Widget getWidget() {
		return widget;
	}
	
	public void setWidget(Widget widget) {
		this.widget = widget;
	}

	public void add(MenuItem item) {
		item.parent = this;
		children.add(item);
	}
	
	@Override
	public Iterator<MenuItem> iterator() {
		return children.iterator();
	}
	
	public boolean isLeafMenuItem() {
		return children.isEmpty();
	}
}
