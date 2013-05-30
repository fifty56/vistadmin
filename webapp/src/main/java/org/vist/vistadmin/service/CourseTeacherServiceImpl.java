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
import org.vist.vistadmin.domain.Teacher;
import org.vist.vistadmin.domain.common.ClassFormat;
import org.vist.vistadmin.util.DateUtil;
import org.vist.vistadmin.web.ReportController;

public class CourseTeacherServiceImpl implements CourseTeacherService {

	private static final Logger LOGGER =  LoggerFactory.getLogger(CourseTeacherServiceImpl.class);
	
	public List<CourseTeacher> findByCourse(Course course) {
		return courseTeacherRepository.findByCourse(course);
	}
	
	public List<CourseTeacher> findByTeacher(Teacher teacher) {
		return courseTeacherRepository.findByTeacher(teacher);
	}
	
	public List<CourseTeacher> findByTeacherAndCourse(Teacher teacher, Course course) {
		return courseTeacherRepository.findByTeacherAndCourse(teacher, course);
	}
		
	public List<CourseTeacher> findByCourseAndStartDateBetweenAndNotTeacher(Course course, Date startDate, Date endDate, Teacher teacher) {
		return courseTeacherRepository.findByCourseAndStartDateBetweenAndTeacherNot(course, startDate, endDate, teacher);
	}
	
	public List<CourseTeacher> findByCourseAndEndDateBetweenAndNotTeacher(Course course, Date startDate, Date endDate, Teacher teacher) {
		return courseTeacherRepository.findByCourseAndEndDateBetweenAndTeacherNot(course, startDate, endDate, teacher);
	}
	
	public List<CourseTeacher> getCurrentTeachersByDateAndCourse(Course course) throws CourseWithMoreCurrentTeachers {		
		Date currDate = new Date();
		return getCurrentTeachersByDateAndCourse(course, currDate);
	}

	public List<CourseTeacher> getCurrentTeachersByDateAndCourse(Course course, Date date) throws CourseWithMoreCurrentTeachers {
		List<CourseTeacher> retVal = new ArrayList<CourseTeacher>();
		List<CourseTeacher> courseTeacherList = courseTeacherRepository.findByCourse(course);		
		for (CourseTeacher courseTeacher : courseTeacherList) {
			LOGGER.debug("check courseTeacher: " + courseTeacher);
			if(DateUtil.isBetweenOrSameDay(date, courseTeacher.getStartDate(), courseTeacher.getEndDate())) {
				LOGGER.debug("courseTeacher is current");	
				retVal.add(courseTeacher);
			}
		}
		if(!course.getCourseFormat().equals(ClassFormat.INSTANT) && retVal.size() != 1) {
			throw new CourseWithMoreCurrentTeachers(retVal);
		}
		return retVal;
	}
	
	
	public List<CourseTeacher> isValidDatesNotOverlap(CourseTeacher courseTeacher) {
		List<CourseTeacher> a = findByCourseAndStartDateBetweenAndNotTeacher(courseTeacher.getCourse(), courseTeacher.getStartDate(), courseTeacher.getEndDate(), courseTeacher.getTeacher());
		List<CourseTeacher> b = findByCourseAndEndDateBetweenAndNotTeacher(courseTeacher.getCourse(), courseTeacher.getStartDate(), courseTeacher.getEndDate(), courseTeacher.getTeacher());
		if((a == null || a.size() == 0) && (b == null || b.size() == 0)) {
			return null;
		} else {
			List<CourseTeacher> retList = new ArrayList<CourseTeacher>();
			if(a != null && a.size() != 0) {
				retList = a;
			}
			if(b != null && b.size() != 0) {
				retList.addAll(b);
			}
			return retList;
		}
	}
}
