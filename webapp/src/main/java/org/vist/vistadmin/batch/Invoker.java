package org.vist.vistadmin.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Invoker {

	private static Logger LOGGER = LoggerFactory.getLogger(Invoker.class);
	
	@Autowired
	StatusEmailer statuseEailer;
	
	@Autowired
	DBArchiver dbArchiver;
		
	@Scheduled(cron = "${cron.email.send}")
	public void doStatusEmailJob() {
		 LOGGER.debug("doStatusEmailJob called");		 		 
		 
		 statuseEailer.sendStatusMail();
		 
		 LOGGER.debug("doStatusEmailJob finished");
	}
	
	@Scheduled(cron = "${cron.db.archive}")
	public void doDbArcive() {
		 LOGGER.debug("doDbArcive called");
		 
		 dbArchiver.doArchiving();		
		 
		 LOGGER.debug("doDbArcive finished");
	}
	 
	  
	
	 
	 
}
