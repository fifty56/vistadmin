package org.vist.vistadmin.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.validator.util.privilegedactions.NewInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.vist.vistadmin.domain.CompletedClass;
import org.vist.vistadmin.domain.Course;
import org.vist.vistadmin.domain.CourseIncome;
import org.vist.vistadmin.domain.CourseStudent;
import org.vist.vistadmin.domain.CourseTeacher;
import org.vist.vistadmin.domain.Teacher; 
import org.vist.vistadmin.domain.common.Data;
import org.vist.vistadmin.domain.common.Languages;
import org.vist.vistadmin.domain.common.PersonStatus;
import org.vist.vistadmin.domain.common.SelectOptionList;
import org.vist.vistadmin.service.CompletedClassService;
import org.vist.vistadmin.service.CourseService;
import org.vist.vistadmin.service.CourseStudentService;
import org.vist.vistadmin.service.CourseTeacherService;
import org.vist.vistadmin.util.CalcUtil;
import org.vist.vistadmin.util.SelectOptionListPropertyEditor;
import org.vist.vistadmin.web.validator.TeacherValidator;

@RequestMapping("/teachers")
@Controller
@RooWebScaffold(path = "teachers", formBackingObject = Teacher.class)
public class TeacherController extends BaseController {

	private static final Logger LOGGER =  LoggerFactory.getLogger(TeacherController.class);
	
	@Autowired
    CourseTeacherService courseTeacherService;
	
	@Autowired
    CompletedClassService completedClassService;
	
	@Autowired
    CourseService courseService;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT_STR);
	    dateFormat.setLenient(false);

	    // true passed to CustomDateEditor constructor means convert empty String to null
	    binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(dateFormat, true));	    
        binder.setValidator(new TeacherValidator(binder.getValidator()));
    }
	
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
    	Teacher teacher = teacherService.findTeacher(id);
    	//TODO return only active (not archived) courses    	
    	List<CourseTeacher> courseTeacherList = courseTeacherService.findByTeacher(teacher);
    	//TODO find completedClasses only for aktiv (not archived) courses
    	List<CompletedClass> completedClassList = completedClassService.findByTeacher(teacher);    	            	
    	
    	setExpenses(teacher, completedClassList, courseTeacherList);
    	CalcUtil.setCompletedClassExpense(completedClassList, true);
    	CalcUtil.setCourseExpense(courseTeacherList);
    	
        uiModel.addAttribute("courseTeacherList", courseTeacherList);
        uiModel.addAttribute("completedClassList", completedClassList);
        uiModel.addAttribute("teacher", teacher);
        uiModel.addAttribute("itemId", id);
        return "teachers/show";
    }
    
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
    	Teacher teacher = teacherService.findTeacher(id);    	
    	List<CourseTeacher> courseTeacherList = courseTeacherService.findByTeacher(teacher);
    	uiModel.addAttribute("courseTeacherList", courseTeacherList);
        populateEditForm(uiModel, teacher);
        return "teachers/update";
    }
    
    void populateEditForm(Model uiModel, Teacher teacher) {
        uiModel.addAttribute("teacher", teacher);
        //uiModel.addAttribute("addresses", addressService.findAllAddresses());
        //uiModel.addAttribute("teacherbillingdatas", teacherBillingDataService.findAllTeacherBillingDatas());
        uiModel.addAttribute("languageses", Arrays.asList(Languages.values()));
        uiModel.addAttribute("personstatuses", Arrays.asList(PersonStatus.values()));
        uiModel.addAttribute("vistadminDateFormat", DATE_TIME_FORMAT_STR);
    }

    
    private static void setExpenses (Teacher teacher, List <CompletedClass> completedClassList, List<CourseTeacher> courseTeacherList) {
    	LOGGER.debug("setExpenses called");    	
    	teacher.setCurrExpense(0);
    	teacher.setAllExpense(0);
    	teacher.setRemExpense(0);
    	    	    	    	    	
    	teacher.setCurrExpense(CalcUtil.getTotalGrossAmountOfPayedCompletedClasses(completedClassList));
    	teacher.setRemExpense(CalcUtil.getTotalGrossAmountOfUnpayedCompletedClasses(completedClassList));    	    	    	
    	//teacher.setRemAllExpense(CalcUtil.getCourseRemainingNettExpenseFromcompletedClasses(null, completedClassList));
    	teacher.setAllExpense(teacher.getCurrExpense() + teacher.getRemExpense());    	    	    
    	LOGGER.debug("setExpenses end");
    }
    
    
    
}
