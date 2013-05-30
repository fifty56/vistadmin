package org.vist.vistadmin.service;

import java.util.Date;
import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;
import org.vist.vistadmin.domain.Course;
import org.vist.vistadmin.domain.CourseStudent;
import org.vist.vistadmin.domain.Student;

@RooService(domainTypes = { org.vist.vistadmin.domain.CourseStudent.class })
public interface CourseStudentService {
	
	public List<CourseStudent> findByCourse(Course course);
	
	public List<CourseStudent> findByStudent(Student student);
	
	public List<CourseStudent> findByStudentAndCourse(Student student, Course course);
	
	public List<CourseStudent> getCurrentStudentsByDateAndCourse(Course course);
	
	public List<CourseStudent> getStudentsByDateAndCourse(Course course, Date date);
}
