package org.appfuse.webapp.pages.admin;

import org.appfuse.webapp.pages.BasePageTester;
import org.junit.Test;

public class UserListTest extends BasePageTester {

    @Test
    public void testListUsers() {
        doc = tester.renderPage("admin/userList");
        assertNotNull(doc.getElementById("userList"));
        assertTrue(doc.getElementById("userList").find("tbody/tr").getChildren().size() >= 2);
    }
}
