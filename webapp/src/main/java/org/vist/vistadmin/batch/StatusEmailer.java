package org.vist.vistadmin.batch;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.vist.vistadmin.domain.CompletedClass;
import org.vist.vistadmin.domain.Course;
import org.vist.vistadmin.domain.CourseIncome;
import org.vist.vistadmin.service.CompletedClassService;
import org.vist.vistadmin.service.CourseIncomeService;
import org.vist.vistadmin.service.CourseService;
import org.vist.vistadmin.service.CourseStudentDiscountService;
import org.vist.vistadmin.service.CourseStudentService;
import org.vist.vistadmin.service.CourseTeacherService;
import org.vist.vistadmin.service.TeacherService;
import org.vist.vistadmin.web.SummaryController;

@Component
public class StatusEmailer {

	private static Logger LOGGER = LoggerFactory.getLogger(StatusEmailer.class);
	
	
	@Value("#{email_props['email.site.url']}")
	private String vistadminRootUrl; 
	
	@Autowired
	CourseService courseService;

	@Autowired
	CourseIncomeService coourseIncomeService;
	
	@Autowired
	CompletedClassService completedCalssService;		
	
	
	@Autowired
	CourseTeacherService courseTeacherService;
	
	@Autowired
	CourseStudentService courseStudentService;
		

	
	@Autowired
	CourseStudentDiscountService courseStudentDiscountService;
	
	@Autowired
	TeacherService teacherService;
	
	@Autowired
	VistEmailSender emailSender;	           
	
	@Transactional
	public void sendStatusMail() {
		StringBuilder sb = new StringBuilder();
		sb.append("<hr/>");
		sb.append("<h1>Tanfolyamok</h1>");
		sb.append(getFinishedLiveClassesString());
		sb.append("<hr/>");
		sb.append(getClassesWillBeFinishedInTwoWeeksString());
		sb.append("<hr/>");		
		sb.append(getDraftFor2weeksClassesString());
		sb.append("<hr/>");
		sb.append("<h1>Tanfolyam befizetések</h1>");
		sb.append(getCourseIncomeDeadlineOver());		
		sb.append("<hr/>");
		sb.append(getCourseIncomeDeadline2Weeks());
		sb.append("<hr/>");
		sb.append(getCourseIncomeNoDeadline());
		sb.append("<hr/>");
		sb.append("<h1>Letanitott orak</h1>");
		sb.append(getCompleteClassDeadlineOver());		
		sb.append("<hr/>");
		sb.append(getCompleteClassDeadline2Weeks());
		sb.append("<hr/>");
		sb.append(getCompleteClassNoDeadline());
		sb.append("<hr/>");
		sb.append(getFiscalData());
		
		emailSender.sendStatusEmail(sb.toString());        
    }
		
	
	private String getClassesWillBeFinishedInTwoWeeksString() {
		List<Course> courses = courseService.findLiveClassesEndedinTwoWeeks();
		if(courses != null && courses.size() > 0) {
			String coursesString = convertCourseListToString(courses);
			return "<p>Tanfolyamok amelyek 2 héten belül véget érnek:" + coursesString + "</p>";
		} else {
			return "";
		}
	}
	
	private String getFinishedLiveClassesString() {
		List<Course> courses = courseService.findLiveClassesEnded();
		if(courses != null && courses.size() > 0) {
			String coursesString = convertCourseListToString(courses);
			return "<p>Tanfolyamok amelyek véget értek, de még nincsenek arhiválva:" + coursesString + 
					"<br/>AKCIÓ: Összes be- és kifizetést felvinni, majd a tanfolyam státuszát arhívba tenni</p>";
		} else {
			return "";
		}
	}
	
	private String getDraftFor2weeksClassesString() {
		List<Course> courses = courseService.findDraftClassesOlderThan2weeks();
		if(courses != null && courses.size() > 0) {
			String coursesString = convertCourseListToString(courses);
			return "<p>Tanfolyamok amelyek több mint 2 hete vázlat állapotban vannak:" + coursesString + 
					"<br/>AKCIÓ: tanulókat és tanárokat felvinni, majd a tanfolyam státuszát elindult-ba rakni. Vagy kitörölni, ha nem fog elindulni</p>";
		} else {
			return "";
		}
	}	
	
