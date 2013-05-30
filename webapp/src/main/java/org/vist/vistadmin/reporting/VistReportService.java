package org.vist.vistadmin.reporting;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabColumnGroup;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabRowGroup;
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRChild;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignSubreport;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vist.vistadmin.calendar.VistCalendarService;
import org.vist.vistadmin.domain.CompletedClass;
import org.vist.vistadmin.domain.Course;
import org.vist.vistadmin.domain.CourseIncome;
import org.vist.vistadmin.domain.CourseStudent;
import org.vist.vistadmin.domain.CourseStudentDiscount;
import org.vist.vistadmin.domain.CourseTeacher;
import org.vist.vistadmin.domain.Student;
import org.vist.vistadmin.domain.Teacher;
import org.vist.vistadmin.domain.common.ClassFormat;
import org.vist.vistadmin.domain.common.Room;
import org.vist.vistadmin.domain.common.SelectOption;
import org.vist.vistadmin.reporting.ClassSheetHelperBean;
import org.vist.vistadmin.reporting.CourseFactoryTest;
import org.vist.vistadmin.reporting.ReportNameValuePair;
import org.vist.vistadmin.service.CompletedClassService;
import org.vist.vistadmin.service.CourseIncomeService;
import org.vist.vistadmin.service.CourseService;
import org.vist.vistadmin.service.CourseStudentDiscountService;
import org.vist.vistadmin.service.CourseStudentService;
import org.vist.vistadmin.service.CourseTeacherService;
import org.vist.vistadmin.service.CourseWithMoreCurrentTeachers;
import org.vist.vistadmin.util.CalcUtil;
import org.vist.vistadmin.util.DateUtil;
import org.vist.vistadmin.web.CourseController;
import org.vist.vistadmin.web.ReportController;

/**
 * Service for processing Jasper reports
 * 
 * ceg letrehozasa: Kapcsolattarto neve, tel szama, email, ... telephely cime
 */
@Service
public class VistReportService {

	private static final Logger LOGGER =  LoggerFactory.getLogger(VistReportService.class);

	private static final int NUMBER_OF_WEEK_PER_MONTH = 4;
	
	private static final String VIST_ADDRESS = "2310, Szigetszentmiklós Gyári út 17";
	
	private static final String COURSE_SCHEDULE_FLEXIBLE = "rugalmas";
	
	@Autowired
	private CourseService courseService;

	@Autowired
	private CourseTeacherService courseTeacherService;
	
	@Autowired
	private CourseStudentDiscountService courseStudentDiscountService;
	
	@Autowired
	private CourseStudentService courseStudentService;
	
	@Autowired
	private CourseIncomeService courseIncomeService;;
	
	@Autowired
	private CompletedClassService completedClassService;	
	
	public void generateTeacherContractReport(Long courseTeacherId, String format,
			ApplicationContext context, Locale locale,
			HttpServletResponse response) 
		throws ClassNotFoundException, JRException, CourseWithMoreCurrentTeachers, VistReportException, IOException {
		
		String reportFile = "/report/contract/teacher.jrxml";
		
		CourseTeacher courseTeacher = courseTeacherService.findCourseTeacher(courseTeacherId);
		Course course = courseTeacher.getCourse();
		Teacher teacher = courseTeacher.getTeacher();
		
		HashMap<String, Object> parameters = new HashMap<String, Object>();		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy, MMMMM dd", locale);
		addCourseCommonParameters(course, parameters, dateFormat);
		
		String contractNumber = course.getCourseId() + ".t." + teacher.getId();
		if(course.getCourseFormat().equals(ClassFormat.INSTANT)) {
			contractNumber = "instant" + ".t." + teacher.getId();
		}
		
		if(teacher.getTeacherBillingData() == null || teacher.getTeacherBillingData().isEmpty()) {
			throw new VistReportException("error_report_teacher_empty_billingdata");	
		}
		if(teacher.getTeacherBillingData().getAddress() == null || teacher.getTeacherBillingData().getAddress().isEmpty()) {
			throw new VistReportException("error_report_teacher_empty_billingdata_address");	
		}		
		String teacherAddress = teacher.getTeacherBillingData().getAddress().getAddressString();
		
		String courseAddress = VIST_ADDRESS;
		if(course.getRoom().equals(Room.R5)) {
			courseAddress = "......................................";
		}
		if(course.getCourseFormat().equals(ClassFormat.ONLINE)) {
			courseAddress = "online";
		}
		
		String classSchedule = COURSE_SCHEDULE_FLEXIBLE;
		String timeOfClassesStr= course.getTimeOfClasses(); 
		if(timeOfClassesStr != null && !timeOfClassesStr.equals("") && !timeOfClassesStr.equals(CourseController.FLEXIBLE_TIME)) {
			List<String> classTimeList = getClassTimeStringList(course, context, locale);
			StringBuilder sb = new StringBuilder();
			for (String string : classTimeList) {
				if(sb.length() > 0) {
					sb.append(", ");
				}
				sb.append(string);
			}
			classSchedule = sb.toString();
		}
		
		String currDateStr= dateFormat.format(new Date());
		
		parameters.put("teacher", teacher);
		parameters.put("course", course);
		parameters.put("courseTeacher", courseTeacher);
		parameters.put("courseAddress", courseAddress);
		parameters.put("classSchedule", classSchedule);
		parameters.put("currDateStr", currDateStr);
		
		parameters.put("teacherAddress", teacherAddress);
		parameters.put("contractNumber", contractNumber);		
		
		
		JRDataSource ds = new JREmptyDataSource();
		LOGGER.debug("load main report: "  + reportFile);
		//InputStream reportStream = this.getClass().getResourceAsStream(reportFile);
		
		ClassLoader ctxLoader = Thread.currentThread().getContextClassLoader();
		URL resURL = ctxLoader.getResource(reportFile);
		URLConnection resConn = resURL.openConnection();
		resConn.setUseCaches(false);
		InputStream reportStream = resConn.getInputStream();
		
		JasperDesign mainReport = JRXmlLoader.load(reportStream);
		JasperReport jr = JasperCompileManager.compileReport(mainReport);
		
		String fileName = "teacher_contract_" + teacher.getPersonalData().getLastName() + teacher.getPersonalData().getFirstName() + "_" + course.getCourseId();
		
		completeReport(jr, parameters, ds, fileName, format, response);
	}
	
