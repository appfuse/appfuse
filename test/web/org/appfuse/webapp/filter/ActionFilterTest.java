package org.appfuse.webapp.filter;

import org.apache.cactus.FilterTestCase;


public class ActionFilterTest extends FilterTestCase {
    //~ Instance fields ========================================================

    ActionFilter filter = null;

    //~ Constructors ===========================================================

    // JUnitDoclet end class
    public ActionFilterTest(String name) {
        super(name);
    }

    //~ Methods ================================================================

    protected void setUp() throws Exception {
        super.setUp();
        filter = new ActionFilter();

        // set initialization parameters
        config.setInitParameter("isSecure", "false");

        filter.init(config);
    }

    protected void tearDown() throws Exception {
        filter = null;
        super.tearDown();
    }

    public void testInit() throws Exception {
        assertTrue(config != null);
        assertEquals(config.getInitParameter("isSecure"), "false");
    }

    public void testDestroy() throws Exception {
        config = null;
        assertTrue(config == null);
    }

    public void testDoFilter() throws Exception {
        filter.doFilter(request, response, filterChain);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(ActionFilterTest.class);
    }
}
