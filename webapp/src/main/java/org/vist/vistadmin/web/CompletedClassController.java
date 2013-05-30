package org.vist.vistadmin.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
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
import org.vist.vistadmin.domain.CompletedClass;
import org.vist.vistadmin.domain.Course;
import org.vist.vistadmin.domain.CourseIncome;
import org.vist.vistadmin.domain.CourseStudent;
import org.vist.vistadmin.domain.CourseTeacher;
import org.vist.vistadmin.domain.Student;
import org.vist.vistadmin.domain.Teacher;
import org.vist.vistadmin.domain.common.SelectOption;
import org.vist.vistadmin.service.CourseIncomeService;
import org.vist.vistadmin.service.CourseService;
import org.vist.vistadmin.service.CourseTeacherService;
import org.vist.vistadmin.service.StudentService;
import org.vist.vistadmin.service.TeacherService;
import org.vist.vistadmin.util.FlashScopeFilter;
import org.vist.vistadmin.web.validator.CompletedClassValidator;
import org.vist.vistadmin.web.validator.CourseValidator;

@RequestMapping("/completedclasses")
@Controller
@RooWebScaffold(path = "completedclasses", formBackingObject = CompletedClass.class)
public class CompletedClassController  extends BaseController {
	
	private static Logger LOGGER = LoggerFactory.getLogger(CompletedClassController.class);
	
	public static final String WARNING_CI_CREATED_FOR_CC = "warning_ci_created_for_cc";
	
	@Autowired
	private ApplicationContext context;
	
	@Autowired
	CourseTeacherService courseTeacherService;
	
	@Autowired	
	CourseIncomeService courseIncomeService;
	
	@Autowired
	CourseService courseService;

	@Autowired
	StudentService studentService;

