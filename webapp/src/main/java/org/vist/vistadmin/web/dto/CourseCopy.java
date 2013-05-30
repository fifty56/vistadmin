package org.vist.vistadmin.web.dto;

import java.util.Date;
import java.util.List;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.vist.vistadmin.domain.Course;
import org.vist.vistadmin.domain.CourseStudent;
import org.vist.vistadmin.domain.CourseStudentDiscount;
import org.vist.vistadmin.domain.CourseTeacher;

public class CourseCopy {

	//spring multiple checkbox
	//http://static.springsource.org/spring/docs/current/spring-framework-reference/html/mvc.html
	
	private Long id;
	
	@NotNull
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(style = "yyyy-MM-dd")
	private Date newStartDate;
	
	public Date getNewStartDate() {
		return newStartDate;
	}

	public void setNewStartDate(Date newStartDate) {
		this.newStartDate = newStartDate;
	}

	public Date getNewEndDate() {
		return newEndDate;
	}

	public void setNewEndDate(Date newEndDate) {
		this.newEndDate = newEndDate;
	}

	@NotNull
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(style = "yyyy-MM-dd")
	private Date newEndDate;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@NotNull
	private Course course;
	
	private int version;
	
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		id = course.getId();
		this.course = course;
	}



	public String[] getCourseStudents() {
		return courseStudents;
	}

	public void setCourseStudents(String[] courseStudents) {
		this.courseStudents = courseStudents;
	}

	public String[] getCourseStudentDiscounts() {
		return courseStudentDiscounts;
	}

	public void setCourseStudentDiscounts(String[] courseStudentDiscounts) {
		this.courseStudentDiscounts = courseStudentDiscounts;
	}

	private String[] courseTeacher;
	
	public String[] getCourseTeacher() {
		return courseTeacher;
	}

	public void setCourseTeacher(String[] courseTeacher) {
		this.courseTeacher = courseTeacher;
	}

	private String[] courseStudents;
	
	private String[] courseStudentDiscounts;
}
