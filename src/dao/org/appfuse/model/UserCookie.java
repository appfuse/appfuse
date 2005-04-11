package org.appfuse.model;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
/**
 * This class is used to manage cookie-based authentication.
 *
 * <p>
 * <a href="UserCookie.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 *
 * @hibernate.class table="user_cookie"
 *
 */
public class UserCookie extends BaseObject {
	private static final long serialVersionUID = 4050479002315731765L;
	private Long id;
    private String username;
    private String cookieId;
    private Date dateCreated;

    public UserCookie() {
        this.dateCreated = new Date();
    }

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
     * Sets the id.
     * @param id The id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns the username.
     * @return String
     *
     * @hibernate.property
     * @hibernate.property
     * @hibernate.column name="username" not-null="true"
     *  length="30" index="user_cookie_username_cookie_id"
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     * @param username The username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the cookieId (a GUID).
     * @return String
     *
     * @hibernate.property
     * @hibernate.column name="cookie_id" not-null="true"
     *  length="100" index="user_cookie_username_cookie_id"
     */
    public String getCookieId() {
        return cookieId;
    }

    /**
     * Sets the cookieId.
     * @param rolename The cookieId to set
     */
    public void setCookieId(String rolename) {
        this.cookieId = rolename;
    }

    /**
     * @return Returns the dateCreated.
    * @hibernate.property column="date_created" not-null="true"
     */
    public Date getDateCreated() {
        return dateCreated;
    }

    /**
     * @param dateCreated The dateCreated to set.
     */
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    /**
     * Generated using Commonclipse (http://commonclipse.sf.net)
     */
    public boolean equals(Object object) {
        if (!(object instanceof UserCookie)) {
            return false;
        }

        UserCookie rhs = (UserCookie) object;

        return new EqualsBuilder().append(this.username, rhs.username)
                                  .append(this.dateCreated, rhs.dateCreated)
                                  .append(this.id, rhs.id)
                                  .append(this.cookieId, rhs.cookieId).isEquals();
    }

    /**
     * Generated using Commonclipse (http://commonclipse.sf.net)
     */
    public int hashCode() {
        return new HashCodeBuilder(1954972321, -113979947).append(this.username)
                                                          .append(this.dateCreated)
                                                          .append(this.id)
                                                          .append(this.cookieId)
                                                          .toHashCode();
    }
    
    /**
     * Generated using Commonclipse (http://commonclipse.sf.net)
     */
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", this.id).append("username", this.username)
                .append("cookieId", this.cookieId).append("dateCreated",
                        this.dateCreated).toString();
    }
}
