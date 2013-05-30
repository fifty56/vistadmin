package org.vist.vistadmin.repository;

import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;
import org.vist.vistadmin.domain.TeacherBillingData;

@RooJpaRepository(domainType = TeacherBillingData.class)
public interface TeacherBillingDataRepository {
}
