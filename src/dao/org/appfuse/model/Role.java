package org.appfuse.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * This class is used to represent available roles in the database.</p>
 *
 * <p><a href="Role.java.html"><i>View Source</i></a></p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 *  Updated by Dan Kibler dan@getrolling.com
 *
 * @struts.form extends="BaseForm"
 * @hibernate.class table="role"
 */
public class Role extends BaseObject implements Serializable {
    //~ Instance fields ========================================================

    private String name;
    private String description;
    protected Date updated;
    private Set users;

    public Role() {}
    
    public Role(String name) {
        this.name = name;
    }
    
    //~ Methods ================================================================

    /**
     * Returns the name.
     * @return String
     *
     * @struts.validator type="required"
     * @hibernate.id column="name" length="20"
     *   generator-class="assigned" unsaved-value="timestamp"
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the description.
     * @return String
     *
     * @struts.validator type="required"
     * @hibernate.property column="description"
     */
    public String getDescription() {
        return this.description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return Returns the users.
     * This inverse relation causes exceptions :-( drk
     * hibernate.set table="user_role" cascade="save-update"
     *                lazy="false" inverse="true"
     * hibernate.collection-key column="role_name"
     * hibernate.collection-many-to-many class="org.appfuse.model.User"
     *                                   column="username"
     */
    public Set getUsers() {
        return users;
    }
    
    /**
     * @param users The users to set.
     */
    public void setUsers(Set users) {
        this.users = users;
    }

    /**
     * @return Returns the updated timestamp.
     * @hibernate.timestamp
     */
    public Date getUpdated() {
        return updated;
    }
    /**
     * @param updated The updated timestamp to set.
     */
    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    /**
     * Generated using Commonclipse (http://commonclipse.sf.net)
     */
    public boolean equals(Object object) {
        if (!(object instanceof Role)) {
            return false;
        }
        Role rhs = (Role) object;
        return new EqualsBuilder().append(this.description, rhs.description)
                .append(this.name, rhs.name).isEquals();
    }

    /**
     * Generated using Commonclipse (http://commonclipse.sf.net)
     */
    public int hashCode() {
        return new HashCodeBuilder(1156335803, 987569255).append(
                this.description).append(this.name).toHashCode();
    }
    
    /**
     * Generated using Commonclipse (http://commonclipse.sf.net)
     */
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("name", this.name).append("description",
                        this.description).toString();
    }

}
