<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<#assign identifierType = pojo.getJavaTypeName(pojo.identifierProperty, jdk5)>
package ${basepackage}.webapp.pages;

<#if genericcore>
import ${appfusepackage}.service.GenericManager;
    <#assign managerClass = 'GenericManager'>
<#else>
import ${basepackage}.service.${pojo.shortName}Manager;
    <#assign managerClass = pojo.shortName + 'Manager'>
</#if>
import ${pojo.packageName}.${pojo.shortName};

import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.dom.Node;

import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Ignore;

import java.util.List;
import java.util.ResourceBundle;

public class ${pojo.shortName}ListTest extends BasePageTestCase {

    @Autowired
    private ${managerClass} ${pojoNameLower}Manager;

    @Test
    public void testList() {
        doc = tester.renderPage("${pojoNameLower}List");
        assertNotNull(doc.getElementById("${pojoNameLower}List"));
        assertTrue(doc.getElementById("${pojoNameLower}List").find("tbody").getChildren().size() >= 2);
    }

    @Test
    public void testEdit() {
        doc = tester.renderPage("${pojoNameLower}List");

        Element table = doc.getElementById("${pojoNameLower}List");
        List<Node> rows = table.find("tbody").getChildren();
        String id = ((Element) rows.get(0)).find("td/a").getChildMarkup().trim();
        Element editLink = table.getElementById("${pojoNameLower}-" + id);
        doc = tester.clickLink(editLink);

        ResourceBundle rb = ResourceBundle.getBundle(MESSAGES);

        assertTrue(doc.toString().contains("<title>" +
                rb.getString("${pojoNameLower}Detail.title")));
    }

    @Test
    @Ignore // test doesn't work with AppFuse Light
    public void testSearch() {
        // regenerate indexes
        ${pojoNameLower}Manager.reindex();

        doc = tester.renderPage("${pojoNameLower}List");

        Element form = doc.getElementById("searchForm");
        assertNotNull(form);

        fieldValues.put("q", "*");
        doc = tester.submitForm(form, fieldValues);
        assertTrue("At least 3 results found",
            doc.getElementById("${pojoNameLower}List").find("tbody").getChildren().size() >= 3);
    }
}
