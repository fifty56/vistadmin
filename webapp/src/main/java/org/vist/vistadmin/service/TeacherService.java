package org.vist.vistadmin.service;

import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;
import org.vist.vistadmin.domain.Teacher;
import org.vist.vistadmin.domain.common.Languages;
import org.vist.vistadmin.domain.common.PersonStatus;

@RooService(domainTypes = { org.vist.vistadmin.domain.Teacher.class })
public interface TeacherService {
	
	public List<Teacher> findByStatusAndLanguages(PersonStatus status, Languages language);
	
	public List<Teacher> findByEmailAddress(String emailAddress);
	
	public boolean validateEmailAddressUnique(Long id, String emailAddress);
}
