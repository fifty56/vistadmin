package org.vist.vistadmin.service;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vist.vistadmin.domain.Address;
import org.vist.vistadmin.domain.BillingAddress;
import org.vist.vistadmin.domain.Course;
import org.vist.vistadmin.domain.CourseStudent;
import org.vist.vistadmin.domain.Discount;
import org.vist.vistadmin.domain.Student;
import org.vist.vistadmin.domain.Teacher;
import org.vist.vistadmin.domain.common.DiscountType;
import org.vist.vistadmin.domain.common.Languages;
import org.vist.vistadmin.domain.common.PersonStatus;
import org.vist.vistadmin.repository.AddressRepository;
import org.vist.vistadmin.repository.BillingAddressRepository;


public class StudentServiceImpl implements StudentService {
	
	private static Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);
	
	 @Autowired
	 BillingAddressService billingAddressService;
	
	 @Autowired
	 AddressService addressService;
	 
    public void saveStudent(Student student) {    	    	
    	Address address = student.getAddress();
    	if(handleAddress(addressService, address) == null) {
    		logger.debug("set address on student to null");
    		student.setAddress(null);
    	}
    	handleBillingAddress(student, false);
    	studentRepository.save(student);
    }
    
    public Student updateStudent(Student student) {    	
    	Address address = student.getAddress();
    	if(handleAddress(addressService, address) == null) {
    		logger.debug("set address on student to null");	
    		student.setAddress(null);
    	}
    	handleBillingAddress(student, true);
        return studentRepository.save(student);
    }
    
    public void handleBillingAddress(Student student, boolean isUpdate) {
    	logger.debug("handleBillingAddress called, student: " + student.getLabelString() + ", isUpdate: " + isUpdate);
    	BillingAddress ba = student.getBillingAddress();
    	if(ba != null) {
	    	logger.debug("billingAddress isEmpty:" + ba.isEmpty());
	    	if(ba.isEmpty()) {
	    		if(ba.getId() != null) {
	    			logger.debug("delete billingAddress: " + ba.getId());		
	    			billingAddressService.deleteBillingAddress(ba);
	    		}
	    		ba = null;
	    		logger.debug("set billingAddress to null on student");
	    		student.setBillingAddress(ba);
	    	} else {
	    		if(ba.getId() != null) {
	    			logger.debug("update billingAddress");
	    			billingAddressService.updateBillingAddress(ba);
	    		} else {
	    			logger.debug("save new billingAddress");
	    			billingAddressService.saveBillingAddress(ba);
	    		} 
	    	}
    	}
    	//student.setBillingAddress(ba);
    }    	
    
    public void deleteStudent(Student student) {
    	if(student.getAddress() != null) {
    		addressService.deleteAddress(student.getAddress());	
    	}
    	if(student.getBillingAddress() != null) {
    		billingAddressService.deleteBillingAddress(student.getBillingAddress());
    	}    
        studentRepository.delete(student);
    }
    
    
    public static Address handleAddress(AddressService addressService, Address address) {    	
    	logger.debug("handleAddress called, address: " + address);
    	if(address != null) {
	    	logger.debug("isEmpty: " + address.isEmpty());
	    	if(address.isEmpty()) {
	    		if(address.getId() != null) {
	    			logger.debug("delete address: " + address.getId());
	    			addressService.deleteAddress(address);
	    		}
	    		logger.debug("set address to null");
	    		address = null;    		
	    	} else {
	    		logger.debug("if(address.getId(): " + address.getId());
	    		if(address.getId() != null) {
	    			addressService.updateAddress(address);
	    		} else {
	    			addressService.saveAddress(address);
	    		}
	    	}
    	}
    	return address;    	
    }
    
    public List<Student> findByStatusAndLanguages(PersonStatus status, Languages languages) {
    	return studentRepository.findByStatusAndLanguages(status, languages);
    }
    
    
    public List<Student> findByEmailAddress(String emailAddress) {
    	return studentRepository.findByPersonalDataEmailAddress(emailAddress);
    }
     
    public boolean validateEmailAddressUnique(Long id, String emailAddress) {
    	List<Student> studentList = findByEmailAddress(emailAddress);
        if(studentList == null || studentList.size() == 0) {
        	return true;
        } else if(id != null) {
        	for(Student student : studentList) {
        		if(student.getId() != id) {
        			return false;
        		}
        	}
        	return true;
        } else {
        	return false;
        }
    }
    
	public List<Student> findByStatusAndLanguagesAndCompany(PersonStatus status, Languages languages, boolean company) {
		return studentRepository.findByStatusAndLanguagesAndCompany(status, languages, company);
	}
}

