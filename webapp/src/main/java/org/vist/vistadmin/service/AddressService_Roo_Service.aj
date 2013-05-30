// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.vist.vistadmin.service;

import java.util.List;
import org.vist.vistadmin.domain.Address;
import org.vist.vistadmin.service.AddressService;

privileged aspect AddressService_Roo_Service {
    
    public abstract long AddressService.countAllAddresses();    
    public abstract void AddressService.deleteAddress(Address address);    
    public abstract Address AddressService.findAddress(Long id);    
    public abstract List<Address> AddressService.findAllAddresses();    
    public abstract List<Address> AddressService.findAddressEntries(int firstResult, int maxResults);    
    public abstract void AddressService.saveAddress(Address address);    
    public abstract Address AddressService.updateAddress(Address address);    
}
