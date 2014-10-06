/**
 * 
 */
package org.appfuse.webapp.integration;

import static org.junit.Assert.assertTrue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;

/**
 * @author ivangsa
 *
 */
public class PasswordResetITCase extends SeleniumBaseTestCase {


    @Test
    public void testPasswordReset() throws Exception {
        log.debug("");
        getDriver().get(getBaseUrl());
        assertTrue(waitForTitle(getDriver(), "^Login[\\s\\S]*$"));

        getDriver().findElement(By.cssSelector("input.gwt-TextBox")).sendKeys("user");
        getDriver().findElement(By.id("requestRecoveryTokenLink")).click();
        assertTrue(waitForElement(By.cssSelector("div.alert-success")));

        Assert.assertEquals(1, getMailMessages().size());
        final String message = getMailMessages().get(0).getContent().toString();

        log.debug("Password reset mail message is: \n" + message);

        final Pattern linkPattern = Pattern.compile("(.*)(http://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|])(.*)", Pattern.DOTALL);
        final Matcher matcher = linkPattern.matcher(message);
        Assert.assertTrue(matcher.matches());
        final String link = matcher.group(2);

        getDriver().get(link);
        assertTrue(waitForTitle(getDriver(), "^Update your Password[\\s\\S]*$"));
        getDriver().findElement(By.name("password")).clear();
        getDriver().findElement(By.name("password")).sendKeys("newpasword");
        getDriver().findElement(By.linkText("Change Password")).click();
        assertTrue(waitForTitle(getDriver(), "^Main Menu[\\s\\S]*$"));
        assertTrue(waitForElement(By.cssSelector("div.alert-success")));
    }

}
