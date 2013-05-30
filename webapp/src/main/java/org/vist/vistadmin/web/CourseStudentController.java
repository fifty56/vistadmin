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
import org.vist.vistadmin.domain.CourseStudent;
import org.vist.vistadmin.domain.Student;
import org.vist.vistadmin.domain.common.ClassStatus;
import org.vist.vistadmin.domain.common.PersonStatus;
import org.vist.vistadmin.util.WebUtil;
import org.vist.vistadmin.web.validator.CourseStudentValidator;
import org.vist.vistadmin.web.validator.CourseValidator;

@RequestMapping("/coursestudents")
@Controller
@RooWebScaffold(path = "coursestudents", formBackingObject = CourseStudent.class)
public class CourseStudentController  extends BaseController {

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT_STR);
	    dateFormat.setLenient(false);

	    // true passed to CustomDateEditor constructor means convert empty String to null
	    binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(dateFormat, true));
				
        binder.setValidator(new CourseStudentValidator(binder.getValidator()));
    }

	
	void addDateTimeFormatPatterns(Model uiModel) {
		addDateTimeFormatPatterns(uiModel, new String[] {"courseStudent_startdate_date_format", "courseStudent_enddate_date_format"});        
    }

    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid CourseStudent courseStudent, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, courseStudent, null, null);
            return "coursestudents/create";
        }
        if(courseStudent.getStudent().isCompany()) {
        	courseStudent.setCompanyStudentNames(WebUtil.getStringFromMultiSelectListOptions(courseStudent.getCompanyStudentNameList()));
        }
        uiModel.asMap().clear();
        courseStudentService.saveCourseStudent(courseStudent);
        return "redirect:/coursestudents/" + encodeUrlPathSegment(courseStudent.getId().toString(), httpServletRequest);
    }

	
	
    @RequestMapping(params = "form", produces = "text/html")
    public String createForm(@RequestParam(value = "courseid", required = false) Long courseid, 
    		@RequestParam(value = "studentid", required = false) Long studentid, Model uiModel, HttpServletRequest httpServletRequest) {

    	String retVal = "coursestudents/create";
    	CourseStudent courseStudent = new CourseStudent();
    	
    	List<Student> students = null;
    	List<Course> courses = null;
    	
    	if(courseid != null) {
    		Course course = courseService.findCourse(courseid);
    		courses = new ArrayList<Course>();
    		courses.add(course);
    		setCourseTeacherDates(courseStudent, course);
    		courseStudent.setCourse(course);
    		
    		students = studentService.findByStatusAndLanguagesAndCompany(PersonStatus.ACTIVE, course.getLang(), course.getCompany());

    		//retVal = "redirect:/courses/" + encodeUrlPathSegment(courseid.toString(), httpServletRequest);    		
    	}
    	if(studentid != null) {
    		Student student = studentService.findStudent(studentid);    		    		
    		students = new ArrayList<Student>();
    		students.add(student);
    		courseStudent.setStudent(student);
    		courses = courseService.findByLangInAndStatusNotAndCompany(student.getLanguages(), ClassStatus.ARCHIVED, student.isCompany());    		
    		if(courses.size() == 1) {
    			setCourseTeacherDates(courseStudent, courses.get(0));
    		}
    		
    		//retVal = "redirect:/students/" + encodeUrlPathSegment(studentid.toString(), httpServletRequest);    		
    	}
        populateEditForm(uiModel, courseStudent, students, courses);
        return retVal;
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid CourseStudent courseStudent, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, courseStudent, null, null);
            return "coursestudents/update";
        }
        if(courseStudent.getStudent().isCompany()) {
        	courseStudent.setCompanyStudentNames(WebUtil.getStringFromMultiSelectListOptions(courseStudent.getCompanyStudentNameList()));
        }
        uiModel.asMap().clear();
        courseStudentService.updateCourseStudent(courseStudent);
        return "redirect:/coursestudents/" + encodeUrlPathSegment(courseStudent.getId().toString(), httpServletRequest);
    }

    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, courseStudentService.findCourseStudent(id), null, null);
        return "coursestudents/update";
    }

    void populateEditForm(Model uiModel, CourseStudent courseStudent, List<Student> students, List<Course> courses) {
        uiModel.addAttribute("courseStudent", courseStudent);
        addDateTimeFormatPatterns(uiModel);        
        if(students == null) {
        	students = studentService.findAllStudents();
        }
        if(courses == null) {
        	courses = courseService.findAllCourses();
        }
        uiModel.addAttribute("courses", courses);
        uiModel.addAttribute("students", students);        
        uiModel.addAttribute("companyStudentNameList", WebUtil.getOptionListFromCompanyStudentNames(courseStudent.getCompanyStudentNames()));
        if(courseStudent.getStudent() != null && courseStudent.getStudent().isCompany()) {
        	uiModel.addAttribute("availableCompanyStudentNameList", WebUtil.getOptionListFromCompanyStudentNames(courseStudent.getStudent().getCompanyData().getCompanyStudentNames()));
        }
    }
        
    private void setCourseTeacherDates(CourseStudent courseStudent, Course course) {
    	Date now = new Date();
		Date startDate = course.getStartDate();
		if(now.after(course.getStartDate())) {
			startDate = now;
		}
		courseStudent.setStartDate(startDate);
		courseStudent.setEndDate(course.getEndDate());
    }
    
}
