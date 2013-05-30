package org.vist.vistadmin.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vist.vistadmin.domain.Address;
import org.vist.vistadmin.domain.BillingAddress;
import org.vist.vistadmin.domain.Student;
import org.vist.vistadmin.domain.Teacher;
import org.vist.vistadmin.domain.TeacherBillingData;
import org.vist.vistadmin.domain.common.Languages;
import org.vist.vistadmin.domain.common.PersonStatus;
import org.vist.vistadmin.repository.BillingAddressRepository;
import org.vist.vistadmin.repository.TeacherBillingDataRepository;
import org.vist.vistadmin.repository.TeacherRepository;

public class TeacherServiceImpl implements TeacherService {
    
	private static Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);
	
	@Autowired
	 TeacherBillingDataService teacherBillingDataService;	
	
	@Autowired
	 AddressService addressService;
	
    public void saveTeacher(Teacher teacher) {
    	logger.debug("saveTeacher called");
    	Address address = teacher.getAddress();
    	if(StudentServiceImpl.handleAddress(addressService, address) == null) {
    		logger.debug("set address on teacher to null");
    		teacher.setAddress(null);
    	}
    	
    	handleBillingData(teacher);
        teacherRepository.save(teacher);
    }
    
    public Teacher updateTeacher(Teacher teacher) {
    	logger.debug("updateTeacher called");
    	Address address = teacher.getAddress();
    	if(StudentServiceImpl.handleAddress(addressService, address) == null) {
    		logger.debug("set address on teacher to null");
    		teacher.setAddress(null);
    	}
    	handleBillingData(teacher);    	
        return teacherRepository.save(teacher);
    }
    
    private void handleBillingData(Teacher teacher) {
    	logger.debug("handleBillingData called");
    	TeacherBillingData ba = teacher.getTeacherBillingData();
    	if(ba != null) { 
	    	logger.debug("billingData isEmpty: " + ba.isEmpty());
	    	if(ba.isEmpty()) {
	    		if(ba.getId() != null) {
	    			logger.debug("delete billingData");
	    			teacherBillingDataService.deleteTeacherBillingData(ba);
	    		}
	    		ba = null;
	    		logger.debug("set billingData null on teacher");
	    		teacher.setTeacherBillingData(ba);
	    	} else {
	    		logger.debug("billingdata id: " + ba.getId());
	    		if(ba.getId() != null) {
	    			teacherBillingDataService.updateTeacherBillingData(ba);
	    		} else {
	    			teacherBillingDataService.saveTeacherBillingData(ba);
	    		} 
	    	}  
    	}
    }   	
    
    public List<Teacher> findByStatusAndLanguages(PersonStatus status, Languages language) {
    	return teacherRepository.findByStatusAndLanguages(status, language);
    }
    
    public List<Teacher> findByEmailAddress(String emailAddress) {
    	return teacherRepository.findByPersonalDataEmailAddress(emailAddress);
    }

    public void deleteTeacher(Teacher teacher) {
    	if(teacher.getAddress() != null) {
    		addressService.deleteAddress(teacher.getAddress());
    	}
    	if(teacher.getTeacherBillingData() != null) {
    		teacherBillingDataService.deleteTeacherBillingData(teacher.getTeacherBillingData());
    	}
        teacherRepository.delete(teacher);
    }

    
    public boolean validateEmailAddressUnique(Long id, String emailAddress) {
    	List<Teacher> teacherList = findByEmailAddress(emailAddress);    	
        if(teacherList == null || teacherList.size() == 0) {
        	return true;
        } else if(id != null) {
        	for(Teacher teacher : teacherList) {
        		if(teacher.getId() != id) {
        			return false;
        		}
        	}
        	return true;
        } else {
        	return false;
        }
    }
}
