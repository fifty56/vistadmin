package org.vist.vistadmin.domain;

import java.util.Date;

public class FromToInterval {

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	private Date fromDate;
	
	private Date toDate;
	
}
