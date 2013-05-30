package org.vist.vistadmin.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.vist.vistadmin.calendar.VistCalendarService;
import org.vist.vistadmin.domain.Course;
import org.vist.vistadmin.domain.CourseStudent;
import org.vist.vistadmin.domain.CourseTeacher;
import org.vist.vistadmin.domain.Student;
import org.vist.vistadmin.domain.Teacher;
import org.vist.vistadmin.domain.common.ClassStatus;
import org.vist.vistadmin.domain.common.Languages;
import org.vist.vistadmin.domain.common.PersonStatus;
import org.vist.vistadmin.repository.CourseRepository;
import org.vist.vistadmin.util.FlashScopeFilter;
import org.vist.vistadmin.web.CompletedClassController;
import org.vist.vistadmin.web.CourseController;

import com.google.gdata.util.ServiceException;

public class CourseServiceImpl implements CourseService {
	
	@Autowired
	CourseStudentService courseStudentService;
	
	@Autowired
	CourseTeacherService courseTeacherService;
	
	@Autowired
	StudentService studentService;

	@Autowired
	TeacherService teacherService;
	
	@Autowired
	VistCalendarService vistCalendarService;
	
	private static final Logger LOGGER =  LoggerFactory.getLogger(CourseServiceImpl.class);
	
	public List<Course> findAllByIds(Collection<Long> ids) {
		return courseRepository.findAll(getfindAllByIdsSpec(ids));				
	}
	
	public List<Course> findByStatusNot(ClassStatus status) {
		return courseRepository.findByStatusNot(status);
	}
	
	public List<Course> findByLanguagesInAndStatusNot(Set<Languages> languageList, ClassStatus status) {
		return courseRepository.findByLangInAndStatusNot(languageList, status);
	}
	
	public List<Course> findByLanguagesAndStatusNot(Set<Languages> languageList, ClassStatus status) {		
		return courseRepository.findAll(findByLanguagesBySpec(languageList, status));				
	}
	
