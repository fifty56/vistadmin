// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.vist.vistadmin.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;
import org.vist.vistadmin.domain.CourseStudentDiscount;

privileged aspect CourseStudentDiscount_Roo_Jpa_Entity {
    
    declare @type: CourseStudentDiscount: @Entity;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long CourseStudentDiscount.id;
    
    @Version
    @Column(name = "version")
    private Integer CourseStudentDiscount.version;
    
    public Long CourseStudentDiscount.getId() {
        return this.id;
    }
    
    public void CourseStudentDiscount.setId(Long id) {
        this.id = id;
    }
    
    public Integer CourseStudentDiscount.getVersion() {
        return this.version;
    }
    
    public void CourseStudentDiscount.setVersion(Integer version) {
        this.version = version;
    }
    
}
