package org.vist.vistadmin.batch;

import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.vist.vistadmin.domain.Course;
import org.vist.vistadmin.domain.common.ClassStatus;
import org.vist.vistadmin.service.CourseService;

@Component
public class VistEmailSender {

	private static Logger LOGGER = LoggerFactory.getLogger(VistEmailSender.class);
	
	@Autowired
	CourseService courseService;
	
	@Autowired
	private JavaMailSenderImpl mailSender;
    
	@Value("#{email_props['email.send']}")
	private boolean sendit;
	
	@Value("#{email_props['email.to']}")
	private String emailTo;
		
	@Value("#{email_props['email.cc']}")
	private String emailCc;
	
	@Value("#{email_props['email.sysadmin.to']}")
	private String emailSysadminTo;
	           
	
	public void sendStatusEmail(String body) {
		LOGGER.debug("sendStatusEmail called");
        try{
    		MimeMessage message = mailSender.createMimeMessage();
    		MimeMessageHelper  msg = new MimeMessageHelper(message, true);
            msg.setTo(emailTo);
            if(emailCc != null && !emailCc.equals("")) {
            	msg.setCc(emailCc);
            }
            msg.setSubject("VISTADMIN - napi státusz");
            StringBuilder sb = new StringBuilder("<html><body");
            sb.append(body);
            sb.append("</body></html>");
            msg.setText(sb.toString(), true);
            if(sendit) {
            	mailSender.send(message);
            }
        }
        catch(MailException ex) {
        	LOGGER.error("Error when sending email", ex);            
        } catch (MessagingException e) {
        	LOGGER.error("Error when preparing email", e);
		}
    }
	
	public void sendSysadminMail(String subject, String body, String fileName) {
		LOGGER.debug("sendSysadminMail called with subject: " + subject + ", fileName: " + fileName);
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper  msg = new MimeMessageHelper(message, true);
	        msg.setTo(emailSysadminTo);
	        if(fileName != null && !fileName.equals("")) {
	        	FileSystemResource file = new FileSystemResource(fileName);
	        	msg.addAttachment(file.getFilename(), file);
	        }
	        msg.setSubject("VISTADMIN - " + subject);
            StringBuilder sb = new StringBuilder("<html><body");
            sb.append(body);
            sb.append("</body></html>");
            msg.setText(sb.toString(), true);
            if(sendit) {
            	mailSender.send(message);
            }
		}
        catch(MailException ex) {
        	LOGGER.error("Error when sending email", ex);            
        } catch (MessagingException e) {
        	LOGGER.error("Error when preparing email", e);
		}
	}		
}
