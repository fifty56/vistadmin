package org.vist.vistadmin.reporting;

public class ClassSheetHelperBean{

	private ReportNameValuePair classOccasionStr;
	
	private ReportNameValuePair  studentName;

	public ReportNameValuePair getClassOccasionStr() {
		return classOccasionStr;
	}

	public void setClassOccasionStr(ReportNameValuePair classOccasionStr) {
		this.classOccasionStr = classOccasionStr;
	}

	public ReportNameValuePair getStudentName() {
		return studentName;
	}

	public void setStudentName(ReportNameValuePair studentName) {
		this.studentName = studentName;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("ClassSheetHelperBean[").append("classOccasionStr[").append("id:").
			append(classOccasionStr.getId()).append(", label:").
			append(classOccasionStr.getLabel()).append("]").
			append(", studentName[").append("id:").
			append(studentName.getId()).append(", label:").
			append(studentName.getLabel()).append("]").
			append("]");
		return sb.toString();
	}
	
}
