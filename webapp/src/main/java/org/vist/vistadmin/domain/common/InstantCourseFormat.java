package org.vist.vistadmin.domain.common;

import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;
import org.vist.vistadmin.util.AppContextProvider_web;


public enum InstantCourseFormat {

	MONTHLY("enum.instantcourseformat.base"),
	MONTHLY_INTENSIVE("enum.instantcourseformat.vip");
	  
    String label;
    
    private InstantCourseFormat(String s) {
    	this.label = s;
    }       
    
    public String getLabel() { 
    	Locale locale = LocaleContextHolder.getLocale();    
    	return AppContextProvider_web.getApplicationContext().getMessage(label, null, locale);
    }


}
