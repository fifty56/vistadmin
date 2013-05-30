package org.vist.vistadmin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.vist.vistadmin.domain.Address;
import org.vist.vistadmin.domain.BillingAddress;
import org.vist.vistadmin.domain.Student;
import org.vist.vistadmin.repository.AddressRepository;
import org.vist.vistadmin.repository.BillingAddressRepository;


public class BillingAddressServiceImpl implements BillingAddressService {
	
	@Autowired
	AddressService addressService;
	
	public void saveBillingAddress(BillingAddress billingAddress) {
		handleAddresses(billingAddress);
        billingAddressRepository.save(billingAddress);
    }
    
    public BillingAddress updateBillingAddress(BillingAddress billingAddress) {
    	handleAddresses(billingAddress);
        return billingAddressRepository.save(billingAddress);
    }
    
    private void handleAddresses(BillingAddress billingAddress) {
    	Address address = handleAddress(billingAddress.getAddress());
		billingAddress.setAddress(address);
		
		Address postalAddress = handleAddress(billingAddress.getPostalAddress());
		billingAddress.setPostalAddress(postalAddress);
    }
    
    private Address handleAddress(Address address) {    	
		if(address.isEmpty()) {						
			if(address.getId() != null) {
				addressService.deleteAddress(address);
			}
			address = null;
		} else {			
			address = addressService.updateAddress(address);
		}
		return address;
    }
    
    public void deleteBillingAddress(BillingAddress billingAddress) {
    	if(billingAddress.getAddress() != null) {
    		addressService.deleteAddress(billingAddress.getAddress());
    	}
    	if(billingAddress.getPostalAddress() != null) {
    		addressService.deleteAddress(billingAddress.getPostalAddress());
    	}
    	billingAddressRepository.delete(billingAddress);
    }
    
    
}
