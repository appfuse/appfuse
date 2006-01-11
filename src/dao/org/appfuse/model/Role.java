package org.appfuse.model;

import java.io.Serializable;
import java.util.Set;

import org.acegisecurity.GrantedAuthority;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * This class is used to represent available roles in the database.</p>
 *
 * <p><a href="Role.java.html"><i>View Source</i></a></p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 *  Version by Dan Kibler dan@getrolling.com
 *  Extended to implement Acegi GrantedAuthority interface 
 *  	by David Carter david@carter.net
 *
 * @struts.form extends="BaseForm"
 * @hibernate.class table="role"
 */
public class Role extends BaseObject implements Serializable, GrantedAuthority {
    private static final long serialVersionUID = 3690197650654049848L;
    private String name;
    private String description;
    private Integer version;
    private Set users;

    public Role() {}
    
    public Role(String name) {
        this.name = name;
    }
    
    /**
     * @struts.validator type="required"
     * @hibernate.id column="name" length="20"
     *   generator-class="assigned" unsaved-value="version"
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * @see org.acegisecurity.GrantedAuthority#getAuthority()
     */
    public String getAuthority() {
        return getName();
    }
    
    /**
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
     *                                    column="username"
     */
    public Set getUsers() {
        return users;
    }
    
    public void setUsers(Set users) {
        this.users = users;
    }

    /**
     * @hibernate.version
     */
    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;

        final Role role = (Role) o;

        if (name != null ? !name.equals(role.name) : role.name != null) return false;

        return true;
    }

    public int hashCode() {
        return (name != null ? name.hashCode() : 0);
    }

    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .append(this.name.toString())
                .toString();
    }

}
