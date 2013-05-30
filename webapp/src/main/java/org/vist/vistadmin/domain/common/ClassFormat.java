package org.vist.vistadmin.domain.common;

import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;
import org.vist.vistadmin.util.AppContextProvider_web;


public enum ClassFormat {

    NORMAL("enum.classformat.normal"), 
    ONLINE("enum.classformat.online"),
    INSTANT("enum.classformat.instant");
    
    String label;
    
    private ClassFormat(String s) {
    	this.label = s;
    }       
    
    public String getLabel() { 
    	Locale locale = LocaleContextHolder.getLocale();    
    	return AppContextProvider_web.getApplicationContext().getMessage(label, null, locale);
    }

    public void setLabel() {    	
    }

}
