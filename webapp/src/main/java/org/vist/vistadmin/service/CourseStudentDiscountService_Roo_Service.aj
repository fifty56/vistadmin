// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.vist.vistadmin.service;

import java.util.List;
import org.vist.vistadmin.domain.CourseStudentDiscount;
import org.vist.vistadmin.service.CourseStudentDiscountService;

privileged aspect CourseStudentDiscountService_Roo_Service {
    
    public abstract long CourseStudentDiscountService.countAllCourseStudentDiscounts();    
    public abstract void CourseStudentDiscountService.deleteCourseStudentDiscount(CourseStudentDiscount courseStudentDiscount);    
    public abstract CourseStudentDiscount CourseStudentDiscountService.findCourseStudentDiscount(Long id);    
    public abstract List<CourseStudentDiscount> CourseStudentDiscountService.findAllCourseStudentDiscounts();    
    public abstract List<CourseStudentDiscount> CourseStudentDiscountService.findCourseStudentDiscountEntries(int firstResult, int maxResults);    
        
    public abstract CourseStudentDiscount CourseStudentDiscountService.updateCourseStudentDiscount(CourseStudentDiscount courseStudentDiscount);    
}