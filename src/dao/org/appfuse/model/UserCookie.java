package org.appfuse.model;

import java.util.Date;


/**
 * This class is used to manage cookie-based authentication.
 *
 * <p>
 * <a href="UserCookie.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @version $Revision: 1.3 $ $Date: 2004/05/16 02:16:44 $
 *
 * @hibernate.class table="user_cookie"
 *
 */
public class UserCookie extends BaseObject {
    //~ Instance fields ========================================================

    private Long id;
    private String username;
    private String cookieId;
    private Date dateCreated;

    //~ Methods ================================================================

    public UserCookie(){
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
     * @hibernate.property column="username" not-null="true" length="30"
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
     * @hibernate.property column="cookie_id" not-null="true" length="100"
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

}
