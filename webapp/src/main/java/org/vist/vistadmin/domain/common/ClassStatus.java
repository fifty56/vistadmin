package org.vist.vistadmin.domain.common;

import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;
import org.vist.vistadmin.util.AppContextProvider_web;


public enum ClassStatus {

    DRAFT("enum.classstatus.draft"), 
    STARTED("enum.classstatus.started"),
    FINISHED("enum.classstatus.finished"),
    ARCHIVED("enum.classstatus.archived");
    
    String label;
    
    private ClassStatus(String s) {
    	this.label = s;
    }       
    
    public String getLabel() { 
    	Locale locale = LocaleContextHolder.getLocale();    
    	return AppContextProvider_web.getApplicationContext().getMessage(label, null, locale);
    }

}
