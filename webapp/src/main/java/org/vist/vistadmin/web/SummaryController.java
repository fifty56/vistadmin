package org.vist.vistadmin.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.vist.vistadmin.domain.BillingAddress;
import org.vist.vistadmin.domain.CompletedClass;
import org.vist.vistadmin.domain.Course;
import org.vist.vistadmin.domain.CourseFiscalDTO;
import org.vist.vistadmin.domain.CourseIncome;
import org.vist.vistadmin.domain.CourseStudent;
import org.vist.vistadmin.domain.CourseStudentDiscount;
import org.vist.vistadmin.domain.CourseTeacher;
import org.vist.vistadmin.domain.Discount;
import org.vist.vistadmin.domain.FromToInterval;
import org.vist.vistadmin.domain.Student;
import org.vist.vistadmin.domain.Teacher;

import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;
import org.vist.vistadmin.domain.BillingAddress;
import org.vist.vistadmin.domain.common.ClassStatus;
import org.vist.vistadmin.domain.common.Data;
import org.vist.vistadmin.domain.common.SelectOption;
import org.vist.vistadmin.service.AddressService;
import org.vist.vistadmin.service.BillingAddressService;
import org.vist.vistadmin.service.CompletedClassService;
import org.vist.vistadmin.service.CourseIncomeService;
import org.vist.vistadmin.service.CourseService;
import org.vist.vistadmin.service.CourseStudentDiscountService;
import org.vist.vistadmin.service.CourseStudentService;
import org.vist.vistadmin.service.CourseTeacherService;
import org.vist.vistadmin.service.CourseWithMoreCurrentTeachers;
import org.vist.vistadmin.service.TeacherService;
import org.vist.vistadmin.util.CalcUtil;
import org.vist.vistadmin.util.DateUtil;
import org.vist.vistadmin.reporting.VistReportException;
import org.vist.vistadmin.reporting.VistReportService;
import org.vist.vistadmin.web.BillingAddressController;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@RequestMapping("/summary")
@Controller
public class SummaryController extends BaseController {

	private static final Logger LOGGER =  LoggerFactory.getLogger(SummaryController.class);
	
	@Autowired	
	CourseService courseService;
	
	@Autowired
	CourseTeacherService courseTeacherService;
	
	@Autowired
	CourseStudentService courseStudentService;
	
	@Autowired
	CompletedClassService completedClassService;
	
	@Autowired
	CourseIncomeService courseIncomeService;
	
	@Autowired
	CourseStudentDiscountService courseStudentDiscountService;
	
	@Autowired
	TeacherService teacherService;
	
	@Autowired
	private ApplicationContext context;
	
	@RequestMapping(method = RequestMethod.GET)
	public String showSummary(Model uiModel, Locale locale, HttpServletRequest httpServletRequest, HttpServletResponse response) throws Exception {
		
		List<Course> allCourses = getCoursesFilledWithFiscalData(courseStudentService, courseTeacherService, completedClassService, courseIncomeService, courseStudentDiscountService, courseService);				
		
        uiModel.addAttribute("courses", allCourses);
		return "summary/show";
	}		
	
	
	@RequestMapping(value = "/interval/{month}", produces = "text/html")
    public String showIntervalSummary(@PathVariable("month") int month, Model uiModel) {
		
		Date from = new Date();
		Date till = new Date();				
				
		from.setDate(1);
		till.setMonth(from.getMonth() + 1);		
		till.setDate(1);

		calculateIntervalCost(from, till, uiModel);				
		
		return "summary/interval";
	}
	
	@RequestMapping(value = "/interval", method = RequestMethod.POST, produces = "text/html")
    public String intervalCalc(FromToInterval fti, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
		
		calculateIntervalCost(fti.getFromDate(), fti.getToDate(), uiModel);
		
		return "summary/interval";
	}	
	
	@RequestMapping(value = "/month", produces = "text/html", method = RequestMethod.GET)
    public String showMonthlySummary(Model uiModel, Locale locale) {
			
		calculateMonthlyCost(new Date(), uiModel, locale);
		
		return "summary/month";
	}
	
	@RequestMapping(value = "/month", method = RequestMethod.POST, produces = "text/html")
    public String showMonthlyIntervalSummary(FromToInterval fti, BindingResult bindingResult, Model uiModel, Locale locale, HttpServletRequest httpServletRequest) {				
		
		calculateMonthlyCost(fti.getFromDate(), uiModel, locale);
		
		return "summary/month";
	}
	
