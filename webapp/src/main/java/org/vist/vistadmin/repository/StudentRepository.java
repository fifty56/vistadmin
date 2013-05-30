package org.vist.vistadmin.repository;

import java.util.List;

import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;
import org.vist.vistadmin.domain.Student;
import org.vist.vistadmin.domain.Teacher;
import org.vist.vistadmin.domain.common.Languages;
import org.vist.vistadmin.domain.common.PersonStatus;

@RooJpaRepository(domainType = Student.class)
public interface StudentRepository {
	
	public List<Student> findByStatusAndLanguages(PersonStatus status, Languages languages);
	
	public List<Student> findByStatusAndLanguagesAndCompany(PersonStatus status, Languages languages, boolean company);
	
	public List<Student> findByPersonalDataEmailAddress(String emailAddress);
}
