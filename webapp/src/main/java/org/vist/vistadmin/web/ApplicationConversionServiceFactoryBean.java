package org.vist.vistadmin.web;

import java.text.SimpleDateFormat;

import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.roo.addon.web.mvc.controller.converter.RooConversionService;
import org.vist.vistadmin.domain.Course;
import org.vist.vistadmin.domain.CourseIncome;
import org.vist.vistadmin.domain.Student;
import org.vist.vistadmin.domain.Teacher;
import org.vist.vistadmin.domain.common.ClassFormat;
import org.vist.vistadmin.domain.common.ClassLevels;
import org.vist.vistadmin.domain.common.ClassSpecializationType;
import org.vist.vistadmin.domain.common.ClassStatus;
import org.vist.vistadmin.domain.common.ClassType;
import org.vist.vistadmin.domain.common.DiscountType;
import org.vist.vistadmin.domain.common.InstantCourseFormat;
import org.vist.vistadmin.domain.common.InstantCourseType;
import org.vist.vistadmin.domain.common.Languages;
import org.vist.vistadmin.domain.common.PersonStatus;
import org.vist.vistadmin.domain.common.Room;
import org.vist.vistadmin.domain.common.SelectOptionList;

/**
 * A central place to register application converters and formatters. 
 */
@RooConversionService
public class ApplicationConversionServiceFactoryBean extends FormattingConversionServiceFactoryBean {

	SimpleDateFormat dateFormat = new SimpleDateFormat(BaseController.DATE_TIME_FORMAT_STR);	
	
