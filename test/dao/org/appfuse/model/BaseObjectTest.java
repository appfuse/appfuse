package org.appfuse.model;

import junit.framework.TestCase;

public class BaseObjectTest extends TestCase {
    
    BaseObject baseobject = null;

    //~ Methods ================================================================

    protected void setUp() throws Exception {
        baseobject = new BaseObject();
    }

    protected void tearDown() throws Exception {
        baseobject = null;
    }

    public void testToString() throws Exception {
        // JUnitDoclet begin method toString
        // JUnitDoclet end method toString
    }

    public void testEquals() throws Exception {
        // JUnitDoclet begin method equals
        // JUnitDoclet end method equals
    }

    public void testHashCode() throws Exception {
        // JUnitDoclet begin method hashCode
        // JUnitDoclet end method hashCode
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(BaseObjectTest.class);
    }
}
