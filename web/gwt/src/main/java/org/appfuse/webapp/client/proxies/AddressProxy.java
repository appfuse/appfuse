package org.appfuse.webapp.client.proxies;

import javax.validation.constraints.Size;

import org.appfuse.model.Address;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

@ProxyFor(Address.class)
public interface AddressProxy extends ValueProxy {

    @Size(max = 150)
    String getAddress();

    @Size(max = 50)
    String getCity();

    @Size(max = 100)
    String getProvince();

    @Size(max = 100)
    String getCountry();

    @Size(max = 15)
    String getPostalCode();

    void setAddress(String address);

    void setCity(String city);

    void setCountry(String country);

    void setPostalCode(String postalCode);

    void setProvince(String province);

}