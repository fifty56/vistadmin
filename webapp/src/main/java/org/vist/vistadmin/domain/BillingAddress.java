package org.vist.vistadmin.domain;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;
 
@RooJavaBean
@RooToString
@RooJpaEntity
public class BillingAddress {

    @NotNull
    private String name;
    
    @Cascade({CascadeType.ALL})
    @OneToOne
    private Address address;
    
    @Cascade({CascadeType.ALL})
    @OneToOne
    private Address postalAddress;
    
    public boolean isEmpty() {
    	if( (address == null || address.isEmpty()) && 
    		(postalAddress == null || postalAddress.isEmpty()) &&    	   	
    	    (name == null || name.equals(""))) {
    		return true;
    	}
    	return false;
    }
    
    public boolean getEmpty1() {
    	return isEmpty();
    }
}