	private void calculateMonthlyCost(Date selectedDate, Model uiModel, Locale locale) {
						
		final Calendar startCal = Calendar.getInstance();
		startCal.setTime(selectedDate);
		startCal.set(Calendar.DAY_OF_MONTH, 1);
		startCal.set(Calendar.HOUR_OF_DAY, 0);
		startCal.set(Calendar.MINUTE, 0);
		startCal.set(Calendar.SECOND, 1);				  
				
		final Calendar endCal = Calendar.getInstance();
		endCal.setTime(selectedDate);
		endCal.set(Calendar.DAY_OF_MONTH, 1);
		endCal.set(Calendar.HOUR_OF_DAY, 0);
		endCal.set(Calendar.MINUTE, 0);
		endCal.set(Calendar.SECOND, 1);
		
		if(startCal.get(Calendar.MONTH) == Calendar.DECEMBER) {
			endCal.set(Calendar.YEAR, startCal.get(Calendar.YEAR) + 1);
			endCal.set(Calendar.MONTH, Calendar.JANUARY);
		} else {
			endCal.set(Calendar.MONTH, startCal.get(Calendar.MONTH) + 1);
		}
		
		Date fromDate = startCal.getTime();
		Date toDate = endCal.getTime();
		
		List<Course> liveCourses = courseService.findLiveClassesAtGivenPeriod(fromDate, toDate);				
		
		double finalEstimatedGrossAmount = 0.0;		
		double finalEstimatedNettAmount = 0.0;
		double finalComplClassGrossAmount = 0.0;
		double finalComplClassNettAmount = 0.0;
		List<CourseFiscalDTO> cfdList = new ArrayList<CourseFiscalDTO>();
				
		for (Course course : liveCourses) {
			CourseFiscalDTO cfd = new CourseFiscalDTO();
			
			cfd.setCourse(course);									
			
			Date periodStartDate = startCal.getTime();
			Date periodEndDate = endCal.getTime();						
			
			Date courseStartDate = course.getStartDate();
			Date courseEndDate = course.getEndDate();
			if(courseStartDate.after(fromDate) && (courseEndDate.after(toDate) || DateUtil.isSameDay(courseEndDate, toDate))) {
				periodStartDate = courseStartDate;
			} else if(courseEndDate.before(toDate) && (courseStartDate.before(fromDate) || DateUtil.isSameDay(courseStartDate, fromDate))) {
				periodEndDate = courseEndDate;
			} else if(courseStartDate.after(fromDate) && courseEndDate.before(toDate)) {
				periodStartDate = courseStartDate;
				periodEndDate = courseEndDate;
			}						
			
			cfd.setPeriodStartDate(periodStartDate);
			cfd.setPeriodEndDate(periodEndDate);
			
			final Calendar periodStartCal = Calendar.getInstance();
			periodStartCal.setTime(periodStartDate);
			final Calendar periodEndCal = Calendar.getInstance();
			periodEndCal.setTime(periodEndDate);
			
			List<CourseStudent> startCourseStudentList = courseStudentService.getStudentsByDateAndCourse(course, periodStartDate);
			List<CourseStudent> endCourseStudentList = courseStudentService.getStudentsByDateAndCourse(course, periodEndDate);
			
			if(startCourseStudentList == null || endCourseStudentList == null || 
					startCourseStudentList.size() == 0 || endCourseStudentList.size() == 0 || 
					startCourseStudentList.size() != endCourseStudentList.size()) {
				
				LOGGER.warn("cannot calculate exact montly fiscal data for course: " + course.getId() + 
						", because the student list is not the same or 0 or null at the start or end of the checked period: " +
						periodStartDate + " - " + periodEndDate);
				
				cfd.setError(true);
				cfd.setErrorMsg(context.getMessage("error_course_fiscaldata_invalid_studentlist", new Object[] {course.getCourseId(), periodStartDate, periodEndDate}, locale));
				cfdList.add(cfd);
				continue;
			} if(course.isFlexibleClassTime()) {
				cfd.setHasCourseFlexibleClassTime(course.isFlexibleClassTime());
				LOGGER.warn("cannot calculate exact montly fiscal data for course: " + course.getId() + 
						", because it has flexible hours");
				
				cfd.setError(true);
				cfd.setErrorMsg(context.getMessage("error_course_fiscaldata_flexibleclass", new Object[] {course.getCourseId()}, locale));
				cfdList.add(cfd);					
				continue;
			} else {			        		       
				List<CourseTeacher> courseTeacherList = courseTeacherService.findByCourse(course);		        
				List<CourseStudentDiscount> courseStudentDiscountList = courseStudentDiscountService.findByCourse(course);
				
				CourseController.assignCourseStudentDiscountToCourseStudent(courseStudentDiscountList, startCourseStudentList);
				Teacher currentTeacher = null;
				try {
					currentTeacher = courseTeacherService.getCurrentTeachersByDateAndCourse(course, periodStartDate).get(0).getTeacher();
				} catch(Exception e) {}
				if(currentTeacher == null) {
					try {
						currentTeacher = courseTeacherService.getCurrentTeachersByDateAndCourse(course, periodEndDate).get(0).getTeacher();
					} catch(Exception e) {
						cfd.setError(true);
						cfd.setErrorMsg(context.getMessage("error_courseteacher_no_teacher_at_dates",new Object[] {periodStartDate, periodEndDate}, locale));
						cfdList.add(cfd);
						continue;
					}
				}
				
				List<CompletedClass> completedClassList = completedClassService.findByCourseAndCompYearAndCompMonth(course, periodStartCal.get(Calendar.YEAR), periodStartCal.get(Calendar.MONTH) + 1);
				
				CourseController.setInOutComes(course, startCourseStudentList, null, null, courseTeacherList, courseStudentDiscountList, true, currentTeacher);
										
				long periodLengthLong = periodEndDate.getTime() - periodStartDate.getTime() + CalcUtil.ONE_DAY_IN_MILLIS;												
				int periodLengthDays = (int) Math.round(((double)periodLengthLong) / 1000 / 60 / 60 / 24);				 											
				
				cfd.setNumberOfDaysInPeriod(periodLengthDays);							
				cfd.setNumberOfEstimatedClassesForPeriod(CalcUtil.getEstimatedNumberOfAllClassesByDates(course, periodStartDate, periodEndDate));
				cfd.setNumberOfCompletedClasses(CalcUtil.getSumOfCompletedClasses(completedClassList));
				
				if(course.getPayPerClasses()) {
					double oneClassCourseProfit = CalcUtil.getOneClassCourseProfit(startCourseStudentList, courseStudentDiscountList, course);																						
					cfd.setEstimatedTotalNumberOfClasses(CalcUtil.getEstimatedNumberOfAllClassesByCourseDates(course));					
					
					cfd.setTotalEstimatedGrossProfitForPerion(oneClassCourseProfit * cfd.getNumberOfEstimatedClassesForPeriod());
					cfd.setTotalGrossProfitByCompClassForPerion(oneClassCourseProfit * cfd.getNumberOfCompletedClasses());

					if(course.getVat() || course.getCompany()) {
						double courseVATForPeriod = CalcUtil.getPayPerClassCourseVATForNumberOfClasses(startCourseStudentList, courseStudentDiscountList, course, cfd.getNumberOfEstimatedClassesForPeriod());
						cfd.setTotalEstimatedNettProfitForPerion(cfd.getTotalEstimatedGrossProfitForPerion() - courseVATForPeriod);
						
						double courseVATForCompletedCalsses = CalcUtil.getPayPerClassCourseVATForNumberOfClasses(startCourseStudentList, courseStudentDiscountList, course, cfd.getNumberOfCompletedClasses());					
						cfd.setTotalNettProfitByCompClassForPerion(cfd.getTotalGrossProfitByCompClassForPerion()  - courseVATForCompletedCalsses);
					} else {
						cfd.setTotalEstimatedNettProfitForPerion(cfd.getTotalEstimatedGrossProfitForPerion());
						cfd.setTotalNettProfitByCompClassForPerion(cfd.getTotalGrossProfitByCompClassForPerion());
					}																		
				} else {
					int totalNumOfClasses = course.getSumOfClasses();
					double periodRatio = (double)cfd.getNumberOfEstimatedClassesForPeriod() / (double)totalNumOfClasses;										
					double completedClassRatio = (double)cfd.getNumberOfCompletedClasses() / (double)totalNumOfClasses;
					
					cfd.setTotalNumberOfClasses(totalNumOfClasses);					
					cfd.setTotalEstimatedGrossProfitForPerion(periodRatio * course.getTotalGrossBalance());				
					cfd.setTotalEstimatedNettProfitForPerion(periodRatio * course.getTotalNetBalance());
					
					cfd.setTotalGrossProfitByCompClassForPerion(completedClassRatio * course.getTotalGrossBalance());
					if(course.getVat() || course.getCompany()) {
						cfd.setTotalNettProfitByCompClassForPerion(completedClassRatio * course.getTotalNetBalance());
					} else {
						cfd.setTotalNettProfitByCompClassForPerion(cfd.getTotalGrossProfitByCompClassForPerion());
					}
				}
														
				finalEstimatedGrossAmount += cfd.getTotalEstimatedGrossProfitForPerion();
				finalEstimatedNettAmount  += cfd.getTotalEstimatedNettProfitForPerion();
				
				finalComplClassGrossAmount += cfd.getTotalGrossProfitByCompClassForPerion();
				finalComplClassNettAmount += cfd.getTotalNettProfitByCompClassForPerion();
			}
			cfdList.add(cfd);
		}
		uiModel.addAttribute("CourseFiscalList", cfdList);		
		uiModel.addAttribute("courseList", liveCourses);
		uiModel.addAttribute("fromDate", fromDate);
		uiModel.addAttribute("toDate", toDate);
		uiModel.addAttribute("finalEstimatedGrossAmount", finalEstimatedGrossAmount);
		uiModel.addAttribute("finalEstimatedNettAmount", finalEstimatedNettAmount);
		uiModel.addAttribute("finalComplCalssGrossAmount", finalComplClassGrossAmount);
		uiModel.addAttribute("finalComplCalssNettAmount", finalComplClassNettAmount);
		uiModel.addAttribute("fti", new FromToInterval());
	}
	
