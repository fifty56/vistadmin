package org.vist.vistadmin.domain;

import java.util.Date;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;
 
@RooJavaBean
@RooToString
@RooJpaEntity
public class CourseTeacher {

    @NotNull
    @ManyToOne
    private Course course;

    @NotNull
    @ManyToOne
    private Teacher teacher;

    @NotNull
    private int numerOfClasses;
    
    /*public int getNumerOfClasses() {
		return numerOfClasses;
	}

	public void setNumerOfClasses(int numerOfClasses) {
		this.numerOfClasses = numerOfClasses;
	}*/

	@Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date endDate;
}
