package org.appfuse.webapp.proxies;

import java.util.Set;

import javax.validation.constraints.NotNull;

import org.appfuse.model.User;
import org.appfuse.webapp.server.locators.UserRequestService;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;


@ProxyFor(value=User.class, locator=UserRequestService.class)
public interface UserProxy extends EntityProxy {

	Long getId();

	@NotNull
	String getUsername();

	String getPassword();

	String getConfirmPassword();

	String getPasswordHint();

	String getFirstName();

	String getLastName();

	@NotNull
	String getEmail();

	String getPhoneNumber();

	String getWebsite();

	String getFullName();

	AddressProxy getAddress();

	Set<RoleProxy> getRoles();

	Integer getVersion();

	boolean isEnabled();

	boolean isAccountExpired();

	boolean isAccountNonExpired();

	boolean isAccountLocked();

	boolean isAccountNonLocked();

	boolean isCredentialsExpired();

	boolean isCredentialsNonExpired();

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