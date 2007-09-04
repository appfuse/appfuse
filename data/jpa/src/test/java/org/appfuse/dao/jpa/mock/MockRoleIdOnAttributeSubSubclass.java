package org.appfuse.dao.jpa.mock;

import javax.persistence.Column;

/**
 * @author <a href="mailto:bwnoll@gmail.com">Bryan Noll</a>
 */
public class MockRoleIdOnAttributeSubSubclass extends MockRoleIdOnAttributeSubclass {
    private static final long serialVersionUID = 3690197650654049848L;
    
    @Column(length=128)
    private String longLongDescription;

    public MockRoleIdOnAttributeSubSubclass() {
    }

    public String getLongLongDescription() {
        return longLongDescription;
    }

    public void setLongLongDescription(String longLongDescription) {
        this.longLongDescription = longLongDescription;
    }

}
