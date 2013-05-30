package org.vist.vistadmin.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vist.vistadmin.domain.CompletedClass;
import org.vist.vistadmin.domain.Course;
import org.vist.vistadmin.domain.CourseIncome;
import org.vist.vistadmin.domain.Discount;
import org.vist.vistadmin.domain.Student;
import org.vist.vistadmin.domain.Teacher;
import org.vist.vistadmin.domain.common.DiscountType;
import org.vist.vistadmin.repository.CourseIncomeRepository;


public class CourseIncomeServiceImpl implements CourseIncomeService {
	
	private static Logger logger = LoggerFactory.getLogger(CourseIncomeServiceImpl.class);
	
	public List<CourseIncome> findByCourse(Course course) {
		return courseIncomeRepository.findByCourse(course);	
	}
	
	public List<CourseIncome> findByStudent(Student student) {
		return courseIncomeRepository.findByStudent(student);
	}

	public List<CourseIncome> findByCourseAndStudent(Course course, Student student) {
		return courseIncomeRepository.findByCourseAndStudent(course, student);
	}
	
	public List<CourseIncome> findByCourseStudentDiscountId(Long discountId) {
		return courseIncomeRepository.findByCourseStudentDiscountId(discountId);
	}
	
	public List<CourseIncome> findByCompletedClassId(Long completedClassId) {
		return courseIncomeRepository.findByCompletedClassId(completedClassId);
	}
	
	
	public List<CourseIncome> findDeadlineDateIsNull() {
		return courseIncomeRepository.findByDeadlineDateIsNullAndPayed(false);								
	}
	
	public List<CourseIncome> findDeadlineDateIn2Weeks() {		
		Long now = System.currentTimeMillis();
		long inTwoWeeks = now + 1000 * 60 * 60 * 24 * 14;
		return courseIncomeRepository.findByDeadlineDateBetweenAndPayed(new Date(now), new Date(inTwoWeeks), false);
	}
	
	public List<CourseIncome> findDeadlineDateOver() {
		Long now = System.currentTimeMillis();
		return courseIncomeRepository.findByDeadlineDateLessThanAndPayed(new Date(now), false);
	}

	public List<CourseIncome> findByDeadLineDateBetweenAndPayed(Date from, Date till, boolean payed) {
		return courseIncomeRepository.findByDeadlineDateBetweenAndPayed(from, till, payed);		
	}
	
	public List<CourseIncome> findByPaymentDateBetweenAndPayed(Date from, Date till, boolean payed) {
		return courseIncomeRepository.findByPaymentDateBetweenAndPayed(from, till, payed);
	}
}