	private static void addCourseCommonParameters(Course course, HashMap<String, Object> parameters, SimpleDateFormat dateFormat) {				
		String courseTitle = course.getLang().getLabel() + " " + course.getCourseFormat().getLabel() + " " + 
				course.getCourseType().getLabel() + " nyelvtanfolyam, " + course.getCourseLevel() + " szint, " + course.getClassSpecializationType().getLabel() + " nyelv";
		String courseStartDate = dateFormat.format(course.getStartDate());
		String courseEndDate = dateFormat.format(course.getEndDate());
		
		parameters.put("courseTitle", courseTitle);
		parameters.put("courseStartDate", courseStartDate);
		parameters.put("courseEndDate", courseEndDate);
	}
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	public void generateStudentContractReport(Long courseStudenrId, String format, ApplicationContext ctx, Locale locale, HttpServletResponse response)
			throws ClassNotFoundException, JRException, CourseWithMoreCurrentTeachers, VistReportException, IOException {
		
		String reportFile = "/report/contract/adult_normal.jrxml";
		
		CourseStudent courseStudent = courseStudentService.findCourseStudent(courseStudenrId);
		
		Course course = courseStudent.getCourse();
		Student student = courseStudent.getStudent();
		List<CourseStudent> courseStudentList = courseStudentService.findByCourse(course);
		HashMap<String, Object> parameters = new HashMap<String, Object>();
						
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy, MMMMM dd", locale);
		addCourseCommonParameters(course, parameters, dateFormat);		

		String studentAddress = ""; 		
		
		int numberOfOccPerWeek = -1;
		int numberOfClassesPerOcc = -1;
		String courseSchedule= COURSE_SCHEDULE_FLEXIBLE;		
		String timeOfClassesStr = course.getTimeOfClasses();
		if(timeOfClassesStr != null && !timeOfClassesStr.equals("") && 
				!timeOfClassesStr.equals(CourseController.FLEXIBLE_TIME)) {			
			String[] tca = timeOfClassesStr.split(";");
			numberOfOccPerWeek = tca.length;			
			numberOfClassesPerOcc =  Integer.parseInt(tca[0].substring(14, 15));
			courseSchedule =  numberOfOccPerWeek + "x" + numberOfClassesPerOcc;
		}
		
		String contractNumber = course.getCourseId() + ".d." + student.getId();
		
		if(course.getCompany()) {			
			if(student.getBillingAddress() == null || student.getBillingAddress().isEmpty()) {
				throw new VistReportException("error_report_company_has_no_billingaddress");
			}
			
			if(courseSchedule.equals(COURSE_SCHEDULE_FLEXIBLE)) {
				throw new VistReportException("error_report_company_course_schedule_flexible_or_emtpy");
			}
			
			String courseStudentNames = "";
			if(courseStudentList == null || courseStudentList.size() > 1) {
				throw new VistReportException("error_report_company_course_has_more_students");									
			} else {				
				courseStudentNames = courseStudent.getCompanyStudentNames().replace(";", ", ");
			}
						
			if(student.getBillingAddress().getAddress() != null && !student.getBillingAddress().getAddress().isEmpty()) {
				studentAddress = student.getBillingAddress().getAddress().getAddressString();
			} else {
				throw new VistReportException("error_report_company_course_billingaddress_address_empty");
			}			 
			
			String courseAddress = "Megbízott telephelyén (" + VIST_ADDRESS + ")";
			if(course.getRoom().equals(Room.R5)) {
				if(student.getAddress() != null && !student.getAddress().isEmpty()) {
					courseAddress = "Megbízó telephelyén (" + student.getAddress().getAddressString() + ")";
				} else {
					throw new VistReportException("error_report_company_address_empty");
				}
			}
			
			parameters.put("courseStudentNames", courseStudentNames);
			parameters.put("courseAddress", courseAddress);					
			
			reportFile = "/report/contract/student_company.jrxml";			
		} else {					
			String coursePlace = VIST_ADDRESS;
			if(course.getRoom().equals(Room.R5)) {
				coursePlace = "..................................................";
			}
		
			if(student.getAddress() != null && !student.getAddress().isEmpty()) {
				studentAddress = student.getAddress().getAddressString();
			}
			
			String studentBornPlaceDate = student.getPersonalData().getBornPlace();
			if(studentBornPlaceDate != null && studentBornPlaceDate.length() > 0) {
				studentBornPlaceDate += ", ";
			} else {
				studentBornPlaceDate = ""; 
			}
			if(student.getPersonalData().getBornDate() != null) {
				studentBornPlaceDate += dateFormat.format(student.getPersonalData().getBornDate());
			}						
			
			String numberOfStudents = new String("" + courseStudentList.size());
			
			String studentParentBornPlaceDate = "";
			String studentParentAddress = "";
			if(student.getParentPersonalData() != null) {
				studentParentBornPlaceDate = student.getParentPersonalData().getBornPlace();
				if(studentParentBornPlaceDate != null && studentParentBornPlaceDate.length() > 0) {
					studentParentBornPlaceDate += ", ";
				} else {
					studentParentBornPlaceDate = ""; 
				}
				if(student.getParentPersonalData().getBornDate() != null) {
					studentParentBornPlaceDate += dateFormat.format(student.getParentPersonalData().getBornDate());
				}
				
				if(student.getParentPersonalData() != null) {
					studentParentAddress = student.getParentPersonalData().getAddressStr();
				}			
			}	
			
			String priceStr = "tandíj";
			String preiceDetailsStr = "            Havi utólagos fizetés történik az előző hónapban letelt óraszám alapján.";
			
			List<CourseStudentDiscount> courseStudentDiscountList = courseStudentDiscountService.findByCourseAndStudent(course, student);
			double coursePrice = CalcUtil.getStudentCoursePrice(student, course, courseStudentDiscountList);
			if(course.getPayPerClasses()) {
				priceStr = "óradíj";
			} else {
				preiceDetailsStr = getPriceDetailStrForStudentContract(course, student, dateFormat, coursePrice);
			}
			priceStr += ": bruttó " + (int)coursePrice + " - forint / fő";
			
			String currDateStr= dateFormat.format(new Date());
			
			LOGGER.debug("start fill parameters");
										
			parameters.put("coursePlace", coursePlace);			
			parameters.put("numberOfStudents", numberOfStudents);						
			parameters.put("studentBornPlaceDate", studentBornPlaceDate);				
			parameters.put("priceStr", priceStr);
			parameters.put("preiceDetailsStr", preiceDetailsStr);
			parameters.put("currDateStr", currDateStr);			
			parameters.put("studentParentBornPlaceDate", studentParentBornPlaceDate);
			parameters.put("studentParentAddress", studentParentAddress);
			if(course.getCourseFormat().equals(ClassFormat.INSTANT)) {
				parameters.put("instantCourseFormat", course.getInstantCourseFormat().getLabel());
				parameters.put("instantCourseType", course.getInstantCourseType().getLabel());
			}
		
		}
		
		parameters.put("course", course);
		parameters.put("student", student);		
		parameters.put("studentAddress", studentAddress);		
		parameters.put("courseSchedule", courseSchedule);
		parameters.put("contractNumber", contractNumber);		
		
		JRDataSource ds = new JREmptyDataSource();
		LOGGER.debug("load main report: "  + reportFile);
		//InputStream reportStream = this.getClass().getResourceAsStream(reportFile);
		
		ClassLoader ctxLoader = Thread.currentThread().getContextClassLoader();
		URL resURL = ctxLoader.getResource(reportFile);
		URLConnection resConn = resURL.openConnection();
		resConn.setUseCaches(false);
		InputStream reportStream = resConn.getInputStream();
		
		JasperDesign mainReport = JRXmlLoader.load(reportStream);
		JasperReport jr = JasperCompileManager.compileReport(mainReport);
		
		String fileName = "student_contract_" + student.getPersonalData().getLastName() + student.getPersonalData().getFirstName() + "_" + course.getCourseId();
		
		completeReport(jr, parameters, ds, fileName, format, response);
		
	}
	
	
	
	
	private String getPriceDetailStrForStudentContract(Course course, Student student, SimpleDateFormat dateFormat, double coursePrice) {
		String preiceDetailsStr = "";
		String payedIncomeStr = ".................";
		String payedIncomeDateStr = ".................";
		boolean addPartialPay = true;
		int numOfpartialPays = 2;
		String[][] numOfpartialPayStrs = new String[numOfpartialPays][2];			
		List<CourseIncome> courseIncomes = courseIncomeService.findByCourseAndStudent(course, student);
		if(courseIncomes != null && courseIncomes.size() > 0) {
			LOGGER.debug("getPriceDetailStrForStudentContract called,  courseIncomes.size(): " +  courseIncomes.size());
			double sumPayed = 0;
			double sumNotPayed = 0;
			Date sumPayedDate = null;
			numOfpartialPays = 0;
			numOfpartialPayStrs = new String[courseIncomes.size() + 1][2];
			for (CourseIncome courseIncome : courseIncomes) {
				if(courseIncome.isPayed()) {
					sumPayed += courseIncome.getAmount();
					sumPayedDate = courseIncome.getPaymentDate();
				} else {
					sumNotPayed += courseIncome.getAmount();
					numOfpartialPayStrs[numOfpartialPays][0] = Integer.toString((int)courseIncome.getAmount());
					numOfpartialPayStrs[numOfpartialPays][1] = dateFormat.format(courseIncome.getDeadlineDate());
					numOfpartialPays++;						
				}					
				if(sumPayed > 0) {
					payedIncomeStr = Integer.toString((int)sumPayed);
					payedIncomeDateStr = dateFormat.format(sumPayedDate);
				}
			}
			LOGGER.debug("sumPayed: " + sumPayed + ", coursePrice: " + coursePrice + ", numOfpartialPays: " + numOfpartialPays);
			if(numOfpartialPays == 0 && (
					(sumPayed - coursePrice < 1 && sumPayed - coursePrice >= 0) || (sumPayed - coursePrice > -1 && sumPayed - coursePrice <= 0)
					)) {
				addPartialPay = false;
				LOGGER.debug("no need partial pay, whole amount is payed");
			} else {
				LOGGER.debug("partial pay needed");
				double allPayed = sumNotPayed + sumPayed;
				if((allPayed - coursePrice < 1 && allPayed - coursePrice >= 0) || 
						(allPayed - coursePrice > -1 && allPayed - coursePrice <= 0)) {					
					LOGGER.debug("partial pay is defined by course incomes, allPayed: " + allPayed + ", coursePrice: " + coursePrice);
				} else {					
					LOGGER.debug("add a default partial pay row, allPayed: " + allPayed + ", coursePrice: " + coursePrice);
					numOfpartialPayStrs[numOfpartialPays][0] = numOfpartialPayStrs[numOfpartialPays][1] = ".................";	
					numOfpartialPays++;
				}
				
			}
		} else {
			LOGGER.debug("no courseIncomes, add default partial pay strs");
			for(int i = 0; i < numOfpartialPays; i++) {
				numOfpartialPayStrs[i][0] = numOfpartialPayStrs[i][1] = ".................";
				
			}
		} 
		
		String placeHolder = "                   ";
		preiceDetailsStr = placeHolder + "befizetett összeg: " + payedIncomeStr + " - forint, dátum: " + payedIncomeDateStr;
		if(addPartialPay) {
			for(int i = 0; i < numOfpartialPays; i++) {
				preiceDetailsStr += "\n" + placeHolder + (i+2) + ". részlet: " + numOfpartialPayStrs[i][0] + " - forint, határidő: " + numOfpartialPayStrs[i][1]; 	
			}
		}
		return preiceDetailsStr;
	}
	
