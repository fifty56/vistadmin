// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.vist.vistadmin.service;

import java.util.List;
import org.vist.vistadmin.domain.CompletedClass;
import org.vist.vistadmin.service.CompletedClassService;

privileged aspect CompletedClassService_Roo_Service {
    
    public abstract long CompletedClassService.countAllCompletedClasses();    
    public abstract void CompletedClassService.deleteCompletedClass(CompletedClass completedClass);    
    public abstract CompletedClass CompletedClassService.findCompletedClass(Long id);    
    public abstract List<CompletedClass> CompletedClassService.findAllCompletedClasses();    
    public abstract List<CompletedClass> CompletedClassService.findCompletedClassEntries(int firstResult, int maxResults);            
    public abstract CompletedClass CompletedClassService.updateCompletedClass(CompletedClass completedClass);    
}
