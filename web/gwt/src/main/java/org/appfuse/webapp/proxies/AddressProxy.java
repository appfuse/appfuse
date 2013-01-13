package org.appfuse.webapp.proxies;

import org.appfuse.model.Address;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

@ProxyFor(Address.class)
public interface AddressProxy extends ValueProxy {

	String getAddress();

	String getCity();

	String getProvince();

	String getCountry();

	String getPostalCode();

	void setAddress(String address);

	void setCity(String city);

	void setCountry(String country);

	void setPostalCode(String postalCode);

	void setProvince(String province);

}