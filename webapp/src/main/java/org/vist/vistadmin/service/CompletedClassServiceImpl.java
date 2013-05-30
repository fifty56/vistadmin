package org.vist.vistadmin.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vist.vistadmin.domain.CompletedClass;
import org.vist.vistadmin.domain.Course;
import org.vist.vistadmin.domain.CourseIncome;
import org.vist.vistadmin.domain.CourseStudent;
import org.vist.vistadmin.domain.CourseStudentDiscount;
import org.vist.vistadmin.domain.Discount;
import org.vist.vistadmin.domain.Student;
import org.vist.vistadmin.domain.Teacher;
import org.vist.vistadmin.domain.common.ClassStatus;
import org.vist.vistadmin.domain.common.DiscountType;
import org.vist.vistadmin.util.FlashScopeFilter;
import org.vist.vistadmin.web.CompletedClassController;


public class CompletedClassServiceImpl implements CompletedClassService {
	
	private static Logger LOGGER = LoggerFactory.getLogger(CompletedClassServiceImpl.class); 
	
	@Autowired
	CourseIncomeService courseIncomeService;
	
	
	@Autowired
	CourseStudentService courseStudentService;
	
	@Autowired
	CourseStudentDiscountService courseStudentDiscountService;
	
	public List<CompletedClass> findByCourse(Course course) {
		return completedClassRepository.findByCourse(course);
	}
	
	public List<CompletedClass> findByTeacher(Teacher teacher) {
		return completedClassRepository.findByTeacher(teacher);
	}
	
	public void saveCompletedClass(CompletedClass completedClass, HttpServletRequest request) {
		CompletedClass savedCompletedClass = completedClassRepository.save(completedClass);
		
		Course course = completedClass.getCourse();
		if(course.getPayPerClasses()) {						
			LOGGER.debug("saveCompletedClass called for a payPerClasses course, create courseIncomes");
			List<CourseStudent> courseStudentList = courseStudentService.findByCourse(course);
			for(CourseStudent courseStudent: courseStudentList) {
				Student student = courseStudent	.getStudent();
				
				CourseIncome courseIncome = new CourseIncome();
				courseIncome.setCourse(course);
				courseIncome.setStudent(student);
				int numOfClasses = completedClass.getNumberOfClasses();
				if(completedClass.isContainsTestClass() && numOfClasses > 0) {
					numOfClasses--;
				}
				double income = numOfClasses * course.getMoneyPerStudent();
				courseIncome.setAmount(income);
				courseIncome.setWeek(completedClass.getCompWeek());
				courseIncome.setMonth(completedClass.getCompMonth());
				courseIncome.setYear(completedClass.getCompYear());				
				courseIncome.setPayed(false);
				courseIncome.setCompletedClassId(savedCompletedClass.getId());
				LOGGER.debug("save courseIncome for student: " + courseStudent.getStudent().getLabelString());
				courseIncomeService.saveCourseIncome(courseIncome);
								
				List<CourseStudentDiscount> csdl = courseStudentDiscountService.findByCourseAndStudent(course, student);
				
				if(csdl != null && csdl.size() > 0) {
					for (CourseStudentDiscount csd : csdl) {
						Discount discount = csd.getDiscount();
						if(discount.getType().equals(DiscountType.PERCENTAGE)) {
							CourseIncome discountCourseIncome = new CourseIncome();
							discountCourseIncome.setCourse(course);
							discountCourseIncome.setStudent(student);
							int discIncome = -(int)(courseIncome.getAmount() * discount.getAmount() / 100);
							discountCourseIncome.setAmount(discIncome);
							discountCourseIncome.setWeek(courseIncome.getWeek());
							discountCourseIncome.setMonth(courseIncome.getMonth());
							discountCourseIncome.setYear(courseIncome.getYear());				
							discountCourseIncome.setPayed(false);
							discountCourseIncome.setFixDiscount(false);
							discountCourseIncome.setCourseStudentDiscountId(csd.getId());
							discountCourseIncome.setCompletedClassId(savedCompletedClass.getId());
							LOGGER.debug("save courseIncome for discount: " + discount.getLabelString() + ", for student: " + courseStudent.getStudent().getLabelString());
							courseIncomeService.saveCourseIncome(discountCourseIncome);
						}
					}
				}					
				request.setAttribute(FlashScopeFilter.FLASH_PREFIX + FlashScopeFilter.WARNING_MESSAGE_KEY, CompletedClassController.WARNING_CI_CREATED_FOR_CC);
			}					
		}		
        
    }
	
	
	
	public List<CompletedClass> findByTeacherAndCourseAndCompYearAndCompMonthAndCompWeek(Teacher teacher, Course course, int year, int month, int week) {
		return completedClassRepository.findByTeacherAndCourseAndCompYearAndCompMonthAndCompWeek(teacher, course, year, month, week);
	}
		
	public List<CompletedClass> findByTeacherAndCourse(Teacher teacher, Course course) {
		return completedClassRepository.findByTeacherAndCourse(teacher, course);
	}
	
