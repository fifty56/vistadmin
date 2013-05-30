package org.vist.vistadmin.domain.common;

import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;
import org.vist.vistadmin.util.AppContextProvider_web;


public enum Languages {

	//ENGLISH, GERMAN,  FRENCH, ITALIAN, SPANISH, JAPAN, TURKISH, CROAT, RUSSIAN, NETHERLANDS, ESPERANTO;
	
	
    ENGLISH("enum.language.english"), 
    GERMAN("enum.language.german"),
    FRENCH("enum.language.french"), 
    ITALIAN("enum.language.italian"), 
    SPANISH("enum.language.spanish"),
    JAPAN("enum.language.japan"), 
    TURKISH("enum.language.turkish"), 
    CROAT("enum.language.croat"), 
    RUSSIAN("enum.language.russian"), 
    NETHERLANDS("enum.language.neatherlands"), 
    ESPERANTO("enum.language.esperanto"),
    HUNGARIAN("enum.language.hungarian");
    
    String label;
    
    private Languages(String s) {
    	this.label = s;
    }       
    
    public String getLabel() { 
    	Locale locale = LocaleContextHolder.getLocale();    
    	return AppContextProvider_web.getApplicationContext().getMessage(label, null, locale);
    }


    
}
