package org.appfuse.dao.jpa.mock;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

/**
 * @author <a href="mailto:bwnoll@gmail.com">Bryan Noll</a>
 */
public class MockRole {
    private static final long serialVersionUID = 3690197650654049848L;
    
    @Id  @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;

    public MockRole() {
    }

    public MockRole(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    /**
     * @see org.springframework.security.core.GrantedAuthority#getAuthority()
     */
    @Transient
    public String getAuthority() {
        return getName();
    }

    @Column(length=20)
    public String getName() {
        return this.name;
    }

    @Column(length=64)
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
        if (!(o instanceof MockRole)) return false;

        final MockRole role = (MockRole) o;

        return !(name != null ? !name.equals(role.name) : role.name != null);

    }

    public int hashCode() {
        return (name != null ? name.hashCode() : 0);
    }

    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .append(this.name)
                .toString();
    }

}
