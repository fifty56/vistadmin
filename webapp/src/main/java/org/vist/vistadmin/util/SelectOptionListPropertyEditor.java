package org.vist.vistadmin.util;

import java.beans.PropertyEditorSupport;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vist.vistadmin.domain.common.SelectOptionList;

public class SelectOptionListPropertyEditor extends PropertyEditorSupport {

	private static final Logger LOGGER =  LoggerFactory.getLogger(SelectOptionListPropertyEditor.class);
	
	@Override
    public void setAsText(String text) {
		LOGGER.debug("setAsText: " + text);
		SelectOptionList list = new SelectOptionList();
		list.add("1");
		list.add("2");
		setValue(list);
     //   setValue(new ExoticType(text.toUpperCase()));
    }
    
    @Override
	public String getAsText() {
		Object value = getValue();
		LOGGER.debug("getAsText: " + value);
		return "1;2";
	}


}
