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
import org.vist.vistadmin.domain.Discount;
import org.vist.vistadmin.domain.Course;
import org.vist.vistadmin.domain.CourseStudent;
import org.vist.vistadmin.domain.CourseStudentDiscount;
import org.vist.vistadmin.domain.CourseTeacher;
import org.vist.vistadmin.domain.Discount;
import org.vist.vistadmin.domain.Student;
import org.vist.vistadmin.domain.Teacher;
import org.vist.vistadmin.domain.common.DiscountType;
import org.vist.vistadmin.service.DiscountService;
import org.vist.vistadmin.service.CourseStudentDiscountService;
import org.vist.vistadmin.service.CourseStudentService;
import org.vist.vistadmin.service.CourseTeacherService;
import org.vist.vistadmin.util.CalcUtil;
import org.vist.vistadmin.web.BaseController;

@Configurable
public class DiscountValidator implements Validator {

		private static Logger LOGGER = LoggerFactory.getLogger(DiscountValidator.class);	
		
	    private Validator validator;

	    public DiscountValidator(Validator validator) {
	        this.validator = validator;
	    }

	    @Override
	    @SuppressWarnings("rawtypes")
	    public boolean supports(Class clazz) {
	        return Discount.class.equals(clazz);
	    }

	    @Override
	    public void validate(Object target, Errors errors) {

	    	validator.validate(target, errors);
	    	
	    	Discount discount = (Discount)target;
	        
	    	boolean isNew = true;
	    	if(discount.getId() != null) {
	    		isNew = false;
	    	}
	    	
	    	double amount = discount.getAmount();	    	
	    		    		    	
	    	if(amount == 0.0 || amount < 0.0) {
	    		errors.rejectValue("amount", "error_common_positive_number");
	    	} else if(amount > 100 && discount.getType().equals(DiscountType.PERCENTAGE)) {
	    		errors.rejectValue("amount", "error_common_percentage_overflow");
	    	}


	    }
}