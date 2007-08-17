package org.appfuse.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.subethamail.wiser.Wiser;
import org.subethamail.wiser.WiserMessage;

import javax.mail.BodyPart;
import javax.mail.Part;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Date;

/**
 * @author Bryan Noll
 */
public class MailEngineTest extends BaseManagerTestCase {
    MailEngine mailEngine;
    SimpleMailMessage mailMessage;
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

    public void setMailEngine(MailEngine mailEngine) {
        this.mailEngine = mailEngine;
    }

    public void setMailMessage(SimpleMailMessage mailMessage) {
        this.mailMessage = mailMessage;
    }

    @Override
    protected void onSetUp() {
        mailSender.setHost("localhost");
        mailEngine.setMailSender(mailSender);
    }

    @Override
    protected void onTearDown() {
       mailEngine.setMailSender(null);
    }
    
    public void testSend() throws Exception {
        // mock smtp server
        Wiser wiser = new Wiser();
        // set the port to a random value so there's no conflicts between tests
        int port = 2525 + (int)(Math.random() * 100);
        mailSender.setPort(port);
        wiser.setPort(port);
        wiser.start();
        
        Date dte = new Date();
        this.mailMessage.setTo("foo@bar.com");
        String emailSubject = "grepster testSend: " + dte;
        String emailBody = "Body of the grepster testSend message sent at: " + dte;
        this.mailMessage.setSubject(emailSubject);
        this.mailMessage.setText(emailBody);
        this.mailEngine.send(this.mailMessage);
        
        wiser.stop();
        assertTrue(wiser.getMessages().size() == 1);
        WiserMessage wm = wiser.getMessages().get(0);
        assertEquals(emailSubject, wm.getMimeMessage().getSubject());
        assertEquals(emailBody, wm.getMimeMessage().getContent());
    }
    
    public void testSendMessageWithAttachment() throws Exception {
        final String ATTACHMENT_NAME = "boring-attachment.txt";
        
        //mock smtp server
        Wiser wiser = new Wiser();
        int port = 2525 + (int)(Math.random() * 100);
        mailSender.setPort(port);
        wiser.setPort(port);
        wiser.start();
        
        Date dte = new Date();
        String emailSubject = "grepster testSendMessageWithAttachment: " + dte;
        String emailBody = "Body of the grepster testSendMessageWithAttachment message sent at: " + dte;
        
        ClassPathResource cpResource = new ClassPathResource("/test-attachment.txt");
        mailEngine.sendMessage(new String[] {"foo@bar.com"}, mailMessage.getFrom(),cpResource, emailBody, emailSubject, ATTACHMENT_NAME);
        
        wiser.stop();
        assertTrue(wiser.getMessages().size() == 1);
        WiserMessage wm = wiser.getMessages().get(0);
        MimeMessage mm = wm.getMimeMessage();

        Object o = wm.getMimeMessage().getContent();
        assertTrue(o instanceof MimeMultipart);
        MimeMultipart multi = (MimeMultipart)o;
        int numOfParts = multi.getCount();
        
        boolean hasTheAttachment = false;
        for (int i = 0; i < numOfParts; i++) {
            BodyPart bp = multi.getBodyPart(i);
            String disp = bp.getDisposition();
            if (disp == null) {                        //the body of the email
                Object innerContent = bp.getContent();
                MimeMultipart innerMulti = (MimeMultipart)innerContent;
                assertEquals(emailBody, innerMulti.getBodyPart(0).getContent());
            } else if (disp.equals(Part.ATTACHMENT)) { //the attachment to the email
                hasTheAttachment = true;
                assertEquals(ATTACHMENT_NAME, bp.getFileName());
            } else {
                fail("Did not expect to be able to get here.");
            }
        }
        assertTrue(hasTheAttachment);
        assertEquals(emailSubject, mm.getSubject());
    }
}
