package org.vist.vistadmin.service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vist.vistadmin.domain.CompletedClass;
import org.vist.vistadmin.domain.Course;
import org.vist.vistadmin.domain.CourseStudent;
import org.vist.vistadmin.domain.CourseTeacher;
import org.vist.vistadmin.domain.Student;
import org.vist.vistadmin.domain.common.SelectOption;
import org.vist.vistadmin.reporting.ClassSheetHelperBean;
import org.vist.vistadmin.reporting.CourseFactoryTest;
import org.vist.vistadmin.reporting.ReportNameValuePair;
import org.vist.vistadmin.web.CourseController;
import org.vist.vistadmin.web.ReportController;

/**
 * Service for processing Jasper reports
 * 
 */
@Service
public class ReportService {

	private static final Logger LOGGER =  LoggerFactory.getLogger(ReportService.class);

	private static final int NUMBER_OF_WEEK_PER_MONTH = 4;
	
	@Autowired
	private CourseService courseService;

	@Autowired
	private CourseTeacherService courseTeacherService;
	
	@Autowired
	private CourseStudentService courseStudentService;
	
	@Autowired
	private CompletedClassService completedClassService;
	
	/**
	 * Processes the download for Excel format
	 * @throws CourseWithMoreCurrentTeachers 
	 */
	@SuppressWarnings("unchecked")
	public void generateReport(Long courseId, String format, ApplicationContext ctx, Locale locale, HttpServletResponse response)
			throws ClassNotFoundException, JRException, CourseWithMoreCurrentTeachers {

		Course course = courseService.findCourse(courseId);
		List<CompletedClass> completedClassList = completedClassService.findByCourse(course);
		CourseTeacher currentCourseTeacher = courseTeacherService.getCurrentTeachersByDateAndCourse(course).get(0);
		List<CourseStudent> courseStudentList = courseStudentService.getCurrentStudentsByDateAndCourse(course);
		
		int ccHours = 0;
		for(CompletedClass completedClass : completedClassList) {
			ccHours += completedClass.getNumberOfClasses();
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
		if(!course.getTimeOfClasses().equals(CourseController.FLEXIBLE_TIME)) {
			List<String> classTimeList = getClassTimeStringList(course, ctx, locale);
			StringBuilder sb = new StringBuilder();
			for (String string : classTimeList) {
				if(sb.length() > 0) {
					sb.append("\n");
				}
				sb.append(string);
			}
			courseClassesStr = sb.toString();
		}
		
		HashMap<String, Object> parameters = new HashMap<String, Object>();		
		parameters.put("course", course);
		parameters.put("remHours", course.getSumOfClasses() - ccHours);		
		parameters.put("teacherName", currentCourseTeacher.getTeacher().getPersonalData().getLastName() + " " + currentCourseTeacher.getTeacher().getPersonalData().getFirstName());
		parameters.put("currdate", new Date());
		parameters.put("courseClassesStr", courseClassesStr);
		parameters.put("studentNameListStr", snsb.toString());
		
		JRDataSource ds = new JRBeanCollectionDataSource(generateDSBeans(courseStudentList, course));

		JRProperties.setProperty("net.sf.jasperreports.default.pdf.encoding", "ISO-8859-2");

		InputStream reportStream = this.getClass().getResourceAsStream("/report/report1.jrxml");
		JasperDesign jd = JRXmlLoader.load(reportStream);

		JRBand[] bands = jd.getAllBands();
		
		// You can also load the template by directly adding the actual path,
		// i.e.
		// JasperDesign jd =
		// JRXmlLoader.load("c:/krams/jasper/reports/tree-template.jrxml");

		// You can also let Spring inject the template
		// See http://stackoverflow.com/questions/734671/read-file-in-classpath

		// Compile our report layout
		JasperReport jr = JasperCompileManager.compileReport(jd);
		JasperPrint jp = JasperFillManager.fillReport(jr, parameters, ds);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		exportToPDF(jp, baos);
		String fileName = "report1.pdf";
		response.setHeader("Content-Disposition", "inline; filename=" + fileName);
		//response.setContentType("application/vnd.ms-excel");
		response.setContentType("application/pdf");
		response.setContentLength(baos.size());
		
		writeReportToResponseStream(response, baos);
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
		rowHeaders.add(" \n \n \nTananyag\n \n \n ");
		
		int max = columnHeaders.size();
		if(rowHeaders.size() > max) {
			max = rowHeaders.size();
		}
		LOGGER.debug("max: " + max + ", columnHeaders.size: " + columnHeaders.size() + ", rowHeaders.size: " + rowHeaders.size());
	
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
		for (CourseStudent courseStudent : courseStudentList) {
			Student student = courseStudent.getStudent();
			if(student.isCompany()) {
				String[] students = student.getCompanyData().getCompanyStudentNames().split(";");
				for (String studentName : students) {
					retVal.add(studentName);
				}
			} else {
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
		if(timeOfClassesStr.equals(CourseController.FLEXIBLE_TIME)) {
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
	
}