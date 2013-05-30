package org.vist.vistadmin.reporting;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vist.vistadmin.domain.Course;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRAbstractBeanDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;

public class Reporter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Reporter.class); 
	
	public static void main(String[] args) {
		Reporter r = new Reporter();
		r.doReport();
	}
	
	public void doReport() {
		LOGGER.debug("doReport called");			
		Map parameters = new HashMap();
		Course c = new Course();
		c.setCourseId("1101");
		c.setBook("konyv 1");
		c.setStartDate(new Date());
		c.setEndDate(new Date());
		c.setSumOfClasses(40);
		parameters.put("course", c);
		parameters.put("remHours", "10");
		parameters.put("courseClassesStr", "H 13:00-13:45;Cs: 16.00-16.45");
		parameters.put("teacherName", "Takacs Monika");
		parameters.put("currDate", "2012-08-08");
		parameters.put("", "");
		
		//parameters.put("param2", new ArrayList("Bela", "Andras"));
		
		try {
			Collection coll = CourseFactoryTest.getGeneratedCollection();			
			JRDataSource ds;
			ds = new JRBeanCollectionDataSource(coll);
			//ds = new CustomDS();
			//((CustomDS)ds).setCourseList(dsBeans);
			JasperFillManager.fillReportToFile("target/classes/report/report1.jasper", "target/classes/report/report1.jrprint", parameters, ds);
			JasperExportManager.exportReportToPdfFile("target/classes/report/report1.jrprint");
		} catch (JRException e) {
			LOGGER.error("Error during generate report", e);
		}
	}
}
