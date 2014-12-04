package org.appfuse.webapp.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.subethamail.wiser.Wiser;

import java.net.BindException;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "classpath:/applicationContext-resources.xml",
    "classpath:/applicationContext-dao.xml",
    "classpath:/applicationContext-service.xml",
    "classpath*:/applicationContext.xml", // for modular archetypes
    "/WEB-INF/applicationContext*.xml",
    "/WEB-INF/dispatcher-servlet.xml"
})
public abstract class BaseControllerTestCase {
    protected transient final Log log = LogFactory.getLog(getClass());
    private int smtpPort;

    @Autowired
    private JavaMailSenderImpl mailSender;

    @Before
    public void onSetUp() {
        smtpPort = (new Random().nextInt(9999 - 1000) + 1000);
        log.debug("SMTP Port set to: " + smtpPort);
    }

    protected int getSmtpPort() {
        return smtpPort;
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
        mailSender.setPort(smtpPort);
        return wiser;
    }
}
