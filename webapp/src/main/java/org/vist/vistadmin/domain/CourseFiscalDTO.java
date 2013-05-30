package org.vist.vistadmin.domain;

import java.util.Date;

public class CourseFiscalDTO {

	private Date periodStartDate;
	
	private Date periodEndDate;
	
	private Course course;
		
	private boolean isError;
	
	private String errorMsg;

	private boolean hasCourseFlexibleClassTime;	

	private double numberOfEstimatedClassesForPeriod;
	
	private int numberOfstudents;
	
	private double teacherFeeForPeriod;
	
	private double studentFeeForPeriod;
	
	private double totalEstimatedGrossProfitForPerion;
	
	private double totalEstimatedNettProfitForPerion;

	private int numberOfDaysInPeriod;

	private int totalNumberOfClasses;
	
	private int estimatedTotalNumberOfClasses;
	
	private int numberOfcompletedCalsses;

	private double totalGrossProfitByCompClassForPerion;
	
	private double totalNettProfitByCompClassForPerion; 
	
	public Date getPeriodStartDate() {
		return periodStartDate;
	}

	public void setPeriodStartDate(Date periodStartDate) {
		this.periodStartDate = periodStartDate;
	}

	public Date getPeriodEndDate() {
		return periodEndDate;
	}

	public void setPeriodEndDate(Date periodEndDate) {
		this.periodEndDate = periodEndDate;
	}

	public double getTotalGrossProfitByCompClassForPerion() {
		return totalGrossProfitByCompClassForPerion;
	}

	public void setTotalGrossProfitByCompClassForPerion(double totalGrossProfitByCompClassForPerion) {
		this.totalGrossProfitByCompClassForPerion = totalGrossProfitByCompClassForPerion;
	}

	public double getTotalNettProfitByCompClassForPerion() {
		return totalNettProfitByCompClassForPerion;
	}

	public void setTotalNettProfitByCompClassForPerion(double totalNettProfitByCompClassForPerion) {
		this.totalNettProfitByCompClassForPerion = totalNettProfitByCompClassForPerion;
	}


	public int getEstimatedTotalNumberOfClasses() {
		return estimatedTotalNumberOfClasses;
	}

	public void setEstimatedTotalNumberOfClasses(int estimatedTotalNumberOfHours) {
		this.estimatedTotalNumberOfClasses = estimatedTotalNumberOfHours;
	}

	public int getTotalNumberOfClasses() {
		return totalNumberOfClasses;
	}

	public void setTotalNumberOfClasses(int totalNumberOfHours) {
		this.totalNumberOfClasses = totalNumberOfHours;
	}

	public boolean isError() {
		return isError;
	}

	public void setError(boolean isError) {
		this.isError = isError;
	}

	public double getNumberOfEstimatedClassesForPeriod() {
		return numberOfEstimatedClassesForPeriod;
	}

	public void setNumberOfEstimatedClassesForPeriod(double numberOfClassesForPeriod) {
		this.numberOfEstimatedClassesForPeriod = numberOfClassesForPeriod;
	}

	public int getNumberOfstudents() {
		return numberOfstudents;
	}

	public void setNumberOfstudents(int numberOfstudents) {
		this.numberOfstudents = numberOfstudents;
	}

	public double getTeacherFeeForPeriod() {
		return teacherFeeForPeriod;
	}

	public void setTeacherFeeForPeriod(double teacherFeeForPeriod) {
		this.teacherFeeForPeriod = teacherFeeForPeriod;
	}

	public double getStudentFeeForPeriod() {
		return studentFeeForPeriod;
	}

	public void setStudentFeeForPeriod(double studentFeeForPeriod) {
		this.studentFeeForPeriod = studentFeeForPeriod;
	}

	public int getNumberOfDaysInPeriod() {
		return numberOfDaysInPeriod;
	}

	public void setNumberOfDaysInPeriod(int numberOfDaysInPeriod) {
		this.numberOfDaysInPeriod = numberOfDaysInPeriod;
	}
	

	
	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
	public boolean isHasCourseFlexibleClassTime() {
		return hasCourseFlexibleClassTime;
	}

	public void setHasCourseFlexibleClassTime(boolean hasCourseFlexibleClassTime) {
		this.hasCourseFlexibleClassTime = hasCourseFlexibleClassTime;
	}
	
	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	
	public double getTotalEstimatedGrossProfitForPerion() {
		return totalEstimatedGrossProfitForPerion;
	}

	public void setTotalEstimatedGrossProfitForPerion(
			double totalEstimatedGrossProfitForPerion) {
		this.totalEstimatedGrossProfitForPerion = totalEstimatedGrossProfitForPerion;
	}

	public double getTotalEstimatedNettProfitForPerion() {
		return totalEstimatedNettProfitForPerion;
	}

	public void setTotalEstimatedNettProfitForPerion(
			double totalEstimatedNettProfitForPerion) {
		this.totalEstimatedNettProfitForPerion = totalEstimatedNettProfitForPerion;
	}

	public int getNumberOfCompletedClasses() {
		return numberOfcompletedCalsses;
	}

	public void setNumberOfCompletedClasses(int numberOfcompletedCalsses) {
		this.numberOfcompletedCalsses = numberOfcompletedCalsses;
	}
	
}
