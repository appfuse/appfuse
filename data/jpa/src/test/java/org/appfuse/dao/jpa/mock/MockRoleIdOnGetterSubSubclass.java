package org.appfuse.dao.jpa.mock;

import javax.persistence.Column;

/**
 * @author <a href="mailto:bwnoll@gmail.com">Bryan Noll</a>
 */
public class MockRoleIdOnGetterSubSubclass extends MockRoleIdOnGetterSubclass {
    private static final long serialVersionUID = 3690197650654049848L;
    
    private String longLongDescription;

    public MockRoleIdOnGetterSubSubclass() {
    }

    @Column(length=128)
    public String getLongLongDescription() {
        return longLongDescription;
    }

    public void setLongLongDescription(String longLongDescription) {
        this.longLongDescription = longLongDescription;
    }

}
