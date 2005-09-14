package org.appfuse.model;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * @hibernate.class table="person"
 * @struts.form include-all="true" extends="BaseForm"
 */
public class Person extends BaseObject {
    private Long personId;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;

    /**
     * @return Returns the id.
     * @hibernate.id column="person_id"
     *  generator-class="increment" unsaved-value="null"
     */
    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    /**
     * @struts.validator type="required"
     * @hibernate.property column="first_name" length="50" not-null="true"
     */
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @struts.validator type="required"
     * @hibernate.property column="last_name" length="50" not-null="true"
     */
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    /**
     * @struts.validator type="required"
     * @hibernate.property column="date_of_birth" length="20" not-null="true"
     */
    public Date getDateOfBirth() {
        return dateOfBirth;
    }
    
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    
    public boolean equals(Object object) {
        if (!(object instanceof Person)) {
            return false;
        }
        Person rhs = (Person) object;
        return new EqualsBuilder().append(this.firstName, rhs.firstName)
                .append(this.lastName, rhs.lastName)
                .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder(1923026325, -1034774675)
                .append(this.firstName).append(this.lastName)
                .toHashCode();
    }

    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("personId", this.personId)
                .append("lastName", this.lastName)
                .append("firstName", this.firstName).toString();
    }
}
