package org.vist.vistadmin.repository;

import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;
import org.vist.vistadmin.domain.Discount;

@RooJpaRepository(domainType = Discount.class)
public interface DiscountRepository {
}
