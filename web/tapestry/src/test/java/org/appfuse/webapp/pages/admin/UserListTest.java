package org.appfuse.webapp.pages.admin;

import org.apache.tapestry5.dom.Element;
import org.appfuse.service.UserManager;
import org.appfuse.service.UserExistsException;
import org.appfuse.model.User;
import org.appfuse.webapp.pages.BasePageTestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.junit.Assert.*;

@Transactional(Transactional.TxType.NOT_SUPPORTED)
public class UserListTest extends BasePageTestCase {
    @Autowired
    private UserManager userManager;

    @Before
    public void before() throws UserExistsException {
        User user = new User("foo");
        user.setPassword("bar");
        user.setFirstName("Foo");
        user.setLastName("Bar");
        user.setEmail("foo@appfuse.org");
        userManager.saveUser(user);
    }

    @After
    public void after() {
        User user = userManager.getUserByUsername("foo");
        userManager.removeUser(user.getId().toString());
    }

    @Test
    public void testListUsers() {
        doc = tester.renderPage("admin/userList");
        assertNotNull(doc.getElementById("userList"));
        assertTrue(doc.getElementById("userList").find("tbody").getChildren().size() >= 2);
    }

    @Test
    public void testEditUser() {
        doc = tester.renderPage("admin/userList");
        doc = tester.clickLink(doc.getElementById("user-admin"));

        ResourceBundle rb = ResourceBundle.getBundle(MESSAGES, new Locale("en"));

        assertTrue(doc.toString().contains("<title>" +
                rb.getString("userProfile.title") + " | " +
                rb.getString("webapp.name") + "</title>"));
    }

    @Test
    public void testSearch() {
        // regenerate search index
        userManager.reindex();

        doc = tester.renderPage("admin/userList");

        Element form = doc.getElementById("searchForm");
        assertNotNull(form);

        fieldValues.put("q", "foo");
        doc = tester.submitForm(form, fieldValues);

        assertNotNull("search doesn't contain any results", doc.getElementById("userList"));
        assertTrue(doc.getElementById("userList").find("tbody").getChildren().size() == 1);
    }
}
