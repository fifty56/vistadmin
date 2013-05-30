package org.vist.vistadmin.web;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.vist.vistadmin.domain.CompletedClass;
import org.vist.vistadmin.domain.Course;
import org.vist.vistadmin.domain.CourseIncome;
import org.vist.vistadmin.domain.CourseStudent;
import org.vist.vistadmin.domain.CourseStudentDiscount;
import org.vist.vistadmin.domain.CourseTeacher;
import org.vist.vistadmin.domain.Discount;
import org.vist.vistadmin.domain.Student;
import org.vist.vistadmin.domain.Teacher;
import org.vist.vistadmin.domain.common.ClassSpecializationType;
import org.vist.vistadmin.domain.common.Room;
import org.vist.vistadmin.domain.common.ClassFormat;
import org.vist.vistadmin.domain.common.ClassLevels;
import org.vist.vistadmin.domain.common.ClassStatus;
import org.vist.vistadmin.domain.common.ClassType;
import org.vist.vistadmin.domain.common.Data;
import org.vist.vistadmin.domain.common.DiscountType;
import org.vist.vistadmin.domain.common.Languages;
import org.vist.vistadmin.domain.common.SelectOption;
import org.vist.vistadmin.domain.common.InstantCourseFormat;
import org.vist.vistadmin.domain.common.InstantCourseType;
import org.vist.vistadmin.service.CompletedClassService;
import org.vist.vistadmin.service.CourseIncomeService;
import org.vist.vistadmin.service.CourseService;
import org.vist.vistadmin.service.CourseStudentDiscountService;
import org.vist.vistadmin.service.CourseStudentService;
import org.vist.vistadmin.service.CourseTeacherService;
import org.vist.vistadmin.service.StudentService;
import org.vist.vistadmin.service.TeacherService;
import org.vist.vistadmin.util.CalcUtil;
import org.vist.vistadmin.util.DateUtil;
import org.vist.vistadmin.util.WebUtil;
import org.vist.vistadmin.web.dto.CourseCopy;
import org.vist.vistadmin.web.validator.CourseValidator;
import org.vist.vistadmin.web.validator.TeacherValidator;
import org.springframework.web.bind.annotation.ModelAttribute;

@RequestMapping("/courses")
@Controller
@RooWebScaffold(path = "courses", formBackingObject = Course.class)
public class CourseController  extends BaseController {
	
	private static final Logger LOGGER =  LoggerFactory.getLogger(CourseController.class);
	
	public static final String FLEXIBLE_TIME = "FLEX";
	
	@Autowired
	private ApplicationContext context;
	