	public List<CompletedClass> findByTeacherAndCourseAndCompYearAndCompMonth(Teacher teacher, Course course, int year, int month) {
		return completedClassRepository.findByTeacherAndCourseAndCompYearAndCompMonth(teacher, course, year, month);
	}
	
	public void deleteCompletedClass(CompletedClass completedClass) {
        completedClassRepository.delete(completedClass);        
        List<CourseIncome> courseIncomeList = courseIncomeService.findByCompletedClassId(completedClass.getId());
        if(courseIncomeList != null && courseIncomeList.size() > 0) {
	        for (CourseIncome courseIncome : courseIncomeList) {
	        	LOGGER.debug("delete related courseIncome if not payed, ID: " + courseIncome.getId());
				if(!courseIncome.isPayed()) {
					courseIncomeService.deleteCourseIncome(courseIncome);
				}
	        }
		}
    }
    
	public CompletedClass updateCompletedClass(CompletedClass completedClass) {
		CompletedClass updatedCompletedClass = completedClassRepository.save(completedClass);
		List<CourseIncome> courseIncomeList = courseIncomeService.findByCompletedClassId(completedClass.getId());
		double rootCourseIncomeAmout = -1;
		if(courseIncomeList != null && courseIncomeList.size() > 0) {
			LOGGER.debug("search root course income");
	        CourseIncome rootCourseIncome = null;
			for (CourseIncome courseIncome : courseIncomeList) {
				if(courseIncome.getCourseStudentDiscountId() == null) {
					LOGGER.debug("root course income: " + courseIncome.getId() + ", payed: " + courseIncome.isPayed());
					courseIncome.setCourse(completedClass.getCourse());
		        	courseIncome.setMonth(completedClass.getCompMonth());
		        	courseIncome.setYear(completedClass.getCompYear());
		        	courseIncome.setWeek(completedClass.getCompWeek());
		        	int numOfClasses = completedClass.getNumberOfClasses();
		        	if(completedClass.isContainsTestClass() && numOfClasses > 0) {
						numOfClasses--;
					}
		        	rootCourseIncomeAmout = numOfClasses * completedClass.getCourse().getMoneyPerStudent();
		        	if(!courseIncome.isPayed()) {
		        		courseIncome.setAmount(rootCourseIncomeAmout);
		        	}
		        	rootCourseIncome = courseIncomeService.updateCourseIncome(courseIncome);
					break;
				}
	        }
			for (CourseIncome courseIncome : courseIncomeList) {
	        	if(!courseIncome.getId().equals(rootCourseIncome.getId())) {
	        		LOGGER.debug("update course income under root, id: " + courseIncome.getId() + ", payed: " + courseIncome.isPayed());
	        		courseIncome.setCourse(completedClass.getCourse());
		        	courseIncome.setMonth(completedClass.getCompMonth());
		        	courseIncome.setYear(completedClass.getCompYear());
		        	courseIncome.setWeek(completedClass.getCompWeek());
		        	if(!courseIncome.isFixDiscount() && !courseIncome.isPayed()) {	        		
		        		CourseStudentDiscount csd = courseStudentDiscountService.findCourseStudentDiscount(courseIncome.getCourseStudentDiscountId());
		        		double discAmount = csd.getDiscount().getAmount();
		        		int newAmount = - (int)(rootCourseIncome.getAmount() * discAmount / 100);
		        		courseIncome.setAmount(newAmount);
		        	}
		        	courseIncomeService.updateCourseIncome(courseIncome);
	        	}
	        }
		}
        return updatedCompletedClass;
    }

	
	public List<CompletedClass> findDeadlineDateIsNull() {
		return completedClassRepository.findByDeadlineDateIsNullAndPayed(false);
	}
	
	public List<CompletedClass> findDeadlineDateIn2Weeks() {		
		Long now = System.currentTimeMillis();
		long inTwoWeeks = now + 1000 * 60 * 60 * 24 * 14;
		return completedClassRepository.findByDeadlineDateBetweenAndPayed(new Date(now), new Date(inTwoWeeks), false);
	}
	
	public List<CompletedClass> findDeadlineDateOver() {
		Long now = System.currentTimeMillis();
		return completedClassRepository.findByDeadlineDateLessThanAndPayed(new Date(now), false);
	}

	public List<CompletedClass> findByDeadlineDateBetweenAndPayed(Date from, Date till, boolean payed) {
		return completedClassRepository.findByDeadlineDateBetweenAndPayed(from, till, payed);
	}
	
	public List<CompletedClass> findByPaymentDateBetweenAndPayed(Date from, Date till, boolean payed) {
		return completedClassRepository.findByPaymentDateBetweenAndPayed(from, till, payed);
	}
	
	public List<CompletedClass> findByCourseAndCompYearAndCompMonth(Course course, int year, int month) {
		return completedClassRepository.findByCourseAndCompYearAndCompMonth(course, year, month);
	}
	
}
