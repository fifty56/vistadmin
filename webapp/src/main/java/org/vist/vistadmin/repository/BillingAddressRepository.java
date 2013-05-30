package org.vist.vistadmin.repository;

import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;
import org.vist.vistadmin.domain.BillingAddress;

@RooJpaRepository(domainType = BillingAddress.class)
public interface BillingAddressRepository {
}
