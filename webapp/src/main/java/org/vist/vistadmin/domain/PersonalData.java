package org.vist.vistadmin.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@Embeddable
public class PersonalData {
 
    @NotNull
    private String firstName;

    @NotNull
    private String lastName;
            
    private String emailAddress;

    private String phoneNumber;

    private String motherName;
    
    //only used for student's parent address
    private String addressStr;
    
	public String getAddressStr() {
		
		return addressStr;
	}

	public void setAddressStr(String addressStr) {
		this.addressStr = addressStr;
	}

	@Temporal(TemporalType.DATE)
    @DateTimeFormat(style = "yyyy-MM-dd")
    private Date bornDate;
    
	private String bornPlace;

	
	public boolean isEmpty() {
    	if((firstName == null || firstName.equals("")) &&
    	   (lastName == null || lastName.equals("")) &&
    	   (emailAddress == null || emailAddress.equals("")) &&
    	   (phoneNumber == null || phoneNumber.equals("")) &&
    	   (bornPlace == null || bornPlace.equals("")) &&
    	   (addressStr == null || addressStr.equals("")) &&
    	   (motherName == null || motherName.equals("")) &&
    	   bornDate == null) {
    		return true;
    	}
    	return false;
    }
    
    public boolean getEmpty1() {
    	return isEmpty();
    }
    
    public String getMotherName() {
		return motherName;
	}

	public void setMotherName(String motherName) {
		this.motherName = motherName;
	}

	
    public String getBornPlace() {
		return bornPlace;
	}

	public void setBornPlace(String bornPlace) {
		this.bornPlace = bornPlace;
	}

}
