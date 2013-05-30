package org.vist.vistadmin.util;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vist.vistadmin.domain.Course;
import org.vist.vistadmin.domain.common.SelectOption;
import org.vist.vistadmin.web.CourseController;

public class WebUtil {

	private static final Logger LOGGER =  LoggerFactory.getLogger(WebUtil.class);
	
	public static String getStringFromMultiSelectListOptions(List<String> options) {		
    	LOGGER.debug("getStringForFromMultiSelectList called, list: " + options);
    	StringBuilder sb = new StringBuilder();
    	if(options != null) {
    		for (String option : options) {
    			if(sb.length() > 0) {
    				sb.append(";");				
    			} 
    			sb.append(option);
    		}
		}	
    	String retVal = sb.toString();
    	LOGGER.debug("string value to return: " + sb.toString());
    	return retVal;
    }
	
	public static List<SelectOption> getOptionListFromCompanyStudentNames(String studentNameStr) {
    	LOGGER.trace("getOptionListFromCompanyStudentNames called");
    	List<SelectOption> options = new ArrayList<SelectOption>();
    	LOGGER.trace("studentNameStr: " + studentNameStr);
    	if(studentNameStr != null && !studentNameStr.equals("")) {
    		String[] tokens = studentNameStr.split(";");
    		for (String str : tokens) {
    			SelectOption option = new SelectOption();
    			option.setId(str);		    			    		
    			option.setLabel(str);
    			options.add(option);
    		}
    	}
    	LOGGER.trace("getOptionListFromCompanyStudentNames returns: " + options);
    	return options;
    }
	
}