	/**
	 * Processes the download for Excel format
	 * @throws CourseWithMoreCurrentTeachers 
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	public void generateClassSheetReport(Long courseId, String format, ApplicationContext ctx, Locale locale, HttpServletResponse response)
			throws ClassNotFoundException, JRException, CourseWithMoreCurrentTeachers {

		Course course = courseService.findCourse(courseId);
		List<CompletedClass> completedClassList = completedClassService.findByCourse(course);
		CourseTeacher currentCourseTeacher = getTeacherForClassSheet(course);
		List<CourseStudent> courseStudentList = getStudentsForClassSheet(course);				
		
		int ccHours = 0;
		if(course.getSumOfClasses() > 0) {
			for(CompletedClass completedClass : completedClassList) {
				ccHours += completedClass.getNumberOfClasses();
			}
		}
		
		List<String> studentNames = getStudentNames(courseStudentList);
		StringBuilder snsb = new StringBuilder();
		for (String studentName : studentNames) {
			if(snsb.length() > 0) {
				snsb.append(", ");
			}
			snsb.append(studentName);
		}
		
		String courseClassesStr = "rugalmas";
		if(!course.getTimeOfClasses().equals(CourseController.FLEXIBLE_TIME) && !course.getTimeOfClasses().equals("")) {
			List<String> classTimeList = getClassTimeStringList(course, ctx, locale);
			StringBuilder sb = new StringBuilder();
			for (String string : classTimeList) {
				if(sb.length() > 0) {
					sb.append("\n");
				}
				sb.append("   - " + string);
			}
			courseClassesStr = sb.toString();
		}
		
		Long now = System.currentTimeMillis();
		Date currDate = new Date(now);
		String monthStr = ctx.getMessage("select_option_completedclass_month_" + (currDate.getMonth() + 1), null, locale);		

		String currDateStr = (currDate.getYear() + 1900) + ", " + monthStr;				
		if(course.getCourseFormat().equals(ClassFormat.INSTANT)) {
			Date startDate = course.getStartDate();
			Date endDate = course.getEndDate();	
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MMMMM-dd", locale);
			currDateStr = dateFormat.format(startDate) + " - " + dateFormat.format(endDate);
		} 
		
		String instantTypeStr = "";
		if(course.getInstantCourseFormat() != null && course.getInstantCourseType() != null) {
			instantTypeStr = course.getInstantCourseFormat().getLabel() + " " + course.getInstantCourseType().getLabel();
		}
		
		LOGGER.debug("start fill parameters");
		HashMap<String, Object> parameters = new HashMap<String, Object>();		
		parameters.put("course", course);
		parameters.put("currDateStr", currDateStr);
		parameters.put("remHours", course.getSumOfClasses() - ccHours);		
		parameters.put("teacherName", currentCourseTeacher.getTeacher().getPersonalData().getLastName() + " " + currentCourseTeacher.getTeacher().getPersonalData().getFirstName());
		parameters.put("courseClassesStr", courseClassesStr);
		parameters.put("instantTypeStr", instantTypeStr);
		parameters.put("studentNameListStr", snsb.toString());
		
		JRDataSource ds = new JRBeanCollectionDataSource(generateDSBeans(courseStudentList, course));

		LOGGER.debug("load subreport");
		InputStream reportStream = this.getClass().getResourceAsStream("/report/ClassSheetSubReport.jrxml");
		JasperDesign subReport = JRXmlLoader.load(reportStream);
		//jd.getSummary().getElements()[0];		
		
		JRBand summaryBand = subReport.getSummary();
		for (JRElement jrElement : summaryBand.getElements()) {
			if (jrElement instanceof JRDesignCrosstab) {
				JRDesignCrosstab crossTab = (JRDesignCrosstab)jrElement;
				JRDesignCrosstabRowGroup rows = (JRDesignCrosstabRowGroup)crossTab.getRowGroups()[0];				
				JRDesignCrosstabColumnGroup cols = (JRDesignCrosstabColumnGroup)crossTab.getColumnGroups()[0];
				
				JRDesignTextField rowHeaderText = (JRDesignTextField)rows.getHeader().getChildren().get(0);
				//headerText.setHeight(50);
				
				JRDesignTextField colHeaderText = (JRDesignTextField)cols.getHeader().getChildren().get(0);				
				//colHeaderText.setWidth(200);
			}
		}
		
		LOGGER.debug("load main report");
		reportStream = this.getClass().getResourceAsStream("/report/ClassSheetReport.jrxml");
		JasperDesign mainReport = JRXmlLoader.load(reportStream);

		JRDesignBand jrBand = (JRDesignBand) mainReport.getDetailSection().getBands()[0];
		JRElement[] jrElements = jrBand.getElements();
		for (JRElement jrElement : jrElements) {
			if (jrElement instanceof JRDesignSubreport) {
		       JRDesignSubreport subReportDesign = (JRDesignSubreport) jrElement;		       
		       JRDesignExpression e2 = new JRDesignExpression(); 
		       e2.setText("$P{MY_SUBREP}"); 
		       e2.setValueClass(net.sf.jasperreports.engine.JasperReport.class); 
		       subReportDesign.setExpression(e2); 
	        }
	    }			
		
		// You can also load the template by directly adding the actual path,
		// i.e.
		// JasperDesign jd =
		// JRXmlLoader.load("c:/krams/jasper/reports/tree-template.jrxml");

		// You can also let Spring inject the template
		// See http://stackoverflow.com/questions/734671/read-file-in-classpath
		
		JasperReport sr = JasperCompileManager.compileReport(subReport);
		JasperReport jr = JasperCompileManager.compileReport(mainReport);
		parameters.put("MY_SUBREP", sr);
		//URL jasperResURL = this.getClass().getResource("/report/ClassSheetReport.jasper");
		//JasperReport jr = (JasperReport) JRLoader.loadObject(jasperResURL);
		
		String fileName = "classSheet_" + course.getCourseId();
		completeReport(jr, parameters, ds, fileName, format, response);
	}	
	
	private void completeReport(JasperReport jr, HashMap<String, Object> parameters, JRDataSource ds , String fileName, String format, HttpServletResponse response) throws JRException {
		JRProperties.setProperty("net.sf.jasperreports.default.pdf.encoding", "ISO-8859-2");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		JasperPrint jp = JasperFillManager.fillReport(jr, parameters, ds);
		
		if(format.equals("pdf")) { 						
			exportToPDF(jp, baos);
			fileName += ".pdf";
			response.setHeader("Content-Disposition", "inline; filename=" + fileName);
			//response.setContentType("application/vnd.ms-excel");
			response.setContentType("application/pdf");			
		} else if(format.equals("docx")) {
			exportToDOCX(jp, baos);
			fileName += ".docx";
			response.setHeader("Content-Disposition", "inline; filename=" + fileName);
			//response.setContentType("application/vnd.ms-excel");
			response.setContentType("application/docx");
		} else if(format.equals("odt")) {
			exportToODT(jp, baos);
			fileName += ".odt";
			response.setHeader("Content-Disposition", "inline; filename=" + fileName);
			//response.setContentType("application/vnd.ms-excel");
			response.setContentType("application/odt");
		} else if(format.equals("rtf")) {
			exportToRTF(jp, baos);
			fileName += ".rtf";
			response.setHeader("Content-Disposition", "inline; filename=" + fileName);
			//response.setContentType("application/vnd.ms-excel");
			response.setContentType("application/rtf");
		}
		
		response.setContentLength(baos.size());
		LOGGER.debug("write report to response");		
		writeReportToResponseStream(response, baos);
		LOGGER.debug("finised");
	}

	
	/**
	* Exports a report to XLS (Excel) format. You can declare another export here, i.e PDF or CSV.
	* You don't really need to create a separate class or method for the exporter. You can call it
	* directly within your Service or Controller.
	* 
	* @param jp the JasperPrint object
	* @param baos the ByteArrayOutputStream where the report will be written
	*/
	private static void exportToXLS(JasperPrint jp, ByteArrayOutputStream baos) throws JRException {
	// Create a JRXlsExporter instance
		JRXlsExporter exporter = new JRXlsExporter();
		 
		// Here we assign the parameters jp and baos to the exporter
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
		 
		// Excel specific parameters
		// Check the Jasper (not DynamicJasper) docs for a description of these settings. Most are 
		// self-documenting
		exporter.setParameter(JRXlsAbstractExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
		exporter.setParameter(JRXlsAbstractExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
		exporter.setParameter(JRXlsAbstractExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
		 
		// Retrieve the exported report in XLS format
		exporter.exportReport();
	}
	
	private static void exportToDOCX(JasperPrint jp, ByteArrayOutputStream baos) throws JRException {				
		JRDocxExporter exporter = new JRDocxExporter();
		
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
		
		exporter.setParameter(JRXlsAbstractExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
		exporter.setParameter(JRXlsAbstractExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
		exporter.setParameter(JRXlsAbstractExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
		
		exporter.exportReport();
	} 
	
	private static void exportToPDF(JasperPrint jp, ByteArrayOutputStream baos) throws JRException {
		// Create a JRXlsExporter instance
			JRPdfExporter exporter = new JRPdfExporter();
			 
			// Here we assign the parameters jp and baos to the exporter
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
			 
			// Excel specific parameters
			// Check the Jasper (not DynamicJasper) docs for a description of these settings. Most are 
			// self-documenting
			exporter.setParameter(JRXlsAbstractExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
			exporter.setParameter(JRXlsAbstractExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
			exporter.setParameter(JRXlsAbstractExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
			 
			// Retrieve the exported report in XLS format
			exporter.exportReport();
		}
	
	public void exportToRTF(JasperPrint jp, ByteArrayOutputStream baos) throws JRException
	{
		JRRtfExporter exporter = new JRRtfExporter();
		
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
		 
		// Excel specific parameters
		// Check the Jasper (not DynamicJasper) docs for a description of these settings. Most are 
		// self-documenting
		exporter.setParameter(JRXlsAbstractExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
		exporter.setParameter(JRXlsAbstractExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
		exporter.setParameter(JRXlsAbstractExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
		 
		// Retrieve the exported report in XLS format
		exporter.exportReport();		
	} 
	
	public void exportToODT(JasperPrint jp, ByteArrayOutputStream baos) throws JRException
	{
		JROdtExporter exporter = new JROdtExporter();
		
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
		 
		// Excel specific parameters
		// Check the Jasper (not DynamicJasper) docs for a description of these settings. Most are 
		// self-documenting
		//exporter.setParameter(JRXlsAbstractExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
		//exporter.setParameter(JRXlsAbstractExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
		//exporter.setParameter(JRXlsAbstractExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
		 
		// Retrieve the exported report in XLS format
		exporter.exportReport();		
	} 
	
	/**
	 * Writes the report to the output stream
	 */
	private void writeReportToResponseStream(HttpServletResponse response,
			ByteArrayOutputStream baos) {

		LOGGER.debug("Writing report to the stream");
		try {
			// Retrieve the output stream
			ServletOutputStream outputStream = response.getOutputStream();
			// Write to the output stream
			baos.writeTo(outputStream);
			// Flush the stream
			outputStream.flush();

		} catch (Exception e) {
			LOGGER.error("Unable to write report to the output stream", e);
		}
	}

	
	private static List<ClassSheetHelperBean> generateDSBeans(List<CourseStudent> courseStudentList, Course course) {
		List<ClassSheetHelperBean> retVal = new ArrayList<ClassSheetHelperBean>();
		List<String> columnHeaders = getColumnHeaders(course);
		List<String> studentNames = getStudentNames(courseStudentList);
		
		List<String> rowHeaders = new ArrayList<String>();
		rowHeaders.add("Dátum");		
		rowHeaders.addAll(studentNames);
		if(course.getCourseFormat().equals(ClassFormat.INSTANT)) {
			rowHeaders.add("Tanár neve");	
		}
		rowHeaders.add(" \n \n \nTananyag\n \n \n ");
		
		int max = columnHeaders.size();
		if(rowHeaders.size() > max) {
			max = rowHeaders.size();
		}
		LOGGER.debug("max: " + max + ", columnHeaders.size: " + columnHeaders.size() + ", rowHeaders.size: " + rowHeaders.size());
	
		//TODO add first record - this is retrieved by the details brand
		ClassSheetHelperBean dummyBean = new ClassSheetHelperBean();
		ReportNameValuePair nv = new ReportNameValuePair();
		nv.setId("-1");
		nv.setLabel("-1");
		dummyBean.setStudentName(nv);
		nv = new ReportNameValuePair();
		nv.setId("-1");
		nv.setLabel("-1");
		dummyBean.setClassOccasionStr(nv);
		retVal.add(dummyBean);
		
		for(int i=0; i<max; i++) {
			ClassSheetHelperBean bean = new ClassSheetHelperBean();
			int colIdx =i;
			int colId = i;
			if(colIdx >= columnHeaders.size()) {
				colIdx = colId = 0;
			}
			
			int rowIdx = i;
			int rowId = i;
			if(rowIdx >= rowHeaders.size()) {
				rowIdx = rowId = 0;
			}
			LOGGER.debug("add new bead with rowIdx: " + rowIdx + ", colIdx: " + colIdx);
			ReportNameValuePair nvp = new ReportNameValuePair();
			nvp.setId("" + colId);
			nvp.setLabel(columnHeaders.get(colIdx));
			bean.setClassOccasionStr(nvp);
			
			nvp = new ReportNameValuePair();
			nvp.setId("" + rowId);
			nvp.setLabel(rowHeaders.get(rowIdx));
			bean.setStudentName(nvp);
			retVal.add(bean);
		}
		LOGGER.trace("retVal: " + retVal);
		return retVal;
	}
	
	private static List<String> getStudentNames(List<CourseStudent> courseStudentList) {
		List<String> retVal = new ArrayList<String>();		
		if(courseStudentList != null && courseStudentList.size() == 1 && courseStudentList.get(0).getStudent().isCompany()) {				
			String[] students = courseStudentList.get(0).getCompanyStudentNames().split(";");
			for (String studentName : students) {
				retVal.add(studentName);
			}
		} else {
			for (CourseStudent courseStudent : courseStudentList) {
				Student student = courseStudent.getStudent();
				String name = student.getPersonalData().getLastName() + " " + student.getPersonalData().getFirstName();
				retVal.add(name);
			}			
		}
		return retVal;
	}


	private static List<String> getColumnHeaders(Course course) {
		List<String> retVal = new ArrayList<String>();
		String timeOfClassesStr = course.getTimeOfClasses();		
		LOGGER.trace("timeOfClassesStr: " + timeOfClassesStr);
		if(course.getCourseFormat().equals(ClassFormat.INSTANT)) {
			String write = "ír";
			String speak = "szó";
			for(int i=1; i<=course.getSumOfClasses(); i++) {
				String classType = speak;
				if(i%2 == 0) {
					classType = write;	
				}
				if(i == course.getSumOfClasses() -1) {
					classType = write;
				} else if(i == course.getSumOfClasses()) {
					classType = speak;
				}
				retVal.add(i + " (" + classType + ")");
			}
		} else {		
			if(timeOfClassesStr.equals(CourseController.FLEXIBLE_TIME) || timeOfClassesStr.equals("")) {
				for(int i=0;i<10;i++) {
					retVal.add(" ");
				}
			} else {
				String[] tca = timeOfClassesStr.split(";");
				int numberOfOccPerWeek = tca.length;
				LOGGER.trace("numberOfOccPerWeek: " + numberOfOccPerWeek);
				int[] occasions = new int[numberOfOccPerWeek];		
				for (int i = 0; i<numberOfOccPerWeek; i++ ) {
					occasions[i] = Integer.parseInt(tca[i].substring(14, 15));
					LOGGER.trace("occasions["+i+"]: " + occasions[i]);
				}
		
				int globalIdx = 1;
				for(int i=0 ; i<NUMBER_OF_WEEK_PER_MONTH; i++) {
					for(int j=0; j<numberOfOccPerWeek; j++) {
						StringBuilder header = new StringBuilder();
						for(int k=0; k<occasions[j]; k++) {
							if(header.length() > 0) {
								header.append(",");
							}
							header.append(globalIdx++);					
						}
						retVal.add(header.toString());
					}
				}
			}		
		}
		return retVal;
	}
	
	private static List<String> getClassTimeStringList(Course course, ApplicationContext ctx, Locale locale) {
		List<String> retVal = new ArrayList<String>();
		List<SelectOption> soList = CourseController.createOptionListFromClassTimes(course, ctx, locale);
		for (SelectOption selectOption : soList) {
			retVal.add(selectOption.getLabel());
		}
		return retVal;
	}
	
	private CourseTeacher getTeacherForClassSheet(Course course) throws CourseWithMoreCurrentTeachers {
		CourseTeacher currentTeacher = null;
		Date currDate = new Date();
		Date courseStartDate = course.getStartDate();
		boolean startDateToday = DateUtil.isSameDay(currDate, courseStartDate);
		if(!startDateToday && courseStartDate.after(currDate)) {
			List<CourseTeacher> ctl = courseTeacherService.findByCourse(course);
			if(ctl != null && ctl.size() > 0) {
				currentTeacher = ctl.get(0);	
			}
		} else {		
			currentTeacher = courseTeacherService.getCurrentTeachersByDateAndCourse(course).get(0);
		}
		return currentTeacher;
	}
	
	private List<CourseStudent> getStudentsForClassSheet(Course course) throws CourseWithMoreCurrentTeachers {
		List<CourseStudent> currentStudents = null;
		Date currDate = new Date();
		Date courseStartDate = course.getStartDate();
		boolean startDateToday = DateUtil.isSameDay(currDate, courseStartDate);
		if(!startDateToday && courseStartDate.after(currDate)) {
			currentStudents = courseStudentService.findByCourse(course);			
		} else {		
			currentStudents = courseStudentService.getCurrentStudentsByDateAndCourse(course);
		}
		return currentStudents;
	}


		
	
}