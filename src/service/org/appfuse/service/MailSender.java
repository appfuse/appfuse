package org.appfuse.service;

import java.util.Properties;
import java.util.ResourceBundle;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.Constants;


public class MailSender extends Object {
    //~ Static fields/initializers =============================================

    private static Log log =
        LogFactory.getFactory().getInstance(MailSender.class);

    //~ Methods ================================================================

    /**
     * Retrieves a Mail session from Tomcat's Resource Factory (JNDI)
     */
    public static Session getSession() {
        Session session = null;

        try {
            session =
                (Session) new InitialContext().lookup("java:comp/env/" +
                                                      Constants.JNDI_MAIL);
        } catch (NamingException ex) {
            if (log.isDebugEnabled()) {
                log.info("error communicating with JNDI, assuming testcase");
            }

            ResourceBundle mailProps = ResourceBundle.getBundle("mail");

            Properties props = new Properties();
            props.setProperty("mail.debug", mailProps.getString("mail.debug"));
            props.setProperty("mail.transport.protocol",
                              mailProps.getString("mail.transport.protocol"));
            props.setProperty("mail.host", mailProps.getString("mail.host"));
            props.setProperty("mail.user", mailProps.getString("mail.user"));
            props.setProperty("mail.password",
                              mailProps.getString("mail.password"));
            session = Session.getDefaultInstance(props, null);
        }

        return session;
    }

    /**
     * This method is used to send a Message with a pre-defined
     * mime-type.
     *
     * @param from e-mail address of sender
     * @param to e-mail address(es) of recipients
     * @param subject subject of e-mail
     * @param content the body of the e-mail
     * @param mimeType type of message, i.e. text/plain or text/html
     * @throws MessagingException the exception to indicate failure
     */
    public static void sendMessage(String from, String[] to, String[] cc,
                                   String subject, String content,
                                   String mimeType) throws MessagingException {
        Message message = new MimeMessage(getSession());

        // TODO: Refactor to use a default from address (maybe in config?!)
        if (!StringUtils.isEmpty(from)) {
            InternetAddress sentFrom = new InternetAddress(from);
            message.setFrom(sentFrom);

            if (log.isDebugEnabled()) {
                log.debug("e-mail from: " + sentFrom);
            }
        }

        InternetAddress[] sendTo = new InternetAddress[to.length];

        for (int i = 0; i < to.length; i++) {
            sendTo[i] = new InternetAddress(to[i]);

            if (log.isDebugEnabled()) {
                log.debug("sending e-mail to: " + to[i]);
            }
        }

        if ((cc.length > 0) && (cc[0] != null)) {
            InternetAddress[] copyTo = new InternetAddress[cc.length];

            for (int i = 0; i < cc.length; i++) {
                copyTo[i] = new InternetAddress(cc[i]);

                if (log.isDebugEnabled()) {
                    log.debug("copying e-mail to: " + cc[i]);
                }
            }

            message.setRecipients(Message.RecipientType.CC, copyTo);
        }

        message.setRecipients(Message.RecipientType.TO, sendTo);

        message.setSubject(subject);
        message.setContent(content, mimeType);

        Transport.send(message);
    }

    /**
     * This method is used to send a Text Message.
     *
     * @param from e-mail address of sender
     * @param to e-mail addresses of recipients
     * @param subject subject of e-mail
     * @param content the body of the e-mail
     * @throws MessagingException the exception to indicate failure
     */
    public static void sendTextMessage(String from, String[] to, String[] cc,
                                       String subject, String content)
    throws MessagingException {
        sendMessage(from, to, cc, subject, content, "text/plain");
    }

    /**
     * This method overrides the sendTextMessage to specify
     * only one sender, rather than an array of senders.
     *
     * @param from e-mail address of sender
     * @param to e-mail address of recipients
     * @param subject subject of e-mail
     * @param content the body of the e-mail
     * @throws MessagingException the exception to indicate failure
     */
    public static void sendTextMessage(String from, String to, String cc,
                                       String subject, String content)
    throws MessagingException {
        String[] recipient = { to };
        String[] copy = { cc };
        sendMessage(from, recipient, copy, subject, content, "text/plain");
    }

    /**
     * This method is used to send a HTML Message
     *
     * @param from e-mail address of sender
     * @param to e-mail address(es) of recipients
     * @param subject subject of e-mail
     * @param content the body of the e-mail
     * @throws MessagingException the exception to indicate failure
     */
    public static void sendHTMLMessage(String from, String[] to, String[] cc,
                                       String subject, String content)
    throws MessagingException {
        sendMessage(from, to, cc, subject, content, "text/html");
    }

    /**
     * This method overrides the sendHTMLMessage to specify
     * only one sender, rather than an array of senders.
     *
     * @param from e-mail address of sender
     * @param to e-mail address of recipients
     * @param subject subject of e-mail
     * @param content the body of the e-mail
     * @throws MessagingException the exception to indicate failure
     */
    public static void sendHTMLMessage(String from, String to, String cc,
                                       String subject, String content)
    throws MessagingException {
        String[] recipient = { to };
        String[] copy = { cc };
        sendMessage(from, recipient, copy, subject, content, "text/html");
    }
}
