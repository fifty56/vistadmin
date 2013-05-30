package org.vist.vistadmin.util;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vist.vistadmin.service.CourseTeacherServiceImpl;

public class DateUtil {
	
	private static final Logger LOGGER =  LoggerFactory.getLogger(DateUtil.class);
	
	public static boolean isSameDay(Date date1, Date date2) {
		boolean retVal = false;
		LOGGER.trace("isSameDay: date1: " + date1 + ", date2: " + date2);
				
		if(date1.getYear() == date2.getYear() && date1.getMonth() == date2.getMonth() && date1.getDate() == date2.getDate()) {
			retVal = true;
		}
		LOGGER.trace("retVAl: " + retVal);
		return retVal;
	}
	
	public static boolean isBetweenOrSameDay(Date target, Date from, Date till) {
		boolean retVal = false;
		LOGGER.trace("isBetweenOrSameDay: target: "+ target + ", from: " + from + ", till: " + till);		
		if((isSameDay(target, from) || target.after(from)) && (isSameDay(target, till) || target.before(till))) {
			retVal = true;
		}
		LOGGER.trace("retVal: " + retVal);
		return retVal;
	}
}
