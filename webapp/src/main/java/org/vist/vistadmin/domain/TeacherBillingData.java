package org.vist.vistadmin.domain;

import javax.persistence.Embedded;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaEntity
public class TeacherBillingData {

    @NotNull
    private String name;
    
    @NotNull
    @OneToOne
    private Address address;

    @NotNull
    private String taxNumber;

    private String companyNumber;

    private String accountNumber;

    private Boolean VAT;
    
    public boolean isEmpty() {
    	if((address == null || address.isEmpty()) && 
    			(name == null || name.equals("") || name.equals(",")) &&
    			(companyNumber == null || companyNumber.equals("") || companyNumber.equals(",")) &&
    			(taxNumber == null || taxNumber.equals("") || taxNumber.equals(",")) &&
    			(accountNumber == null || accountNumber.equals("") || accountNumber.equals(","))) {
    		return true;
    	}
    	return false;
    }
    
    public boolean getEmpty1() {
    	return isEmpty();
    }
}
