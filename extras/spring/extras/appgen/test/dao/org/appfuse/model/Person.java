package org.appfuse.model;

import java.util.Date;

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
    private Date birthDate;

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
     * @hibernate.property column="first_name" length="50" not-null="true"
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @spring.validator type="required"
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @hibernate.property column="last_name" length="50" not-null="true"
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @spring.validator type="required"
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @hibernate.property column="birth_date" length="20"
     */
    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;

        final Person person = (Person) o;

        if (firstName != null ? !firstName.equals(person.firstName) : person.firstName != null) return false;
        if (lastName != null ? !lastName.equals(person.lastName) : person.lastName != null) return false;
        if (birthDate != null ? !birthDate.equals(person.birthDate) : person.birthDate != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (firstName != null ? firstName.hashCode() : 0);
        result = 29 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 29 * result + (birthDate != null ? birthDate.hashCode() : 0);
        return result;
    }

    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("lastName", this.lastName).append("personId", this.personId)
                .append("firstName", this.firstName).append("birthDate", this.birthDate).toString();
    }
}
