package org.vist.vistadmin.domain;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;
import org.vist.vistadmin.domain.common.ClassFormat;
import org.vist.vistadmin.domain.common.ClassLevels;
import org.vist.vistadmin.domain.common.ClassSpecializationType;
import org.vist.vistadmin.domain.common.ClassStatus;
import org.vist.vistadmin.domain.common.ClassType;
import org.vist.vistadmin.domain.common.Data;
import org.vist.vistadmin.domain.common.InstantCourseFormat;
import org.vist.vistadmin.domain.common.InstantCourseType;
import org.vist.vistadmin.domain.common.Languages;
import org.vist.vistadmin.domain.common.Room;
import org.vist.vistadmin.domain.common.SelectOptionList;
import org.vist.vistadmin.web.CourseController;

@RooJavaBean
@RooToString
@RooJpaEntity
public class Course {

	@NotNull	
	private String courseId;

	@NotNull
	@Enumerated
	private Languages lang;

	@NotNull
	@Enumerated
	private ClassLevels courseLevel;

	@NotNull
	@Enumerated
	private ClassSpecializationType classSpecializationType;
	
	@NotNull
	@Enumerated
	private ClassFormat courseFormat;

	@NotNull
	@Enumerated
	private ClassType courseType;

	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(style = "yyyy-MM-dd")
	private Date creationDate;
	
	@Transient
	private int weeklyNumberOfClasses;	

	private String timeOfClasses;
	
	@NotNull
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(style = "yyyy-MM-dd")
	private Date startDate;

	@NotNull
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(style = "yyyy-MM-dd")
	private Date endDate;

	@NotNull
	@Enumerated
	private ClassStatus status;

	@NotNull
	@Enumerated
	private Room room;	  
	
	@NotNull
	private int sumOfClasses;

	private String book;

	private String comment;

	@NotNull
	private Boolean payPerClasses;

	private double moneyPerStudent;

	private int teacherClassPrice;

	@NotNull
	private Boolean Vat;
	
	@NotNull
	private Boolean company;


	private InstantCourseFormat instantCourseFormat; 
	
	private InstantCourseType instantCourseType; 
	
    public Boolean getCompany() {
		return company;
	}

	public void setCompany(Boolean company) {
		this.company = company;
	}

	@Transient
    private String uriVal = "courses/" + getId();
            
    public String getUriVal() {
		return "courses/" + getId();
	}

	@Transient
	private double currGrossIncomeByCI;

	@Transient
	private double currGrossExpenseByCC;

	@Transient
	private double allGrossIncome;

	@Transient
	private double allNetIncome;
	
	
	@Transient
	private double allEstimatedNetExpense;

	@Transient
	private double allEstimatedGrossExpense;
	
	@Transient
	private double allNetExpense;

	@Transient
	private double allGrossExpenseByCC;

	@Transient
	private double remCurrGrossIncomeByCI;
	
	
	@Transient	
	private double remGrossIncome;

	@Transient
	private double remNetIncome;
	
	@Transient
	private double remCurrGrossExpenseByCC;

	@Transient
	private double remCurrNetExpenseByCC;	
	
	@Transient
	private double remNetAllExpense;

	@Transient	
	private double totalGrossBalance;

	@Transient
	private double totalNetBalance;
	
	@Transient
	private boolean flexibleClassTime;
	
	
	@Transient
	private List classTimesList;
	
	@Transient
	private double estimatedNumberOfAllCalsses;

	
	@Transient
	private double estimatedTotalGrossAmount;
	
	@Transient
	private double estimatedTotalNetAmount;
	
	@Transient
	private double sumOfCompletedClasses;

	@Transient
	private double remNetExpense;
					  
	public double getRemNetExpense() {
		return remNetExpense;
	}

	public void setRemNetExpense(double remNetExpense) {
		this.remNetExpense = remNetExpense;
	}

	public double getSumOfCompletedClasses() {
		return sumOfCompletedClasses;
	}

	public void setSumOfCompletedClasses(double sumOfCompletedClasses) {
		this.sumOfCompletedClasses = sumOfCompletedClasses;
	}

	@Transient
	private double estimatedNumberOfCurrentCalsses;
	
	public List getClassTimesList() {
		return classTimesList;
	}

	public void setClassTimesList(List classTimesList) {
		this.classTimesList = classTimesList;
	}

	@Transient
    private Set<CourseStudentDiscount> discounts;
	
	public Set<CourseStudentDiscount> getDiscounts() {
		return discounts;
	}

	public void setDiscounts(Set<CourseStudentDiscount> discounts) {
		this.discounts = discounts;
	}

	public double getRemNetAllExpense() {
		return remNetAllExpense;
	}

	public void setRemNetAllExpense(double remAllExpense) {
		this.remNetAllExpense = remAllExpense;
	}

	public String getLabelString() {
		return lang.getLabel() + " " + courseFormat.getLabel() + " " + courseLevel;
	}
	
