package org.vist.vistadmin.repository;

import java.util.Date;
import java.util.List;

import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;
import org.vist.vistadmin.domain.CompletedClass;
import org.vist.vistadmin.domain.Course;
import org.vist.vistadmin.domain.CourseIncome;
import org.vist.vistadmin.domain.Student;
import org.vist.vistadmin.domain.Teacher;

@RooJpaRepository(domainType = CourseIncome.class)
public interface CourseIncomeRepository {
	
	public List<CourseIncome> findByCourse(Course course);
	
	public List<CourseIncome> findByStudent(Student student);
	
	public List<CourseIncome> findByCourseStudentDiscountId(Long discountId);
	
	public List<CourseIncome> findByCompletedClassId(Long completedClassId);
	
	public List<CourseIncome> findByCourseAndStudent(Course course, Student student);
	
	public List<CourseIncome> findByDeadlineDateIsNullAndPayed(boolean payed);
	
	public List<CourseIncome> findByDeadlineDateLessThanAndPayed(Date deadlineDate, boolean payed);
	
	public List<CourseIncome> findByDeadlineDateGreaterThanAndPayed(Date deadlineDate, boolean payed);
	
	public List<CourseIncome> findByDeadlineDateBetweenAndPayed(Date from, Date till, boolean payed);
	
	public List<CourseIncome> findByPaymentDateBetweenAndPayed(Date from, Date till, boolean payed);
	
}
