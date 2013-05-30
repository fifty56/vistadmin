package org.vist.vistadmin.web.validator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.vist.vistadmin.domain.CompletedClass;
import org.vist.vistadmin.domain.Course;
import org.vist.vistadmin.domain.CourseTeacher;
import org.vist.vistadmin.domain.Teacher;
import org.vist.vistadmin.service.CompletedClassService;
import org.vist.vistadmin.service.CourseTeacherService;
import org.vist.vistadmin.web.BaseController;

@Configurable
public class CompletedClassValidator implements Validator {

		private static Logger LOGGER = LoggerFactory.getLogger(CompletedClassValidator.class);
	
		@Autowired
		CompletedClassService completedClassService;
	
		@Autowired
		CourseTeacherService courseTeacherService;
		
	    private Validator validator;

	    public CompletedClassValidator(Validator validator) {
	        this.validator = validator;
	    }

	    @Override
	    @SuppressWarnings("rawtypes")
	    public boolean supports(Class clazz) {
	        return CompletedClass.class.equals(clazz);
	    }

	    @Override
	    public void validate(Object target, Errors errors) {

	    	validator.validate(target, errors);
	    		    	
	    	CompletedClass completedClass = (CompletedClass)target;
	    	CompletedClass oldCompletedClass = null;
	    	
	    	boolean isNew = true;
	    	if(completedClass.getId() != null) {
	    		isNew = false;
	    		oldCompletedClass = completedClassService.findCompletedClass(completedClass.getId());
	    	}
	    	
	    	Course course = completedClass.getCourse();
	    	Teacher teacher = completedClass.getTeacher();
	    	int year = completedClass.getCompYear();
	    	int month = completedClass.getCompMonth();
	    	int week = completedClass.getCompWeek();
	    	int classes = completedClass.getNumberOfClasses();
	    
	    	ValidatorUtil.checkCourseIsNotArchived(course, errors);
	    	
	    	ValidatorUtil.checkTeacherIsNotArchived(teacher, errors);
	    	
	    	if(course == null) {
	    		errors.rejectValue("course", "error_common_required_field", 
	    					new Object[] {new DefaultMessageSourceResolvable("label_org_vist_vistadmin_domain_completedclass_course")}, null);
	    	}

	    	if(teacher == null) {
	    		errors.rejectValue("teacher", "error_common_required_field", 
	    					new Object[] {new DefaultMessageSourceResolvable("label_org_vist_vistadmin_domain_completedclass_teacher")}, null);
	    	}
	    	
	    	List<CompletedClass> completedClassList = completedClassService.findByTeacherAndCourseAndCompYearAndCompMonth(teacher, course, year, month);
	    	LOGGER.debug("completedClassList for teacher, course, year, month: " + completedClassList);
	    	
	    	if(course != null && teacher != null && completedClassList != null && completedClassList.size() > 0) {
	    		LOGGER.debug("isNew: " + isNew + ", week: " + week);
	    		if(week == -1) {
	    			if(!isNew) {
	    				boolean hasAnother = false;
	    				for(CompletedClass cc : completedClassList) {
	    					if(cc.getId() != completedClass.getId()) {
	    						LOGGER.debug("has another, id: " + completedClass.getId()); 
	    						hasAnother = true;
	    						break;
	    					}
	    				}
	    				if(hasAnother) {
	    					errors.rejectValue("teacher", "error_completedclass_already_has_for_that_month");	
	    				}
	    			} else {
	    				errors.rejectValue("teacher", "error_completedclass_already_has_for_that_month");
	    			}
	    		} else {	    		
		    		boolean hasSameWeek = false;
		    		Long sameWeekId = null;
		    		boolean hasMonthly = false;
		    		Long hasMonthlyId = null;
		    				    					
	    			for(CompletedClass cc : completedClassList) {
	    				LOGGER.debug("check cc in completedClassList: " + cc);
	    				if(cc.getCompWeek() == week) {
	    					LOGGER.debug("has same week, id: " + cc.getId());
	    					sameWeekId = cc.getId();
	    					hasSameWeek = true;
	    					break;
	    				}
	    				if(cc.getCompWeek() == -1) {
	    					LOGGER.debug("has monthly, id: " + cc.getId());
	    					hasMonthlyId = cc.getId();
	    					hasMonthly = true;
	    				}
	    			}
	    			if(hasSameWeek) {
	    				if(isNew || (!isNew && !sameWeekId.equals(completedClass.getId()))) {		    					 
	    					errors.rejectValue("teacher", "error_completedclass_not_unique");
	    				}
	    			} else if(hasMonthly) {
	    				if(isNew || (!isNew && !hasMonthlyId.equals(completedClass.getId()))) {
	    					errors.rejectValue("teacher", "error_completedclass_already_has_monthly_one");
	    				}
	    			}
	    		
	    		}	    			    			    		
	    	}	    	
	    	
	    	
	    	if(course != null && teacher != null) {
	    		List<CourseTeacher> courseTeacherList = courseTeacherService.findByTeacherAndCourse(teacher, course);
	    		if(courseTeacherList == null || courseTeacherList.size() == 0) {
	    			errors.rejectValue("teacher", "error_completedclass_relation_missing");
	    		}
	    	}
	    	
    		if(course != null && !course.getPayPerClasses()) {
    			int sumOfCC = 0;    			
    			List<CompletedClass> ccl = completedClassService.findByTeacherAndCourse(teacher, course);
    			if(ccl != null) {
    				for(CompletedClass cc: ccl) {
    					sumOfCC += cc.getNumberOfClasses();	
    				}
    			}
    			int currentSumOfCC = 0;
    			if(!isNew) {
    				currentSumOfCC = oldCompletedClass.getNumberOfClasses();
    			}    			
    			int totalSum = classes + sumOfCC - currentSumOfCC;
    			
    			if(totalSum > course.getSumOfClasses()) {
    				errors.rejectValue("numberOfClasses", "error_completedclass_overflow", new Object[] {course.getSumOfClasses(), sumOfCC, totalSum}, null);
    			}
    		}
    		
    		if(course != null) {
    			Date date = new Date(year-1900, month-1, 1);    			
    			Date startDate = new Date(course.getStartDate().getYear(), course.getStartDate().getMonth(), 1);    			
    			Date endDate = new Date(course.getEndDate().getYear(), course.getEndDate().getMonth(), 1);
    			LOGGER.debug("date: " + date + ", startDate: " + startDate + ", endDate: " + endDate);
    			if(date.before(startDate) || date.after(endDate)) {
    				SimpleDateFormat dateFormat = new SimpleDateFormat(BaseController.DATE_TIME_FORMAT_STR);
    				errors.rejectValue("numberOfClasses", "error_completedclass_daterange", new Object[] {
    							year, month, dateFormat.format(course.getStartDate()), 
    							dateFormat.format(course.getEndDate())}, null);
    			}
    		}
    		
    		if(completedClass.getPayed() && completedClass.getPaymentDate() == null) {
    			errors.rejectValue("paymentDate", "error_common_payed_without_paymentdate");
    		}
	    }
}