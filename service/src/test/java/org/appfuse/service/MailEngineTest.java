package org.appfuse.service;

import java.util.Date;

import javax.mail.BodyPart;
import javax.mail.Part;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.subethamail.wiser.Wiser;
import org.subethamail.wiser.WiserMessage;

/**
 * @author Bryan Noll
 */
public class MailEngineTest extends BaseManagerTestCase {
    MailEngine mailEngine = null;
    SimpleMailMessage mailMessage = null;

    public MailEngineTest() {}
    
    @Override
    protected void onSetUp() throws Exception {
        // change the port on the mailSender so it doesn't conflict with an 
        // existing SMTP server on localhost
        JavaMailSenderImpl mailSender = (JavaMailSenderImpl) super.applicationContext.getBean("mailSender");
        mailSender.setPort(2525);
        mailSender.setHost("localhost");        
    }
    
    //tests
    
    public void testSend() throws Exception {
        //mock smtp server
        Wiser wiser = new Wiser();
        wiser.setPort(2525);  //default is 25, see onSetUp method for why it is set to 2525
        wiser.start();
        
        Date dte = new Date();
        this.mailMessage.setTo("foo@bar.com");
        String emailSubject = "grepster testSend: " + dte;
        String emailBody = "Body of the grepster testSend message sent at: " + dte;
        this.mailMessage.setSubject(emailSubject);
        this.mailMessage.setText(emailBody);
        this.mailEngine.send(this.mailMessage);
        
        wiser.stop();
        super.assertTrue(wiser.getMessages().size() == 1);
        WiserMessage wm = wiser.getMessages().get(0);
        super.assertEquals(emailSubject, wm.getMimeMessage().getSubject());
        super.assertEquals(emailBody, wm.getMimeMessage().getContent());
    }
    
    public void testSendMessageWithAttachment() throws Exception {
        final String ATTACHMENT_NAME = "boring-attachment.txt";
        
        //mock smtp server
        Wiser wiser = new Wiser();
        wiser.setPort(2525);  //default is 25, see onSetUp method for why it is set to 2525
        wiser.start();
        
        Date dte = new Date();
        
        String emailSubject = "grepster testSendMessageWithAttachment: " + dte;
        String emailBody = "Body of the grepster testSendMessageWithAttachment message sent at: " + dte;
        
        ClassPathResource cpResource = new ClassPathResource("/test-attachment.txt");
        this.mailEngine.sendMessage(new String[] {"foo@bar.com"}, this.getMailMessage().getFrom(),cpResource, emailBody, emailSubject, ATTACHMENT_NAME);
        
        wiser.stop();
        super.assertTrue(wiser.getMessages().size() == 1);
        WiserMessage wm = wiser.getMessages().get(0);
        MimeMessage mm = wm.getMimeMessage();

        Object o = wm.getMimeMessage().getContent();
        super.assertTrue(o instanceof MimeMultipart);
        MimeMultipart multi = (MimeMultipart)o;
        int numOfParts = multi.getCount();
        
        boolean hasTheAttachment = false;
        for (int i = 0; i < numOfParts; i++) {
            BodyPart bp = multi.getBodyPart(i);
            String disp = bp.getDisposition();
            if (disp == null) {                        //the body of the email
                Object innerContent = bp.getContent();
                MimeMultipart innerMulti = (MimeMultipart)innerContent;
                super.assertEquals(emailBody, innerMulti.getBodyPart(0).getContent());
            } else if (disp.equals(Part.ATTACHMENT)) { //the attachment to the email
                hasTheAttachment = true;
                super.assertEquals(ATTACHMENT_NAME, bp.getFileName());
            } else {
                super.fail("Did not expect to be able to get here.");
            }
        }
        super.assertTrue(hasTheAttachment);
        super.assertEquals(emailSubject, mm.getSubject());
    }
    
    
    //getters and setters
    public MailEngine getMailEngine() {
        return mailEngine;
    }

    public void setMailEngine(MailEngine mailEngine) {
        this.mailEngine = mailEngine;
    }

    public SimpleMailMessage getMailMessage() {
        return mailMessage;
    }

    public void setMailMessage(SimpleMailMessage mailMessage) {
        this.mailMessage = mailMessage;
    }
}
