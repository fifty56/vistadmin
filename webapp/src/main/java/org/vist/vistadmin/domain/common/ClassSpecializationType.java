package org.vist.vistadmin.domain.common;

import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;
import org.vist.vistadmin.util.AppContextProvider_web;


public enum ClassSpecializationType {
    
    BASIC("enum.classspecializationtype.basic"),
    BUSINESS("enum.classspecializationtype.business"),
    PROFESSIONAL("enum.classspecializationtype.professional");
    
    String label;
    
    private ClassSpecializationType(String s) {
    	this.label = s;
    }       
    
    public String getLabel() { 
    	Locale locale = LocaleContextHolder.getLocale();    
    	return AppContextProvider_web.getApplicationContext().getMessage(label, null, locale);
    }
}
