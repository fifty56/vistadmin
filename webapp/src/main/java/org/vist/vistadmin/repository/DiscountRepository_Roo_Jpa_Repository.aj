// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.vist.vistadmin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.vist.vistadmin.domain.Discount;
import org.vist.vistadmin.repository.DiscountRepository;

privileged aspect DiscountRepository_Roo_Jpa_Repository {
    
    declare parents: DiscountRepository extends JpaRepository<Discount, Long>;
    
    declare parents: DiscountRepository extends JpaSpecificationExecutor<Discount>;
    
    declare @type: DiscountRepository: @Repository;
    
}