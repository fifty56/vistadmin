package org.vist.vistadmin.domain;

import javax.persistence.Embeddable;
import javax.validation.constraints.Min;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaEntity
public class Address {
     
	
    private int zipAddress;
    
    private String city;

    private String street;
    
    public boolean isEmpty() {
    	if(zipAddress == 0 && 
    			(city == null || city.equals("") || city.equals(",")) && 
    			(street == null || street.equals("")  || street.equals(","))) {
    		return true;
    	}
    	return false;
    }
    
    public boolean getEmpty1() {
    	return isEmpty();
    }
    
    public String getAddressString() {
    	return zipAddress + ", " + city + " " + street;
    }
}
