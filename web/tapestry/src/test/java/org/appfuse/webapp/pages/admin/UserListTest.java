package org.appfuse.webapp.pages.admin;

import org.appfuse.webapp.pages.BasePageTestCase;
import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ResourceBundle;

public class UserListTest extends BasePageTestCase {

    @Test
    public void testListUsers() {
        doc = tester.renderPage("admin/userList");
        assertNotNull(doc.getElementById("userList"));
        assertTrue(doc.getElementById("userList").find("tbody/tr").getChildren().size() >= 2);
    }

    @Test
    public void testEditUser() {
        doc = tester.renderPage("admin/userList");
        doc = tester.clickLink(doc.getElementById("user-user"));

        ResourceBundle rb = ResourceBundle.getBundle(MESSAGES);

        assertTrue(doc.toString().contains("<title>" +
                rb.getString("userProfile.title") +
                " | " + rb.getString("webapp.name") + "</title>"));
    }
}
