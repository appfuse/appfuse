package org.appfuse.buildtools;

import java.io.StringReader;

import junit.framework.TestCase;

import org.apache.tools.ant.util.FileUtils;

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
        StringReader str = new StringReader("\u00E4\u00FC\u00F6\u00DF-\u00D6\u00F3");
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
        assertEquals("\u00E4\u00FC\u00F6\u00DF-\u00D6\u00F3&noentity;",result);
    }
}
