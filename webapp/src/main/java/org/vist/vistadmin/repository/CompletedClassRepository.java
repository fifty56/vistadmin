package org.vist.vistadmin.repository;

import java.util.Date;
import java.util.List;

import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;
import org.vist.vistadmin.domain.CompletedClass;
import org.vist.vistadmin.domain.Course;
import org.vist.vistadmin.domain.CourseTeacher;
import org.vist.vistadmin.domain.Teacher;

@RooJpaRepository(domainType = CompletedClass.class)
public interface CompletedClassRepository {
	
	public List<CompletedClass> findByCourse(Course course);
	
	public List<CompletedClass> findByTeacher(Teacher teacher);
	
	public List<CompletedClass> findByTeacherAndCourse(Teacher teacher, Course course);
	
	public List<CompletedClass> findByCourseAndCompYearAndCompMonth(Course course, int year, int month);
	
	public List<CompletedClass> findByTeacherAndCourseAndCompYearAndCompMonth(Teacher teacher, Course course, int year, int month);
	
	public List<CompletedClass> findByTeacherAndCourseAndCompYearAndCompMonthAndCompWeek(Teacher teacher, Course course, int year, int month, int week);
	
	public List<CompletedClass> findByDeadlineDateIsNullAndPayed(boolean payed);
	
	public List<CompletedClass> findByDeadlineDateLessThanAndPayed(Date deadlineDate, boolean payed);
	
	public List<CompletedClass> findByDeadlineDateGreaterThanAndPayed(Date deadlineDate, boolean payed);
	
	public List<CompletedClass> findByPaymentDateBetweenAndPayed(Date from, Date till, boolean payed);
	
	public List<CompletedClass> findByDeadlineDateBetweenAndPayed(Date from, Date till, boolean payed);
}
