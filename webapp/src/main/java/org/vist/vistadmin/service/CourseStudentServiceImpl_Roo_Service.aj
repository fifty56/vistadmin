// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.vist.vistadmin.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vist.vistadmin.domain.CourseStudent;
import org.vist.vistadmin.repository.CourseStudentRepository;
import org.vist.vistadmin.service.CourseStudentServiceImpl;

privileged aspect CourseStudentServiceImpl_Roo_Service {
    
    declare @type: CourseStudentServiceImpl: @Service;
    
    declare @type: CourseStudentServiceImpl: @Transactional;
    
    @Autowired
    CourseStudentRepository CourseStudentServiceImpl.courseStudentRepository;
    
    public long CourseStudentServiceImpl.countAllCourseStudents() {
        return courseStudentRepository.count();
    }
    
    public void CourseStudentServiceImpl.deleteCourseStudent(CourseStudent courseStudent) {
        courseStudentRepository.delete(courseStudent);
    }
    
    public CourseStudent CourseStudentServiceImpl.findCourseStudent(Long id) {
        return courseStudentRepository.findOne(id);
    }
    
    public List<CourseStudent> CourseStudentServiceImpl.findAllCourseStudents() {
        return courseStudentRepository.findAll();
    }
    
    public List<CourseStudent> CourseStudentServiceImpl.findCourseStudentEntries(int firstResult, int maxResults) {
        return courseStudentRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
    }
    
    public void CourseStudentServiceImpl.saveCourseStudent(CourseStudent courseStudent) {
        courseStudentRepository.save(courseStudent);
    }
    
    public CourseStudent CourseStudentServiceImpl.updateCourseStudent(CourseStudent courseStudent) {
        return courseStudentRepository.save(courseStudent);
    }
    
}
