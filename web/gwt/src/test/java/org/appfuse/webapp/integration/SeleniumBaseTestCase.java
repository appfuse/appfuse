package org.appfuse.webapp.integration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.runner.RunWith;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.tuckey.web.filters.urlrewrite.utils.StringUtils;

@ContextConfiguration(locations = {
        "classpath:/applicationContext-resources.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public abstract class SeleniumBaseTestCase {

    protected final Log log = LogFactory.getLog(getClass());

    private final int waitTimeOutSeconds = 60;

    private String baseUrl;
    private WebDriver driver;


    public String getBaseUrl() {
        return baseUrl;
    }

    public WebDriver getDriver() {
        return driver;
    }

    @Value("#{systemProperties.baseUrl}") 
    public void setBaseUrl(final String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setDriver(final WebDriver driver) {
        this.driver = driver;
    }



    /**
     * WaitFor callback interface.
     * 
     */
    protected interface WaitFor {
        boolean isFinished();
    }


    private boolean waitFor(final WaitFor waitFor) {
        boolean isFinished = false;
        for (int second = 0; second < waitTimeOutSeconds; second++) {
            try {
                if (waitFor.isFinished()) {
                    isFinished = true;
                    break;
                }
            } catch (final Exception e) {
            }
            try {
                Thread.sleep(1000);
            } catch (final InterruptedException e) {
                log.error("Error sleeping waitFor thread", e);
                throw new RuntimeException(e);
            }
        }

        return isFinished;
    }

    protected boolean waitForTitle(final WebDriver driver, final String title) {
        return waitFor(new WaitFor() {
            @Override public boolean isFinished() {
                return driver.getTitle().matches(title);
            }
        });
    }

    protected boolean waitForElement(final By selector) {
        return waitFor(new WaitFor() {
            @Override public boolean isFinished() {
                return isElementPresent(selector);
            }
        });
    }

    protected boolean waitForElementNotPresent(final By selector) {
        return waitFor(new WaitFor() {
            @Override public boolean isFinished() {
                return !isElementPresent(selector);
            }
        });
    }

    protected boolean waitForAlert() {
        return waitForAlert(null);
    }

    private boolean waitForAlert(final String alertText) {
        return waitFor(new WaitFor() {
            @Override public boolean isFinished() {
                String regex = alertText;
                if (StringUtils.isBlank(regex) || "*".equals(regex)) {
                    regex = "^[\\s\\S]*$";
                }
                return closeAlertAndGetItsText().matches(regex);
            }
        });
    }

    protected boolean isElementPresent(final By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (final NoSuchElementException e) {
            return false;
        }
    }

    private boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (final NoAlertPresentException e) {
            return false;
        }
    }

    private String closeAlertAndGetItsText() {
        final Alert alert = driver.switchTo().alert();
        final String alertText = alert.getText();
        alert.accept();
        return alertText;
    }

    @After
    public void tearDown() throws Exception {
        getDriver().quit();
    }

}