package org.vist.vistadmin.domain.common;

import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;
import org.vist.vistadmin.util.AppContextProvider_web;


public enum InstantCourseType {

	MONTH("enum.instantcoursetype.month"),
	MONTH_DOUBLE("enum.instantcoursetype.month_double"),
	TWO_MONTH("enum.instantcoursetype.two_month"),
	TWO_MONTH_DOUBLE("enum.instantcoursetype.two_month_double"),
	MONTH_INTENSIVE("enum.instantcoursetype.month_intensive");
	
	//havi, havi dupla, kéthavi, kéthavi dupla, havi intenzív
    
    String label;
    
    private InstantCourseType(String s) {
    	this.label = s;
    }       
    
    public String getLabel() { 
    	Locale locale = LocaleContextHolder.getLocale();    
    	return AppContextProvider_web.getApplicationContext().getMessage(label, null, locale);
    }


}
