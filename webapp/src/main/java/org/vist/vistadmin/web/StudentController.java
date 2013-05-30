package org.vist.vistadmin.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.ApplicationContext;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.vist.vistadmin.domain.Course;
import org.vist.vistadmin.domain.CourseIncome;
import org.vist.vistadmin.domain.CourseStudent;
import org.vist.vistadmin.domain.CourseStudentDiscount;
import org.vist.vistadmin.domain.CourseTeacher;
import org.vist.vistadmin.domain.Discount;
import org.vist.vistadmin.domain.Student; 
import org.vist.vistadmin.domain.Teacher;
import org.vist.vistadmin.domain.common.ClassFormat;
import org.vist.vistadmin.domain.common.DiscountType;
import org.vist.vistadmin.domain.common.Languages;
import org.vist.vistadmin.domain.common.PersonStatus;
import org.vist.vistadmin.domain.common.SelectOption;
import org.vist.vistadmin.service.CourseIncomeService;
import org.vist.vistadmin.service.CourseStudentDiscountService;
import org.vist.vistadmin.service.CourseStudentService;
import org.vist.vistadmin.service.CourseTeacherService;
import org.vist.vistadmin.util.CalcUtil;
import org.vist.vistadmin.util.WebUtil;
import org.vist.vistadmin.web.validator.StudentValidator;
import org.vist.vistadmin.web.validator.TeacherValidator;

@RequestMapping("/students")
@Controller
@RooWebScaffold(path = "students", formBackingObject = Student.class)
public class StudentController  extends BaseController  {
 
	private static final Logger LOGGER =  LoggerFactory.getLogger(StudentController.class);
	
	@Autowired
	CourseStudentService courseStudentService;	
	
	@Autowired
	CourseIncomeService courseIncomeService;
	
