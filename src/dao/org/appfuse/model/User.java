package org.appfuse.model;

import java.util.ArrayList;
import java.util.List;


/**
 * User class
 *
 * This class is used to generate the Struts Validator Form
 * as well as the Hibernate persistence later.
 *
 * <p>
 * <a href="User.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @version $Revision: 1.1 $ $Date: 2004/03/01 06:19:03 $
 *
 * @struts.form include-all="true" extends="BaseForm"
 * @hibernate.class table="app_user"
 */
public class User extends BaseObject {
    //~ Instance fields ========================================================

	protected Long id;
	protected String username;
	protected String password;
	protected String confirmPassword;
	protected String firstName;
	protected String lastName;
	protected String address;
	protected String city;
	protected String province;
	protected String country;
	protected String postalCode;
	protected String phoneNumber;
	protected String email;
	protected String website;
	protected String passwordHint;
    protected List roles;

    //~ Methods ================================================================

    /**
     * Returns the id.
     * @return String
     *
     * @hibernate.id column="id"
     *  generator-class="increment" unsaved-value="null"
     */
    public Long getId() {
        return id;
    }

    /**
     * Returns the username.
     * @return String
     *
     * @struts.validator type="required"
     * @hibernate.property
     *  column="username" not-null="true" unique="true"
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the password.
     * @return String
     *
     * @struts.validator type="required"
     * @struts.validator type="twofields" msgkey="errors.twofields"
     * @struts.validator-args arg1resource="userFormEx.password"
     * @struts.validator-args arg1resource="userFormEx.confirmPassword"
     * @struts.validator-var name="secondProperty" value="confirmPassword"
     * @hibernate.property
     *  column="password" not-null="true"
     */
    public String getPassword() {
        return password;
    }

	/**
	 * Returns the confirmedPassword.
	 * @return String
	 *
	 * @struts.validator type="required"
	 */
	public String getConfirmPassword() {
		return confirmPassword;
	}
	
    /**
     * Returns the firstName.
     * @return String
     *
     * @struts.validator type="required"
     * @hibernate.property
     *  column="firstName" not-null="true"
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Returns the lastName.
     * @return String
     *
     * @struts.validator type="required"
     * @hibernate.property
     *  column="lastName" not-null="true"
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Returns the address.
     * @return String
     *
     * @hibernate.property
     *  column="address" not-null="false"
     */
    public String getAddress() {
        return address;
    }

    /**
     * Returns the city.
     * @return String
     *
     * @struts.validator type="required"
     * @hibernate.property
     *  column="city" not-null="true"
     */
    public String getCity() {
        return city;
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
     * Returns the email.  This is an optional field for specifying a
     * different e-mail than the username.
     * @return String
     *
     * @struts.validator type="required"
     * @struts.validator type="email"
     * @hibernate.property column="email" not-null="false"
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns the phoneNumber.
     * @return String
     *
     * @struts.validator type="mask" msgkey="errors.phone"
     * @struts.validator-var name="mask" value="${phone}"
     * @hibernate.property
     *  column="phoneNumber" not-null="false"
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Returns the postalCode.
     * @return String
     *
     * @struts.validator type="required"
     * @struts.validator type="mask" msgkey="errors.zip"
     * @struts.validator-var name="mask" value="${zip}"
     * @hibernate.property
     *  column="postalCode" not-null="true"
     */
    public String getPostalCode() {
        return postalCode;
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
     * Returns the website.
     * @return String
     *
     * @struts.validator type="required"
     * @hibernate.property
     *  column="website" not-null="false"
     */
    public String getWebsite() {
        return website;
    }

	/**
     * Returns the passwordHint.
     * @return String
     *
     * @struts.validator type="required"
     * @hibernate.property
     *  column="passwordHint" not-null="false"
     */
	public String getPasswordHint() {
		return passwordHint;
	}
	
    /**
     * Returns the user's roles.
     * @return List
     *
     * @hibernate.bag name="roles" inverse="true" lazy="false" cascade="delete"
     * @hibernate.collection-key column="user_id"
     * @hibernate.collection-one-to-many class="org.appfuse.model.UserRole"
     */
    public List getRoles() {
        return roles;
    }

    /**
     * Adds a role for the user
     * @param role
     */
    public void addRole(UserRole role) {
        role.setUserId(this.id);
        role.setUsername(this.username);
        if (roles == null) {
            roles = new ArrayList();
        }
        roles.add(role);
    }

    /**
     * Sets the id.
     * @param id The id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Sets the password.
     * @param password The password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

	/**
	 * Sets the confirmedPassword.
	 * @param confirmPassword The confirmed password to set
	 */
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
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
     * Sets the email.
     * @param email The email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Sets the firstName.
     * @param firstName The firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Sets the lastName.
     * @param lastName The lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Sets the phoneNumber.
     * @param phoneNumber The phoneNumber to set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
     * Sets the username.
     * @param username The username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sets the website.
     * @param website The website to set
     */
    public void setWebsite(String website) {
        this.website = website;
    }
    
    /**
     * @param passwordHint The password hint to set
     */
    public void setPasswordHint(String passwordHint) {
        this.passwordHint = passwordHint;
    }

    /**
     * Sets the roles.
     * @param roles The roles to set
     */
    public void setRoles(List roles) {
        this.roles = roles;
    }

}
