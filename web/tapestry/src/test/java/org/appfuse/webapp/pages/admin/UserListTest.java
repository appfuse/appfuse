package org.appfuse.webapp.pages.admin;

import java.util.Locale;
import org.apache.tapestry5.dom.Element;
import org.appfuse.webapp.pages.BasePageTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ResourceBundle;
import org.appfuse.service.UserManager;

import static org.junit.Assert.*;

public class UserListTest extends BasePageTestCase {

    @Autowired
    private UserManager userManager;

    @Test
    public void testListUsers() {
        doc = tester.renderPage("admin/userList");
        assertNotNull(doc.getElementById("userList"));
        assertTrue(doc.getElementById("userList").find("tbody").getChildren().size() >= 2);
    }

    @Test
    public void testEditUser() {
        doc = tester.renderPage("admin/userList");
        doc = tester.clickLink(doc.getElementById("user-user"));

        ResourceBundle rb = ResourceBundle.getBundle(MESSAGES, new Locale("en"));

        assertTrue(doc.toString().contains("<title>" +
                rb.getString("userProfile.title") + " | " +
                rb.getString("webapp.name") + "</title>"));
    }

    @Test
    public void testSearch() {
        // regenerate search index
        userManager.reindex();
        flushSearchIndexes();

        doc = tester.renderPage("admin/userList");

        Element form = doc.getElementById("searchForm");
        assertNotNull(form);

        fieldValues.put("q", "admin");
        doc = tester.submitForm(form, fieldValues);
        assertTrue(doc.getElementById("userList").find("tbody").getChildren().size() == 1);
    }
}
