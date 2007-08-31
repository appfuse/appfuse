package org.appfuse.tool;

import junit.framework.TestCase;

public class StringUtilsTest extends TestCase {

    public void testPlurals() {
        StringUtils util = new StringUtils();

        assertEquals("Persons", util.getPluralForWord("Person"));
        assertEquals("persons", util.getPluralForWord("person"));
        assertEquals("holidays", util.getPluralForWord("holiday"));
        assertEquals("companies", util.getPluralForWord("company"));
        assertEquals("hobbies", util.getPluralForWord("hobby"));
        assertEquals("dishes", util.getPluralForWord("dish"));
        assertEquals("potatoes", util.getPluralForWord("potato"));
        //assertEquals("wolves", util.getPluralForWord("wolf"));
    }
}
