// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.vist.vistadmin.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;
import org.vist.vistadmin.domain.TeacherBillingData;

privileged aspect TeacherBillingData_Roo_Jpa_Entity {
    
    declare @type: TeacherBillingData: @Entity;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long TeacherBillingData.id;
    
    @Version
    @Column(name = "version")
    private Integer TeacherBillingData.version;
    
    public Long TeacherBillingData.getId() {
        return this.id;
    }
    
    public void TeacherBillingData.setId(Long id) {
        this.id = id;
    }
    
    public Integer TeacherBillingData.getVersion() {
        return this.version;
    }
    
    public void TeacherBillingData.setVersion(Integer version) {
        this.version = version;
    }
    
}
