// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.vist.vistadmin.service;

import java.util.List;
import org.vist.vistadmin.domain.CourseIncome;
import org.vist.vistadmin.service.CourseIncomeService;

privileged aspect CourseIncomeService_Roo_Service {
    
    public abstract long CourseIncomeService.countAllCourseIncomes();    
    public abstract void CourseIncomeService.deleteCourseIncome(CourseIncome courseIncome);    
    public abstract CourseIncome CourseIncomeService.findCourseIncome(Long id);    
    public abstract List<CourseIncome> CourseIncomeService.findAllCourseIncomes();    
    public abstract List<CourseIncome> CourseIncomeService.findCourseIncomeEntries(int firstResult, int maxResults);    
    public abstract void CourseIncomeService.saveCourseIncome(CourseIncome courseIncome);    
    public abstract CourseIncome CourseIncomeService.updateCourseIncome(CourseIncome courseIncome);    
}
