<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
package ${basepackage}.webapp;

import net.sourceforge.jwebunit.html.Row;
import net.sourceforge.jwebunit.html.Table;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ResourceBundle;

import static net.sourceforge.jwebunit.junit.JWebUnit.*;

public class ${pojo.shortName}WebTest {

    private ResourceBundle messages;

    @Before
    public void setUp() {
        setScriptingEnabled(false);
        getTestContext().setBaseUrl("http://" + System.getProperty("cargo.host") + ":" + System.getProperty("cargo.port"));
        getTestContext().setResourceBundleName("messages");
        messages = ResourceBundle.getBundle("messages");
    }

    @Before
    public void add${pojo.shortName}() {
        beginAt("/${pojoNameLower}form");
        assertTitleKeyMatches("${pojoNameLower}Detail.title");
<#foreach field in pojo.getAllPropertiesIterator()>
    <#foreach column in field.getColumnIterator()>
        <#if !field.equals(pojo.identifierProperty) && !column.nullable && !c2h.isCollection(field) && !c2h.isManyToOne(field) && !c2j.isComponent(field)>
            <#lt/>        setTextField("${field.name}", "${data.getValueForWebTest(column)}");
        </#if>
    </#foreach>
</#foreach>
        clickButton("save");
        assertTitleKeyMatches("${pojoNameLower}List.title");
        assertKeyPresent("${pojoNameLower}.added");
    }

    @Test
    public void list${util.getPluralForWord(pojo.shortName)}() {
        beginAt("/${util.getPluralForWord(pojoNameLower)}");
        assertTitleKeyMatches("${pojoNameLower}List.title");

        // check that table is present
        assertTablePresent("${pojoNameLower}List");
    }

    @Test
    public void edit${pojo.shortName}() {
        beginAt("/${pojoNameLower}form?id=" + getInsertedId());
        clickButton("save");
        assertTitleKeyMatches("${pojoNameLower}Detail.title");
    }

    @Test
    public void save${pojo.shortName}() {
        beginAt("/${pojoNameLower}form?id=" + getInsertedId());
        assertTitleKeyMatches("${pojoNameLower}Detail.title");

        // update some of the required fields
        <#foreach field in pojo.getAllPropertiesIterator()>
            <#foreach column in field.getColumnIterator()>
                <#if !field.equals(pojo.identifierProperty) && !column.nullable && !c2h.isCollection(field) && !c2h.isManyToOne(field) && !c2j.isComponent(field)>
                    <#lt/>        setTextField("${field.name}", "${data.getValueForWebTest(column)}");
                </#if>
            </#foreach>
        </#foreach>
        clickButton("save");
        assertTitleKeyMatches("${pojoNameLower}Detail.title");
        assertKeyPresent("${pojoNameLower}.updated");
    }

    @After
    public void remove${pojo.shortName}() {
        beginAt("/${pojoNameLower}form?id=" + getInsertedId());
        clickButton("delete");
        assertTitleKeyMatches("${pojoNameLower}List.title");
        assertKeyPresent("${pojoNameLower}.deleted");
    }

    /**
     * Convenience method to get the id of the inserted record
     *
     * @return last id in the table
     */
    protected String getInsertedId() {
        beginAt("/${util.getPluralForWord(pojoNameLower)}");
        assertTablePresent("${pojoNameLower}List");
        Table table = getTable("${pojoNameLower}List");
        // Find link in last row, skip header row
        for (int i = 1; i < table.getRows().size(); i++) {
            Row row = table.getRows().get(i);
            if (i == table.getRowCount() - 1) {
                return row.getCells().get(0).getValue();
            }
        }
        return "";
    }

    private void assertTitleKeyMatches(String title) {
        assertTitleEquals(messages.getString(title) + " | " + messages.getString("webapp.name"));
    }
}
