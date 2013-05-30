package org.vist.vistadmin.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;
import org.vist.vistadmin.domain.common.Languages;
import org.vist.vistadmin.domain.common.PersonStatus;

@RooJavaBean
@RooToString
@RooJpaEntity
@Table(
        name="TEACHER", 
        uniqueConstraints=
        {@UniqueConstraint(columnNames={"email_address"})}
    )
public class Teacher {

	 @Embedded
		@AttributeOverrides( {
		  @AttributeOverride(name="firstName", column = @Column(name="first_name") ),
		  @AttributeOverride(name="lastName", column = @Column(name="last_name") ),
		  @AttributeOverride(name="emailAddress", column = @Column(name="email_address") ),
		  @AttributeOverride(name="phoneNumber", column = @Column(name="phone_number") ),
		  @AttributeOverride(name="bornDate", column = @Column(name="born_date") ),
		})  
    private PersonalData personalData;

    @OneToOne
    private Address address;
         
    @OneToOne
    private TeacherBillingData teacherBillingData;  

    @ElementCollection
    private Set<Languages> languages = new HashSet<Languages>(); 
        
	@Transient
    private double currExpense;

    @Transient
    private double allExpense;
    
    @NotNull
    @Enumerated
    private PersonStatus status;
    
    @Transient
    private String uriVal = "teachers/" + getId();
            
    public String getUriVal() {
		return "teachers/" + getId();
	}
    
    @Transient
    private double remExpense;
    
    private String comment;
    
    @Transient
    private double remAllExpense;        
    
    public double getRemAllExpense() {
		return remAllExpense;
	}

	public void setRemAllExpense(double remAllExpense) {
		this.remAllExpense = remAllExpense;
	}

    
    public String getComment() {
		return comment;
	}


	public void setComment(String comment) {
		this.comment = comment;
	}


	public String getLabelString() {
    	return personalData.getLastName() + " " + personalData.getFirstName();
    }
    

	public double getCurrExpense() {
		return currExpense;
	}


	public void setCurrExpense(double currExpense) {
		this.currExpense = currExpense;
	}


	public double getAllExpense() {
		return allExpense;
	}


	public void setAllExpense(double allExpense) {
		this.allExpense = allExpense;
	}


	public double getRemExpense() {
		return remExpense;
	}


	public void setRemExpense(double remExpense) {
		this.remExpense = remExpense;
	}

}
