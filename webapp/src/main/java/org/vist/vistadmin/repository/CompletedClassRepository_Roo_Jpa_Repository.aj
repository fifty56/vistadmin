// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.vist.vistadmin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.vist.vistadmin.domain.CompletedClass;
import org.vist.vistadmin.repository.CompletedClassRepository;

privileged aspect CompletedClassRepository_Roo_Jpa_Repository {
    
    declare parents: CompletedClassRepository extends JpaRepository<CompletedClass, Long>;
    
    declare parents: CompletedClassRepository extends JpaSpecificationExecutor<CompletedClass>;
    
    declare @type: CompletedClassRepository: @Repository;
    
}
