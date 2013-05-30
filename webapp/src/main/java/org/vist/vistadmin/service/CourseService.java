package org.vist.vistadmin.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.roo.addon.layers.service.RooService;
import org.vist.vistadmin.domain.Course;
import org.vist.vistadmin.domain.common.ClassStatus;
import org.vist.vistadmin.domain.common.Languages;

@RooService(domainTypes = { org.vist.vistadmin.domain.Course.class })
public interface CourseService {
	
	public List<Course> findAllByIds(Collection<Long> ids);
	
	public List<Course> findByStatusNot(ClassStatus status);
	
	public List<Course> findLiveClasses();
	
	public List<Course> findByLanguagesInAndStatusNot(Set<Languages> languageList, ClassStatus status);
	
	public List<Course> findByLanguagesAndStatusNot(Set<Languages> languageList, ClassStatus status);
	
    public List<Course> findByCourseId(String courseId);
    	
    public List<Course> findByStatusNotAndEndDateLessThan(ClassStatus status, Date endDate);
    
    public List<Course> findLiveClassesEndedinTwoWeeks() ;

	public List<Course> findLiveClassesEnded();
	
	public List<Course> findDraftClassesOlderThan2weeks();
	
	public List<Course> findByLangInAndStatusNotAndCompany(Set<Languages> langList, ClassStatus status, boolean company);
	
	public List<Course> findLiveClassesAtGivenPeriod(Date fromDate, Date toDate);
	
	public List<Course> findAllClasses();
}