	@Override
	protected void installFormatters(FormatterRegistry registry) {
		super.installFormatters(registry);
		// Register application converters and formatters
	}
	
	
    public Converter<Room, String> getRoomToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<Room, java.lang.String>() {
            public String convert(Room enumvalue) {
                return enumvalue.getLabel();
            }
        };
    }
	
    public Converter<Course, String> getCourseToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<org.vist.vistadmin.domain.Course, java.lang.String>() {
            public String convert(Course course) {
                return new StringBuilder().append(course.getCourseId()).append(", ").append(course.getLang()).append(" ").append(course.getCourseFormat()).append(", ").append(dateFormat.format(course.getStartDate())).append(" - ").append(dateFormat.format(course.getEndDate())).toString();
            }
        };
    }

    public Converter<Languages, String> getLanguagesToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<Languages, java.lang.String>() {
            public String convert(Languages language) {
                return language.getLabel();
            }
        };
    }
    
    public Converter<InstantCourseType, String> getInstantCourseTypeToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<InstantCourseType, java.lang.String>() {
            public String convert(InstantCourseType instantCourseType) {
                return instantCourseType.getLabel();
            }
        };
    }

    public Converter<InstantCourseFormat, String> getInstantCourseFormatToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<InstantCourseFormat, java.lang.String>() {
            public String convert(InstantCourseFormat enumvalue) {
                return enumvalue.getLabel();
            }
        };
    }
    
    public Converter<ClassFormat, String> getClassFormatToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<ClassFormat, java.lang.String>() {
            public String convert(ClassFormat enumvalue) {
                return enumvalue.getLabel();
            }
        };
    }
    
    public Converter<DiscountType, String> getDiscountTypeToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<DiscountType, java.lang.String>() {
            public String convert(DiscountType enumvalue) {
                return enumvalue.getLabel();
            }
        };
    }
    
    public Converter<ClassSpecializationType, String> getClassSpecializationTypeToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<ClassSpecializationType, java.lang.String>() {
            public String convert(ClassSpecializationType enumvalue) {
                return enumvalue.getLabel();
            }
        };
    }
    
    public Converter<ClassStatus, String> getClassStatusToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<ClassStatus, java.lang.String>() {
            public String convert(ClassStatus enumvalue) {
                return enumvalue.getLabel();
            }
        };
    }
    
    public Converter<ClassType, String> getClassTypeToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<ClassType, java.lang.String>() {
            public String convert(ClassType enumvalue) {
                return enumvalue.getLabel();
            }
        };
    }

    public Converter<PersonStatus, String> getPersonStatusToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<PersonStatus, java.lang.String>() {
            public String convert(PersonStatus enumvalue) {
                return enumvalue.getLabel();
            }
        };
    }
    
    public Converter<Teacher, String> getTeacherToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<org.vist.vistadmin.domain.Teacher, java.lang.String>() {
            public String convert(Teacher teacher) {
            	StringBuilder sb = new StringBuilder().append(teacher.getPersonalData().getLastName()).append(" ").append(teacher.getPersonalData().getFirstName());
            	if(teacher.getLanguages().size() > 0) {
            		sb.append(" [");
            		boolean isFirst = true;
            		for(org.vist.vistadmin.domain.common.Languages lang : teacher.getLanguages()) {            			
            			if(isFirst) {
            				isFirst = false;
            			} else {
            				sb.append(",");
            			}
            			sb.append(lang);
            		}
            		sb.append("]");
            	}
            	return sb.toString();                
            }
        };
    }

    public Converter<CourseIncome, String> getCourseIncomeToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<org.vist.vistadmin.domain.CourseIncome, java.lang.String>() {
            public String convert(CourseIncome courseIncome) {
                return new StringBuilder().append(courseIncome.getYear()).append(" ").append(courseIncome.getMonth()).append(" ").append(courseIncome.getWeek()).append(" ").append(courseIncome.getAmount()).toString();
            }
        };
    }
	

    public Converter<Student, String> getStudentToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<org.vist.vistadmin.domain.Student, java.lang.String>() {
            public String convert(Student student) {
            	StringBuilder sb = new StringBuilder().append(student.getPersonalData().getLastName()).append(" ").append(student.getPersonalData().getFirstName()).append(", ").append(student.getPersonalData().getEmailAddress());
            	return sb.toString();                
            }
        };
    }

    public void installLabelConverters(FormatterRegistry registry) {
        registry.addConverter(getBillingAddressToStringConverter());
        registry.addConverter(getIdToBillingAddressConverter());
        registry.addConverter(getStringToBillingAddressConverter());
        registry.addConverter(getCompletedClassToStringConverter());
        registry.addConverter(getIdToCompletedClassConverter());
        registry.addConverter(getStringToCompletedClassConverter());
        registry.addConverter(getCourseToStringConverter());
        registry.addConverter(getIdToCourseConverter());
        registry.addConverter(getStringToCourseConverter());
        registry.addConverter(getCourseIncomeToStringConverter());
        registry.addConverter(getIdToCourseIncomeConverter());
        registry.addConverter(getStringToCourseIncomeConverter());
        registry.addConverter(getCourseStudentToStringConverter());
        registry.addConverter(getIdToCourseStudentConverter());
        registry.addConverter(getStringToCourseStudentConverter());
        registry.addConverter(getCourseStudentDiscountToStringConverter());
        registry.addConverter(getIdToCourseStudentDiscountConverter());
        registry.addConverter(getStringToCourseStudentDiscountConverter());
        registry.addConverter(getCourseTeacherToStringConverter());
        registry.addConverter(getIdToCourseTeacherConverter());
        registry.addConverter(getStringToCourseTeacherConverter());
        registry.addConverter(getDiscountToStringConverter());
        registry.addConverter(getIdToDiscountConverter());
        registry.addConverter(getStringToDiscountConverter());
        registry.addConverter(getStudentToStringConverter());
        registry.addConverter(getIdToStudentConverter());
        registry.addConverter(getStringToStudentConverter());
        registry.addConverter(getTeacherToStringConverter());
        registry.addConverter(getIdToTeacherConverter());
        registry.addConverter(getStringToTeacherConverter());
        registry.addConverter(getTeacherPaymentToStringConverter());
        registry.addConverter(getStringToTeacherPaymentConverter());
        registry.addConverter(getLanguagesToStringConverter());        
        registry.addConverter(getClassFormatToStringConverter());
        registry.addConverter(getPersonStatusToStringConverter());
        registry.addConverter(getClassStatusToStringConverter());
        registry.addConverter(getClassTypeToStringConverter());
        registry.addConverter(getDiscountTypeToStringConverter());
        registry.addConverter(getInstantCourseFormatToStringConverter());
        registry.addConverter(getInstantCourseTypeToStringConverter());        
        registry.addConverter(getRoomToStringConverter());
        registry.addConverter(getClassSpecializationTypeToStringConverter());        
    }
    
}
