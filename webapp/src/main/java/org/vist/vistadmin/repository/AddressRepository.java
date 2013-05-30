package org.vist.vistadmin.repository;

import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;
import org.vist.vistadmin.domain.Address;

@RooJpaRepository(domainType = Address.class)
public interface AddressRepository {
}
