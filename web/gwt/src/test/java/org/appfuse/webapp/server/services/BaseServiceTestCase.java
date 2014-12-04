package org.appfuse.webapp.server.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.subethamail.wiser.Wiser;
import org.subethamail.wiser.WiserMessage;

import java.net.BindException;
import java.util.List;
import java.util.Random;

@ContextConfiguration(locations = {
    "classpath:/applicationContext-resources.xml",
    "classpath:/applicationContext-dao.xml",
    "classpath:/applicationContext-service.xml",
    "classpath*:/applicationContext.xml",
    "/WEB-INF/applicationContext*.xml",
    "/WEB-INF/cxf-servlet.xml",
    "/WEB-INF/security.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public abstract class BaseServiceTestCase {
    protected final Log log = LogFactory.getLog(getClass());
    private int smtpPort;
    @Autowired
    private UserManager userManager;
    @Autowired
    private JavaMailSenderImpl mailSender;

    private Wiser wiser;

    @Before
    public void onSetUp() {
        smtpPort = (new Random().nextInt(9999 - 1000) + 1000);
        log.debug("SMTP Port set to: " + smtpPort);
    }

    protected int getSmtpPort() {
        return smtpPort;
    }

    protected UserManager getUserManager() {
        return userManager;
    }

    protected void login(final String username) {
        final User user = userManager.getUserByUsername(username);
        final UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
            user.getUsername(), "", user.getAuthorities());
        auth.setDetails(user);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    protected void logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    protected void startSmtpServer() {
        wiser = startWiser(getSmtpPort());
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

    protected List<WiserMessage> getReceivedMailMessages(final boolean stopServer) {
        if (stopServer) {
            wiser.stop();
        }
        return wiser.getMessages();
    }
}