	private String getCompleteClassNoDeadline() {
		List<CompletedClass> completedClassList = completedCalssService.findDeadlineDateIsNull();
		if(completedClassList != null && completedClassList.size() > 0) {
			String ccsString = convertCompletedClassListToString(completedClassList);
			return "<p>Letanított órák, amelyeknek nincs kifizetés határideje megadva:" + ccsString + 
					"<br/>AKCIÓ: Adj meg kifizetési határidőt, hogy lehessen figyelmeztetni a határidő lejárta előtt.</p>";
		} else {
			return "";
		}
	}
	
	private String getCompleteClassDeadline2Weeks() {
		List<CompletedClass> completedClassList = completedCalssService.findDeadlineDateIn2Weeks();
		if(completedClassList != null && completedClassList.size() > 0) {
			String ccsString = convertCompletedClassListToString(completedClassList);
			return "<p>Letanított órák, amelyeknek kifizetés határideje 2 hét múlva lejár:" + ccsString + 
					"<br/>AKCIÓ: Hamarosan ki kell őket fizetni.</p>";
		} else {
			return "";
		}
	}	
	
	private String getCompleteClassDeadlineOver() {
		List<CompletedClass> completedClassList = completedCalssService.findDeadlineDateOver();
		if(completedClassList != null && completedClassList.size() > 0) {
			String ccsString = convertCompletedClassListToString(completedClassList);
			return "<p>Letanított órák, amelyeknek kifizetés határideje lejárt:" + ccsString + 
					"<br/>AKCIÓ: Ki kell őket fizetni.</p>";
		} else {
			return "";
		}
	}	

	
	private String getCourseIncomeNoDeadline() {
		List<CourseIncome> courseIncomeList = coourseIncomeService.findDeadlineDateIsNull();
		if(courseIncomeList != null && courseIncomeList.size() > 0) {
			String ccsString = convertCourseIncomeListToString(courseIncomeList);
			return "<p>Tanfolyem befizetések, amelyeknek nincs befizetés határideje megadva:" + ccsString + 
					"<br/>AKCIÓ: Adj meg befizetési határidőt, hogy lehessen figyelmeztetni a határidő lejárta előtt.</p>";
		} else {
			return "";
		}
	}
	
	private String getCourseIncomeDeadline2Weeks() {
		List<CourseIncome> courseIncomeList = coourseIncomeService.findDeadlineDateIn2Weeks();
		if(courseIncomeList != null && courseIncomeList.size() > 0) {
			String ccsString = convertCourseIncomeListToString(courseIncomeList);
			return "<p>Tanfolyem befizetések, amelyeknek befizetési határideje 2 hét múlva lejár:" + ccsString + 
					"<br/>AKCIÓ: Hamarosan ki kell őket hajtani.</p>";
		} else {
			return "";
		}
	}	
	
	private String getCourseIncomeDeadlineOver() {
		List<CourseIncome> courseIncomeList = coourseIncomeService.findDeadlineDateOver();
		if(courseIncomeList != null && courseIncomeList.size() > 0) {
			String ccsString = convertCourseIncomeListToString(courseIncomeList);
			return "<p>Tanfolyem befizetések, amelyeknek befizetési határideje lejárt:" + ccsString + 
					"<br/>AKCIÓ: Be kell őket hajtani.</p>";
		} else {
			return "";
		}
	}

	
	private String convertCourseListToString(List<Course> courseList) {		
		StringBuilder sb = new StringBuilder();
		if(courseList != null) {
			sb.append("<ul>");
			LOGGER.debug("number of courses: " + courseList.size());
			for (Course course : courseList) {
				sb.append("<li><a href=\"").append(vistadminRootUrl).append("courses/").append(course.getId()).append("\"").
				append(">").append(course.getCourseId()).append(": ").append(course.getLang()).append(" ").
				append(course.getCourseFormat()).append(" ").append(course.getCourseType()).append(" ").append(course.getCourseLevel()).
				append("</a></li>");
			}
			sb.append("</ul>");
		}
		return sb.toString();
	}
	
	private String convertCompletedClassListToString(List<CompletedClass> completedClassList) {		
		StringBuilder sb = new StringBuilder();
		if(completedClassList != null) {
			sb.append("<ul>");
			LOGGER.debug("number of completedClasses: " + completedClassList.size());
			for (CompletedClass cc : completedClassList) {
				sb.append("<li><a href=\"").append(vistadminRootUrl).append("completedclasses/").append(cc.getId()).append("\"").
				append(">").append("Tanfolyam: " +  cc.getCourse().getCourseId()).append(", tanár: ").append(cc.getTeacher().getPersonalData().getLastName()).
				append(" ").append(cc.getTeacher().getPersonalData().getLastName()).
				append(" (").append(cc.getCompYear()).append("-").append(cc.getCompMonth()).append("-").append(cc.getCompWeek()).append(")")					
				.append("</a></li>");
			}
			sb.append("</ul>");
		}
		return sb.toString();
	}
	
