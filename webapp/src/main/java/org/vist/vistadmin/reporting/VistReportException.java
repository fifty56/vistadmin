package org.vist.vistadmin.reporting;

public class VistReportException extends Exception  {

	private String messageKey;
	
	public String getMessageKey() {
		return messageKey;
	}

	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}

	public VistReportException(String key) {
		messageKey = key;
	}
	
}
