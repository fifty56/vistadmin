package org.vist.vistadmin.repository;

import java.util.List;

import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;
import org.vist.vistadmin.domain.Course;
import org.vist.vistadmin.domain.CourseStudent;
import org.vist.vistadmin.domain.Student;

@RooJpaRepository(domainType = CourseStudent.class)
public interface CourseStudentRepository {
	
	List<CourseStudent> findByCourse(Course course);
	
	List<CourseStudent> findByStudent(Student student);
	
	List<CourseStudent> findByStudentAndCourse(Student student, Course course);
	
}
