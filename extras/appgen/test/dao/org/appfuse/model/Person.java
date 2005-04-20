package org.appfuse.model;

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

    public boolean equals(Object object) {
        if (!(object instanceof Person)) {
            return false;
        }
        Person rhs = (Person) object;
        return new EqualsBuilder().append(this.firstName, rhs.firstName)
                .append(this.personId, rhs.personId)
                .append(this.lastName, rhs.lastName)
                .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder(1923026325, -1034774675).append(
                this.firstName).append(this.personId).append(this.lastName)
                .toHashCode();
    }

    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("lastName", this.lastName).append("personId", this.personId)
                .append("firstName", this.firstName).toString();
    }
}
