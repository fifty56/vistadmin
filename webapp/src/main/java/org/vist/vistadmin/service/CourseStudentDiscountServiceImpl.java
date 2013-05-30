package org.vist.vistadmin.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vist.vistadmin.domain.Course;
import org.vist.vistadmin.domain.CourseIncome;
import org.vist.vistadmin.domain.CourseStudent;
import org.vist.vistadmin.domain.CourseStudentDiscount;
import org.vist.vistadmin.domain.Discount;
import org.vist.vistadmin.domain.Student;
import org.vist.vistadmin.domain.common.DiscountType;
import org.vist.vistadmin.util.FlashScopeFilter;
import org.vist.vistadmin.web.CompletedClassController;


public class CourseStudentDiscountServiceImpl implements CourseStudentDiscountService {
	
	private static Logger LOGGER =  LoggerFactory.getLogger(CourseStudentDiscountServiceImpl.class);
	
	@Autowired
	CourseIncomeService courseIncomeService;
	
	public List<CourseStudentDiscount> findByCourse(Course course) {		
		return courseStudentDiscountRepository.findByCourse(course);
	}
	
	public List<CourseStudentDiscount> findByStudent(Student student) {		
		return courseStudentDiscountRepository.findByStudent(student);
	}
	
	public List<CourseStudentDiscount> findByDiscount(Discount discount) {
		return courseStudentDiscountRepository.findByDiscount(discount);
	}
	
	public List<CourseStudentDiscount> findByCourseAndStudent(Course course, Student student) {
		return courseStudentDiscountRepository.findByCourseAndStudent(course, student);
	}
	
	public List<CourseStudentDiscount> findByCourseAndStudentAndDiscount(Course course, Student student, Discount discount) {
		return courseStudentDiscountRepository.findByCourseAndStudentAndDiscount(course, student, discount);
	}
	
    public void saveCourseStudentDiscount(CourseStudentDiscount courseStudentDiscount, HttpServletRequest request) {
    	CourseStudentDiscount csd = courseStudentDiscountRepository.save(courseStudentDiscount);
    	
    	Course course = courseStudentDiscount.getCourse();
    	Discount discount = courseStudentDiscount.getDiscount();
    	Student student = courseStudentDiscount.getStudent();
    	LOGGER.debug("course payPerClass: " +course.getPayPerClasses()); 
    	if(course.getPayPerClasses()) {
    		if(discount.getType().equals(DiscountType.FIX_PRICE)) {
    			LOGGER.debug("discount is fix priced");
    			Date currDate = new Date();
    			CourseIncome courseIncome = new CourseIncome();
    			courseIncome.setAmount(-(int)discount.getAmount());
    			courseIncome.setCourse(course);
    			courseIncome.setPayed(false);
    			courseIncome.setMonth(currDate.getMonth() + 1);
    			courseIncome.setStudent(student);
    			courseIncome.setWeek(-1);
    			courseIncome.setYear(currDate.getYear() + 1900);
    			courseIncome.setCourseStudentDiscountId(csd.getId());
    			courseIncome.setFixDiscount(csd.getDiscount().getType().equals(DiscountType.FIX_PRICE));
    			courseIncomeService.saveCourseIncome(courseIncome);
    			LOGGER.debug("courseIncome created: " + courseIncome.getId());
    		} else {
    			LOGGER.debug("discount is not fix priced");
    			List<CourseIncome>	courseIncomeList = courseIncomeService.findByCourseAndStudent(course, student);    			
    			double disAmount = discount.getAmount();
    	    	if(courseIncomeList != null && courseIncomeList.size() > 0) {
    	    		LOGGER.debug("courseIncomeList size: " + courseIncomeList.size());
    	    		for (CourseIncome courseIncome2 : courseIncomeList) {
    	    			if(courseIncome2.getCourseStudentDiscountId() == null) {
			    			CourseIncome newCourseIncome = new CourseIncome();
			    			double defAmount = courseIncome2.getAmount();
			    			double newAmount = -(defAmount * disAmount / 100);
			    			newCourseIncome.setAmount(newAmount);
			    			newCourseIncome.setCourse(course);
			    			newCourseIncome.setPayed(false);
			    			newCourseIncome.setMonth(courseIncome2.getMonth());
			    			newCourseIncome.setStudent(student);
			    			newCourseIncome.setWeek(courseIncome2.getWeek());
			    			newCourseIncome.setYear(courseIncome2.getYear());
			    			newCourseIncome.setCourseStudentDiscountId(csd.getId());
			    			newCourseIncome.setFixDiscount(csd.getDiscount().getType().equals(DiscountType.FIX_PRICE));
			    			courseIncomeService.saveCourseIncome(newCourseIncome);
			    			LOGGER.debug("courseIncome created: " + newCourseIncome.getId());
    	    			}
    	    		}
    	    	}
    		}
    		request.setAttribute(FlashScopeFilter.FLASH_PREFIX + FlashScopeFilter.WARNING_MESSAGE_KEY, CompletedClassController.WARNING_CI_CREATED_FOR_CC);
    	}        
    }     
    
    
    public CourseStudentDiscount updateCourseStudentDiscount(CourseStudentDiscount courseStudentDiscount) {
        return courseStudentDiscountRepository.save(courseStudentDiscount);
    }

    public void deleteCourseStudentDiscount(CourseStudentDiscount courseStudentDiscount) {    	
    	List<CourseIncome>	courseIncomeList = courseIncomeService.findByCourseStudentDiscountId(courseStudentDiscount.getId());
    	if(courseIncomeList != null && courseIncomeList.size() > 0) {
    		for (CourseIncome courseIncome2 : courseIncomeList) {
    			LOGGER.debug("delete courseIncome : " + courseIncome2.getId());
    			courseIncomeService.deleteCourseIncome(courseIncome2);    			
			}    		
    	}
        courseStudentDiscountRepository.delete(courseStudentDiscount);
    }
    
    public List<CourseStudentDiscount> findByCoursStudent(CourseStudent courseStudent) {
    	Course course = courseStudent.getCourse();
    	Student student = courseStudent.getStudent();
    	return findByCourseAndStudent(course, student);
    }
}