	private void calculateIntervalCost(Date fromDate, Date toDate, Model uiModel) {
		List<CompletedClass> payedCompletedClassList = completedClassService.findByPaymentDateBetweenAndPayed(fromDate, toDate, true);
		List<CourseIncome> payedCourseIncomeList = courseIncomeService.findByPaymentDateBetweenAndPayed(fromDate, toDate, true);
		
		List<CompletedClass> notPayedCompletedClassList = completedClassService.findByDeadlineDateBetweenAndPayed(fromDate, toDate, false);
		List<CourseIncome> notPayedCourseIncomeList = courseIncomeService.findByDeadLineDateBetweenAndPayed(fromDate, toDate, false);
		
		CalcUtil.setCompletedClassExpense(payedCompletedClassList, true);
		CalcUtil.setCompletedClassExpense(notPayedCompletedClassList, true);
		
		int payedSumOfExpenses = 0;
		for(CompletedClass cs : payedCompletedClassList) {
			payedSumOfExpenses += cs.getExpense();
		}
		
		int payedSumOfIncomes = 0;
		for(CourseIncome ci : payedCourseIncomeList) {
			payedSumOfIncomes += ci.getAmount();
		}
		
		uiModel.addAttribute("payedSumOfExpenses", payedSumOfExpenses);
		uiModel.addAttribute("payedSumOfIncomes", payedSumOfIncomes);
		
		int notPayedSumOfExpenses = 0;
		for(CompletedClass cs : notPayedCompletedClassList) {
			notPayedSumOfExpenses += cs.getExpense();
		}
		
		int notPayedSumOfIncomes = 0;
		for(CourseIncome ci : notPayedCourseIncomeList) {
			notPayedSumOfIncomes += ci.getAmount();
		}
		
		uiModel.addAttribute("notPayedSumOfExpenses", notPayedSumOfExpenses);
		uiModel.addAttribute("notPayedSumOfIncomes", notPayedSumOfIncomes);
		
		uiModel.addAttribute("sumOfExpenses", (notPayedSumOfExpenses + payedSumOfExpenses));
		uiModel.addAttribute("sumOfIncomes", (notPayedSumOfIncomes + payedSumOfIncomes));
		
		uiModel.addAttribute("fromDate", fromDate);
		uiModel.addAttribute("toDate", toDate);
		
		uiModel.addAttribute("payedCompletedClassList", payedCompletedClassList);
		uiModel.addAttribute("payedCourseIncomeList", payedCourseIncomeList);
		uiModel.addAttribute("notPayedCompletedClassList", notPayedCompletedClassList);
		uiModel.addAttribute("notPayedCourseIncomeList", notPayedCourseIncomeList);
		
		FromToInterval fti = new FromToInterval();
		uiModel.addAttribute("fti", fti);
	}
	
	public static List<Course> getCoursesFilledWithFiscalData(CourseStudentService courseStudentService, CourseTeacherService courseTeacherService,
			CompletedClassService completedClassService, CourseIncomeService courseIncomeService, CourseStudentDiscountService courseStudentDiscountService, 
			CourseService courseService) {
		
		List<Course> allCourses = courseService.findAllClasses();
		
		for(Course course: allCourses) {				
	        List<CourseStudent> courseStudentList = courseStudentService.findByCourse(course);
	        List<CourseTeacher> courseTeacherList = courseTeacherService.findByCourse(course);
	        List<CompletedClass> completedClassList = completedClassService.findByCourse(course);
			List<CourseIncome> courseIncomeList = courseIncomeService.findByCourse(course);
			List<CourseStudentDiscount> courseStudentDiscountList = courseStudentDiscountService.findByCourse(course); 
				
			CourseController.assignCourseStudentDiscountToCourseStudent(courseStudentDiscountList, courseStudentList);		
			CourseController.setInOutComes(course, courseStudentList, completedClassList, courseIncomeList, courseTeacherList, courseStudentDiscountList, false, null);
			
			CalcUtil.setCompletedClassExpense(completedClassList, true);
		}
		
		return allCourses;
	}
}
