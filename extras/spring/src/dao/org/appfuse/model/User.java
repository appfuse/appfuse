package org.appfuse.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
/**
 * User class
 *
 * This class is used to generate Spring Validation rules
 * as well as the Hibernate mapping file.
 *
 * <p>
 * <a href="User.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 *         Updated by Dan Kibler (dan@getrolling.com)
 *
 * @hibernate.class table="app_user"
 */
public class User extends BaseObject {

    protected String username;
    protected String password;
    protected String confirmPassword;
    protected String firstName;
    protected String lastName;
    protected Address address = new Address();
    protected String phoneNumber;
    protected String email;
    protected String website;
    protected String passwordHint;
    protected Date updated;
    protected Set roles = new HashSet();

    /**
     * Returns the username.
     * 
     * @return String
     * 
     * @hibernate.id column="username" length="20" generator-class="assigned"
     *               unsaved-value="timestamp"
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
     * @hibernate.property column="first_name" not-null="true" length="50"
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Returns the lastName.
     * @return String
     *
     * @hibernate.property column="last_name" not-null="true" length="50"
     */
    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return firstName + ' ' + lastName;
    }
    /**
     * Returns the address. 
     * 
     * @return Address
     *
     * @hibernate.component
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Returns the email.  This is an optional field for specifying a
     * different e-mail than the username.
     * @return String
     *
     * @hibernate.property column="email" not-null="false" unique="true"
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns the phoneNumber.
     * @return String
     *
     * @hibernate.property column="phone_number" not-null="false"
     */
    public String getPhoneNumber() {
        return phoneNumber;
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
     * @hibernate.property column="password_hint" not-null="false"
     */
    public String getPasswordHint() {
        return passwordHint;
    }

    /**
     * Returns the user's roles.
     * @return Set
     * 
     * @hibernate.set table="user_role" cascade="save-update" lazy="false"
     * @hibernate.collection-key column="username"
     * @hibernate.collection-many-to-many class="org.appfuse.model.Role"
     *                                    column="role_name"
     */
    public Set getRoles() {
        return roles;
    }

    /**
     * Adds a role for the user
     * 
     * @param rolename
     */

    public void addRole(Role role) {
        getRoles().add(role);
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
     * 
     * @spring.validator
     */
    public void setAddress(Address address) {
        this.address = address;
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
    public void setRoles(Set roles) {
        this.roles = roles;
    }

    /**
     * @return Returns the updated timestamp.
     * @hibernate.timestamp
     */
    public Date getUpdated() {
        return updated;
    }

    /**
     * @param updated
     *            The updated timestamp to set.
     */
    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    /**
     * Convert user roles to LabelValue objects for
     * convenience.  Then UserAction doesn't have to manually
     * convert them and validation doesn't puke.
     */
    public List getRoleList() {
        List userRoles = new ArrayList();

        if (this.roles != null) {
            for (Iterator it = roles.iterator(); it.hasNext();) {
                UserRole role = (UserRole) it.next();

                // convert the user's roles to LabelValue Objects
                userRoles.add(new LabelValue(role.getRoleName(),
                                             role.getRoleName()));
            }
        }

        return userRoles;
    }

    /**
     * Generated using Commonclipse (http://commonclipse.sf.net)
     */
    public boolean equals(Object object) {
        if (!(object instanceof User)) {
            return false;
        }

        User rhs = (User) object;

        return new EqualsBuilder().append(this.password, rhs.password).append(
                this.passwordHint, rhs.passwordHint).append(this.address,
                rhs.address).append(this.confirmPassword, rhs.confirmPassword)
                .append(this.username, rhs.username).append(this.email,
                        rhs.email).append(this.phoneNumber, rhs.phoneNumber)
                .append(this.roles, rhs.roles)
                .append(this.website, rhs.website).append(this.firstName,
                        rhs.firstName).append(this.lastName, rhs.lastName)
                .isEquals();
    }

    /**
     * Generated using Commonclipse (http://commonclipse.sf.net)
     */
    public int hashCode() {
        return new HashCodeBuilder(-2022315247, 1437659757).append(
                this.password).append(this.passwordHint).append(this.address)
                .append(this.confirmPassword).append(this.username).append(
                        this.email).append(this.phoneNumber).append(this.roles)
                .append(this.website).append(this.firstName).append(
                        this.lastName).toHashCode();
    }

    /**
     * Generated using Commonclipse (http://commonclipse.sf.net)
     */
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("roles", this.roles)
                .append("firstName", this.firstName).append("lastName",
                        this.lastName)
                .append("passwordHint", this.passwordHint).append("username",
                        this.username).append("fullName", this.getFullName())
                .append("email", this.email).append("phoneNumber",
                        this.phoneNumber).append("password", this.password)
                .append("address", this.address).append("confirmPassword",
                        this.confirmPassword).append("website", this.website)
                .append("updated", this.getUpdated()).toString();
    }
}
