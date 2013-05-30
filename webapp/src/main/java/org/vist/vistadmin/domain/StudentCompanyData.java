package org.vist.vistadmin.domain;

import java.util.List;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

@Embeddable            
public class StudentCompanyData {

	private String companyStudentNames;

    @Transient
	private List companyStudentNameList;


	public String getTaxNumber() {
		return taxNumber;
	}

	public void setTaxNumber(String taxNumber) {
		this.taxNumber = taxNumber;
	}

	public String getRegNumber() {
		return regNumber;
	}

	public void setRegNumber(String regNumber) {
		this.regNumber = regNumber;
	}

	private String taxNumber;
    
    private String regNumber;
    
	public String getCompanyStudentNames() {
		return companyStudentNames;
	}
	
	public void setCompanyStudentNames(String companyStudentNames) {
		this.companyStudentNames = companyStudentNames;
	}
	
	public List getCompanyStudentNameList() {
		return companyStudentNameList;
	}

	public void setCompanyStudentNameList(List companyStudentNameList) {
		this.companyStudentNameList = companyStudentNameList;
	}

	
}
