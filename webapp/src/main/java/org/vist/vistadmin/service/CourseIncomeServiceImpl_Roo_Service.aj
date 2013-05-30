// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.vist.vistadmin.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vist.vistadmin.domain.CourseIncome;
import org.vist.vistadmin.repository.CourseIncomeRepository;
import org.vist.vistadmin.service.CourseIncomeServiceImpl;

privileged aspect CourseIncomeServiceImpl_Roo_Service {
    
    declare @type: CourseIncomeServiceImpl: @Service;
    
    declare @type: CourseIncomeServiceImpl: @Transactional;
    
    @Autowired
    CourseIncomeRepository CourseIncomeServiceImpl.courseIncomeRepository;
    
    public long CourseIncomeServiceImpl.countAllCourseIncomes() {
        return courseIncomeRepository.count();
    }
    
    public void CourseIncomeServiceImpl.deleteCourseIncome(CourseIncome courseIncome) {
        courseIncomeRepository.delete(courseIncome);
    }
    
    public CourseIncome CourseIncomeServiceImpl.findCourseIncome(Long id) {
        return courseIncomeRepository.findOne(id);
    }
    
    public List<CourseIncome> CourseIncomeServiceImpl.findAllCourseIncomes() {
        return courseIncomeRepository.findAll();
    }
    
    public List<CourseIncome> CourseIncomeServiceImpl.findCourseIncomeEntries(int firstResult, int maxResults) {
        return courseIncomeRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
    }
    
    public void CourseIncomeServiceImpl.saveCourseIncome(CourseIncome courseIncome) {
        courseIncomeRepository.save(courseIncome);
    }
    
    public CourseIncome CourseIncomeServiceImpl.updateCourseIncome(CourseIncome courseIncome) {
        return courseIncomeRepository.save(courseIncome);
    }
    
}