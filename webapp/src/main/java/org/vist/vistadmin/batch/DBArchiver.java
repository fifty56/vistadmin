package org.vist.vistadmin.batch;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class DBArchiver {

	private static Logger LOGGER = LoggerFactory.getLogger(DBArchiver.class);
	
	@Autowired
	CourseService courseService;
	
	@Autowired
	VistEmailSender emailSender;
	
	@Value("#{system_props['db.archive.do']}")
	private boolean doArchiving;
	
	@Value("#{system_props['db.dump.command']}")
    private String dbDumpCmd;
	
	public void doArchiving() {
		LOGGER.debug("doArchiving called");
		try {
			Calendar cal = Calendar.getInstance();
			 int day = cal.get(Calendar.DATE);
		     int month = cal.get(Calendar.MONTH) + 1;
		     int year = cal.get(Calendar.YEAR);	     	     
		     
		     LOGGER.debug("dbDumpCmd before replacing DATESTR {}", dbDumpCmd);
		     String dateStr = year + "-" + month + "-" + day;
		     dbDumpCmd = dbDumpCmd.replace("[DATESTR]", dateStr);		    		 
		     LOGGER.debug("dbDumpCmd after replacing DATESTR {}", dbDumpCmd);
		     
		     if(doArchiving) {
				 Process p = Runtime.getRuntime().exec(dbDumpCmd);
				 LOGGER.debug("DB dump process: {}", p);
				 p.waitFor();			 
				 
				 int idx = dbDumpCmd.indexOf('>');
				 String fileName = dbDumpCmd.substring(idx + 2);
				 LOGGER.debug("fileName to send: {}", fileName);
				 emailSender.sendSysadminMail("db dump", "", fileName);
		     }
		} catch (Exception e) {
			LOGGER.error("Error while archiving DB", e);
		}
		LOGGER.debug("doArchiving finished");
    }
	
	
}