	@Autowired
    CourseTeacherService courseTeacherService;
	@Autowired
	CourseStudentService courseStudentService;
	@Autowired
	CompletedClassService completedClassService;
	@Autowired
    CourseIncomeService courseIncomeService;
	@Autowired
    StudentService studentService;
	@Autowired
    TeacherService teacherService;
	@Autowired
	CourseStudentDiscountService courseStudentDiscountService;
	
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT_STR);
	    dateFormat.setLenient(false);

	    // true passed to CustomDateEditor constructor means convert empty String to null
	    binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(dateFormat, true));
				
        binder.setValidator(new CourseValidator(binder.getValidator()));
    }

	
    void addDateTimeFormatPatterns(Model uiModel) {
    	addDateTimeFormatPatterns(uiModel, 
    			new String[]{"course_startdate_date_format","course_enddate_date_format"});
    }    
    

    @RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel, Locale locale, HttpServletRequest httpServletRequest) {
        populateEditForm(uiModel, new Course(), null, context, locale);
        return "courses/create";
    }
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Course course, BindingResult bindingResult, Model uiModel, Locale locale, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
        	LOGGER.debug("create has errors: " + bindingResult.getAllErrors());
            populateEditForm(uiModel, course, null, context, locale);
            return "courses/create";
        }        
        course.setTimeOfClasses(setClassTimesStrFromOptionList(course));        
        uiModel.asMap().clear();
        courseService.saveCourse(course, httpServletRequest);
        return "redirect:/courses/" + encodeUrlPathSegment(course.getId().toString(), httpServletRequest);
    }

    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel, Locale locale, HttpServletRequest httpServletRequest) {
        addDateTimeFormatPatterns(uiModel);
        
        Course course = courseService.findCourse(id);          
        List<CourseStudent> courseStudentList = courseStudentService.findByCourse(course);
        List<CourseTeacher> courseTeacherList = courseTeacherService.findByCourse(course);
        List<CompletedClass> completedClassList = completedClassService.findByCourse(course);
		List<CourseIncome> courseIncomeList = courseIncomeService.findByCourse(course);
		List<CourseStudentDiscount> courseStudentDiscountList = courseStudentDiscountService.findByCourse(course); 
			
		assignCourseStudentDiscountToCourseStudent(courseStudentDiscountList, courseStudentList);
		StudentController.assignCourseIncomeToCourseStudent(courseStudentDiscountList, courseStudentList);
		setInOutComes(course, courseStudentList, completedClassList, courseIncomeList, courseTeacherList, courseStudentDiscountList, false, null);
		CalcUtil.setCompletedClassExpense(completedClassList, true);
		
		List<SelectOption> optionList = createOptionListFromClassTimes(course, context, locale);
		if(!course.isFlexibleClassTime()) {
			StringBuilder sb = new StringBuilder();
			if(optionList != null && optionList.size() > 0) {
				for (SelectOption selectOption : optionList) {
					if(sb.length() > 0) {
						sb.append("; ");
					}
					sb.append(selectOption.getLabel());
				}			
			}
			course.setTimeOfClasses(sb.toString());
    	}
        uiModel.addAttribute("course", course);
        uiModel.addAttribute("courseTeacherList", courseTeacherList);
        uiModel.addAttribute("courseStudentList", courseStudentList); 
        uiModel.addAttribute("completedClassList", completedClassList);
        uiModel.addAttribute("courseIncomeList", courseIncomeList);
        uiModel.addAttribute("itemId", id);
        
        return "courses/show";
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel, Locale locale, HttpServletRequest httpServletRequest) {
    	Course course = courseService.findCourse(id);
    	createOptionListFromClassTimes(course, context, locale);		     	
    	List<CourseStudent> courseStudentList = courseStudentService.findByCourse(course);
    	List<CourseTeacher> courseTeacherList = courseTeacherService.findByCourse(course);
    	populateEditForm(uiModel, course, null, context, locale);
    	uiModel.addAttribute("courseStudentList", courseStudentList);
    	uiModel.addAttribute("courseTeacherList", courseTeacherList);
        return "courses/update";
    }

    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Course course, BindingResult bindingResult, Model uiModel, Locale locale, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, course, null, context, locale);
            return "courses/update";
        }
        course.setTimeOfClasses(setClassTimesStrFromOptionList(course));
        uiModel.asMap().clear();
        courseService.updateCourse(course, httpServletRequest);
        return "redirect:/courses/" + encodeUrlPathSegment(course.getId().toString(), httpServletRequest);
    }

    @RequestMapping(value = "/copy/{id}", params = "form", produces = "text/html")
    public String copyForm(@PathVariable("id") Long id, Model uiModel, Locale locale, HttpServletRequest httpServletRequest) {
    	Course course = courseService.findCourse(id);
    	CourseCopy cc = new CourseCopy();    	
    	cc.setCourse(course);
    	createOptionListFromClassTimes(course, context, locale);		     	
    	List<CourseStudent> courseStudentList = courseStudentService.findByCourse(course);
    	List<CourseTeacher> courseTeacherList = courseTeacherService.findByCourse(course);    	
    	populateEditForm(uiModel, course, cc, context, locale);
    	uiModel.addAttribute("courseStudentList", courseStudentList2SelectOptionList(courseStudentList));
    	uiModel.addAttribute("courseTeacherList", courseTeacherList2SelectOptionList(courseTeacherList));
    	uiModel.addAttribute("courseStudentDiscountList", generateSelectOptionCSDListFromCSList(courseStudentList));
        return "courses/copy";
    }

    @RequestMapping(value = "/copy", method = RequestMethod.PUT, produces = "text/html")
    public String copy(@Valid CourseCopy courseCopy, BindingResult bindingResult, Model uiModel, Locale locale, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
        	LOGGER.trace("" , bindingResult.getFieldErrors());
        	LOGGER.trace("" , bindingResult.getGlobalErrors());
            populateEditForm(uiModel, courseCopy.getCourse(), courseCopy, context, locale);
            Course origCourse = courseService.findCourse(courseCopy.getCourse().getId());
            List<CourseStudent> courseStudentList = courseStudentService.findByCourse(origCourse);
        	List<CourseTeacher> courseTeacherList = courseTeacherService.findByCourse(origCourse);
            uiModel.addAttribute("courseStudentList", courseStudentList2SelectOptionList(courseStudentList));
        	uiModel.addAttribute("courseTeacherList", courseTeacherList2SelectOptionList(courseTeacherList));
        	uiModel.addAttribute("courseStudentDiscountList", generateSelectOptionCSDListFromCSList(courseStudentList));
            return "courses/copy";
        }
        uiModel.asMap().clear();
        Course newCourse = handleCourseCopyPost(courseCopy, httpServletRequest);
        return "redirect:/courses/" + encodeUrlPathSegment(newCourse.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel, Locale locale, HttpServletRequest httpServletRequest) {
        Course course = courseService.findCourse(id);
        courseService.deleteCourse(course, httpServletRequest);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/courses";
    }
    
    
    
    
    public static void assignCourseStudentDiscountToCourseStudent(List<CourseStudentDiscount> courseStudentDiscountList, List<CourseStudent> courseStudentList) {
    	LOGGER.trace("size of courseStudentDiscountList: " + courseStudentDiscountList.size());
    	LOGGER.trace("size of courseStudentList: " + courseStudentList.size());
    	for(CourseStudentDiscount courseStudentDiscount : courseStudentDiscountList) {
			boolean hasAdded = false;
			for(CourseStudent courseStudent : courseStudentList) {
				if(courseStudent.getStudent().equals(courseStudentDiscount.getStudent())) {
					LOGGER.trace("add " + courseStudentDiscount + " to student");
					Student student = courseStudent.getStudent();
					Set<CourseStudentDiscount> discounts = student.getDiscounts(); 
					if(discounts == null) {
						discounts = new HashSet<CourseStudentDiscount>();
						student.setDiscounts(discounts);
					}
					discounts.add(courseStudentDiscount);
					LOGGER.trace("discounts.size: " + discounts.size());
					LOGGER.trace("student.getDiscounts(): " + student.getDiscounts().size());
					hasAdded = true;
				}
			}
			if(!hasAdded) {
				LOGGER.warn("courseStudentDiscount cannot be assigned to student: " + courseStudentDiscount);
			}			
		}
    }
    
    private static Teacher getCurrentTeacher(List<CourseTeacher> courseTeacherList) {
    	Teacher currentTeacher = null;
    	if(courseTeacherList != null && courseTeacherList.size() > 1) {
			currentTeacher = courseTeacherList.get(0).getTeacher();
			Date currentTeacherEndDate = courseTeacherList.get(0).getEndDate();
			for(CourseTeacher courseTeacher : courseTeacherList) {
				if(courseTeacher.getEndDate().after(currentTeacherEndDate)) {
					currentTeacher = courseTeacher.getTeacher();
					currentTeacherEndDate = courseTeacher.getEndDate();
				}
			}	
		}
    	return currentTeacher;
    }
    
    public static void setInOutComes(Course course, List<CourseStudent> courseStudentList, List<CompletedClass> completedClassList, List<CourseIncome> courseIncomeList, List<CourseTeacher> currentTeacherList, List<CourseStudentDiscount> courseStudentDiscountList, boolean guessPayPerClassCourses, Teacher periodTeacher) {
    	LOGGER.debug("setInOutComes called");
    	course.setCurrGrossIncomeByCI(0);
    	course.setCurrGrossExpenseByCC(0);
    	course.setAllGrossIncome(0);
    	course.setAllGrossExpenseByCC(0);
    	course.setAllNetExpense(0);    	    	
    	
    	course.setCurrGrossIncomeByCI(CalcUtil.getTotalGrossAmountOfPayedCourseIncomes(courseIncomeList));
    	course.setRemCurrGrossIncomeByCI(CalcUtil.getTotalGrossAmountOfUnpayedCourseIncomes(courseIncomeList));
    	//course.setRemCurrNetIncome(CalcUtil.getTotalGrossAmountOfUnpayedCourseIncomes(courseIncomeList));
    	
    	course.setCurrGrossExpenseByCC(CalcUtil.getTotalGrossAmountOfPayedCompletedClasses(completedClassList));
    	course.setRemCurrGrossExpenseByCC(CalcUtil.getTotalGrossAmountOfUnpayedCompletedClasses(completedClassList));
    	course.setRemCurrNetExpenseByCC(CalcUtil.getTotalNettAmountOfUnpayedCompletedClasses(completedClassList));
    	    	
    	course.setAllGrossExpenseByCC(course.getCurrGrossExpenseByCC() + course.getRemCurrGrossExpenseByCC());
    	
    	double payedCompleteClassNetExpense = CalcUtil.getTotalNettAmountOfPayedCompletedClasses(completedClassList);
    	double unpayedCompleteClassNetExpense = CalcUtil.getTotalNettAmountOfUnpayedCompletedClasses(completedClassList);    			    	
    	
    	if(!course.getPayPerClasses()) {
    		double allNettExpense = course.getSumOfClasses() * course.getTeacherClassPrice();
    		course.setAllNetExpense(allNettExpense);
    		course.setRemNetExpense(CalcUtil.getCourseRemainingNettExpenseFromcompletedClasses(course, completedClassList));
	    	course.setAllGrossIncome(CalcUtil.getAllStudentOneClassPrice(courseStudentList, course, courseStudentDiscountList));    					
	    	course.setRemGrossIncome(course.getAllGrossIncome() - course.getCurrGrossIncomeByCI());
	    	course.setTotalGrossBalance(course.getAllGrossIncome() - course.getAllNetExpense());
	    	
    	} else {    		
    		course.setRemGrossIncome(course.getRemCurrGrossIncomeByCI());
    		course.setAllGrossIncome(course.getRemCurrGrossIncomeByCI() + course.getCurrGrossIncomeByCI());
    		course.setTotalGrossBalance(course.getAllGrossIncome() - course.getAllNetExpense());
    		
    		course.setAllNetExpense(payedCompleteClassNetExpense + unpayedCompleteClassNetExpense);
    		course.setEstimatedNumberOfCurrentCalsses(CalcUtil.getEstimatedNumberOfAllClassesByDates(course, course.getStartDate(), new Date()));
    		course.setEstimatedNumberOfAllCalsses(CalcUtil.getEstimatedNumberOfAllClassesByCourseDates(course));
    		course.setSumOfCompletedClasses(CalcUtil.getSumOfCompletedClasses(completedClassList));    		
    		    		
    		double oneClassCourseProfit = CalcUtil.getOneClassCourseProfit(courseStudentList, courseStudentDiscountList, course);    		
    		course.setEstimatedTotalGrossAmount(course.getEstimatedNumberOfCurrentCalsses() * oneClassCourseProfit); 
    		    		
    		double courseVATForPeriod = CalcUtil.getPayPerClassCourseVATForNumberOfClasses(courseStudentList, courseStudentDiscountList, course, course.getEstimatedNumberOfCurrentCalsses());
    		course.setEstimatedTotalNetAmount(course.getEstimatedTotalGrossAmount() - courseVATForPeriod);
       	}    	    		
    	if(course.getVat() || course.getCompany()) {
    		course.setTotalNetBalance(course.getAllNetIncome() - course.getAllNetExpense());
    	} else {
    		course.setTotalNetBalance(course.getTotalGrossBalance());
    	}
    	LOGGER.debug("course TotalBalance after calc: " + course.getTotalGrossBalance());
    }
    
