// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.vist.vistadmin.service;

import java.util.List;
import org.vist.vistadmin.domain.CourseTeacher;
import org.vist.vistadmin.service.CourseTeacherService;

privileged aspect CourseTeacherService_Roo_Service {
    
    public abstract long CourseTeacherService.countAllCourseTeachers();    
    public abstract void CourseTeacherService.deleteCourseTeacher(CourseTeacher courseTeacher);    
    public abstract CourseTeacher CourseTeacherService.findCourseTeacher(Long id);    
    public abstract List<CourseTeacher> CourseTeacherService.findAllCourseTeachers();    
    public abstract List<CourseTeacher> CourseTeacherService.findCourseTeacherEntries(int firstResult, int maxResults);    
    public abstract void CourseTeacherService.saveCourseTeacher(CourseTeacher courseTeacher);    
    public abstract CourseTeacher CourseTeacherService.updateCourseTeacher(CourseTeacher courseTeacher);    
}