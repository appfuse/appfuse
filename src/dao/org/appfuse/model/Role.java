package org.appfuse.model;

import java.io.Serializable;

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
    private Long id;
    private String name;
    private String description;

    public Role() {}
    
    public Role(String name) {
        this.name = name;
    }
    
    /**
     * @hibernate.id column="id" generator-class="native" unsaved-value="null"
     */
    public Long getId() {
        return id;
    }

    /**
     * @see org.acegisecurity.GrantedAuthority#getAuthority()
     */
    public String getAuthority() {
        return getName();
    }
    
    /**
     * @hibernate.property column="name" length="20"
     */
    public String getName() {
        return this.name;
    }

    /**
     * @hibernate.property column="description" length="64"
     */
    public String getDescription() {
        return this.description;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
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
