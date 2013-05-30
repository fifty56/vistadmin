package org.vist.vistadmin.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;
 
@RooJavaBean
@RooToString
@RooJpaEntity
public class CourseStudent {

    @ManyToOne
    private Course course;

    @ManyToOne
    private Student student;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date endDate;
    
    private String companyStudentNames;

	@Transient
	private List companyStudentNameList;
	
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
