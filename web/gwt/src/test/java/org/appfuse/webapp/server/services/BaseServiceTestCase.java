package org.appfuse.webapp.server.services;

import java.util.List;

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

    private int smtpPort = 25250;
    @Autowired private UserManager userManager;
    @Autowired private JavaMailSenderImpl mailSender;

    private Wiser wiser;

    @Before public void onSetUp() {
        smtpPort = smtpPort + (int) (Math.random() * 100);
        // change the port on the mailSender so it doesn't conflict with an
        // existing SMTP server on localhost
        mailSender.setPort(getSmtpPort());
        mailSender.setHost("localhost");
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
        wiser = new Wiser();
        wiser.setPort(getSmtpPort());
        wiser.start();
    }

    protected List<WiserMessage> getReceivedMailMessages(final boolean stopServer) {
        if (stopServer) {
            wiser.stop();
        }
        return wiser.getMessages();
    }
}