/*    private static Map<Student, Set<Discount>> getStudentDiscountSetMap(List<CourseStudentDiscount> courseStudentDiscountList) { 
    	Map<Student, Set<Discount>> courseStudentDiscountMap = new HashMap<Student, Set<Discount>>();
		
    	for(CourseStudentDiscount courseStudentDiscount : courseStudentDiscountList) {
    		Set<Discount> studentDiscountSet;
    		Student student = courseStudentDiscount.getStudent();
    		if(courseStudentDiscountMap.containsKey(student)) {
    			studentDiscountSet = courseStudentDiscountMap.get(student);
    		} else {
    			studentDiscountSet = new HashSet<Discount>();
    		}
    		studentDiscountSet.add(courseStudentDiscount.getDiscount());
    		courseStudentDiscountMap.put(student, studentDiscountSet);
    	}
    	return courseStudentDiscountMap;
    }
*/
    
    void populateEditForm(Model uiModel, Course course, CourseCopy courseCopy, ApplicationContext ctx, Locale locale) {
        uiModel.addAttribute("course", course);
        uiModel.addAttribute("courseCopy", courseCopy);
        uiModel.addAttribute("draftStatus", ClassStatus.DRAFT);
        addDateTimeFormatPatterns(uiModel);
        
        uiModel.addAttribute("instantCourseTypes", Arrays.asList(InstantCourseType.values()));
        uiModel.addAttribute("instantCourseFormats", Arrays.asList(InstantCourseFormat.values()));
        uiModel.addAttribute("classformats", Arrays.asList(ClassFormat.values()));
        uiModel.addAttribute("classlevelses", Arrays.asList(ClassLevels.values()));
        uiModel.addAttribute("classstatuses", Arrays.asList(ClassStatus.values()));
        uiModel.addAttribute("classtypes", Arrays.asList(ClassType.values()));
        uiModel.addAttribute("languageses", Arrays.asList(Languages.values()));
        uiModel.addAttribute("classspecializations", Arrays.asList(ClassSpecializationType.values()));        
        uiModel.addAttribute("rooms", Arrays.asList(Room.values()));        
        uiModel.addAttribute("classTimes",  createOptionListFromClassTimes(course, ctx, locale));
        addClassTimeDependencies(uiModel, ctx, locale);
    }
    
    private static void addClassTimeDependencies(Model uiModel, ApplicationContext ctx, Locale locale) {
    	List<SelectOption> days = new ArrayList<SelectOption>();
    	for(int i = 0; i<7; i++) {
    		SelectOption option = new SelectOption();
    		option.setId(Integer.toString(i));    		
    		option.setLabel(ctx.getMessage("select_option_day_" + i, null, locale));
    		days.add(option);
    	}    	
    	
    	List<String> hours = new ArrayList<String>();
    	List<String> minutes = new ArrayList<String>();
    	
    	DecimalFormat myFormatter = new DecimalFormat("00");    	
    	for(int i = 0; i < 60; i++) {
    		String number = myFormatter.format(i);     	
    		if(i<24) {
    			hours.add(number);
    		}
    		minutes.add(number);
    	}
    	
    	uiModel.addAttribute("days", days);
    	uiModel.addAttribute("hours", hours);
    	uiModel.addAttribute("minutes", minutes);
    }
    
    private static String setClassTimesStrFromOptionList(Course course) {
    	String retVal = null;
    	if(course.isFlexibleClassTime()) {
    		retVal = FLEXIBLE_TIME;
    	} else {
    		retVal = WebUtil.getStringFromMultiSelectListOptions(course.getClassTimesList());	    	
    	}
    	return retVal;
    }
    
    public static List<SelectOption> createOptionListFromClassTimes(Course course, ApplicationContext ctx, Locale locale) {
    	LOGGER.debug("createOptionListFromClassTimes called, string: " + course.getTimeOfClasses());    	
    	List<SelectOption> options = new ArrayList<SelectOption>();    
    	if(course.getTimeOfClasses() != null && !course.getTimeOfClasses().equals("")) {
    		if(course.getTimeOfClasses().equals(FLEXIBLE_TIME)) {
    			course.setFlexibleClassTime(true);
    		} else {
    			course.setFlexibleClassTime(false);
	    		String[] tokens = course.getTimeOfClasses().split(";");
		    	for (String str : tokens) {
		    		SelectOption option = new SelectOption();
		    		option.setId(str);
		    		
		    		StringBuilder sb = new StringBuilder();
		    		int dayIdx = Integer.parseInt(str.substring(0, 1));
		    		sb.append(ctx.getMessage("select_option_day_" + dayIdx, null, locale));
		    		sb.append(": ").append(str.substring(2, 13)).append(" (").append(str.substring(14,15)).append(")");	    		
		    		option.setLabel(sb.toString());
		    		options.add(option);
		    	}
    		}
    	}
    	LOGGER.debug("course.isFlexibleClassTime: " + course.isFlexibleClassTime() + ", returns: " + options);
    	return options;
    }
    
    private static List<SelectOption> courseStudentList2SelectOptionList(List<CourseStudent> courseStudentList) {
    	List<SelectOption> retVal = new ArrayList<SelectOption>();
    	if(courseStudentList != null) {
    		for (CourseStudent courseStudent : courseStudentList) {
				SelectOption so = new SelectOption();
				so.setId(courseStudent.getStudent().getId().toString());
				so.setLabel(courseStudent.getStudent().getPersonalData().getLastName() + " " + courseStudent.getStudent().getPersonalData().getFirstName());
				retVal.add(so);
			}
    	}
    	return retVal;
    }
    
    private static List<SelectOption> courseTeacherList2SelectOptionList(List<CourseTeacher> courseTeacherList) {
    	List<SelectOption> retVal = new ArrayList<SelectOption>();
    	if(courseTeacherList != null) {
    		for (CourseTeacher courseTeacher : courseTeacherList) {
				SelectOption so = new SelectOption();
				so.setId(courseTeacher.getTeacher().getId().toString());
				so.setLabel(courseTeacher.getTeacher().getPersonalData().getLastName() + " " + courseTeacher.getTeacher().getPersonalData().getFirstName());
				retVal.add(so);
			}
    	}
    	return retVal;
    }
    
    private Course handleCourseCopyPost(CourseCopy courseCopy, HttpServletRequest request) {
    	Course newCourse = courseCopy.getCourse();
        newCourse.setTimeOfClasses(setClassTimesStrFromOptionList(newCourse));
        newCourse.setId(null);
        newCourse.setVersion(null);        
        newCourse.setStartDate(courseCopy.getNewStartDate());
        newCourse.setEndDate(courseCopy.getNewEndDate());
        
        courseService.saveCourse(newCourse, request);
        LOGGER.debug("new course created with id: " + newCourse.getId() + ", courseId: " + newCourse.getCourseId());
        
        if(courseCopy.getCourseStudents() != null) {
        	LOGGER.debug("courseStudents is not null");
        	for (String studentIdStr : courseCopy.getCourseStudents()) {
        		LOGGER.debug("StudentIdStr: " + studentIdStr);
				Long studentId = Long.valueOf(studentIdStr);
				Student student = studentService.findStudent(studentId);
				CourseStudent cs = new CourseStudent();
				cs.setCourse(newCourse);
				cs.setStudent(student);
				cs.setStartDate(courseCopy.getNewStartDate());
				cs.setEndDate(courseCopy.getNewEndDate());
				courseStudentService.saveCourseStudent(cs);
				LOGGER.debug("new courseStudent (" + cs.getId() + ") created for new course, studentID: " + student.getId());
			}
        }

        if(courseCopy.getCourseTeacher() != null) {
        	LOGGER.debug("courseTeacher is not null");
        	for (String teacherIdStr : courseCopy.getCourseTeacher()) {
        		LOGGER.debug("teacherIdStr: " + teacherIdStr);
				Long teacherId = Long.valueOf(teacherIdStr);				
				Teacher teacher = teacherService.findTeacher(teacherId);
				CourseTeacher cs = new CourseTeacher();
				cs.setCourse(newCourse);
				cs.setTeacher(teacher);
				cs.setStartDate(courseCopy.getNewStartDate());
				cs.setEndDate(courseCopy.getNewEndDate());
				//cs.setNumerOfClasses(newCourse.getSumOfClasses());
				courseTeacherService.saveCourseTeacher(cs);
				LOGGER.debug("new courseTeacher (" + cs.getId() + ") created for new course, teacherID: " + teacher.getId());
        	}
        }
        
        if(courseCopy.getCourseStudentDiscounts() != null) {
        	LOGGER.debug("getCourseStudentDiscounts is not null");
        	for (String csdiStr : courseCopy.getCourseStudentDiscounts()) {
        		String[] strs = csdiStr.split("_");
        		Long csdId = Long.valueOf(strs[1]);
        		CourseStudentDiscount csd = courseStudentDiscountService.findCourseStudentDiscount(csdId);
        		CourseStudentDiscount newCsd = new CourseStudentDiscount();
        		newCsd.setCourse(newCourse);
        		newCsd.setStudent(csd.getStudent());
        		newCsd.setDiscount(csd.getDiscount());
        		courseStudentDiscountService.saveCourseStudentDiscount(newCsd, request);
        		LOGGER.debug("create new courseStudentDiscount, id: " + newCsd.getId());
			}
        }
        return newCourse;
    }
    
    private List<SelectOption> generateSelectOptionCSDListFromCSList(List<CourseStudent> courseStudentList) {
    	List<SelectOption> csdol = new ArrayList<SelectOption>();
    	for (CourseStudent courseStudent : courseStudentList) {
			List<CourseStudentDiscount> csd = courseStudentDiscountService.findByCoursStudent(courseStudent);
			if(csd != null) {
				for (CourseStudentDiscount courseStudentDiscount : csd) {										
					Student student = courseStudentDiscount.getStudent();
					Discount disc = courseStudentDiscount.getDiscount();
					StringBuilder sb = new StringBuilder();
					sb.append(student.getPersonalData().getLastName()).append(" ").append(student.getPersonalData().getFirstName())
					.append(", ").append(disc.getLabelString());
					
					SelectOption so = new SelectOption();
					so.setId(courseStudent.getStudent().getId() + "_" + courseStudentDiscount.getId());
					so.setLabel(sb.toString());
					csdol.add(so);
				}
			}
		}
    	return csdol;
    }
    
}
