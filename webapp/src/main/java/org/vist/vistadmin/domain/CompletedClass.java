package org.vist.vistadmin.domain;

import java.util.Date;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;
 
@RooJavaBean
@RooToString
@RooJpaEntity
public class CompletedClass {

    @NotNull
    @ManyToOne
    private Course course;

    @NotNull
    @ManyToOne
    private Teacher teacher;

    @Min(1900L)
    @Max(2100L)
    @NotNull
    private int compYear;

    @Min(1L)
    @Max(12L)
    @NotNull
    private int compMonth;

    @Min(-1L)
    @Max(52L)
    @NotNull
    private int compWeek;
    
    private boolean containsTestClass;
    
    
	public int getCompWeek() {
		return compWeek;
	}

	public void setCompWeek(int compWeek) {
		this.compWeek = compWeek;
	}

	private int numberOfClasses;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date currDate;

    private Boolean payed;
    
    @Transient
    private int expense;

    private String billOrderNumber;
    
    
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date deadlineDate;
    
	@Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date paymentDate;

	
	public int getExpense() {
		return expense;
	}

	public void setExpense(int expense) {
		this.expense = expense;
	}
	

    public String getBillOrderNumber() {
		return billOrderNumber;
	}

	public void setBillOrderNumber(String billOrderNumber) {
		this.billOrderNumber = billOrderNumber;
	}

	public Date getDeadlineDate() {
		return deadlineDate;
	}

	public void setDeadlineDate(Date deadlineDate) {
		this.deadlineDate = deadlineDate;
	}


	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public boolean isContainsTestClass() {
		return containsTestClass;
	}

	public void setContainsTestClass(boolean containsTestClass) {
		this.containsTestClass = containsTestClass;
	}

	
}