	@Autowired
	TeacherService teacherService;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT_STR);
	    dateFormat.setLenient(false);

	    // true passed to CustomDateEditor constructor means convert empty String to null
	    binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(dateFormat, true));
				
        binder.setValidator(new CompletedClassValidator(binder.getValidator()));
    }

	
	void addDateTimeFormatPatterns(Model uiModel) {
		addDateTimeFormatPatterns(uiModel, new String[] {"completedClass_currdate_date_format"});
    }
	
	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid CompletedClass completedClass, BindingResult bindingResult, Model uiModel, Locale locale, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, locale, completedClass);
            return "completedclasses/create";
        }
        completedClass.setCurrDate(new Date());
        uiModel.asMap().clear();
        completedClassService.saveCompletedClass(completedClass, httpServletRequest);
        return "redirect:/completedclasses/" + encodeUrlPathSegment(completedClass.getId().toString(), httpServletRequest);
    }
	
    @RequestMapping(params = "form", produces = "text/html")    
    public String createForm(@RequestParam(value = "courseid", required = false) Long courseid, 
    		@RequestParam(value = "teacherid", required = false) Long teacherid, Model uiModel, Locale locale, 
    		HttpServletRequest httpServletRequest) {    		
    	CompletedClass completedClass = new CompletedClass();

        List<String[]> dependencies = new ArrayList<String[]>();
        if (courseService.countAllCourses() == 0) {
            dependencies.add(new String[] { "course", "courses" });
        }
        if (teacherService.countAllTeachers() == 0) {
            dependencies.add(new String[] { "teacher", "teachers" });
        }

        boolean populateWithDefaults = true;
        
        List<Course> courses = new ArrayList<Course>();
        List<Teacher> teachers = new ArrayList<Teacher>();
        
        if(courseid != null) {        	
    		Course course = courseService.findCourse(courseid);
    		completedClass.setCourse(course);    		
    		courses.add(course);    		    		
    		
    		List<CourseTeacher> courseTeacherList = courseTeacherService.findByCourse(course);
    		for(CourseTeacher courseTeacher: courseTeacherList) {
    			teachers.add(courseTeacher.getTeacher());
    		}    		
    		populateWithDefaults = false;
    	}
    	if(teacherid != null) {
    		Teacher teacher = teacherService.findTeacher(teacherid);
    		completedClass.setTeacher(teacher);    		
    		teachers.add(teacher);
    		
    		List<CourseTeacher> courseTeacherList = courseTeacherService.findByTeacher(teacher);
    		for(CourseTeacher courseTeacher: courseTeacherList) {
    			courses.add(courseTeacher.getCourse());
    		}
    		
    		populateWithDefaults = false;
    	}
        
    	Date currDate = new Date();
    	completedClass.setCurrDate(currDate);
    	completedClass.setCompYear(currDate.getYear() + 1900);
    	completedClass.setCompMonth(currDate.getMonth());
    	completedClass.setCompWeek(1);
    	
    	if(populateWithDefaults) {
    		populateEditForm(uiModel, locale, completedClass); 
    	} else {
    		uiModel.addAttribute("completedClass", completedClass);
            addDateTimeFormatPatterns(uiModel);
            uiModel.addAttribute("courses", courses);
            uiModel.addAttribute("teachers", teachers);
            addYearMonthWeekComboOptions(context, locale, uiModel);    
    	}            	    	
    	
        uiModel.addAttribute("dependencies", dependencies);
        return "completedclasses/create";
    }


    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid CompletedClass completedClass, BindingResult bindingResult, Model uiModel, Locale locale, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
        	LOGGER.trace(bindingResult.toString());
            populateEditForm(uiModel, locale, completedClass);
            return "completedclasses/update";
        }
        uiModel.asMap().clear();
        completedClassService.updateCompletedClass(completedClass);
        return "redirect:/completedclasses/" + encodeUrlPathSegment(completedClass.getId().toString(), httpServletRequest);
    }
    
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel, Locale locale, HttpServletRequest httpServletRequest) {
    	CompletedClass completedClass = completedClassService.findCompletedClass(id);    	
    	boolean editEnabled = hasPayedCourseIncome(id);
        uiModel.addAttribute("editEnabled", editEnabled);
        populateEditForm(uiModel, locale, completedClass);
        return "completedclasses/update";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel, HttpServletRequest request) {
        CompletedClass completedClass = completedClassService.findCompletedClass(id);
    	addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("completedclass", completedClass);
        uiModel.addAttribute("itemId", id);
        boolean deleteEnabled = hasPayedCourseIncome(id);
        uiModel.addAttribute("deleteEnabled", deleteEnabled);
        
        
        if(WARNING_CI_CREATED_FOR_CC.equals(request.getAttribute(FlashScopeFilter.WARNING_MESSAGE_KEY))) {
        	List<CourseIncome> courseIncomeList = courseIncomeService.findByCompletedClassId(id);
        	if(courseIncomeList != null && courseIncomeList.size() > 0) {
        		uiModel.addAttribute("createdCourseIncomes", courseIncomeList);
        	}
        }
        
        return "completedclasses/show";
    }

    
    void populateEditForm(Model uiModel, Locale currentLocale,  CompletedClass completedClass) {
        uiModel.addAttribute("completedClass", completedClass);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("courses", courseService.findAllCourses());
        uiModel.addAttribute("teachers", teacherService.findAllTeachers());
        
        addYearMonthWeekComboOptions(context, currentLocale, uiModel);
    }

    public static void addYearMonthWeekComboOptions(ApplicationContext ctx, Locale locale, Model uiModel) {
        List<Integer> yearList = new ArrayList<Integer>();
        List<SelectOption> monthList = new ArrayList<SelectOption>();
        List<SelectOption> weekList = new ArrayList<SelectOption>();

        for(int i = -1; i<=5; i++) {
        	if(i != 0) {
        		SelectOption option = new SelectOption();
        		option.setId(Integer.toString(i));
        		if(i == -1) {        			
        			option.setLabel(ctx.getMessage("select_option_completedclass_monthly", null, locale));
        		} else {
        			option.setLabel(ctx.getMessage("select_option_completedclass_week", new Object[] {new Integer(i)}, locale));
        		}
        		weekList.add(option);
        	}
        }
        for(int i = 1; i<=12; i++) {
        	SelectOption option = new SelectOption();
        	option.setId(Integer.toString(i));
    		option.setLabel(ctx.getMessage("select_option_completedclass_month_" + i, null, locale));
        	monthList.add(option);
        }
        
        for(int i = 2010; i<=2050; i++) {
        	yearList.add(new Integer(i));
        }
        uiModel.addAttribute("weeklist", weekList);
        uiModel.addAttribute("monthlist", monthList);
        uiModel.addAttribute("yearlist", yearList);
    }

    private boolean hasPayedCourseIncome(Long courseIncomeId) {
        boolean hasPayedCourseIncome = true;
        List<CourseIncome> courseIncomes = courseIncomeService.findByCompletedClassId(courseIncomeId);
        if(courseIncomes != null && courseIncomes.size() > 0) {
        	for (CourseIncome courseIncome : courseIncomes) {
				if(courseIncome.isPayed()) {
					hasPayedCourseIncome = false;
					break;
				}        	
        	}
        }        
        return hasPayedCourseIncome;
    }
}