	public static Specification<Course> getfindAllByIdsSpec(final Collection<Long> idList) {
		return new Specification<Course>() {
			@Override
			public Predicate toPredicate(Root<Course> courseRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {							
				return courseRoot.get("id").in(idList);
			}
		};
	}

	public void saveCourse(Course course, HttpServletRequest request) {
		course.setCreationDate(new Date());
        courseRepository.save(course);
        try {
			vistCalendarService.synchronizeCourse(course);
		} catch (Exception e) {
			request.setAttribute(FlashScopeFilter.FLASH_PREFIX + FlashScopeFilter.WARNING_MESSAGE_KEY, VistCalendarService.WARNING_COULDNT_CREATE_GOOGLE_CAL); 
			LOGGER.error("error during updating google calendar", e);
		} 
    }
	
    public List<Course> findByCourseId(String courseId) {
    	return courseRepository.findByCourseId(courseId);
    }        
	
	private Specification<Course> findByLanguagesBySpec(final Set<Languages> languageList, final ClassStatus status) {
		return new Specification<Course>() {
			@Override
			public Predicate toPredicate(Root<Course> courseRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {				
				Predicate predicate = cb.conjunction();
				for(Languages language : languageList) {
					Expression<Collection<Languages>> languages = courseRoot.get("languages");					
					predicate = cb.or(predicate, cb.isMember(language, languages));					
				}
				return predicate;	 				
			}
		};
	}
	
	public List<Course> findByStatusNotAndEndDateLessThan(ClassStatus status, Date endDate) {
		return courseRepository.findByStatusNotAndEndDateLessThan( status, endDate);
	}

	public List<Course> findLiveClassesAtGivenPeriod(Date fromDate, Date toDate) {		
		Set<ClassStatus> classStatuses = new HashSet<ClassStatus>();
		classStatuses.add(ClassStatus.ARCHIVED);
		classStatuses.add(ClassStatus.DRAFT);
		return courseRepository.findByStatusNotInAndEndDateGreaterThanAndStartDateLessThan(classStatuses, fromDate, toDate);
	}
	
	public List<Course> findLiveClassesEndedinTwoWeeks() {
		ClassStatus status = ClassStatus.ARCHIVED;
		Long now = System.currentTimeMillis();
		long inWeeks = now + 1000 * 60 * 60 * 24 * 14;		
		return courseRepository.findByStatusNotAndEndDateBetween(status, new Date(now), new Date(inWeeks));
	}

	public List<Course> findLiveClassesEnded() {
		ClassStatus status = ClassStatus.ARCHIVED;
		Long now = System.currentTimeMillis();
		return courseRepository.findByStatusNotAndEndDateLessThan(status, new Date(now));
	}
	
	public List<Course> findDraftClassesOlderThan2weeks() {
		ClassStatus status = ClassStatus.DRAFT;
		Long now = System.currentTimeMillis();
		long twoWeeksAgo = now - 1000 * 60 * 60 * 24 * 14;
		return courseRepository.findByStatusAndCreationDateLessThan(status, new Date(twoWeeksAgo));
	}	

	public Course updateCourse(Course course, HttpServletRequest request) {		
		if(course.getStatus().equals(ClassStatus.ARCHIVED)) {
			Course oldCourse = courseRepository.findOne(course.getId());
			if(!oldCourse.getStatus().equals(ClassStatus.ARCHIVED)) {
				LOGGER.debug("archive a course, id: " + course.getId() + ", courseId: " + course.getCourseId());
				
				List<CourseStudent> courseStudentList = courseStudentService.findByCourse(course);
				if(courseStudentList != null && courseStudentList.size() > 0) {
					for (CourseStudent courseStudent : courseStudentList) {
						Student student = courseStudent.getStudent();
						List<CourseStudent> studentCourses = courseStudentService.findByStudent(student);
						if(studentCourses.size() == 1) {
							LOGGER.debug("Student has no more courses, archive it: " + student.getId());
							student.setStatus(PersonStatus.ARCHIVED);
							studentService.updateStudent(student);							
						}						
					}
				}
				
				List<CourseTeacher> courseTeacherList = courseTeacherService.findByCourse(course);
				if(courseTeacherList != null && courseTeacherList.size() > 0) {
					for (CourseTeacher courseTeacher : courseTeacherList) {
						Teacher teacher = courseTeacher.getTeacher();
						List<CourseTeacher> teacherCourses = courseTeacherService.findByTeacher(teacher);
						if(teacherCourses.size() == 1) {
							LOGGER.debug("Teacher has no more courses, archive it: " + teacher.getId());
							teacher.setStatus(PersonStatus.ARCHIVED);
							teacherService.updateTeacher(teacher);							
						}						
					}
				}
			}
		}
		
		try {
			vistCalendarService.synchronizeCourse(course);
		} catch (Exception e) {
			request.setAttribute(FlashScopeFilter.FLASH_PREFIX + FlashScopeFilter.WARNING_MESSAGE_KEY, VistCalendarService.WARNING_COULDNT_UPDATE_GOOGLE_CAL); 
			LOGGER.error("error during updating google calendar", e);
		}
		
        return courseRepository.save(course);
    }

	public void deleteCourse(Course course, HttpServletRequest request) {
        courseRepository.delete(course);
        try {
			vistCalendarService.removeCourse(course);
		} catch (Exception e) {
			request.setAttribute(FlashScopeFilter.FLASH_PREFIX + FlashScopeFilter.WARNING_MESSAGE_KEY, VistCalendarService.WARNING_COULDNT_DELETE_GOOGLE_CAL); 
			LOGGER.error("error during updating google calendar", e);
		}
    }
	
	public List<Course> findByLangInAndStatusNotAndCompany(Set<Languages> langList, ClassStatus status, boolean company) {
		return courseRepository.findByLangInAndStatusNotAndCompany(langList, status, company);		
	}

	public List<Course> findLiveClasses() {
		Set<ClassStatus> liveStatusSet = new HashSet<ClassStatus>();
		liveStatusSet.add(ClassStatus.FINISHED);
		liveStatusSet.add(ClassStatus.STARTED);
		return courseRepository.findByStatusIn(liveStatusSet);		
	}
	
	public List<Course> findAllClasses() {
		return courseRepository.findAll();		
	}
}
