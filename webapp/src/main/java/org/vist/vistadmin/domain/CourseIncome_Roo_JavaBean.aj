// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.vist.vistadmin.domain;

import java.util.Date;
import org.vist.vistadmin.domain.Course;
import org.vist.vistadmin.domain.CourseIncome;
import org.vist.vistadmin.domain.Student;

privileged aspect CourseIncome_Roo_JavaBean {
    
    public Student CourseIncome.getStudent() {
        return this.student;
    }
    
    public void CourseIncome.setStudent(Student student) {
        this.student = student;
    }
    
    public Course CourseIncome.getCourse() {
        return this.course;
    }
    
    public void CourseIncome.setCourse(Course course) {
        this.course = course;
    }
    
    public double CourseIncome.getAmount() {
        return this.amount;
    }
    
    public void CourseIncome.setAmount(double amount) {
        this.amount = amount;
    }
    
    public Date CourseIncome.getCurrDate() {
        return this.currDate;
    }
    
    public void CourseIncome.setCurrDate(Date currDate) {
        this.currDate = currDate;
    }
    
}
