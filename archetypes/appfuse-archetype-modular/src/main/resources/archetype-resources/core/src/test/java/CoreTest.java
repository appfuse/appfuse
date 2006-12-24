// There is no package since the Maven Archetype plugin doesn't support package expansion
// for multi-module archetypes: http://jira.codehaus.org/browse/ARCHETYPE-23.

import junit.framework.TestCase;

public class CoreTest extends TestCase {
    public void testGetHello() throws Exception {
        assertEquals("Hello", Core.getHello());
    }
}
