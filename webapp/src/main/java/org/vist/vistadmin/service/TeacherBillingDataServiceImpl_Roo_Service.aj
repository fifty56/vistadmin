// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.vist.vistadmin.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vist.vistadmin.domain.TeacherBillingData;
import org.vist.vistadmin.repository.TeacherBillingDataRepository;
import org.vist.vistadmin.service.TeacherBillingDataServiceImpl;

privileged aspect TeacherBillingDataServiceImpl_Roo_Service {
    
    declare @type: TeacherBillingDataServiceImpl: @Service;
    
    declare @type: TeacherBillingDataServiceImpl: @Transactional;
    
    @Autowired
    TeacherBillingDataRepository TeacherBillingDataServiceImpl.teacherBillingDataRepository;
    
    public long TeacherBillingDataServiceImpl.countAllTeacherBillingDatas() {
        return teacherBillingDataRepository.count();
    }
    
    
    
    public TeacherBillingData TeacherBillingDataServiceImpl.findTeacherBillingData(Long id) {
        return teacherBillingDataRepository.findOne(id);
    }
    
    public List<TeacherBillingData> TeacherBillingDataServiceImpl.findAllTeacherBillingDatas() {
        return teacherBillingDataRepository.findAll();
    }
    
    public List<TeacherBillingData> TeacherBillingDataServiceImpl.findTeacherBillingDataEntries(int firstResult, int maxResults) {
        return teacherBillingDataRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
    }
    
}
