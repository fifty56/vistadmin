package org.vist.vistadmin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.vist.vistadmin.domain.Address;
import org.vist.vistadmin.domain.TeacherBillingData;


public class TeacherBillingDataServiceImpl implements TeacherBillingDataService {
	
	@Autowired
	 AddressService addressService;
	
    public void saveTeacherBillingData(TeacherBillingData teacherBillingData) {
    	Address address = teacherBillingData.getAddress();
    	if(StudentServiceImpl.handleAddress(addressService, address) == null) {
    		teacherBillingData.setAddress(null);
    	}
        teacherBillingDataRepository.save(teacherBillingData);
    }
    
    public TeacherBillingData updateTeacherBillingData(TeacherBillingData teacherBillingData) {
    	Address address = teacherBillingData.getAddress();
    	if(StudentServiceImpl.handleAddress(addressService, address) == null) {
    		teacherBillingData.setAddress(null);
    	}
        return teacherBillingDataRepository.save(teacherBillingData);
    }
    
    public void deleteTeacherBillingData(TeacherBillingData teacherBillingData) {
    	if(teacherBillingData.getAddress() != null) {
    		addressService.deleteAddress(teacherBillingData.getAddress());
    	}
        teacherBillingDataRepository.delete(teacherBillingData);
    }
	
}
