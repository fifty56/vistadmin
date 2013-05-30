package org.vist.vistadmin.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;
import javax.persistence.AttributeOverrides;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;
import org.vist.vistadmin.domain.common.ClassLevels;
import org.vist.vistadmin.domain.common.Languages;
import org.vist.vistadmin.domain.common.PersonStatus;

@RooJavaBean
@RooToString
@RooJpaEntity
public class Student {

    @Embedded
    private PersonalData personalData;

    @ElementCollection
    private Set<Languages> languages = new HashSet<Languages>();
    
    @OneToOne
    private Address address;

    @Embedded
	@AttributeOverrides( {
	  @AttributeOverride(name="firstName", column = @Column(name="parent_firstName") ),
	  @AttributeOverride(name="lastName", column = @Column(name="parent_lastName") ),
	  @AttributeOverride(name="motherName", column = @Column(name="parent_motherName") ),
	  @AttributeOverride(name="emailAddress", column = @Column(name="parent_emailAddress") ),
	  @AttributeOverride(name="phoneNumber", column = @Column(name="parent_phoneNumber") ),
	  @AttributeOverride(name="bornDate", column = @Column(name="parent_bornDate") ),
	  @AttributeOverride(name="addressStr", column = @Column(name="parent_addressStr") ),	  
	  @AttributeOverride(name="bornPlace", column = @Column(name="parent_bornPlace") ),	  
	})  
    private PersonalData parentPersonalData;
  
    @OneToOne
    private BillingAddress billingAddress;
    
    @Embedded
    @AttributeOverrides( {
  	  @AttributeOverride(name="taxNumber", column = @Column(name="companyTaxNumber") ),
  	  @AttributeOverride(name="regNumber", column = @Column(name="companyRegNumber") )
    })  
    private StudentCompanyData companyData;
    

	@NotNull
    @Enumerated
    private PersonStatus status;
    
    private boolean company;
    
            

	@Transient
    private double currExpenses;
    
    @Transient
    private double allExpenses;
    
    @Transient
    private double remExpenses;
    
    @Transient
    private String uriVal = "students/" + getId();
            
    public String getUriVal() {
		return "students/" + getId();
	}

	@Transient
    private Set<CourseStudentDiscount> discounts;
    

	private String comment;    
    
    public String getLabelString() {
    	return personalData.getLastName() + " " + personalData.getFirstName();
    }
    
    public double getCurrExpenses() {
		return currExpenses;
	}

	public void setCurrExpenses(double currExpenses) {
		this.currExpenses = currExpenses;
	}

	public double getAllExpenses() {
		return allExpenses;
	}

	public void setAllExpenses(double allExpenses) {
		this.allExpenses = allExpenses;
	}

	public double getRemExpenses() {
		return remExpenses;
	}

	public void setRemExpenses(double remExpenses) {
		this.remExpenses = remExpenses;
	}	

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
    public Set<CourseStudentDiscount> getDiscounts() {
		return discounts;
	}

	public void setDiscounts(Set<CourseStudentDiscount> discounts) {
		this.discounts = discounts;
	}
	

	public boolean isCompany() {
		return company;
	}

	public void setCompany(boolean company) {
		this.company = company;
	}

    public StudentCompanyData getCompanyData() {
		return companyData;
	}

	public void setCompanyData(StudentCompanyData companyData) {
		this.companyData = companyData;
	}

	public String getCompanyName() {
		String retVal = "-";
		if(isCompany()) {
			if(billingAddress != null) {
				retVal = billingAddress.getName();
			} else {
				retVal = "!!!!!";
			}
		}
		return retVal;
	}
	
}
