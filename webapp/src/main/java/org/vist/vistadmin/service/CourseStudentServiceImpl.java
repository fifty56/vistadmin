package org.vist.vistadmin.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vist.vistadmin.domain.Course;
import org.vist.vistadmin.domain.CourseStudent;
import org.vist.vistadmin.domain.CourseTeacher;
import org.vist.vistadmin.domain.Student;
import org.vist.vistadmin.util.DateUtil;


public class CourseStudentServiceImpl implements CourseStudentService {
	
	private static final Logger LOGGER =  LoggerFactory.getLogger(CourseStudentServiceImpl.class);
	
	public List<CourseStudent> findByCourse(Course course) {
		return courseStudentRepository.findByCourse(course);
	}
	
	public List<CourseStudent> findByStudent(Student student) {
		return courseStudentRepository.findByStudent(student);		
	}
	
	
	public List<CourseStudent> findByStudentAndCourse(Student student, Course course) {
		return courseStudentRepository.findByStudentAndCourse(student, course);
	}
	
	
	public List<CourseStudent> getCurrentStudentsByDateAndCourse(Course course) {
		List<CourseStudent> retVal = new ArrayList<CourseStudent>();
		List<CourseStudent> courseStudentList = courseStudentRepository.findByCourse(course);
		Date currDate = new Date();
		for (CourseStudent courseStudent : courseStudentList) {
			LOGGER.debug("check courseStudent: " + courseStudent);
			if(DateUtil.isBetweenOrSameDay(currDate, courseStudent.getStartDate(), courseStudent.getEndDate())) {
				LOGGER.debug("courseStudent is current");	
				retVal.add(courseStudent);
			}
		}
		return retVal;
	}
	
	public List<CourseStudent> getStudentsByDateAndCourse(Course course, Date date) {
		List<CourseStudent> retVal = new ArrayList<CourseStudent>();
		List<CourseStudent> courseStudentList = courseStudentRepository.findByCourse(course);		
		for (CourseStudent courseStudent : courseStudentList) {
			LOGGER.debug("check courseStudent: " + courseStudent);
			if(DateUtil.isBetweenOrSameDay(date, courseStudent.getStartDate(), courseStudent.getEndDate())) {
				LOGGER.debug("courseStudent is in date");	
				retVal.add(courseStudent);
			}
		}
		return retVal;
	}
	
}