	public double getTotalGrossBalance() {
		return totalGrossBalance;
	}

	public double getTotalNetBalance() {
		return totalNetBalance;
	}
	
	public Boolean getVat() {
		return Vat;
	}

	public void setVat(Boolean vAT) {
		Vat = vAT;
	}
	
	public void setTotalGrossBalance(double totalGBalance) {
		this.totalGrossBalance = totalGBalance;
	}

	public void setTotalNetBalance(double totalNBalance) {
		this.totalNetBalance = totalNBalance;
	}
	
	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}
	
	public int getWeeklyNumberOfClasses() {
		return weeklyNumberOfClasses;
	}

	public void setWeeklyNumberOfClasses(int weeklyNumberOfClasses) {
		this.weeklyNumberOfClasses = weeklyNumberOfClasses;
	}

	public String getTimeOfClasses() {
		return timeOfClasses;
	}

	public void setTimeOfClasses(String timeOfClasses) {
		this.timeOfClasses = timeOfClasses;
	}

	public boolean isFlexibleClassTime() {
		return flexibleClassTime;
	}

	public void setFlexibleClassTime(boolean flexibleClassTime) {
		this.flexibleClassTime = flexibleClassTime;
	}
	

	public InstantCourseFormat getInstantCourseFormat() {
		return instantCourseFormat;
	}

	public void setInstantCourseFormat(InstantCourseFormat instantCourseFormat) {
		this.instantCourseFormat = instantCourseFormat;
	}

	public InstantCourseType getInstantCourseType() {
		return instantCourseType;
	}

	public void setInstantCourseType(InstantCourseType instantCourseType) {
		this.instantCourseType = instantCourseType;
	}
	
	

	public ClassSpecializationType getClassSpecializationType() {
		return classSpecializationType;
	}

	public void setClassSpecializationType(
			ClassSpecializationType classSpecializationType) {
		this.classSpecializationType = classSpecializationType;
	}
	
	public double getAllGrossExpenseByCC() {
		return allGrossExpenseByCC;
	}

	public void setAllGrossExpenseByCC(double allExpenseWithTeacherVAT) {
		this.allGrossExpenseByCC = allExpenseWithTeacherVAT;
	}
	

	public double getAllEstimatedNetExpense() {
		return allEstimatedNetExpense;
	}

	public void setAllEstimatedNetExpense(
			double allEstimatedExpenseWithoutTeacherVAT) {
		this.allEstimatedNetExpense = allEstimatedExpenseWithoutTeacherVAT;
	}

	public double getAllEstimatedGrossExpense() {
		return allEstimatedGrossExpense;
	}

	public void setAllEstimatedGrossExpense(
			double allEstimatedExpenseWithTeacherVAT) {
		this.allEstimatedGrossExpense = allEstimatedExpenseWithTeacherVAT;
	}

	public double getEstimatedNumberOfAllCalsses() {
		return estimatedNumberOfAllCalsses;
	}

	public void setEstimatedNumberOfAllCalsses(double estimatedNumberOfAllCalsses) {
		this.estimatedNumberOfAllCalsses = estimatedNumberOfAllCalsses;
	}

	public double getEstimatedNumberOfCurrentCalsses() {
		return estimatedNumberOfCurrentCalsses;
	}

	public void setEstimatedNumberOfCurrentCalsses(
			double estimatedNumberOfCurrentCalsses) {
		this.estimatedNumberOfCurrentCalsses = estimatedNumberOfCurrentCalsses;
	}

	public double getEstimatedTotalGrossAmount() {
		return estimatedTotalGrossAmount;
	}

	public void setEstimatedTotalGrossAmount(double estimatedTotalGrossAmount) {
		this.estimatedTotalGrossAmount = estimatedTotalGrossAmount;
	}

	public double getEstimatedTotalNetAmount() {
		return estimatedTotalNetAmount;
	}

	public void setEstimatedTotalNetAmount(double estimatedTotalNetAmount) {
		this.estimatedTotalNetAmount = estimatedTotalNetAmount;
	}

	public double getRemCurrGrossIncomeByCI() {
		return remCurrGrossIncomeByCI;
	}

	public void setRemCurrGrossIncomeByCI(double remCurrGrossIncome) {
		this.remCurrGrossIncomeByCI = remCurrGrossIncome;
	}

	public double getAllNetIncome() {
		return getVat() ? allGrossIncome / Data.VAT_PERCENT : allGrossIncome ;
	}

	public double getRemNetIncome() {
		return getVat() ? remGrossIncome / Data.VAT_PERCENT : remGrossIncome ;		
	}

	public double getRemCurrNetExpenseByCC() {
		return remCurrNetExpenseByCC;
	}
				
	public void setRemCurrNetExpenseByCC(double remCurrNetExpense) {
		this.remCurrNetExpenseByCC = remCurrNetExpense;
	}

}
