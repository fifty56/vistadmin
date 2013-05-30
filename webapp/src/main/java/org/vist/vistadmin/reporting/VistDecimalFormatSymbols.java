package org.vist.vistadmin.reporting;

import java.text.DecimalFormatSymbols;

public class VistDecimalFormatSymbols extends DecimalFormatSymbols {

	public VistDecimalFormatSymbols() {
		setGroupingSeparator('.');
	}
	
}
