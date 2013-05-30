package org.vist.vistadmin.domain;

import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaEntity
public class CourseStudentDiscount {

    @NotNull
    @ManyToOne
    private Course course;

    @NotNull
    @ManyToOne
    private Student student;

    private String comment;

    @NotNull
    @ManyToOne
    private Discount discount;
    

}
