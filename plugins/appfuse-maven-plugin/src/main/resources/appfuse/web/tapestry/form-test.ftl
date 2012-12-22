<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<#assign getIdMethodName = pojo.getGetterSignature(pojo.identifierProperty)>
<#assign setIdMethodName = 'set' + pojo.getPropertyName(pojo.identifierProperty)>
<#assign identifierType = pojo.getJavaTypeName(pojo.identifierProperty, jdk5)>
package ${basepackage}.webapp.pages;

import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.dom.Node;
import static org.junit.Assert.*;
import org.junit.Test;

import java.util.List;
import java.util.ResourceBundle;

public class ${pojo.shortName}FormTest extends BasePageTestCase {

    @Test
    public void testCancel() throws Exception {
        doc = tester.renderPage("${pojoNameLower}List");
        Element table = doc.getElementById("${pojoNameLower}List");
        List<Node> rows = table.find("tbody").getChildren();
        String ${pojo.identifierProperty.name} = ((Element) rows.get(0)).find("td/a").getChildMarkup().trim();

        Element editLink = table.getElementById("${pojoNameLower}-" + ${pojo.identifierProperty.name});
        doc = tester.clickLink(editLink);

        Element cancelButton = doc.getElementById("cancel");
        doc = tester.clickLink(cancelButton);

        ResourceBundle rb = ResourceBundle.getBundle(MESSAGES);

        assertTrue(doc.toString().contains(rb.getString("${pojoNameLower}List.title")));
    }

    @Test
    public void testSave() throws Exception {
        doc = tester.renderPage("${pojoNameLower}Form");

        Element form = doc.getElementById("${pojoNameLower}Form");
        assertNotNull(form);

        // enter all required fields
<#foreach field in pojo.getAllPropertiesIterator()>
    <#foreach column in field.getColumnIterator()>
        <#if !field.equals(pojo.identifierProperty) && !column.nullable && !c2h.isCollection(field) && !c2h.isManyToOne(field) && !c2j.isComponent(field)>
            <#lt/>        fieldValues.put("${field.name}", <#rt/>
            <#if field.value.typeName == "java.lang.String" && column.isUnique()><#lt/>${data.generateRandomStringValue(column)}<#else>"${data.getValueForWebTest(column)}"</#if><#lt/>);
        </#if>
    </#foreach>
</#foreach>

        doc = tester.submitForm(form, fieldValues);

        Element errors = doc.getElementById("errorMessages");

        if (errors != null) {
            System.out.println(errors);
        }

        assertNull(doc.getElementById("errorMessages"));

        assertTrue(doc.toString().contains("added successfully"));
        Element table = doc.getElementById("${pojoNameLower}List");
    }

    @Test
    public void testRemove() throws Exception {
        doc = tester.renderPage("${pojoNameLower}List");
        Element table = doc.getElementById("${pojoNameLower}List");
        List<Node> rows = table.find("tbody").getChildren();
        String ${pojo.identifierProperty.name} = ((Element) rows.get(1)).find("td/a").getChildMarkup().trim();

        Element editLink = table.getElementById("${pojoNameLower}-" + ${pojo.identifierProperty.name});
        doc = tester.clickLink(editLink);

        Element deleteButton = doc.getElementById("delete");
        doc = tester.clickLink(deleteButton);
        assertTrue(doc.toString().contains("deleted successfully"));
    }
}