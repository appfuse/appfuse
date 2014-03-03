package org.appfuse.webapp.integration;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Date;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class AppFuseWebITCase extends SeleniumBaseTestCase {


    @Test
    public void testSeleniumTestCase() throws Exception {

        testLoadBaseUrl();

        testSignUp();

        testViewProfile();

        testLogout();

        testRequestPasswordHint();

        testLogin();

        testEditProfile();

        testViewUsers();

        testEditUser();

        testAddNewUser();

        testDeleteUser("newuser");

        testDeleteUser("signupuser");

        testViewActiveUsers();

        testReloadOptions();

        testUploadFile();

        testLogout();

    }

    public void testLoadBaseUrl() throws InterruptedException {
        log.debug("");
        getDriver().get(getBaseUrl());
        assertTrue(waitForTitle(getDriver(), "^Login[\\s\\S]*$"));
    }

    public void testSignUp() throws InterruptedException {
        log.debug("");
        assertTrue(isElementPresent(By.linkText("Signup")));
        getDriver().findElement(By.linkText("Signup")).click();
        assertTrue(waitForElement(By.linkText("Save")));

        getDriver().findElement(By.linkText("Save")).click();
        assertTrue(waitForAlert());

        assertTrue(isElementPresent(By.cssSelector("form div.error")));
        getDriver().findElement(By.name("username")).clear();
        getDriver().findElement(By.name("username")).sendKeys("signupuser");
        getDriver().findElement(By.name("password")).clear();
        getDriver().findElement(By.name("password")).sendKeys("signupuser");
        getDriver().findElement(By.name("passwordHint")).clear();
        getDriver().findElement(By.name("passwordHint")).sendKeys("signupuser");
        getDriver().findElement(By.name("firstName")).clear();
        getDriver().findElement(By.name("firstName")).sendKeys("signupuser");
        getDriver().findElement(By.name("lastName")).clear();
        getDriver().findElement(By.name("lastName")).sendKeys("signupuser");
        getDriver().findElement(By.name("email")).clear();
        getDriver().findElement(By.name("email")).sendKeys("signup@test.org");

        getDriver().findElement(By.linkText("Save")).click();
        assertTrue(waitForTitle(getDriver(), "^Main Menu[\\s\\S]*$"));
    }

    public void testViewProfile() throws InterruptedException {
        log.debug("");
        assertTrue(getDriver().getTitle().matches("^Main Menu[\\s\\S]*$"));
        getDriver().findElement(By.linkText("Edit Profile")).click();
        assertTrue(waitForTitle(getDriver(), "^User Settings[\\s\\S]*$"));

        assertTrue(waitForElement(By.linkText("Save")));

        getDriver().findElement(By.linkText("Save")).click();
        assertTrue(waitForElement(By.cssSelector("div.alert-success")));
    }

    public void testRequestPasswordHint() throws InterruptedException {
        log.debug("");
        getDriver().findElement(By.linkText("password hint e-mailed to you")).click();
        assertTrue(waitForAlert());

        getDriver().findElement(By.cssSelector("input.gwt-TextBox")).clear();
        getDriver().findElement(By.cssSelector("input.gwt-TextBox")).sendKeys("nouser");
        getDriver().findElement(By.linkText("password hint e-mailed to you")).click();
        assertTrue(waitForElement(By.cssSelector("div.alert-error")));

        getDriver().findElement(By.cssSelector("input.gwt-TextBox")).clear();
        getDriver().findElement(By.cssSelector("input.gwt-TextBox")).sendKeys("admin");
        getDriver().findElement(By.linkText("password hint e-mailed to you")).click();
        assertTrue(waitForElement(By.cssSelector("div.alert-success")));
    }

    public void testLogin() throws InterruptedException {
        log.debug("");
        assertTrue(waitForTitle(getDriver(), "^Login[\\s\\S]*$"));

        assertTrue(isElementPresent(By.linkText("Login")));
        getDriver().findElement(By.cssSelector("input.gwt-TextBox")).clear();
        getDriver().findElement(By.cssSelector("input.gwt-TextBox")).sendKeys("baduser");
        getDriver().findElement(By.cssSelector("input.gwt-PasswordTextBox")).clear();
        getDriver().findElement(By.cssSelector("input.gwt-PasswordTextBox")).sendKeys("badpassword");
        getDriver().findElement(By.xpath("(//a[contains(text(),'Login')])[2]")).click();
        assertTrue(waitForElement(By.cssSelector("div.alert-error")));

        getDriver().findElement(By.cssSelector("input.gwt-TextBox")).clear();
        getDriver().findElement(By.cssSelector("input.gwt-TextBox")).sendKeys("admin");
        getDriver().findElement(By.cssSelector("input.gwt-PasswordTextBox")).clear();
        getDriver().findElement(By.cssSelector("input.gwt-PasswordTextBox")).sendKeys("admin");
        getDriver().findElement(By.xpath("(//a[contains(text(),'Login')])[2]")).click();
        assertTrue(waitForTitle(getDriver(), "^Main Menu[\\s\\S]*$"));
    }

    public void testEditProfile() throws InterruptedException {
        log.debug("");
        getDriver().findElement(By.linkText("Edit Profile")).click();
        assertTrue(waitForTitle(getDriver(), "^User Settings[\\s\\S]*$"));

        assertTrue(waitForElement(By.cssSelector("form.well")));

        getDriver().findElement(By.name("phoneNumber")).clear();
        getDriver().findElement(By.name("phoneNumber")).sendKeys("555 555 555");
        getDriver().findElement(By.linkText("Save")).click();
        assertTrue(waitForElement(By.cssSelector("div.alert-success")));
    }

    public void testViewUsers() throws InterruptedException {
        log.debug("");
        getDriver().findElement(By.linkText("Administration")).click();
        getDriver().findElement(By.linkText("View Users")).click();

        assertTrue(waitForTitle(getDriver(), "^User List[\\s\\S]*$"));
        assertTrue(waitForElement(By.cssSelector("div#search input")));

        getDriver().findElement(By.cssSelector("div#search input")).clear();
        getDriver().findElement(By.cssSelector("div#search input")).sendKeys("matt");
        getDriver().findElement(By.cssSelector("button.btn")).click();
        assertTrue(waitForElement(By.linkText("admin")));

        getDriver().findElement(By.cssSelector("input.input-medium.search-query")).clear();
        getDriver().findElement(By.cssSelector("input.input-medium.search-query")).sendKeys("nonuser");
        getDriver().findElement(By.cssSelector("button.btn")).click();
        assertTrue(waitForElementNotPresent(By.linkText("admin")));

        getDriver().findElement(By.linkText("Done")).click();
        assertTrue(waitForTitle(getDriver(), "^Main Menu[\\s\\S]*$"));
    }

    public void testEditUser() throws InterruptedException {
        log.debug("");
        getDriver().findElement(By.linkText("Administration")).click();
        getDriver().findElement(By.linkText("View Users")).click();

        assertTrue(waitForElement(By.linkText("admin")));
        getDriver().findElement(By.linkText("admin")).click();

        assertTrue(waitForElement(By.name("phoneNumber")));
        getDriver().findElement(By.name("phoneNumber")).clear();
        getDriver().findElement(By.name("phoneNumber")).sendKeys("555 555 777");
        getDriver().findElement(By.linkText("Save")).click();
        assertTrue(waitForElement(By.cssSelector("div.alert-success")));
        getDriver().findElement(By.linkText("Done")).click();
    }

    public void testViewActiveUsers() {
        log.debug("");
        getDriver().findElement(By.linkText("Administration")).click();
        getDriver().findElement(By.linkText("Current Users")).click();
        assertTrue(waitForTitle(getDriver(), "^Active Users[\\s\\S]*$"));
    }

    public void testAddNewUser() throws InterruptedException {
        log.debug("");
        getDriver().findElement(By.linkText("Administration")).click();
        getDriver().findElement(By.linkText("View Users")).click();
        assertTrue(waitForElement(By.linkText("Add")));

        getDriver().findElement(By.linkText("Add")).click();
        getDriver().findElement(By.linkText("Save")).click();
        assertTrue(waitForAlert());

        getDriver().findElement(By.name("username")).clear();
        getDriver().findElement(By.name("username")).sendKeys("newuser");
        getDriver().findElement(By.name("passwordHint")).clear();
        getDriver().findElement(By.name("passwordHint")).sendKeys("newuser");
        getDriver().findElement(By.name("firstName")).clear();
        getDriver().findElement(By.name("firstName")).sendKeys("New");
        getDriver().findElement(By.name("lastName")).clear();
        getDriver().findElement(By.name("lastName")).sendKeys("User");
        getDriver().findElement(By.name("email")).clear();
        getDriver().findElement(By.name("email")).sendKeys("email@test.org");
        getDriver().findElement(By.name("enabled")).click();
        getDriver().findElement(By.linkText("Save")).click();

        assertTrue(waitForElement(By.cssSelector("div.alert-success")));
    }

    public void testDeleteUser(final String username) throws InterruptedException {
        log.debug("");
        getDriver().findElement(By.linkText("Administration")).click();
        getDriver().findElement(By.linkText("View Users")).click();

        // log.debug("waiting for link " + new Date());
        // assertTrue(waitForElement(By.linkText(username)));
        // log.debug("done waiting for link " + new Date());
        Thread.sleep(1000);
        log.debug("clicking link " + new Date());
        getDriver().findElement(By.linkText(username)).click();
        log.debug("link clicked " + new Date());

        assertTrue(waitForElement(By.linkText("Delete")));
        getDriver().findElement(By.linkText("Delete")).click();

        assertTrue(waitForAlert());
        assertTrue(waitForElement(By.cssSelector("div.alert-success")));
    }

    public void testReloadOptions() throws InterruptedException {
        log.debug("");
        getDriver().findElement(By.linkText("Administration")).click();
        getDriver().findElement(By.linkText("Reload Options")).click();
        assertTrue(waitForElement(By.cssSelector("div.alert-success")));
    }

    public void testUploadFile() throws InterruptedException {
        log.debug("");
        getDriver().findElement(By.linkText("Administration")).click();
        getDriver().findElement(By.linkText("Upload A File")).click();
        assertTrue(waitForTitle(getDriver(), "^File Upload[\\s\\S]*$"));

        getDriver().findElement(By.name("name")).clear();
        getDriver().findElement(By.name("name")).sendKeys("File");
        // getDriver().findElement(By.name("file")).clear();
        final WebElement fileinput = getDriver().findElement(By.name("file"));
        fileinput.sendKeys(new File("pom.xml").getAbsolutePath());
        getDriver().findElement(By.cssSelector("button.btn.btn-primary")).click();
        assertTrue(waitForElement(By.cssSelector("div.alert-success")));
    }

    public void testLogout() throws InterruptedException {
        log.debug("");
        getDriver().findElement(By.linkText("Logout")).click();
        // signup end
        // password hint
        assertTrue(waitForElement(By.linkText("password hint e-mailed to you")));
    }
}
