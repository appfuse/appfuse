package org.appfuse.webapp.proxies;

import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.appfuse.model.User;
import org.appfuse.webapp.server.locators.UserLocator;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;


@ProxyFor(value=User.class, locator=UserLocator.class)
public interface UserProxy extends EntityProxy {

	Long getId();

	@NotNull @Size(max=150)
	String getUsername();

	@NotNull @Size(max=150)
	String getPassword();

	@NotNull @Size(max=150)
	String getConfirmPassword();

	@NotNull @Size(max=150)
	String getPasswordHint();

	@NotNull @Size(max=150)
	String getFirstName();

	@NotNull @Size(max=150)
	String getLastName();

	@NotNull @Size(max=150)
	String getEmail();

	@NotNull @Size(max=150)
	String getPhoneNumber();

	@NotNull @Size(max=150)
	String getWebsite();

	//@Valid
	AddressProxy getAddress();

	Set<RoleProxy> getRoles();

	Integer getVersion();

	boolean isEnabled();

	boolean isAccountExpired();

	boolean isAccountLocked();

	boolean isCredentialsExpired();

	void setId(Long id);

	void setUsername(String username);

	void setPassword(String password);

	void setConfirmPassword(String confirmPassword);

	void setPasswordHint(String passwordHint);

	void setFirstName(String firstName);

	void setLastName(String lastName);

	void setEmail(String email);

	void setPhoneNumber(String phoneNumber);

	void setWebsite(String website);

	void setAddress(AddressProxy address);

	void setRoles(Set<RoleProxy> roles);

	void setVersion(Integer version);

	void setEnabled(boolean enabled);

	void setAccountExpired(boolean accountExpired);

	void setAccountLocked(boolean accountLocked);

	void setCredentialsExpired(boolean credentialsExpired);

}