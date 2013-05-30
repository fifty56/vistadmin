package org.vist.vistadmin.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.roo.addon.layers.service.RooService;
import org.vist.vistadmin.domain.Course;
import org.vist.vistadmin.domain.CourseStudent;
import org.vist.vistadmin.domain.CourseStudentDiscount;
import org.vist.vistadmin.domain.Discount;
import org.vist.vistadmin.domain.Student;

@RooService(domainTypes = { org.vist.vistadmin.domain.CourseStudentDiscount.class })
public interface CourseStudentDiscountService {
	
	public List<CourseStudentDiscount> findByCourse(Course course);
	
	public List<CourseStudentDiscount> findByStudent(Student student);
	
	public List<CourseStudentDiscount> findByDiscount(Discount discount);
	
	public List<CourseStudentDiscount> findByCourseAndStudentAndDiscount(Course course, Student student, Discount discount);
	
	public List<CourseStudentDiscount> findByCourseAndStudent(Course course, Student student);
	
	public List<CourseStudentDiscount> findByCoursStudent(CourseStudent courseStudent);
	
	public void saveCourseStudentDiscount(CourseStudentDiscount courseStudentDiscount, HttpServletRequest request);
}