	@Autowired
	CourseStudentDiscountService courseStudentDiscountService;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT_STR);
	    dateFormat.setLenient(false);

	    // true passed to CustomDateEditor constructor means convert empty String to null
	    binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(dateFormat, true));
				
        binder.setValidator(new StudentValidator(binder.getValidator()));
    }
	

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Student student, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, student);
            return "students/create";
        }
        if(student.isCompany()) {
        	student.getCompanyData().setCompanyStudentNames(WebUtil.getStringFromMultiSelectListOptions(student.getCompanyData().getCompanyStudentNameList()));
        }
        uiModel.asMap().clear();
        studentService.saveStudent(student);
        return "redirect:/students/" + encodeUrlPathSegment(student.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new Student());
        return "students/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {    	
    	Student student = studentService.findStudent(id);
    	//TODO return only active (not archived) courses
    	List<CourseStudent> courseStudentList = courseStudentService.findByStudent(student);
    	//TODO return only incomes of active (not archived) courses
    	List<CourseIncome> courseIncomeList = courseIncomeService.findByStudent(student);
    	
    	List<CourseStudentDiscount> courseStudentDiscountList = courseStudentDiscountService.findByStudent(student);

    	assignCourseStudentDiscountToCourseStudent(courseStudentDiscountList, courseStudentList);
    	assignCourseIncomeToCourseStudent(courseStudentDiscountList, courseStudentList);
    	setExpenses(student, courseIncomeList, courseStudentList, courseStudentDiscountList);
    	
    	uiModel.addAttribute("courseStudentList", courseStudentList);
        uiModel.addAttribute("courseStudentList", courseStudentList);
        uiModel.addAttribute("courseIncomeList", courseIncomeList);    	
        uiModel.addAttribute("student", student);
        uiModel.addAttribute("itemId", id);
        return "students/show";
    }
	
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
    	Student student = studentService.findStudent(id);     	
    	List<CourseStudent> courseStudentList = courseStudentService.findByStudent(student);

    	uiModel.addAttribute("courseStudentList", courseStudentList);
        populateEditForm(uiModel, student);
        return "students/update";
    }	
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Student student, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, student);
            return "students/update";
        }
        if(student.isCompany()) {
        	student.getCompanyData().setCompanyStudentNames(WebUtil.getStringFromMultiSelectListOptions(student.getCompanyData().getCompanyStudentNameList()));
        }
        uiModel.asMap().clear();
        studentService.updateStudent(student);
        return "redirect:/students/" + encodeUrlPathSegment(student.getId().toString(), httpServletRequest);
    }
    
    void populateEditForm(Model uiModel, Student student) {
        uiModel.addAttribute("student", student);
        //uiModel.addAttribute("addresses", addressService.findAllAddresses());
        //uiModel.addAttribute("billingaddresses", billingAddressService.findAllBillingAddresses());
        uiModel.addAttribute("personstatuses", Arrays.asList(PersonStatus.values()));
        uiModel.addAttribute("languageses", Arrays.asList(Languages.values()));
        if(student.isCompany()) {
        	uiModel.addAttribute("companyStudentNameList", WebUtil.getOptionListFromCompanyStudentNames(student.getCompanyData().getCompanyStudentNames()));
        }
        uiModel.addAttribute("vistadminDateFormat", DATE_TIME_FORMAT_STR); 
    }
    
    private static void setExpenses(Student student,List<CourseIncome> courseIncomeList, List<CourseStudent> courseStudentList, List<CourseStudentDiscount> courseStudentDiscountList) {
    	LOGGER.debug("setExpenses called");
    	student.setAllExpenses(0);
    	student.setRemExpenses(0);
    	student.setCurrExpenses(0);
    	
    	double partAllIncome = 0;
    	for(CourseIncome courseIncome : courseIncomeList) {
    		double currExp = student.getCurrExpenses();
    		double currAllExp = student.getAllExpenses();
    		double newCurrExp = courseIncome.getAmount();
    		double remExp = student.getRemExpenses();    		
			if(courseIncome.isPayed()) {
				student.setCurrExpenses(currExp + newCurrExp);
    			LOGGER.debug("add payed course income by course: " + courseIncome.getCourse().getLabelString() + 
					", amount: "  + newCurrExp);
			} else {
				student.setRemExpenses(remExp + newCurrExp);
				LOGGER.debug("add not payed course income by course: " + courseIncome.getCourse().getLabelString() + 
					", amount: "  + newCurrExp);
			}
			if(courseIncome.getCourse().getPayPerClasses()) {
				LOGGER.debug("increase allExpenses ("+ currAllExp +") by: " + newCurrExp); 
				student.setAllExpenses(currAllExp + newCurrExp);
			}
    	}
    	    	
		Map<Course, Set<Discount>> courseDiscountSetMap = CalcUtil.getCourseDiscountSetMapByCourse(courseStudentDiscountList);
	    	for(CourseStudent courseStudent : courseStudentList) {	    		
	    		Course course = courseStudent.getCourse();
	    		if(!course.getPayPerClasses()) {
	    			double allExp = student.getAllExpenses();
	    			Set<Discount> courseDiscountSet = courseDiscountSetMap.get(course);
	    			double newAllExp = CalcUtil.getStudentCoursePrice(student, course, courseDiscountSet);    		    		
	    			student.setAllExpenses(allExp + newAllExp);
	    			LOGGER.debug("add all income by course: " + courseStudent.getCourse().getLabelString() + 
						", amount: "  + newAllExp);
	    		}
	    	}
			
		student.setRemExpenses(student.getAllExpenses() - student.getCurrExpenses());    	
		LOGGER.debug("setExpenses end");	
    }
	
    
    public static void assignCourseIncomeToCourseStudent(List<CourseStudentDiscount> courseStudentDiscountList, List<CourseStudent> courseStudentList) {
    	LOGGER.trace("size of courseStudentDiscountList: " + courseStudentDiscountList.size());
    	LOGGER.trace("size of courseStudentList: " + courseStudentList.size());
    	
    	Map<Course, Set<Discount>> courseDiscountMap = CalcUtil.getCourseDiscountSetMapByCourse(courseStudentDiscountList);
    	Map<Student, Set<Discount>> studentDiscountMap = CalcUtil.getCourseDiscountSetMapByStudent(courseStudentDiscountList);
    		
    	for(CourseStudent courseStudent : courseStudentList) {
    		Course course = courseStudent.getCourse();    		
    		Student student = courseStudent.getStudent();
    		Set<Discount> courseDiscounts = courseDiscountMap.get(course);
    		Set<Discount> studentDiscounts = studentDiscountMap.get(student);
    		    		
			double courseIncome = CalcUtil.getStudentCoursePrice(student, course, courseDiscounts);
			course.setAllGrossIncome(courseIncome);	
		
			courseIncome = CalcUtil.getStudentCoursePrice(student, course, studentDiscounts);
			student.setAllExpenses(courseIncome);    		
    	}
    }
    
    private static void assignCourseStudentDiscountToCourseStudent(List<CourseStudentDiscount> courseStudentDiscountList, List<CourseStudent> courseStudentList) {
    	LOGGER.trace("size of courseStudentDiscountList: " + courseStudentDiscountList.size());
    	LOGGER.trace("size of courseStudentList: " + courseStudentList.size());
    	for(CourseStudentDiscount courseStudentDiscount : courseStudentDiscountList) {
			boolean hasAdded = false;
			for(CourseStudent courseStudent : courseStudentList) {
				if(courseStudent.getCourse().equals(courseStudentDiscount.getCourse())) {
					LOGGER.trace("add " + courseStudentDiscount + " to course");
					Course course = courseStudent.getCourse();
					Set<CourseStudentDiscount> discounts = course.getDiscounts(); 
					if(discounts == null) {
						discounts = new HashSet<CourseStudentDiscount>();
						course.setDiscounts(discounts);
					}					
					discounts.add(courseStudentDiscount);
					LOGGER.trace("discounts.size: " + discounts.size());
					LOGGER.trace("course.getDiscounts(): " + course.getDiscounts().size());
					hasAdded = true;
				}
			}
			if(!hasAdded) {
				LOGGER.warn("courseStudentDiscount cannot be assigned to course: " + courseStudentDiscount);
			}			
		}
    }

    
    
}
