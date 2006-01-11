package org.appfuse.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * This class is used to represent an address.</p>
 *
 * <p><a href="Address.java.html"><i>View Source</i></a></p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class Address extends BaseObject implements Serializable {
    private static final long serialVersionUID = 3617859655330969141L;
    protected String address;
    protected String city;
    protected String province;
    protected String country;
    protected String postalCode;

    /**
     * @hibernate.property column="address" not-null="false" length="150"
     */
    public String getAddress() {
        return address;
    }

    /**
     * @hibernate.property column="city" not-null="true" length="50"
     */
    public String getCity() {
        return city;
    }

    /**
     * @hibernate.property column="province" length="100"
     */
    public String getProvince() {
        return province;
    }

    /**
     * @hibernate.property column="country" length="100"
     */
    public String getCountry() {
        return country;
    }

    /**
     * @hibernate.property column="postal_code" not-null="true" length="15"
     */
    public String getPostalCode() {
        return postalCode;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @spring.validator type="required"
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @spring.validator type="required"
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @spring.validator type="required"
     * @spring.validator type="mask" msgkey="errors.zip"
     * @spring.validator-var name="mask" value="${zip}"
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * @spring.validator type="required"
     */
    public void setProvince(String province) {
        this.province = province;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address)) return false;

        final Address address1 = (Address) o;

        if (address != null ? !address.equals(address1.address) : address1.address != null) return false;
        if (city != null ? !city.equals(address1.city) : address1.city != null) return false;
        if (country != null ? !country.equals(address1.country) : address1.country != null) return false;
        if (postalCode != null ? !postalCode.equals(address1.postalCode) : address1.postalCode != null) return false;
        if (province != null ? !province.equals(address1.province) : address1.province != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (address != null ? address.hashCode() : 0);
        result = 29 * result + (city != null ? city.hashCode() : 0);
        result = 29 * result + (province != null ? province.hashCode() : 0);
        result = 29 * result + (country != null ? country.hashCode() : 0);
        result = 29 * result + (postalCode != null ? postalCode.hashCode() : 0);
        return result;
    }

    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("country", this.country)
                .append("address", this.address).append("province",
                        this.province).append("postalCode", this.postalCode)
                .append("city", this.city).toString();
    }
}
