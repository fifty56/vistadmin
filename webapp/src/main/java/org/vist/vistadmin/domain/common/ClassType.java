package org.vist.vistadmin.domain.common;

import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;
import org.vist.vistadmin.util.AppContextProvider_web;


public enum ClassType {
    
    SINGLE("enum.classtype.single"),
    DOUBLE("enum.classtype.double"),
    TRIPLE("enum.classtype.triple"),
    QUATTRO("enum.classtype.quattro"),
    GROUP("enum.classtype.group");
    
    String label;
    
    private ClassType(String s) {
    	this.label = s;
    }       
    
    public String getLabel() { 
    	Locale locale = LocaleContextHolder.getLocale();    
    	return AppContextProvider_web.getApplicationContext().getMessage(label, null, locale);
    }
}
