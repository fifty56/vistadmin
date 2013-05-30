package org.vist.vistadmin.web.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.vist.vistadmin.domain.Teacher;
import org.vist.vistadmin.domain.TeacherBillingData;
import org.vist.vistadmin.service.TeacherService;

@Configurable
public class TeacherValidator implements Validator {

		@Autowired
		TeacherService teacherService;
	
	    private Validator validator;

	    public TeacherValidator(Validator validator) {
	        this.validator = validator;
	    }

	    @Override
	    @SuppressWarnings("rawtypes")
	    public boolean supports(Class clazz) {
	        return Teacher.class.equals(clazz);
	    }

	    @Override
	    public void validate(Object target, Errors errors) {

	    	validator.validate(target, errors);
	    	
	    	Teacher teacher = (Teacher)target;
	    	TeacherBillingData tbd = teacher.getTeacherBillingData();
	    	
	    	if(teacher.getLanguages() == null || teacher.getLanguages().size() == 0) {
	    		errors.rejectValue("languages", "error_common_list_at_least_one_required", 
	    					new Object[] {new DefaultMessageSourceResolvable("label_org_vist_vistadmin_domain_teacher_languages")}, null);
	    	}
	    	if(teacher.getPersonalData() == null || teacher.getPersonalData().getEmailAddress() == null || teacher.getPersonalData().getEmailAddress().equals("")) {
	    		errors.rejectValue("personalData.emailAddress", "error_common_required_field", 
	    					new Object[] {new DefaultMessageSourceResolvable("label_org_vist_vistadmin_domain_studentx_emailaddress")}, null);
	    	} else if(!teacherService.validateEmailAddressUnique(teacher.getId(), teacher.getPersonalData().getEmailAddress())) {
	    		errors.rejectValue("personalData.emailAddress", "error_common_unique_field", 
    					new Object[] {new DefaultMessageSourceResolvable("label_org_vist_vistadmin_domain_teacher"), 
    								new DefaultMessageSourceResolvable("label_org_vist_vistadmin_domain_studentx_emailaddress")}, null);
	    	}	    		
	    	
	    	if(tbd != null && !tbd.isEmpty()) {
	    		 if(tbd.getCompanyNumber() == null || tbd.getCompanyNumber().equals("")) {
	    			 errors.rejectValue("teacherBillingData.companyNumber", "error_common_required_field");
	    		 }
	    		
	    		 if(tbd.getName() == null || tbd.getName().equals("")) {
	    			 errors.rejectValue("teacherBillingData.name", "error_common_required_field");
	    		 }
	    		 
	    		 if(tbd.getTaxNumber() == null || tbd.getTaxNumber().equals("")) {
	    			 errors.rejectValue("teacherBillingData.taxNumber", "error_common_required_field");
	    		 }
	    		 
	    		 ValidatorUtil.checkAddress(tbd.getAddress(), errors, "teacherBillingData.address");
	    	}
	    	
	    }
}