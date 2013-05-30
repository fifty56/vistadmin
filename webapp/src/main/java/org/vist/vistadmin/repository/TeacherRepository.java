package org.vist.vistadmin.repository;

import java.util.List;

import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;
import org.vist.vistadmin.domain.Course;
import org.vist.vistadmin.domain.CourseTeacher;
import org.vist.vistadmin.domain.Teacher;
import org.vist.vistadmin.domain.common.Languages;
import org.vist.vistadmin.domain.common.PersonStatus;

@RooJpaRepository(domainType = Teacher.class)
public interface TeacherRepository {
	
	public List<Teacher> findByStatusAndLanguages(PersonStatus status, Languages languages);
	
	public List<Teacher> findByPersonalDataEmailAddress(String emailAddress);
}
