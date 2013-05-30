package org.vist.vistadmin.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;
 
@RooJavaBean
@RooToString
@RooJpaEntity
public class CourseIncome {

    @NotNull
    @ManyToOne
    private Student student;

    @NotNull
    @ManyToOne
    private Course course;

    @NotNull
    private double amount;


	private int year;
    
    private int month;
    
    private int week;

    private boolean payed;
    
    private Long courseStudentDiscountId;
	
	private boolean fixDiscount = false;
	
	private Long completedClassId;
	
	@Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date deadlineDate;
	
	@Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date paymentDate;

	@Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date currDate;

	private String billOrderNumber;
	
    
    public String getBillOrderNumber() {
		return billOrderNumber;
	}

	public void setBillOrderNumber(String billOrderNumber) {
		this.billOrderNumber = billOrderNumber;
	}

	public int getWeek() {
		return week;
	}

	public void setWeek(int week) {
		this.week = week;
	}

	
	
	public Long getCourseStudentDiscountId() {
		return courseStudentDiscountId;
	}

	public void setCourseStudentDiscountId(Long discountId) {
		this.courseStudentDiscountId = discountId;
	}	
	
	
    public Long getCompletedClassId() {
		return completedClassId;
	}

	public void setCompletedClassId(Long completedClassId) {
		this.completedClassId = completedClassId;
	}

	public boolean isFixDiscount() {
		return fixDiscount;
	}

	public void setFixDiscount(boolean fixDiscount) {
		this.fixDiscount = fixDiscount;
	}

	public boolean isPayed() {
		return payed;
	}

	public void setPayed(boolean payed) {
		this.payed = payed;
	}	
    
    public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
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

	
	
}
