package org.appfuse.webapp.pages.admin;

import org.apache.tapestry5.dom.Element;
import org.appfuse.webapp.pages.BasePageTestCase;
import org.compass.gps.CompassGps;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ResourceBundle;

import static org.junit.Assert.*;

public class UserListTest extends BasePageTestCase {

    @Autowired
    private CompassGps compassGps;

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

        ResourceBundle rb = ResourceBundle.getBundle(MESSAGES);

        assertTrue(doc.toString().contains("<title>" +
                rb.getString("userProfile.title") + " | " +
                rb.getString("webapp.name") + "</title>"));
    }

    @Test
    public void testSearch() {
        compassGps.index();
        doc = tester.renderPage("admin/userList");

        Element form = doc.getElementById("searchForm");
        assertNotNull(form);

        fieldValues.put("q", "admin");
        doc = tester.submitForm(form, fieldValues);
        assertTrue(doc.getElementById("userList").find("tbody").getChildren().size() == 1);
    }
}
