package org.vist.vistadmin.repository;

import java.util.Date;
import java.util.List;

import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;
import org.vist.vistadmin.domain.Course;
import org.vist.vistadmin.domain.CourseStudent;
import org.vist.vistadmin.domain.CourseTeacher;
import org.vist.vistadmin.domain.Student;
import org.vist.vistadmin.domain.Teacher;

@RooJpaRepository(domainType = CourseTeacher.class)
public interface CourseTeacherRepository {
	
	List<CourseTeacher> findByCourse(Course course);
	
	List<CourseTeacher> findByTeacher(Teacher teacher);
	
	List<CourseTeacher> findByTeacherAndCourse(Teacher teacher, Course course);
	
	List<CourseTeacher> findByCourseAndStartDateBetweenAndTeacherNot(Course course, Date startDate, Date endDate, Teacher teacher);
	
	List<CourseTeacher> findByCourseAndEndDateBetweenAndTeacherNot(Course course, Date startDate, Date endDate, Teacher teacher);
}
