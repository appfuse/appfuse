package org.appfuse.webapp.proxies;

import org.appfuse.model.Role;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

@ProxyFor(value=Role.class)
public interface RoleProxy extends ValueProxy {

	Long getId();

	String getName();

	String getDescription();

	void setId(Long id);

	void setName(String name);

	void setDescription(String description);

}