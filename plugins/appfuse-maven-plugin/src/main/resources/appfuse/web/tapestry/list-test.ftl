<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
package ${basepackage}.webapp.pages;

import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.dom.Node;
import static org.junit.Assert.*;
import org.junit.Test;

import java.util.List;
import java.util.ResourceBundle;

public class ${pojo.shortName}ListTest extends BasePageTestCase {

    @Test
    public void testList() {
        doc = tester.renderPage("${pojoNameLower}List");
        assertNotNull(doc.getElementById("${pojoNameLower}List"));
        assertTrue(doc.getElementById("${pojoNameLower}List").find("tbody/tr").getChildren().size() >= 2);
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
}
