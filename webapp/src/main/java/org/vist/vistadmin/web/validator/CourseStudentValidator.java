package org.vist.vistadmin.web.validator;

import org.vist.vistadmin.domain.common.Languages;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.vist.vistadmin.domain.Course;
import org.vist.vistadmin.domain.CourseStudent;
import org.vist.vistadmin.domain.Student;
import org.vist.vistadmin.service.CourseStudentService;

@Configurable
public class CourseStudentValidator implements Validator {

		@Autowired
		CourseStudentService courseStudentService;
	
	    private Validator validator;

	    public CourseStudentValidator(Validator validator) {
	        this.validator = validator;
	    }

	    @Override
	    @SuppressWarnings("rawtypes")
	    public boolean supports(Class clazz) {
	        return CourseStudent.class.equals(clazz);
	    }

	    @Override
	    public void validate(Object target, Errors errors) {

	    	validator.validate(target, errors);
	    	
	    	CourseStudent courseStudent = (CourseStudent)target;
	        
	    	boolean isNew = true;
	    	if(courseStudent.getId() != null) {
	    		isNew = false;
	    	}
	    	
	    	
	    	Course course = courseStudent.getCourse();
	    	Student student = courseStudent.getStudent();
	    	Date startDate = courseStudent.getStartDate();
	    	Date endDate = courseStudent.getEndDate();
	    	
	    	ValidatorUtil.checkCourseIsNotArchived(course, errors);
	    	
	    	ValidatorUtil.checkStudentIsNotArchived(student, errors);
	    	
	    	if(course == null) {
	    		errors.rejectValue("course", "error_common_required_field", 
	    					new Object[] {new DefaultMessageSourceResolvable("label_org_vist_vistadmin_domain_coursestudent_course")}, null);
	    	}
	    	if(student == null) {
	    		errors.rejectValue("student", "error_common_required_field", 
	    					new Object[] {new DefaultMessageSourceResolvable("label_org_vist_vistadmin_domain_coursestudent_student")}, null);
	    	}	    	
	    	if(student != null && course != null) {
	    		
	    		List<CourseStudent> csl = courseStudentService.findByStudentAndCourse(student, course);
	    		
	    		if(isNew && csl != null && csl.size() > 0) {	    		
	    			errors.rejectValue("course", "error_coursestudent_not_unique");
	    		} else if(!isNew && csl != null && (csl.size() > 1 || csl.get(0).getId() != courseStudent.getId())) {
	    			errors.rejectValue("course", "error_coursestudent_not_unique");
	    		}
	    		
	    		if(!student.getLanguages().contains(course.getLang())) {	    		
	    			errors.rejectValue("course", "error_common_no_common_language",	    					
	    					new Object[] {new DefaultMessageSourceResolvable("label_org_vist_vistadmin_domain_coursestudent_course"),
	    					              new DefaultMessageSourceResolvable("label_org_vist_vistadmin_domain_coursestudent_student")}, null);	    					
	    		}
	    	}
	    	
	    	if(startDate == null) {
	    		errors.rejectValue("startDate", "error_common_required_field", 
	    					new Object[] {new DefaultMessageSourceResolvable("label_org_vist_vistadmin_domain_coursestudent_startdate")}, null);
	    	}
	    	if(endDate == null) {
	    		errors.rejectValue("endDate", "error_common_required_field", 
	    					new Object[] {new DefaultMessageSourceResolvable("label_org_vist_vistadmin_domain_coursestudent_enddate")}, null);
	    	}
	    	if(startDate != null && endDate != null) {
	    		if(startDate.after(endDate)) {
	    			errors.rejectValue("endDate", "error_startdate_after_enddate");
	    		}
	    	}
	    	
	    	if(startDate != null && course != null) {
	    		if(course.getStartDate() != null && course.getStartDate().after(startDate)) {
	    			errors.rejectValue("startDate", "error_startdate_before_course_startdate", new Object[] {course.getStartDate()}, null);
	    		}
	    	}
	    	if(endDate != null && course != null) {
	    		if(course.getEndDate() != null && course.getEndDate().before(endDate)) {
	    			errors.rejectValue("endDate", "error_enddate_after_course_enddate", new Object[] {course.getEndDate()}, null);
	    		}
	    	}
	    	
	    	if(student.isCompany() && !course.getCompany() || !student.isCompany() && course.getCompany()) {	    		
	    		errors.rejectValue("student", "error_student_course_oneofthem_not_company");	    		
	    	} else if(student.isCompany() && course.getCompany()) {	    		
	    		List<CourseStudent> courseList = courseStudentService.findByCourse(course);
	    		if(courseList != null && courseList.size() > 0) {
		    		if(isNew || !(courseList.size() == 1 && courseList.get(0).getId().equals(courseStudent.getId()))) { 
		    			errors.rejectValue("course", "error_student_course_company_not_one");				    		
		    		}
	    		}
	    		/*List<CourseStudent> studentList = courseStudentService.findByStudent(student);
	    		if(studentList != null && studentList.size() > 0) {
		    		if(isNew || !(studentList.size() == 1 && studentList.get(0).getId().equals(courseStudent.getId()))) { 
		    			errors.rejectValue("student", "error_course_student_company_not_one");				    		
		    		}
	    		}*/
	    	}
	    	
	    	
	    }
}