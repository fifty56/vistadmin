package org.vist.vistadmin.domain;

import javax.persistence.Enumerated;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;
import org.vist.vistadmin.domain.common.DiscountType;

@RooJavaBean
@RooToString
@RooJpaEntity
public class Discount {

    @NotNull
    private String name;

    @Enumerated
    private DiscountType type;

    @NotNull
    private double amount;

    private String comment;
    
    public String getLabelString() {
    	String suffix = "%";
    	if(type.equals(DiscountType.FIX_PRICE)) {
    		suffix = " Ft";
    	}
    	return type + ": " + amount + suffix;
    }
    
    @Transient
    private String uriVal = "discounts/" + getId();
            
    public String getUriVal() {
		return "discounts/" + getId();
	}
}
