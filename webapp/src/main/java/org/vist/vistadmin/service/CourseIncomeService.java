package org.vist.vistadmin.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.roo.addon.layers.service.RooService;
import org.vist.vistadmin.domain.CompletedClass;
import org.vist.vistadmin.domain.Course;
import org.vist.vistadmin.domain.CourseIncome;
import org.vist.vistadmin.domain.Discount;
import org.vist.vistadmin.domain.Student;
import org.vist.vistadmin.domain.Teacher;

@RooService(domainTypes = { org.vist.vistadmin.domain.CourseIncome.class })
public interface CourseIncomeService {
	
	public List<CourseIncome> findByCourse(Course course);
	
	public List<CourseIncome> findByStudent(Student student);
		
	public List<CourseIncome> findByCourseAndStudent(Course course, Student student);
	
	public List<CourseIncome> findByCourseStudentDiscountId(Long discountId);
	
	public List<CourseIncome> findByCompletedClassId(Long completedClassId);
	
	public List<CourseIncome> findByPaymentDateBetweenAndPayed(Date from, Date till, boolean payed);
	
	public List<CourseIncome> findDeadlineDateIsNull() ;
	
	public List<CourseIncome> findDeadlineDateIn2Weeks() ;
	
	public List<CourseIncome> findDeadlineDateOver() ;
	
	public List<CourseIncome> findByDeadLineDateBetweenAndPayed(Date from, Date till, boolean payed);
}
