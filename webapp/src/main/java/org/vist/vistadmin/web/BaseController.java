package org.vist.vistadmin.web;

import java.text.SimpleDateFormat;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

public class BaseController {
	
	public static final String DATE_TIME_FORMAT_STR = "yyyy-MM-dd"; 
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
	    SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT_STR);
	    dateFormat.setLenient(false);

	    // true passed to CustomDateEditor constructor means convert empty String to null
	    binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(dateFormat, true));
	} 
	
	public void addDateTimeFormatPatterns(Model uiModel, String[] keys) {		
		for(String key: keys) {
			uiModel.addAttribute(key, DATE_TIME_FORMAT_STR);
			//uiModel.addAttribute("course_enddate_date_format", DATE_TIME_FORMAT_STR, LocaleContextHolder.getLocale()));
		}                
    }    
}
