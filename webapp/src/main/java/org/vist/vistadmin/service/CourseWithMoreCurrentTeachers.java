package org.vist.vistadmin.service;

import java.util.List;

import org.vist.vistadmin.domain.CourseTeacher;

public class CourseWithMoreCurrentTeachers extends Exception {

	List<CourseTeacher> courseTeacherList;
	
	public CourseWithMoreCurrentTeachers(List<CourseTeacher> courseTeacherList) {
		this.courseTeacherList = courseTeacherList;
	}
}
