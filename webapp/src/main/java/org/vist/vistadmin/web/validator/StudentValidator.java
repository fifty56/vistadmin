package org.vist.vistadmin.web.validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.vist.vistadmin.domain.BillingAddress;
import org.vist.vistadmin.domain.CourseStudent;
import org.vist.vistadmin.domain.Student;
import org.vist.vistadmin.domain.common.ClassStatus;
import org.vist.vistadmin.service.CourseStudentService;
import org.vist.vistadmin.service.StudentService;

@Configurable
public class StudentValidator implements Validator {

		@Autowired
		StudentService studentService;
	
		@Autowired
		CourseStudentService courseStudentService;
		
	    private Validator validator;

	    public StudentValidator(Validator validator) {
	        this.validator = validator;
	    }

	    @Override
	    @SuppressWarnings("rawtypes")
	    public boolean supports(Class clazz) {
	        return Student.class.equals(clazz);
	    }

	    @Override
	    public void validate(Object target, Errors errors) {

	    	validator.validate(target, errors);
	    	
	    	Student student = (Student)target;
	    	
	    	boolean isNew = false;
	    	if(student.getId() == null) {
	    		isNew = true;
	    	}
	    	
	    	
	        BillingAddress billingAddress = student.getBillingAddress();
	        
	    	if(student.getLanguages() == null || student.getLanguages().size() == 0) {
	    		errors.rejectValue("languages", "error_common_list_at_least_one_required", 
	    					new Object[] {new DefaultMessageSourceResolvable("label_org_vist_vistadmin_domain_student_languages")}, null);
	    	}
	    	if(student.getPersonalData() == null || student.getPersonalData().getEmailAddress() == null || student.getPersonalData().getEmailAddress().equals("")) {
	    		errors.rejectValue("personalData.emailAddress", "error_common_required_field", 
	    					new Object[] {new DefaultMessageSourceResolvable("label_org_vist_vistadmin_domain_studentx_emailaddress")}, null);
	    	} else if(!studentService.validateEmailAddressUnique(student.getId(), student.getPersonalData().getEmailAddress())) {
	    		errors.rejectValue("personalData.emailAddress", "error_common_unique_field", 
    					new Object[] {new DefaultMessageSourceResolvable("label_org_vist_vistadmin_domain_student"), 
    								new DefaultMessageSourceResolvable("label_org_vist_vistadmin_domain_studentx_emailaddress")}, null);
	    	}
	    	
	    	if(billingAddress != null && !billingAddress.isEmpty()) {
	    		if(billingAddress.getName() == null || billingAddress.getName().equals("")) {
	    			errors.rejectValue("billingAddress.name", "error_common_required_field");
	    		}
	    		
	    		ValidatorUtil.checkAddress(billingAddress.getAddress(), errors, "billingAddress.address");
	    		
	    		ValidatorUtil.checkAddress(billingAddress.getPostalAddress(), errors, "billingAddress.postalAddress");
	    	}	    		
	    	
	    	if(!isNew) {
	    		Student oldStudent = studentService.findStudent(student.getId());
	    		if(oldStudent.isCompany()) {
	    			List<CourseStudent> courses = courseStudentService.findByStudent(oldStudent);
	    			if(courses != null && courses.size() > 0) {
	    				List<String> oldAssignedStudentNameList = new ArrayList<String>();
	    				for(CourseStudent courseStudent : courses) {
	    					if(courseStudent.getCompanyStudentNames() != null && !courseStudent.getCompanyStudentNames().equals("") 
	    								&& !courseStudent.getCourse().getStatus().equals(ClassStatus.ARCHIVED)) {
	    						String[] studentNames = courseStudent.getCompanyStudentNames().split(";");
	    						List<String> studentList = Arrays.asList(studentNames);
	    						oldAssignedStudentNameList.addAll(studentList);
	    					}
	    				}
	    					    				
	    				if(student.isCompany()) {
		    				List newStudentNameList = student.getCompanyData().getCompanyStudentNameList();						
							
							if(!newStudentNameList.containsAll(oldAssignedStudentNameList)) {							
								oldAssignedStudentNameList.removeAll(newStudentNameList);
								Object[] missingNames = oldAssignedStudentNameList.toArray();
								errors.rejectValue("companyData.companyStudentNames", "error_student_comapny_names_cannot_removed", missingNames, "");
							}
	    				}
	    			}
	    		}
	    	}
	    	
	    }	    	    
}