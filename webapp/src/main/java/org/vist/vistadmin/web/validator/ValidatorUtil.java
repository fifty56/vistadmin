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
import org.vist.vistadmin.domain.Address;
import org.vist.vistadmin.domain.CourseIncome;
import org.vist.vistadmin.domain.Course;
import org.vist.vistadmin.domain.CourseStudent;
import org.vist.vistadmin.domain.CourseStudentDiscount;
import org.vist.vistadmin.domain.CourseTeacher;
import org.vist.vistadmin.domain.Discount;
import org.vist.vistadmin.domain.Student;
import org.vist.vistadmin.domain.Teacher;
import org.vist.vistadmin.domain.common.ClassStatus;
import org.vist.vistadmin.domain.common.PersonStatus;
import org.vist.vistadmin.service.CourseIncomeService;
import org.vist.vistadmin.service.CourseService;
import org.vist.vistadmin.service.CourseStudentDiscountService;
import org.vist.vistadmin.service.CourseStudentService;
import org.vist.vistadmin.service.CourseTeacherService;
import org.vist.vistadmin.util.CalcUtil;
import org.vist.vistadmin.web.BaseController;

public class ValidatorUtil {

		
		protected static void checkCourseIsNotArchived(Course course, Errors errors) {			
			if(course != null && course.getStatus().equals(ClassStatus.ARCHIVED)) {
	    		errors.rejectValue("course", "error_common_cannotbe_archived", 
	    				new Object[] {new DefaultMessageSourceResolvable("label_org_vist_vistadmin_domain_course")}, null);
	    	}						
		}
		
		protected static void checkStudentIsNotArchived(Student student, Errors errors) {			
			if(student != null && student.getStatus().equals(PersonStatus.ARCHIVED)) {
	    		errors.rejectValue("student", "error_common_cannotbe_archived", 
	    				new Object[] {new DefaultMessageSourceResolvable("label_org_vist_vistadmin_domain_student")}, null);
	    	}						
		}
		
		protected static void checkTeacherIsNotArchived(Teacher teacher, Errors errors) {			
			if(teacher != null && teacher.getStatus().equals(PersonStatus.ARCHIVED)) {
	    		errors.rejectValue("teacher", "error_common_cannotbe_archived", 
	    				new Object[] {new DefaultMessageSourceResolvable("label_org_vist_vistadmin_domain_teacher")}, null);
	    	}						
		}
		
		
		public static void checkAddress(Address address, Errors errors, String field) {				
				if(address.getCity() == null || address.getCity().equals("")) {
				
					errors.rejectValue(field + ".city", "error_common_required_field");
				}
				
				if(address.getStreet() == null || address.getStreet().equals("")) {
					
					errors.rejectValue(field + ".street", "error_common_required_field");
				}
				
				if(address.getZipAddress() == 0) {
					errors.rejectValue(field + ".zipAddress", "error_common_required_field");
				}
			
		}
}