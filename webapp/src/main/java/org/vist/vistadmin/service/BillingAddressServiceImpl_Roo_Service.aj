// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.vist.vistadmin.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vist.vistadmin.domain.BillingAddress;
import org.vist.vistadmin.repository.BillingAddressRepository;
import org.vist.vistadmin.service.BillingAddressServiceImpl;

privileged aspect BillingAddressServiceImpl_Roo_Service {
    
    declare @type: BillingAddressServiceImpl: @Service;
    
    declare @type: BillingAddressServiceImpl: @Transactional;
    
    @Autowired
    BillingAddressRepository BillingAddressServiceImpl.billingAddressRepository;
    
    public long BillingAddressServiceImpl.countAllBillingAddresses() {
        return billingAddressRepository.count();
    }
    
    
    
    public BillingAddress BillingAddressServiceImpl.findBillingAddress(Long id) {
        return billingAddressRepository.findOne(id);
    }
    
    public List<BillingAddress> BillingAddressServiceImpl.findAllBillingAddresses() {
        return billingAddressRepository.findAll();
    }
    
    public List<BillingAddress> BillingAddressServiceImpl.findBillingAddressEntries(int firstResult, int maxResults) {
        return billingAddressRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
    }
    
}