package org.appfuse.webapp.integration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.subethamail.wiser.Wiser;
import org.subethamail.wiser.WiserMessage;
import org.tuckey.web.filters.urlrewrite.utils.StringUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.net.BindException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@ContextConfiguration(locations = {
        "classpath:/applicationContext-resources.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public abstract class SeleniumBaseTestCase {

    protected final Log log = LogFactory.getLog(getClass());

    private final int waitTimeOutSeconds = 60;

    protected int smtpPort = 25250;
    public Wiser wiser;

    /**
     * WaitFor callback interface.
     *
     */
    protected interface WaitFor {
        boolean isFinished();
    }

    private String baseUrl = "http://localhost:8080/";
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

    @Value("${mail.port}")
    public void setSmtpPort(final int smtpPort) {
        this.smtpPort = smtpPort;
    }

    @Before
    public void setUp() throws Exception {
        log.debug("");

        log.debug("Starting wiser on port " + smtpPort);
        wiser = startWiser(smtpPort);

        setDriver(new ChromeDriver());
        getDriver().manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        getDriver().manage().window().setSize(new Dimension(1024, 900));
    }


    @After
    public void tearDown() throws Exception {
        if (getDriver() != null) {
            getDriver().quit();
        }
        if (wiser != null) {
            wiser.stop();
        }
    }

    protected Wiser startWiser(int smtpPort) {
        Wiser wiser = new Wiser();
        wiser.setPort(smtpPort);
        try {
            wiser.start();
        } catch (RuntimeException re) {
            if (re.getCause() instanceof BindException) {
                int nextPort = smtpPort + 1;
                if (nextPort - smtpPort > 10) {
                    log.error("Exceeded 10 attempts to start SMTP server, aborting...");
                    throw re;
                }
                log.error("SMTP port " + smtpPort + " already in use, trying " + nextPort);
                return startWiser(nextPort);
            }
        }
        return wiser;
    }

    protected List<MimeMessage> getMailMessages() throws MessagingException {
        final List<MimeMessage> messages = new ArrayList<MimeMessage>();
        for (final WiserMessage message : wiser.getMessages()) {
            messages.add(message.getMimeMessage());
        }
        return messages;
    }

    protected boolean waitFor(final WaitFor waitFor) {
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

    protected boolean waitForAlert(final String alertText) {
        return waitFor(new WaitFor() {
            @Override public boolean isFinished() {
                String regex = alertText;
                if (StringUtils.isBlank(regex) || "*".equals(regex)) {
                    regex = "^[\\s\\S]*$";
                }
                return closeAlertAndGetItsText(true).matches(regex);
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

    private String closeAlertAndGetItsText(final boolean accept) {
        final Alert alert = driver.switchTo().alert();
        final String alertText = alert.getText();
        if (accept) {
            alert.accept();
        } else {
            alert.dismiss();
        }
        return alertText;
    }

}
