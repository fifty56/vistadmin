// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.vist.vistadmin.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;
import org.vist.vistadmin.domain.TeacherPayment;

privileged aspect TeacherPayment_Roo_Jpa_Entity {
    
    declare @type: TeacherPayment: @Entity;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long TeacherPayment.id;
    
    @Version
    @Column(name = "version")
    private Integer TeacherPayment.version;
    
    public Long TeacherPayment.getId() {
        return this.id;
    }
    
    public void TeacherPayment.setId(Long id) {
        this.id = id;
    }
    
    public Integer TeacherPayment.getVersion() {
        return this.version;
    }
    
    public void TeacherPayment.setVersion(Integer version) {
        this.version = version;
    }
    
}
