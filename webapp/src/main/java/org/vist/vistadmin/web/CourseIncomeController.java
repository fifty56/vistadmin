package org.vist.vistadmin.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
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
import org.vist.vistadmin.domain.CourseIncome;
import org.vist.vistadmin.domain.CourseStudent;
import org.vist.vistadmin.domain.Student;
import org.vist.vistadmin.domain.common.ClassStatus;
import org.vist.vistadmin.domain.common.PersonStatus;
import org.vist.vistadmin.service.CourseStudentService;
import org.vist.vistadmin.web.validator.CourseIncomeValidator;
import org.vist.vistadmin.web.validator.CourseStudentValidator;

@RequestMapping("/courseincomes")
@Controller
@RooWebScaffold(path = "courseincomes", formBackingObject = CourseIncome.class)
public class CourseIncomeController  extends BaseController {

	
	@Autowired
	private ApplicationContext context;

	
	@Autowired
	CourseStudentService courseStudentService;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT_STR);
	    dateFormat.setLenient(false);

	    // true passed to CustomDateEditor constructor means convert empty String to null
	    binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(dateFormat, true));
				
        binder.setValidator(new CourseIncomeValidator(binder.getValidator()));
    } 

	
	void addDateTimeFormatPatterns(Model uiModel) {
		addDateTimeFormatPatterns(uiModel, new String[] {"courseIncome_currdate_date_format"});
    }		
	
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid CourseIncome courseIncome, BindingResult bindingResult, Model uiModel, Locale locale, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, locale, courseIncome, null, null);
            return "courseincomes/create";
        }
        courseIncome.setCurrDate(new Date());
        uiModel.asMap().clear();
        courseIncomeService.saveCourseIncome(courseIncome);
        return "redirect:/courseincomes/" + encodeUrlPathSegment(courseIncome.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String createForm(@RequestParam(value = "courseid", required = false) Long courseid, 
    		@RequestParam(value = "studentid", required = false) Long studentid, Model uiModel, Locale locale, HttpServletRequest httpServletRequest) {

    	
    	CourseIncome courseIncome = new CourseIncome();
    	
    	List<Student> students = null;
    	List<Course> courses = null;
    	
    	if(courseid != null) {
    		Course course = courseService.findCourse(courseid);
    		courseIncome.setCourse(course);
    		
    		List<CourseStudent> courseStudentList = courseStudentService.findByCourse(course);
    		if(courseStudentList != null && courseStudentList.size() > 0) {
    			students = new ArrayList<Student>();
    			for(CourseStudent courseStudent : courseStudentList) {
    				students.add(courseStudent.getStudent());
    			}
    		}    		     		    		
    		
    	}
    	if(studentid != null) {
    		Student student = studentService.findStudent(studentid);
    		courseIncome.setStudent(student);
    		
    		List<CourseStudent> courseStudentList = courseStudentService.findByStudent(student);
    		if(courseStudentList != null && courseStudentList.size() > 0) {
    			courses = new ArrayList<Course>();
    			for(CourseStudent courseStudent : courseStudentList) {
    				courses.add(courseStudent.getCourse());
    			}
    		}    
    		
       	}    	
        populateEditForm(uiModel, locale, courseIncome, students, courses);    	
    	
    	
        List<String[]> dependencies = new ArrayList<String[]>();
        if (studentService.countAllStudents() == 0) {
            dependencies.add(new String[] { "student", "students" });
        }
        if (courseService.countAllCourses() == 0) {
            dependencies.add(new String[] { "course", "courses" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "courseincomes/create";
    }
 
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid CourseIncome courseIncome, BindingResult bindingResult, Model uiModel, Locale locale, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, locale, courseIncome, null, null);
            return "courseincomes/update";
        }
        uiModel.asMap().clear();
        courseIncomeService.updateCourseIncome(courseIncome);
        return "redirect:/courseincomes/" + encodeUrlPathSegment(courseIncome.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel, Locale locale, HttpServletRequest httpServletRequest) {
        populateEditForm(uiModel, locale, courseIncomeService.findCourseIncome(id), null, null);
        return "courseincomes/update";
    }
    
    void populateEditForm(Model uiModel, Locale currentLocale, CourseIncome courseIncome, List<Student> studentList, List<Course> courseList) {
        uiModel.addAttribute("courseIncome", courseIncome);
        addDateTimeFormatPatterns(uiModel);
        
        List<Student> students = null;
        List<Course> courses = null; 
        
        if(courseIncome.getCourse() != null && courseIncome.getStudent() == null) {
        	courses = new ArrayList<Course>();
        	courses.add(courseIncome.getCourse());
        	if(studentList != null) {
        		students = studentList;
        	} else {
        		students = studentService.findAllStudents();	
        	}
        } else if(courseIncome.getStudent() != null && courseIncome.getCourse() == null) {
        	students = new ArrayList<Student>();
        	students.add(courseIncome.getStudent());
        	if(courseList != null) {
        		courses = courseList;
        	} else {
        		courses = courseService.findAllCourses();	
        	}
        }  else if(courseIncome.getStudent() != null && courseIncome.getCourse() != null) {
        	students = new ArrayList<Student>();
        	students.add(courseIncome.getStudent());
        	
        	courses = new ArrayList<Course>();
        	courses.add(courseIncome.getCourse());
        }
        uiModel.addAttribute("courses", courses);
        uiModel.addAttribute("students", students);
        
        CompletedClassController.addYearMonthWeekComboOptions(context, currentLocale, uiModel);
    }

    
    
}
