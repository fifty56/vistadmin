package org.vist.vistadmin.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.vist.vistadmin.domain.Course;
import org.vist.vistadmin.domain.CourseTeacher;
import org.vist.vistadmin.domain.Teacher;
import org.vist.vistadmin.domain.common.ClassStatus;
import org.vist.vistadmin.domain.common.PersonStatus;
import org.vist.vistadmin.web.validator.CourseStudentValidator;
import org.vist.vistadmin.web.validator.CourseTeacherValidator;

@RequestMapping("/courseteachers")
@Controller
@RooWebScaffold(path = "courseteachers", formBackingObject = CourseTeacher.class)
public class CourseTeacherController  extends BaseController {
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT_STR);
	    dateFormat.setLenient(false);

	    // true passed to CustomDateEditor constructor means convert empty String to null
	    binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(dateFormat, true));
				
        binder.setValidator(new CourseTeacherValidator(binder.getValidator()));
    }

	
    void addDateTimeFormatPatterns(Model uiModel) {
    	addDateTimeFormatPatterns(uiModel, new String[] {"courseTeacher_startdate_date_format", "courseTeacher_enddate_date_format"});        
    }

    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid CourseTeacher courseTeacher, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, courseTeacher, null, null);
            return "courseteachers/create";
        }
        uiModel.asMap().clear();
        courseTeacherService.saveCourseTeacher(courseTeacher);
        return "redirect:/courseteachers/" + encodeUrlPathSegment(courseTeacher.getId().toString(), httpServletRequest);
    }

    
    @RequestMapping(params = "form", produces = "text/html")
    public String createForm(@RequestParam(value = "courseid", required = false) Long courseid, 
    		@RequestParam(value = "teacherid", required = false) Long teacherid, Model uiModel) {
    	CourseTeacher courseTeacher = new CourseTeacher();
        
        List<String[]> dependencies = new ArrayList<String[]>();
        if (courseService.countAllCourses() == 0) {
            dependencies.add(new String[] { "course", "courses" });
        }
        if (teacherService.countAllTeachers() == 0) {
            dependencies.add(new String[] { "teacher", "teachers" });
        }
        
       List<Teacher> teachers = null;
       List<Course> courses = null;
       
        if(courseid != null) {
    		Course course = courseService.findCourse(courseid);
    		courses = new ArrayList<Course>();    		
    		courses.add(course);
    		setCourseTeacherDates(courseTeacher, course);
    		
    		teachers = teacherService.findByStatusAndLanguages(PersonStatus.ACTIVE, course.getLang());    		    		    	
    	}
    	if(teacherid != null) {
    		Teacher teacher = teacherService.findTeacher(teacherid);
    		teachers = new ArrayList<Teacher>();    		
    		teachers.add(teacher);
    		courses = courseService.findByLanguagesInAndStatusNot(teacher.getLanguages(), ClassStatus.ARCHIVED);
    		
    		if(courses.size() == 1) {
    			setCourseTeacherDates(courseTeacher, courses.get(0));	
    		}
    	}
    	    	
    	populateEditForm(uiModel, courseTeacher, teachers, courses);
    	
    	
    	uiModel.addAttribute("dependencies", dependencies);
        return "courseteachers/create";
    }

    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid CourseTeacher courseTeacher, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, courseTeacher, null, null);
            return "courseteachers/update";
        }
        uiModel.asMap().clear();
        courseTeacherService.updateCourseTeacher(courseTeacher);
        return "redirect:/courseteachers/" + encodeUrlPathSegment(courseTeacher.getId().toString(), httpServletRequest);
    }

    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, courseTeacherService.findCourseTeacher(id), null, null);
        return "courseteachers/update";
    }

    void populateEditForm(Model uiModel, CourseTeacher courseTeacher, List<Teacher> teachers, List<Course> courses) {
        uiModel.addAttribute("courseTeacher", courseTeacher);
        addDateTimeFormatPatterns(uiModel);        
        if(courses == null) {
        	courses = courseService.findAllCourses();
        }                
        if(teachers == null) {
        	teachers = teacherService.findAllTeachers();
        }
        uiModel.addAttribute("teachers", teachers);
        uiModel.addAttribute("courses", courses);
    }

    
    private void setCourseTeacherDates(CourseTeacher courseTeacher, Course course) {
    	Date now = new Date();
		Date startDate = course.getStartDate();
		if(now.after(course.getStartDate())) {
			startDate = now;
		}
		courseTeacher.setStartDate(startDate);
		courseTeacher.setEndDate(course.getEndDate());
    }
    
}
