package org.vist.vistadmin.reporting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.vist.vistadmin.domain.Course;

public class CourseFactoryTest {

	public static Collection getGeneratedCollection() {
		
			List courseList = new ArrayList();
		
			ClassSheetHelperBean hp = new ClassSheetHelperBean();
			ReportNameValuePair so = new ReportNameValuePair();
			
			
			so.setId("-1");
			so.setLabel("-1");
			hp.setClassOccasionStr(so);
			
			so = new ReportNameValuePair();
			so.setId("-1");
			so.setLabel("-1");
			hp.setStudentName(so);
			courseList.add(hp);			
			
			
			
			
			
			
			hp = new ClassSheetHelperBean();
			so = new ReportNameValuePair();
			so.setId("0");
			so.setLabel("");
			hp.setClassOccasionStr(so);
			
			so = new ReportNameValuePair();
			so.setId("0");
			so.setLabel("Datum1");
			hp.setStudentName(so);
			courseList.add(hp);
			
			
			
			hp = new ClassSheetHelperBean();
			so = new ReportNameValuePair();
			so.setId("1");
			so.setLabel("");
			hp.setClassOccasionStr(so);
			
			so = new ReportNameValuePair();
			so.setId("1");
			so.setLabel("Kicsike Nikike");
			hp.setStudentName(so);
			courseList.add(hp);
			
			
			
			
			hp = new ClassSheetHelperBean();			
			so = new ReportNameValuePair();
			so.setId("2");
			so.setLabel("");
			hp.setClassOccasionStr(so);			
			
			so = new ReportNameValuePair();
			so.setId("2");
			so.setLabel("\n\n tananyag \n\n");
			hp.setStudentName(so);
			courseList.add(hp);
			
			
			hp = new ClassSheetHelperBean();
			so = new ReportNameValuePair();
			so.setId("3");
			so.setLabel("");
			hp.setClassOccasionStr(so);
			
			so = new ReportNameValuePair();
			so.setId("0");
			so.setLabel("Datum");
			hp.setStudentName(so);
			courseList.add(hp);
			
			
			
			hp = new ClassSheetHelperBean();
			so = new ReportNameValuePair();
			so.setId("4");
			so.setLabel("");
			hp.setClassOccasionStr(so);
			
			so = new ReportNameValuePair();
			so.setId("0");
			so.setLabel("Datum");
			hp.setStudentName(so);
			courseList.add(hp);
			
			
			
			hp = new ClassSheetHelperBean();
			so = new ReportNameValuePair();
			so.setId("5");
			so.setLabel("");
			hp.setClassOccasionStr(so);
			
			so = new ReportNameValuePair();
			so.setId("0");
			so.setLabel("Datum");
			hp.setStudentName(so);
			courseList.add(hp);
			
			
			
			
			hp = new ClassSheetHelperBean();
			so = new ReportNameValuePair();
			so.setId("6");
			so.setLabel("");
			hp.setClassOccasionStr(so);
			
			so = new ReportNameValuePair();
			so.setId("0");
			so.setLabel("Datum");
			hp.setStudentName(so);
			courseList.add(hp);
			
			
			
			
			hp = new ClassSheetHelperBean();
			so = new ReportNameValuePair();
			so.setId("7");
			so.setLabel("");
			hp.setClassOccasionStr(so);
						
			so = new ReportNameValuePair();
			so.setId("0");
			so.setLabel("Datum");
			hp.setStudentName(so);
			courseList.add(hp);
			
			
			
			hp = new ClassSheetHelperBean();
			so = new ReportNameValuePair();
			so.setId("8");
			so.setLabel("");
			hp.setClassOccasionStr(so);
						
			so = new ReportNameValuePair();
			so.setId("0");
			so.setLabel("Datum");
			hp.setStudentName(so);
			courseList.add(hp);
			

			
			
			hp = new ClassSheetHelperBean();
			so = new ReportNameValuePair();
			so.setId("9");
			so.setLabel("");
			hp.setClassOccasionStr(so);
						
			so = new ReportNameValuePair();
			so.setId("0");
			so.setLabel("Datum");
			hp.setStudentName(so);
			courseList.add(hp);
			
		return courseList;		
	}
	

	
}
