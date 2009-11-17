package org.appfuse.webapp.pages;

import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.dom.Node;
import static org.junit.Assert.*;
import org.junit.Test;
import org.subethamail.wiser.Wiser;

import java.util.List;
import java.util.ResourceBundle;

public class UserEditTest extends BasePageTestCase {

    @Test
    public void testCancel() throws Exception {
        doc = tester.renderPage("admin/UserList");
        Element table = doc.getElementById("userList");
        List<Node> rows = table.find("tbody").getChildren();
        String username = "";

        username = ((Element) rows.get(0)).find("td/a").getChildMarkup().trim();

        Element idLink = table.getElementById("user-" + username);
        doc = tester.clickLink(idLink);

        Element cancelButton = doc.getElementById("cancel");
        doc = tester.clickLink(cancelButton);

        ResourceBundle rb = ResourceBundle.getBundle(MESSAGES);

        assertTrue(doc.toString().contains(rb.getString("userList.title")));
    }

    @Test
    public void testSave() throws Exception {
        doc = tester.renderPage("userEdit");

        Element form = doc.getElementById("form");
        assertNotNull(form);

        fieldValues.put("username", "tapestry");
        fieldValues.put("password", "isfun");
        fieldValues.put("confirmPassword", "isfun");
        fieldValues.put("passwordHint", "funstuff");
        fieldValues.put("firstName", "Tapestry");
        fieldValues.put("lastName", "5");
        fieldValues.put("email", "tapestry@appfuse.org");
        fieldValues.put("website", "http://tapestry.apache.org");
        fieldValues.put("city", "Portland");
        fieldValues.put("state", "OR");
        fieldValues.put("postalCode", "97303");
        fieldValues.put("country", "US");

        // start SMTP Server
        Wiser wiser = new Wiser();
        wiser.setPort(getSmtpPort());
        wiser.start();
        
        doc = tester.submitForm(form, fieldValues);

        Element errors = doc.getElementById("errorMessages");

        if (errors != null) {
            System.out.println(errors);
        }

        assertNull(doc.getElementById("errorMessages"));

        // verify an account information e-mail was sent
        assertEquals(1, wiser.getMessages().size());
        wiser.stop();

        Element successMessages = doc.getElementById("successMessages");
        assertNotNull(successMessages);
        assertTrue(successMessages.toString().contains("added successfully"));
        Element table = doc.getElementById("userList");
        assertTrue(table.toString().contains("tapestry"));
    }

    @Test
    public void testRemove() throws Exception {
        doc = tester.renderPage("admin/UserList");
        Element table = doc.getElementById("userList");
        List<Node> rows = table.find("tbody").getChildren();
        String username = "";

        username = ((Element) rows.get(1)).find("td/a").getChildMarkup().trim();

        Element idLink = table.getElementById("user-" + username);
        doc = tester.clickLink(idLink);

        Element deleteButton = doc.getElementById("delete");
        doc = tester.clickSubmit(deleteButton, fieldValues);
        assertTrue(doc.toString().contains("deleted successfully"));
    }
}
