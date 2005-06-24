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
 * @hibernate.class table="user_cookie"
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

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserCookie)) return false;

        final UserCookie userCookie = (UserCookie) o;

        if (cookieId != null ? !cookieId.equals(userCookie.cookieId) : userCookie.cookieId != null) return false;
        if (dateCreated != null ? !dateCreated.equals(userCookie.dateCreated) : userCookie.dateCreated != null) return false;
        if (username != null ? !username.equals(userCookie.username) : userCookie.username != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (username != null ? username.hashCode() : 0);
        result = 29 * result + (cookieId != null ? cookieId.hashCode() : 0);
        result = 29 * result + (dateCreated != null ? dateCreated.hashCode() : 0);
        return result;
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
