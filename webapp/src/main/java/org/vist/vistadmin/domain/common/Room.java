package org.vist.vistadmin.domain.common;

import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;
import org.vist.vistadmin.util.AppContextProvider_web;


public enum Room {

    R1("enum.room.1"), 
    R2("enum.room.2"),
    R3("enum.room.3"),
    R4("enum.room.4"),
    R5("enum.room.5");

    String label;
    
    private Room(String s) {
    	this.label = s;
    }       
    
    public String getLabel() { 
    	Locale locale = LocaleContextHolder.getLocale();    
    	return AppContextProvider_web.getApplicationContext().getMessage(label, null, locale);
    }


}