	private String convertCourseIncomeListToString(List<CourseIncome> courseIncomeList) {		
		StringBuilder sb = new StringBuilder();
		if(courseIncomeList != null) {
			sb.append("<ul>");
			LOGGER.debug("number of courseIncomes: " + courseIncomeList.size());
			for (CourseIncome cc : courseIncomeList) {
				sb.append("<li><a href=\"").append(vistadminRootUrl).append("courseincomes/").append(cc.getId()).append("\"").
				append(">").append("Tanfolyam: " +  cc.getCourse().getCourseId()).append(", diák: ").append(cc.getStudent().getPersonalData().getLastName()).
				append(" ").append(cc.getStudent().getPersonalData().getLastName()).
				append(" (").append(cc.getYear()).append("-").append(cc.getMonth()).append("-").append(cc.getWeek()).append(")")					
				.append("</a></li>");
			}
			sb.append("</ul>");
		}
		return sb.toString();
	}	
	
	
	private String getFiscalData() {		
		StringBuilder sb = new StringBuilder();
		sb.append("<p>Pénzügy:<br/>");
		
		List<Course> courses = SummaryController.getCoursesFilledWithFiscalData(courseStudentService, courseTeacherService,
				completedCalssService, coourseIncomeService, courseStudentDiscountService, 
				 courseService);
		
		double totalCurrIncome = 0.0;
		double totalRemIncomeFromComplClass = 0.0;
		double totalRemGrossIncome = 0.0;
		double totalRemNetIncome = 0.0;
		double totalAllGrossIncome = 0.0;
		double totalAllNetIncome = 0.0;
		
		double totalCurrExpense = 0.0;
		double totalRemGrossExpense = 0.0;
		double totalRemCurrNetExpense = 0.0;
		double totalRemNetExpense = 0.0;
		double totalTotalBalance = 0.0;
				
		for (Course course : courses) {
			totalCurrIncome += course.getCurrGrossIncomeByCI();
			totalRemGrossIncome += course.getRemGrossIncome();
			totalAllGrossIncome += course.getAllGrossIncome();
			
			totalCurrExpense += course.getCurrGrossExpenseByCC();
			totalRemGrossExpense += course.getRemCurrGrossExpenseByCC();
			totalRemCurrNetExpense += course.getRemCurrNetExpenseByCC();
			totalTotalBalance += course.getTotalNetBalance();
			totalRemIncomeFromComplClass += course.getRemCurrGrossIncomeByCI();
			totalRemNetExpense += course.getRemNetExpense();
			totalAllNetIncome += course.getAllNetIncome();
			totalRemNetIncome += course.getRemNetIncome();
		}
		
		if(courses != null) {
			sb.append("<ul>");
			sb.append("<li>Eddig befizetett (bruttó): ").append(totalCurrIncome).append("</li>");
			sb.append("<li>Hátralévő felvitt befizetés: ").append(totalRemIncomeFromComplClass).append("</li>");
			sb.append("<li>Hátralévő összes befizetés (bruttó): ").append(totalRemGrossIncome).append("</li>");
			sb.append("<li>Hátralévő összes befizetés (nettó): ").append(totalRemNetIncome).append("</li>");
			sb.append("<li>Összes befizetés (bruttó): ").append(totalAllGrossIncome).append("</li>");
			sb.append("<li>Összes befizetés (nettó): ").append(totalAllNetIncome).append("</li>");
			sb.append("<li>-</li>");
			sb.append("<li>Már kifizetett (bruttó): ").append(totalCurrExpense).append("</li>");
			sb.append("<li>Most kifizetendő (bruttó): ").append(totalRemGrossExpense).append("</li>");
			sb.append("<li>Most kifizetendő (nettó): ").append(totalRemCurrNetExpense).append("</li>");
			sb.append("<li>További kifizetések (nettó): ").append(totalRemNetExpense).append("</li>");
			sb.append("<li>-</li>");
			sb.append("<li>Tartozás (nettó): ").append(totalRemNetExpense + totalRemCurrNetExpense - totalRemNetIncome).append("</li>");
			sb.append("<li>Teljes egyenleg (): ").append(totalTotalBalance).append("</li>");
			sb.append("</ul>");
		}
		sb.append("</p>");
		return sb.toString();
	}
	
}

