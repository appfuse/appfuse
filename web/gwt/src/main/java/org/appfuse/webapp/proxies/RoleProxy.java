package org.appfuse.webapp.proxies;

import org.appfuse.model.Role;
import org.appfuse.webapp.server.locators.RoleLocator;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

@ProxyFor(value=Role.class, locator=RoleLocator.class)
public interface RoleProxy extends EntityProxy {

	public static final String ROLE_USER = "ROLE_USER";
	public static final String ROLE_ADMIN = "ROLE_ADMIN";
	
	Long getId();

	String getName();

	String getDescription();

	void setId(Long id);

	void setName(String name);

	void setDescription(String description);

}