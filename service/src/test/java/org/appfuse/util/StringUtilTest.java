package org.appfuse.util;

import junit.framework.TestCase;


public class StringUtilTest extends TestCase {
    public StringUtilTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testEncodePassword() throws Exception {
        String password = "tomcat";
        String encrypted = "536c0b339345616c1b33caf454454d8b8a190d6c";
        assertEquals(StringUtil.encodePassword(password, "SHA"), encrypted);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(StringUtilTest.class);
    }
}
