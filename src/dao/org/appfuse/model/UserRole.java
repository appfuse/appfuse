package org.appfuse.model;


/**
 * Role class used to determine roles for a user
 *
 * <p>
 * <a href="UserRole.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @version $Revision: 1.3 $ $Date: 2004/05/16 02:16:45 $
 *
 * @struts.form include-all="true" extends="BaseForm"
 * @hibernate.class table="user_role"
 *
 */
public class UserRole extends BaseObject {
    //~ Instance fields ========================================================

    // XDoclet's Hibernate module does not generate composite keys 
    // yet, so we have to add an id to this table - it's never used.
    private Long id;

    /**
     * userId is used to create the mapping from user to user_role.
     * Using username should work, but it doesn't seem to
     * (with hibernate), and adding a link to the primary key does
     * work.
     */
    private Long userId;
    private String username;
    private String roleName;

    //~ Constructors ===========================================================

    public UserRole() {
    }

    public UserRole(String roleName) {
        this.roleName = roleName;
    }

    //~ Methods ================================================================

    /**
     * @return a users Id
     * @hibernate.property column="user_id" not-null="true"
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(Long userId) {
        this.userId = userId;
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
     * @struts.validator type="required"
     * @struts.validator type="email" 
     * @hibernate.property column="username" not-null="true"
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
     * Returns the rolename.
     * @return String
     *
     * @struts.validator type="required"
     * @hibernate.property column="role_name" not-null="true"
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * Sets the rolename.
     * @param rolename The rolename to set
     */
    public void setRoleName(String rolename) {
        this.roleName = rolename;
    }
}
