package org.appfuse.webapp.services.impl;

import org.apache.tapestry5.ioc.Messages;
import org.appfuse.model.User;
import org.appfuse.service.MailEngine;
import org.appfuse.webapp.services.EmailService;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 *
 * @author Serge Eby
 */
public class EmailServiceImpl implements EmailService {
    final private MailEngine mailEngine;
    final private SimpleMailMessage simpleMailMessage;
    final private Messages messages;

    public EmailServiceImpl(SimpleMailMessage simpleMailMessage, MailEngine mailEngine, Messages messages) {
        this.simpleMailMessage = simpleMailMessage;
        this.mailEngine = mailEngine;
        this.messages = messages;
    }


    public void send(User user, String subject, String message, String url, boolean hint)
            throws UsernameNotFoundException, MailException {

        // Message Template
        StringBuilder msg = new StringBuilder(message);
        // Skip credentials in Password Hint
        if (!hint) {
            msg.append("\n\n").append(messages.get("user.username"));
            msg.append(": ").append(user.getUsername()).append("\n");
            msg.append(messages.get("user.password")).append(": ");
            msg.append(user.getPassword());
        }
        msg.append("\n\nLogin at: ").append(url);

        simpleMailMessage.setTo(user.getFullName() + "<" + user.getEmail() + ">");
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(msg.toString());

        mailEngine.send(simpleMailMessage);
    }
}
