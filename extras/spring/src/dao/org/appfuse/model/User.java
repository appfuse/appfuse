package org.appfuse.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;


/**
 * User class
 *
 * This class is used to generate Spring Validation rules
 * as well as the Hibernate persistence later.
 *
 * <p>
 * <a href="User.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @version $Revision: 1.3 $ $Date: 2004/05/16 02:15:00 $
 *
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
    protected List roles = new ArrayList();

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
     * @hibernate.property column="username" not-null="true" unique="true"
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the password.
     * @return String
     *
     * @hibernate.property column="password" not-null="true"
     */
    public String getPassword() {
        return password;
    }

	/**
	 * Returns the confirmedPassword.
	 * @return String
	 */
	public String getConfirmPassword() {
		return confirmPassword;
	}
	
    /**
     * Returns the firstName.
     * @return String
     *
     * @hibernate.property column="firstName" not-null="true"
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Returns the lastName.
     * @return String
     *
     * @hibernate.property column="lastName" not-null="true"
     */
    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
    	return firstName + ' ' + lastName;
    }
    /**
     * Returns the address.
     * @return String
     *
     * @hibernate.property column="address" not-null="false"
     */
    public String getAddress() {
        return address;
    }

    /**
     * Returns the city.
     * @return String
     *
     * @hibernate.property column="city" not-null="true"
     */
    public String getCity() {
        return city;
    }

    /**
     * Returns the country.
     * @return String
     *
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
     * @hibernate.property column="email" not-null="false"
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns the phoneNumber.
     * @return String
     *
     * @hibernate.property column="phoneNumber" not-null="false"
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Returns the postalCode.
     * @return String
     *
     * @hibernate.property column="postalCode" not-null="true"
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Returns the province.
     * @return String
     *
     * @hibernate.property column="province" length="100"
     */
    public String getProvince() {
        return province;
    }

    /**
     * Returns the website.
     * @return String
     *
     * @hibernate.property column="website" not-null="false"
     */
    public String getWebsite() {
        return website;
    }

	/**
     * Returns the passwordHint.
     * @return String
     *
     * @hibernate.property column="passwordHint" not-null="false"
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
     * @param rolename
     */
    public void addRole(String rolename) {
        boolean exists = false;
        if (roles == null) {
            roles = new ArrayList();
        } else {
            // loop through and make sure the rolename doesn't already exist
            for (Iterator it = roles.iterator(); it.hasNext();) {
            	UserRole r = (UserRole) it.next();
                if (StringUtils.equals(r.getRoleName(), rolename)) {
                	exists = true;
                    break;
                }
            }
        }
        if (!exists) {
            UserRole role = new UserRole();
            role.setRoleName(rolename);
            role.setUserId(this.id);
            role.setUsername(this.username);
        	roles.add(role);
        }
    }

    /**
     * Sets the id.
     * @param id The id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Sets the username.
     * @param username The username to set
     * @spring.validator type="required"
     */
    public void setUsername(String username) {
        this.username = username;
    }
    
    /**
     * Sets the password.
     * @param password The password to set
     * 
     * @spring.validator type="required"
     * @spring.validator type="twofields" msgkey="errors.twofields"
     * @spring.validator-args arg1resource="user.password"
     * @spring.validator-args arg1resource="user.confirmPassword"
     * @spring.validator-var name="secondProperty" value="confirmPassword"
     */
    public void setPassword(String password) {
        this.password = password;
    }

	/**
	 * Sets the confirmedPassword.
	 * @param confirmPassword The confirmed password to set
     * 
     * @spring.validator type="required"
	 */
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	

    /**
     * Sets the firstName.
     * @spring.validator type="required"
     * @param firstName The firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Sets the lastName.
     * @param lastName The lastName to set
     * 
     * @spring.validator type="required"
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
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
     * 
     * @spring.validator type="required"
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Sets the province.
     * @param province The province to set
     * 
     * @spring.validator type="required"
     */
    public void setProvince(String province) {
        this.province = province;
    }
    
    /**
     * Sets the country.
     * @param country The country to set
     * 
     * @spring.validator type="required"
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Sets the postalCode.
     * @param postalCode The postalCode to set
     * 
     * @spring.validator type="required"
     * @spring.validator type="mask" msgkey="errors.zip"
     * @spring.validator-var name="mask" value="${zip}"
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * Sets the email.
     * @param email The email to set
     * 
     * @spring.validator type="required"
     * @spring.validator type="email"
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Sets the phoneNumber.
     * @param phoneNumber The phoneNumber to set
     * 
     * @spring.validator type="mask" msgkey="errors.phone"
     * @spring.validator-var name="mask" value="${phone}"
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
     * 
     * @spring.validator type="required"
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
    
    public void setUserRoles(String[] userRoles) {
        for (int i = 0; i < userRoles.length; i++) {
        	addRole(userRoles[i]);
        }
    }
}
