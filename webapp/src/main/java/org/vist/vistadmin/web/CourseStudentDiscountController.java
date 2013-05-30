package org.vist.vistadmin.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.vist.vistadmin.domain.CourseIncome;
import org.vist.vistadmin.domain.CourseStudentDiscount;
import org.vist.vistadmin.domain.CourseTeacher;
import org.vist.vistadmin.domain.Student;
import org.vist.vistadmin.domain.Teacher;
import org.vist.vistadmin.domain.common.ClassStatus;
import org.vist.vistadmin.domain.common.PersonStatus;
import org.vist.vistadmin.service.CourseIncomeService;
import org.vist.vistadmin.service.CourseService;
import org.vist.vistadmin.service.DiscountService;
import org.vist.vistadmin.service.StudentService;
import org.vist.vistadmin.service.TeacherService;
import org.vist.vistadmin.util.FlashScopeFilter;
import org.vist.vistadmin.web.validator.CourseStudentDiscountValidator;
import org.vist.vistadmin.web.validator.CourseTeacherValidator;

@RequestMapping("/coursestudentdiscounts")
@Controller
@RooWebScaffold(path = "coursestudentdiscounts", formBackingObject = CourseStudentDiscount.class)
public class CourseStudentDiscountController extends BaseController {
	
	@Autowired
	CourseService courseService;
	
	@Autowired
	StudentService studentService;
	
	@Autowired
	DiscountService discountService;
	
	@Autowired
	CourseIncomeService courseIncomeService;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT_STR);
	    dateFormat.setLenient(false);

	    // true passed to CustomDateEditor constructor means convert empty String to null
	    binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(dateFormat, true));
				
        binder.setValidator(new CourseStudentDiscountValidator(binder.getValidator()));
    }
	
    @RequestMapping(params = "form", produces = "text/html")
    public String createForm(@RequestParam(value = "courseid", required = false) Long courseid, 
    		@RequestParam(value = "studentid", required = false) Long studentid, Model uiModel) {
        
    	CourseStudentDiscount courseStudentDiscount = new CourseStudentDiscount();
        
         if(courseid != null) {
     		Course course = courseService.findCourse(courseid);
     		courseStudentDiscount.setCourse(course);
     	}
    	
         if(studentid != null) {
      		Student student = studentService.findStudent(studentid);
      		courseStudentDiscount.setStudent(student);     		    		    		    	
      	}
         
        List<String[]> dependencies = new ArrayList<String[]>();
        if (courseService.countAllCourses() == 0) {
            dependencies.add(new String[] { "course", "courses" });
        }
        if (studentService.countAllStudents() == 0) {
            dependencies.add(new String[] { "student", "students" });
        }
        if (discountService.countAllDiscounts() == 0) {
            dependencies.add(new String[] { "discount", "discounts" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        
        populateEditForm(uiModel, courseStudentDiscount);
        
        return "coursestudentdiscounts/create";
    }
	
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid CourseStudentDiscount courseStudentDiscount, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, courseStudentDiscount);
            return "coursestudentdiscounts/create";
        }
        uiModel.asMap().clear();
        courseStudentDiscountService.saveCourseStudentDiscount(courseStudentDiscount, httpServletRequest);
        return "redirect:/coursestudentdiscounts/" + encodeUrlPathSegment(courseStudentDiscount.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel, HttpServletRequest request) {
        uiModel.addAttribute("coursestudentdiscount", courseStudentDiscountService.findCourseStudentDiscount(id));
        uiModel.addAttribute("itemId", id);
        
        if(CompletedClassController.WARNING_CI_CREATED_FOR_CC.equals(request.getAttribute(FlashScopeFilter.WARNING_MESSAGE_KEY))) {
        	List<CourseIncome> courseIncomeList = courseIncomeService.findByCourseStudentDiscountId(id);
        	if(courseIncomeList != null && courseIncomeList.size() > 0) {
        		uiModel.addAttribute("createdCourseIncomes", courseIncomeList);
        	}
        }
        
        return "coursestudentdiscounts/show";
    }
    
    void populateEditForm(Model uiModel, CourseStudentDiscount courseStudentDiscount) {
        uiModel.addAttribute("courseStudentDiscount", courseStudentDiscount);
        
        List<Course> courses;
        List<Student> students;
        
        if(courseStudentDiscount.getCourse() == null) {
        	courses= courseService.findAllCourses();
        } else {
        	courses = new ArrayList<Course>();
        	courses.add(courseStudentDiscount.getCourse());
        }
        uiModel.addAttribute("courses", courses);
        
        if(courseStudentDiscount.getStudent() == null) {
        	students = studentService.findAllStudents();
        } else {
        	students = new ArrayList<Student>();
        	students.add(courseStudentDiscount.getStudent());
        }
        uiModel.addAttribute("students", students);
        
        uiModel.addAttribute("discounts", discountService.findAllDiscounts());
    }


	
}
