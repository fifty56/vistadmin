package org.vist.vistadmin.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class AppContextProvider_web implements ApplicationContextAware { 
	
	private static ApplicationContext ctx = null;

	 public static ApplicationContext getApplicationContext() {
		 return ctx;
	 }

	 public void setApplicationContext(ApplicationContext ctx) throws BeansException {
		 this.ctx = ctx;
	 }

}
