package org.vist.vistadmin.web.validator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.vist.vistadmin.domain.CourseIncome;
import org.vist.vistadmin.domain.Course;
import org.vist.vistadmin.domain.CourseStudent;
import org.vist.vistadmin.domain.CourseStudentDiscount;
import org.vist.vistadmin.domain.CourseTeacher;
import org.vist.vistadmin.domain.Discount;
import org.vist.vistadmin.domain.Student;
import org.vist.vistadmin.domain.Teacher;
import org.vist.vistadmin.service.CourseIncomeService;
import org.vist.vistadmin.service.CourseStudentDiscountService;
import org.vist.vistadmin.service.CourseStudentService;
import org.vist.vistadmin.service.CourseTeacherService;
import org.vist.vistadmin.util.CalcUtil;
import org.vist.vistadmin.web.BaseController;

@Configurable
public class CourseIncomeValidator implements Validator {

		private static Logger LOGGER = LoggerFactory.getLogger(CourseIncomeValidator.class);
	
		@Autowired
		CourseIncomeService courseIncomeService;
	
		@Autowired
		CourseTeacherService courseTeacherService;
		
		@Autowired
		CourseStudentService courseStudentService;
		
		@Autowired
		CourseStudentDiscountService courseStudentDiscountService;
		
	    private Validator validator;

	    public CourseIncomeValidator(Validator validator) {
	        this.validator = validator;
	    }

	    @Override
	    @SuppressWarnings("rawtypes")
	    public boolean supports(Class clazz) {
	        return CourseIncome.class.equals(clazz);
	    }

	    @Override
	    public void validate(Object target, Errors errors) {

	    	validator.validate(target, errors);
	    	
	    	CourseIncome courseIncome = (CourseIncome)target;
	    	//CourseIncome defCourseIncome = null;
	    	
	    	boolean isNew = true;
	    	if(courseIncome.getId() != null) {
	    		isNew = false;
	    		//defCourseIncome = courseIncomeService.findCourseIncome(courseIncome.getId());
	    	}
	    	
	    	Course course = courseIncome.getCourse();
	    	Student student = courseIncome.getStudent();
	    	double amount = courseIncome.getAmount();
	    	int month = courseIncome.getMonth();
	    	int week = courseIncome.getWeek();
	    	int year = courseIncome.getYear();	    	
	    		    
	    	
	    	ValidatorUtil.checkCourseIsNotArchived(course, errors);
	    	
	    	ValidatorUtil.checkStudentIsNotArchived(student, errors);
	    	
	    	if(course == null) {
	    		errors.rejectValue("course", "error_common_required_field", 
	    					new Object[] {new DefaultMessageSourceResolvable("label_org_vist_vistadmin_domain_completedclass_course")}, null);
	    	}

	    	if(student == null) {
	    		errors.rejectValue("student", "error_common_required_field", 
	    					new Object[] {new DefaultMessageSourceResolvable("label_org_vist_vistadmin_domain_completedclass_student")}, null);
	    	}
	    	
	    	
	    	
	    	if(course.getPayPerClasses()) {
	    		if(courseIncome.isPayed() && courseIncome.getCurrDate() == null) {
	    			//errors.rejectValue("currDate", "error_common_required_field");
	    		}
	    	} else {	    		
	    		List<CourseStudentDiscount> courseStudentDiscountList = courseStudentDiscountService.findByCourseAndStudent(course, student);
	    		Map<Course, Set<Discount>> csds = CalcUtil.getCourseDiscountSetMapByCourse(courseStudentDiscountList);
	    		double courseAllExpense = CalcUtil.getStudentCoursePrice(student, course, csds.get(course));
	    		
	    		LOGGER.debug("course all exp: " + courseAllExpense);
	    		
	    		List<CourseIncome> courseIncomeList = courseIncomeService.findByCourseAndStudent(course, student);	    			    		
	    		
	    		int currSum = 0;
	    		for(CourseIncome ci : courseIncomeList) {
	    			if(courseIncome.getId() == null || (ci.getId().longValue() != courseIncome.getId().longValue())) {
	    				currSum += ci.getAmount();
	    			}
	    		}
	    		
	    		
	    		
	    		LOGGER.debug("course currSum: " + currSum);
	    		LOGGER.debug("amount: " + amount);
	    		if(courseAllExpense < currSum + amount) {	    			    			
	    				errors.rejectValue("amount", "error_courseincome_amount_overflow", 
	    					new Object[] {courseAllExpense, currSum, (currSum + amount)}, null);	    			
	    		}

	    	}
	    		    	
	    	
	    	if(course != null && student != null) {
	    		List<CourseStudent> courseStudentList = courseStudentService.findByStudentAndCourse(student, course);
	    		if(courseStudentList == null || courseStudentList.size() == 0) {
	    			errors.rejectValue("course", "error_common_no_relation_course_student");
	    		}
	    	}
	    	
    		if(courseIncome.isPayed() && courseIncome.getPaymentDate() == null) {
    			errors.rejectValue("paymentDate", "error_common_payed_without_paymentdate");
    		}
	    	
	    }
}