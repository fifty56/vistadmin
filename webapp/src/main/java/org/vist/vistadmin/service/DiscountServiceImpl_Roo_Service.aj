// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.vist.vistadmin.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vist.vistadmin.domain.Discount;
import org.vist.vistadmin.repository.DiscountRepository;
import org.vist.vistadmin.service.DiscountServiceImpl;

privileged aspect DiscountServiceImpl_Roo_Service {
    
    declare @type: DiscountServiceImpl: @Service;
    
    declare @type: DiscountServiceImpl: @Transactional;
    
    @Autowired
    DiscountRepository DiscountServiceImpl.discountRepository;
    
    public long DiscountServiceImpl.countAllDiscounts() {
        return discountRepository.count();
    }
    
    public void DiscountServiceImpl.deleteDiscount(Discount discount) {
        discountRepository.delete(discount);
    }
    
    public Discount DiscountServiceImpl.findDiscount(Long id) {
        return discountRepository.findOne(id);
    }
    
    public List<Discount> DiscountServiceImpl.findAllDiscounts() {
        return discountRepository.findAll();
    }
    
    public List<Discount> DiscountServiceImpl.findDiscountEntries(int firstResult, int maxResults) {
        return discountRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
    }
    
    public void DiscountServiceImpl.saveDiscount(Discount discount) {
        discountRepository.save(discount);
    }
    
    public Discount DiscountServiceImpl.updateDiscount(Discount discount) {
        return discountRepository.save(discount);
    }
    
}
