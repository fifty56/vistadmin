package org.vist.vistadmin.service;

import java.util.Date;
import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;
import org.vist.vistadmin.domain.Course;
import org.vist.vistadmin.domain.CourseTeacher;
import org.vist.vistadmin.domain.Teacher;

@RooService(domainTypes = { org.vist.vistadmin.domain.CourseTeacher.class })
public interface CourseTeacherService {

	public List<CourseTeacher> findByCourse(Course course);
	
	public List<CourseTeacher> findByTeacher(Teacher teacher);
	
	public List<CourseTeacher> findByTeacherAndCourse(Teacher teacher, Course course);
	
	public List<CourseTeacher> findByCourseAndStartDateBetweenAndNotTeacher(Course course, Date startDate, Date endDate, Teacher teacher);
	
	public List<CourseTeacher> findByCourseAndEndDateBetweenAndNotTeacher(Course course, Date startDate, Date endDate, Teacher teacher);
	
	public List<CourseTeacher> isValidDatesNotOverlap(CourseTeacher courseTeacher);
	
	public List<CourseTeacher> getCurrentTeachersByDateAndCourse(Course course)  throws CourseWithMoreCurrentTeachers;
	
	public List<CourseTeacher> getCurrentTeachersByDateAndCourse(Course course, Date date) throws CourseWithMoreCurrentTeachers;
}
