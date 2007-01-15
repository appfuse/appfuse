package org.appfuse.dao.jpa.mock;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author <a href="mailto:bwnoll@gmail.com">Bryan Noll</a>
 */
public class MockRoleIdOnAttributeSubclass extends MockRole {
    private static final long serialVersionUID = 3690197650654049848L;
    
    @Column(length=128)
    private String longDescription;

    public MockRoleIdOnAttributeSubclass() {
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

}
