package org.vist.vistadmin.repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;
import org.vist.vistadmin.domain.Course;
import org.vist.vistadmin.domain.Student;
import org.vist.vistadmin.domain.common.ClassStatus;
import org.vist.vistadmin.domain.common.Languages;
import org.vist.vistadmin.domain.common.PersonStatus;

@RooJpaRepository(domainType = Course.class)
public interface CourseRepository {
	
	List<Course> findAll();
	
	List<Course> findByStatusNot(ClassStatus status);
	
	List<Course> findByStatusIn(Set<ClassStatus> statusList);
	
	List<Course> findByCourseId(String courseId);
	
	List<Course> findByLangInAndStatusNot(Set<Languages> langList, ClassStatus status);
	
	List<Course> findByLangInAndStatusNotAndCompany(Set<Languages> langList, ClassStatus status, boolean company);
	
	List<Course> findByStatusNotAndEndDateLessThan(ClassStatus status, Date endDate);
	
	List<Course> findByStatusNotAndEndDateBetween(ClassStatus status, Date startDate, Date endDate);
	
	List<Course> findByStatusNotAndEndDateGreaterThan(ClassStatus status, Date endDate);
	
	List<Course> findByStatusAndCreationDateLessThan(ClassStatus status, Date creationDate);
	
	List<Course> findByStatusNotAndEndDateGreaterThanAndStartDateLessThan(ClassStatus status, Date startDate, Date stopDate);
	
	List<Course> findByStatusNotInAndEndDateGreaterThanAndStartDateLessThan(Set<ClassStatus> statudList, Date startDate, Date stopDate);
}
