package org.vist.vistadmin.repository;

import java.util.List;

import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;
import org.vist.vistadmin.domain.Course;
import org.vist.vistadmin.domain.CourseStudentDiscount;
import org.vist.vistadmin.domain.Discount;
import org.vist.vistadmin.domain.Student;

@RooJpaRepository(domainType = CourseStudentDiscount.class)
public interface CourseStudentDiscountRepository {
	
	List<CourseStudentDiscount> findByCourse(Course course);
	
	List<CourseStudentDiscount> findByStudent(Student student);
	
	List<CourseStudentDiscount> findByCourseAndStudent(Course course, Student student);
	
	List<CourseStudentDiscount> findByCourseAndStudentAndDiscount(Course course, Student student, Discount discount);
	
	List<CourseStudentDiscount> findByDiscount(Discount discount);
	
}
