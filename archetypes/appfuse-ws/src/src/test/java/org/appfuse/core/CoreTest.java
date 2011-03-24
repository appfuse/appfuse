package org.appfuse.core;

import junit.framework.TestCase;

public class CoreTest extends TestCase {
    public void testGetHello() throws Exception {
        assertEquals("Hello", Core.getHello());
    }
}
