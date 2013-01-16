package org.appfuse.webapp.proxies;

import org.appfuse.model.Role;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

@ProxyFor(value=Role.class)
public interface RoleProxy extends ValueProxy {

	public static final String ROLE_USER = "ROLE_USER";
	public static final String ROLE_ADMIN = "ROLE_ADMIN";
	
	Long getId();

	String getName();

	String getDescription();

	void setId(Long id);

	void setName(String name);

	void setDescription(String description);

}