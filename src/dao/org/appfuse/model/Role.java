package org.appfuse.model;

import java.io.Serializable;


/**
 * This class is used to represent available roles in the database.</p>
 *
 * <p><a href="Role.java.html"><i>View Source</i></a></p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @version $Revision: 1.3 $ $Date: 2004/05/16 02:16:44 $
 *
 * @struts.form include-all="true" extends="BaseForm"
 * @hibernate.class table="role"
 */
public class Role extends BaseObject implements Serializable {
    //~ Instance fields ========================================================

    private String name;
    private String description;

    //~ Methods ================================================================

    /**
     * Returns the name.
     * @return String
     *
     * @struts.validator type="required"
     * @hibernate.id column="name" generator-class="assigned"
     *  unsaved-value="null"
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the userId.
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
}
