package org.appfuse.webapp.pages.admin;

import org.appfuse.webapp.pages.BasePageTestCase;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class UserListTest extends BasePageTestCase {

    @Test
    public void testListUsers() {
        doc = tester.renderPage("admin/userList");
        assertNotNull(doc.getElementById("userList"));
        assertTrue(doc.getElementById("userList").find("tbody/tr").getChildren().size() >= 2);
    }
}
