package org.vist.vistadmin.web.validator;

import org.vist.vistadmin.domain.common.ClassFormat;
import org.vist.vistadmin.domain.common.Languages;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.vist.vistadmin.domain.Course;
import org.vist.vistadmin.domain.CourseTeacher;
import org.vist.vistadmin.domain.Teacher;
import org.vist.vistadmin.service.CourseTeacherService;
import org.vist.vistadmin.web.BaseController;

@Configurable
public class CourseTeacherValidator implements Validator {

		private static final Logger LOGGER =  LoggerFactory.getLogger(CourseTeacherValidator.class);
	
		@Autowired
		CourseTeacherService courseTeacherService;
	
	    private Validator validator;

	    public CourseTeacherValidator(Validator validator) {
	        this.validator = validator;
	    }

	    @Override
	    @SuppressWarnings("rawtypes")
	    public boolean supports(Class clazz) {
	        return CourseTeacher.class.equals(clazz);
	    }

	    @Override
	    public void validate(Object target, Errors errors) {

	    	validator.validate(target, errors);
	    	
	    	CourseTeacher courseTeacher = (CourseTeacher)target;
	        
	    	boolean isNew = false;
	    	if(courseTeacher.getId() == null) {
	    		isNew = true;
	    	}
	    	
	    	Course course = courseTeacher.getCourse();
	    	Teacher teacher = courseTeacher.getTeacher();
	    	Date startDate = courseTeacher.getStartDate();
	    	Date endDate = courseTeacher.getEndDate();
	    	
	    	ValidatorUtil.checkCourseIsNotArchived(course, errors);
	    	
	    	ValidatorUtil.checkTeacherIsNotArchived(teacher, errors);
	    	
	    	if(course == null) {
	    		errors.rejectValue("course", "error_common_required_field", 
	    					new Object[] {new DefaultMessageSourceResolvable("label_org_vist_vistadmin_domain_courseteacher_course")}, null);
	    	}
	    	if(teacher == null) {
	    		errors.rejectValue("teacher", "error_common_required_field", 
	    					new Object[] {new DefaultMessageSourceResolvable("label_org_vist_vistadmin_domain_courseteacher_teacher")}, null);
	    	}	    	
	    	if(teacher != null && course != null) {
	    		List<CourseTeacher> ctl = courseTeacherService.findByTeacherAndCourse(teacher, course);
	    		
	    		LOGGER.debug("current CT.id: " + courseTeacher.getId());
	    		LOGGER.debug("isNew: " + isNew + ", ctl: " + (ctl == null ? "null" : "size: " + ctl.size()));
	    		if(ctl != null) {
	    			for (CourseTeacher courseTeacher2 : ctl) {
	    				LOGGER.debug("CT.id: " + courseTeacher2.getId());
					}
	    		}
	    		
	    		if(isNew && ctl != null && ctl.size() > 0) {	    		
	    			errors.rejectValue("course", "error_courseteacher_not_unique");
	    		} else if(!isNew && ctl != null && (ctl.size() > 1 || !ctl.get(0).getId().equals(courseTeacher.getId()))) {
	    			errors.rejectValue("course", "error_courseteacher_not_unique");
	    		}
	    			    	
	    		
	    		if(!teacher.getLanguages().contains(course.getLang())) {	    		
	    			errors.rejectValue("course", "error_common_no_common_language",	    					
	    					new Object[] {new DefaultMessageSourceResolvable("label_org_vist_vistadmin_domain_courseteacher_course"),
	    					              new DefaultMessageSourceResolvable("label_org_vist_vistadmin_domain_courseteacher_teacher")}, null);	    					
	    		}
	    	}
	    	
	    	if(startDate == null) {
	    		errors.rejectValue("startDate", "error_common_required_field", 
	    					new Object[] {new DefaultMessageSourceResolvable("label_org_vist_vistadmin_domain_courseteacher_startdate")}, null);
	    	}
	    	if(endDate == null) {
	    		errors.rejectValue("endDate", "error_common_required_field", 
	    					new Object[] {new DefaultMessageSourceResolvable("label_org_vist_vistadmin_domain_courseteacher_enddate")}, null);
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
	    	
	    	/*if(course != null && courseTeacher.getNumerOfClasses() > course.getSumOfClasses()) {
	    		errors.rejectValue("numerOfClasses", "error_courseteacher_numofclass_higher", new Object[] {course.getSumOfClasses()}, null);
	    	}*/

	    	if(startDate != null && endDate != null && course != null && !course.getCourseFormat().equals(ClassFormat.INSTANT)) {
	    		List<CourseTeacher> courseTeacherList = courseTeacherService.isValidDatesNotOverlap(courseTeacher);
	    		if(courseTeacherList != null && courseTeacherList.size() > 1) {
	    			errors.rejectValue("startDate", "error_courseteacher_dates_overlap");	    			
	    		} else if(courseTeacherList != null && courseTeacherList.size() == 1) {
	    			Date sd = courseTeacherList.get(0).getStartDate();
	    			Date ed = courseTeacherList.get(0).getEndDate();
	    			SimpleDateFormat dateFormat = new SimpleDateFormat(BaseController.DATE_TIME_FORMAT_STR);
	    			errors.rejectValue("startDate", "error_courseteacher_dates_overlap_wparam", new Object[] {dateFormat.format(sd), dateFormat.format(ed)}, null);
	    		}
	    	}
	    	
	    }
}