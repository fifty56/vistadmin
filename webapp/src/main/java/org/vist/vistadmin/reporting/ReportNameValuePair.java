package org.vist.vistadmin.reporting;

public class ReportNameValuePair implements  Comparable<ReportNameValuePair> {

	private String id;
	
	private String label;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}


	@Override
	public String toString() {
		return label;
	}

	@Override
	public int compareTo(ReportNameValuePair o) {
		if(Integer.parseInt(getId()) > Integer.parseInt(o.getId())) {
			return 1;
		} else if(Integer.parseInt(getId()) < Integer.parseInt(o.getId())) {
			return -1;
		}		
		return 0;
	}
	
	
	
}
