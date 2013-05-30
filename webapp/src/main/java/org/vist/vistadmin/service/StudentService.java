package org.vist.vistadmin.service;

import java.util.List;
import java.util.Set;

import org.springframework.roo.addon.layers.service.RooService;
import org.vist.vistadmin.domain.Discount;
import org.vist.vistadmin.domain.Student;
import org.vist.vistadmin.domain.Course;
import org.vist.vistadmin.domain.Discount;
import org.vist.vistadmin.domain.Teacher;
import org.vist.vistadmin.domain.common.Languages;
import org.vist.vistadmin.domain.common.PersonStatus;

@RooService(domainTypes = { org.vist.vistadmin.domain.Student.class })
public interface StudentService {
	
	public List<Student> findByStatusAndLanguages(PersonStatus status, Languages languages);
	
    public List<Student> findByEmailAddress(String emailAddress);
    
    public boolean validateEmailAddressUnique(Long id, String emailAddress);
    
    public List<Student> findByStatusAndLanguagesAndCompany(PersonStatus status, Languages languages, boolean company);
}
