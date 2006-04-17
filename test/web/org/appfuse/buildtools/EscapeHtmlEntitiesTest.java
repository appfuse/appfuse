package org.appfuse.buildtools;

import java.io.StringReader;

import org.apache.tools.ant.util.FileUtils;

import junit.framework.TestCase;

/**
 * Testcase to verify EscapeHtmlEntities filter.
 * @author <a href="mailto:mikagoeckel@codehaus.org">Mika Göckel</a>
 */
public class EscapeHtmlEntitiesTest extends TestCase {

    /**
     * Test Unicode->Entity escaping.
     * @throws Exception
     */
    public void testEscape() throws Exception {
        StringReader str = new StringReader("äüöß-Öó");
        EscapeHtmlEntities boot = new EscapeHtmlEntities();
        EscapeHtmlEntities filter = (EscapeHtmlEntities) boot.chain(str);
        filter.setMode(EscapeHtmlEntities.ESCAPE);
        String result = FileUtils.readFully(filter, 200);
        assertEquals("&auml;&uuml;&ouml;&szlig;-&Ouml;&oacute;",result);
    }
    
    /**
     * Test Entity->Unicode unescaping.
     * @throws Exception
     */
    public void testUnescape() throws Exception {
        StringReader str = new StringReader("&auml;&uuml;&ouml;&szlig;-&Ouml;&oacute;&noentity;");
        EscapeHtmlEntities boot = new EscapeHtmlEntities();
        EscapeHtmlEntities filter = (EscapeHtmlEntities) boot.chain(str);
        filter.setMode(EscapeHtmlEntities.UNESCAPE);
        String result = FileUtils.readFully(filter, 200);
        assertEquals("äüöß-Öó&noentity;",result);
    }
}
