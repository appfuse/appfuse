package org.appfuse.service;

import javax.mail.MessagingException;
import javax.mail.Session;

import junit.framework.TestCase;

import org.appfuse.Constants;


/**
 * Tests the methods in MailSender class
 **/
public class MailSenderTest extends TestCase {
    //~ Instance fields ========================================================

    private String to = "junk@raibledesigns.com";
    private String from = Constants.DEFAULT_FROM;
    private Session mailSes = null;

    //~ Constructors ===========================================================

    /**
     * Sends a text-based e-mail
     */
    public void testSendTextMessage() throws Exception {
        try {
            MailSender.sendTextMessage(from, to, null, "[JUnit] Text Message",
                                     "Text with a URL:\n\nhttp://www.raibledesigns.com.com");
        } catch (MessagingException me) {
            fail("MessagingException: " + me);
        }
    }

    /**
     * Sends an html-based e-mail
     */
    public void testSendHTMLMessage() throws Exception {
        try {
            MailSender.sendHTMLMessage(from, to, null, "[JUnit] HTML Message",
                                     "HTML message <b>with</b> a URL:<br /><br /><a href=\"http://www.raibledesigns.com\">Raible Designs</a>");
        } catch (MessagingException me) {
            fail("MessagingException: " + me);
        }
    }
    
    /** main method
     *
     * @param args any arguments for this test
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(MailSenderTest.class);
    }

}
