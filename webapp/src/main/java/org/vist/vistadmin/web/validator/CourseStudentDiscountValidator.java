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
import org.vist.vistadmin.domain.CourseStudentDiscount;
import org.vist.vistadmin.domain.Course;
import org.vist.vistadmin.domain.CourseStudent;
import org.vist.vistadmin.domain.CourseTeacher;
import org.vist.vistadmin.domain.CourseStudentDiscount;
import org.vist.vistadmin.domain.Discount;
import org.vist.vistadmin.domain.Student;
import org.vist.vistadmin.domain.Teacher;
import org.vist.vistadmin.service.CourseStudentDiscountService;
import org.vist.vistadmin.service.CourseStudentService;
import org.vist.vistadmin.service.CourseTeacherService;
import org.vist.vistadmin.util.CalcUtil;
import org.vist.vistadmin.web.BaseController;

@Configurable
public class CourseStudentDiscountValidator implements Validator {

		private static Logger LOGGER = LoggerFactory.getLogger(CourseStudentDiscountValidator.class);	
		
	    private Validator validator;

	    @Autowired
	    CourseStudentService courseStudentService;
	    
	    @Autowired
	    CourseStudentDiscountService courseStudentDiscountService;
	    
	    public CourseStudentDiscountValidator(Validator validator) {
	        this.validator = validator;
	    }

	    @Override
	    @SuppressWarnings("rawtypes")
	    public boolean supports(Class clazz) {
	        return CourseStudentDiscount.class.equals(clazz);
	    }

	    @Override
	    public void validate(Object target, Errors errors) {

	    	validator.validate(target, errors);
	    	
	    	CourseStudentDiscount courseStudentDiscount = (CourseStudentDiscount)target;
	        
	    	boolean isNew = true;
	    	if(courseStudentDiscount.getId() != null) {
	    		isNew = false;
	    	}
	    	
	    	Course course = courseStudentDiscount.getCourse();
	    	Student student = courseStudentDiscount.getStudent();
	    	Discount discount = courseStudentDiscount.getDiscount();

	    	ValidatorUtil.checkCourseIsNotArchived(course, errors);
	    	
	    	ValidatorUtil.checkStudentIsNotArchived(student, errors);
	    	
	    	if(course == null) {
	    		errors.rejectValue("course", "error_common_required_field", 
	    					new Object[] {new DefaultMessageSourceResolvable("label_org_vist_vistadmin_domain_coursestudentdiscount_course")}, null);
	    	}

	    	if(student == null) {
	    		errors.rejectValue("student", "error_common_required_field", 
	    					new Object[] {new DefaultMessageSourceResolvable("label_org_vist_vistadmin_domain_coursestudentdiscount_student")}, null);
	    	}

	    	if(discount == null) {
	    		errors.rejectValue("discount", "error_common_required_field", 
	    					new Object[] {new DefaultMessageSourceResolvable("label_org_vist_vistadmin_domain_coursestudentdiscount_discount")}, null);
	    	}
	    	
	    	
	    	if(course != null && student != null) {
	    		List<CourseStudent> courseStudentList = courseStudentService.findByStudentAndCourse(student, course);
	    		if(courseStudentList == null || courseStudentList.size() == 0) {
	    			errors.rejectValue("course", "error_common_no_relation_course_student");
	    		}
	    	}
	    	
	    	if(course != null && student != null && discount != null) {
	    		List<CourseStudentDiscount> csdl = courseStudentDiscountService.findByCourseAndStudentAndDiscount(course, student, discount);

	    		if(csdl != null && csdl.size() > 0) {
	    			if(isNew) {
	    				errors.rejectValue("discount", "error_coursestudentdiscount_not_unique");
	    			} else {	    			    				
	    				for(CourseStudentDiscount csd : csdl) {
	    					if(csd.getId() != courseStudentDiscount.getId()) {
	    						errors.rejectValue("discount", "error_coursestudentdiscount_not_unique");
	    						break;
	    					}
	    				}
	    			}
	    		}
	    	}
	    	
	    	
	    }
}