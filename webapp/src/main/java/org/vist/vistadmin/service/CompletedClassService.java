package org.vist.vistadmin.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.roo.addon.layers.service.RooService;
import org.vist.vistadmin.domain.CompletedClass;
import org.vist.vistadmin.domain.Course;
import org.vist.vistadmin.domain.Teacher;

@RooService(domainTypes = { org.vist.vistadmin.domain.CompletedClass.class })
public interface CompletedClassService {
	
	public List<CompletedClass> findByCourse(Course course);
	
	public List<CompletedClass> findByTeacher(Teacher teacher);
	
	public List<CompletedClass> findByTeacherAndCourseAndCompYearAndCompMonthAndCompWeek(Teacher teacher, Course course, int year, int month, int week);
	
	public List<CompletedClass> findByTeacherAndCourse(Teacher teacher, Course course);
	
	public List<CompletedClass> findByTeacherAndCourseAndCompYearAndCompMonth(Teacher teacher, Course course, int year, int month);
	
	public List<CompletedClass> findDeadlineDateIsNull() ;
	
	public List<CompletedClass> findDeadlineDateIn2Weeks() ;
	
	public List<CompletedClass> findByPaymentDateBetweenAndPayed(Date from, Date till, boolean payed);
	
	public List<CompletedClass> findByCourseAndCompYearAndCompMonth(Course course, int year, int month);
	
	public List<CompletedClass> findDeadlineDateOver() ;
	
	public void saveCompletedClass(CompletedClass completedClass, HttpServletRequest request);
	
	public List<CompletedClass> findByDeadlineDateBetweenAndPayed(Date from, Date till, boolean payed);
}
