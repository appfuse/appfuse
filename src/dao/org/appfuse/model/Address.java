package org.appfuse.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
/**
 * This class is used to represent an address.</p>
 *
 * <p><a href="Address.java.html"><i>View Source</i></a></p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 *
 * @struts.form include-all="true" extends="BaseForm"
 */
public class Address extends BaseObject implements Serializable {
    protected String address;
    protected String city;
    protected String province;
    protected String country;
    protected String postalCode;

    /**
     * Returns the address.
     * @return String
     *
     * @hibernate.property column="address" not-null="false" length="150"
     */
    public String getAddress() {
        return address;
    }

    /**
     * Returns the city.
     * @return String
     *
     * @struts.validator type="required"
     * @hibernate.property column="city" not-null="true" length="50"
     */
    public String getCity() {
        return city;
    }

    /**
     * Returns the province.
     * @return String
     *
     * @struts.validator type="required"
     * @hibernate.property column="province" length="100"
     */
    public String getProvince() {
        return province;
    }

    /**
     * Returns the country.
     * @return String
     *
     * @struts.validator type="required"
     * @hibernate.property column="country" length="100"
     */
    public String getCountry() {
        return country;
    }

    /**
     * Returns the postalCode.
     * @return String
     *
     * @struts.validator type="required"
     * @struts.validator type="mask" msgkey="errors.zip"
     * @struts.validator-var name="mask" value="${zip}"
     * @hibernate.property column="postal_code" not-null="true" length="15"
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Sets the address.
     * @param address The address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Sets the city.
     * @param city The city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Sets the country.
     * @param country The country to set
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Sets the postalCode.
     * @param postalCode The postalCode to set
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * Sets the province.
     * @param province The province to set
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * Generated using Commonclipse (http://commonclipse.sf.net)
     */
    public boolean equals(Object object) {
        if (!(object instanceof Address)) {
            return false;
        }

        Address rhs = (Address) object;

        return new EqualsBuilder().append(this.postalCode, rhs.postalCode)
                                  .append(this.country, rhs.country)
                                  .append(this.address, rhs.address)
                                  .append(this.province, rhs.province)
                                  .append(this.city, rhs.city).isEquals();
    }

    /**
     * Generated using Commonclipse (http://commonclipse.sf.net)
     */
    public int hashCode() {
        return new HashCodeBuilder(-426830461, 631494429).append(this.postalCode)
                                                         .append(this.country)
                                                         .append(this.address)
                                                         .append(this.province)
                                                         .append(this.city)
                                                         .toHashCode();
    }

    /**
     * Generated using Commonclipse (http://commonclipse.sf.net)
     */
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("country", this.country)
                .append("address", this.address).append("province",
                        this.province).append("postalCode", this.postalCode)
                .append("city", this.city).toString();
    }
}
