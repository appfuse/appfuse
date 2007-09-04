package org.appfuse.dao.jpa.mock;

import javax.persistence.Column;

import org.appfuse.model.Role;

/**
 * @author <a href="mailto:bwnoll@gmail.com">Bryan Noll</a>
 */
public class MockRoleIdOnGetterSubclass extends Role {
    private static final long serialVersionUID = 3690197650654049848L;
    
    private String longDescription;

    public MockRoleIdOnGetterSubclass() {
    }

    @Column(length=128)
    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

}
